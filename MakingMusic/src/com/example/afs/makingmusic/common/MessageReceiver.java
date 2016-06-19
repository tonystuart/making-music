// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.example.afs.makingmusic.common.MessageBroker.Message;
import com.example.afs.makingmusic.common.MessageBroker.Subscriber;

public class MessageReceiver<T extends Message> {

  public interface MessageHandler<T> {
    void doMessage(T message);
  }

  public enum MonitorStyle {
    ASYNC, NONE, SYNC
  }

  protected AtomicReference<ConcurrentLinkedQueue<T>> messageQueue = new AtomicReference<ConcurrentLinkedQueue<T>>();
  private Class<T> messageClass;
  private Subscriber<T> messageSubscriber;

  public MessageReceiver(Class<T> messageClass) {
    this.messageClass = messageClass;
  }

  public void runMessages() {
    if (messageQueue.get() != null) {
      while (messageQueue.get().size() > 0) {
        onSynchronousMessage(messageQueue.get().remove());
      }
    }
  }

  public void setMonitorStyle(MonitorStyle monitorStyle) {
    switch (monitorStyle) {
    case ASYNC:
      if (messageSubscriber != null) {
        throw new IllegalStateException();
      }
      initializeMessageSubscriber();
      break;
    case NONE:
      if (messageQueue.get() != null) {
        Injector.getMessageBroker().unsubscribe(messageClass, messageSubscriber);
        messageQueue.set(null);
        messageSubscriber = null;
      }
      break;
    case SYNC:
      if (messageSubscriber != null) {
        throw new IllegalStateException();
      }
      messageQueue.set(new ConcurrentLinkedQueue<T>());
      initializeMessageSubscriber();
      break;
    default:
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Process an asynchronous ({@link MonitorStyle#ASYNC}) message on another
   * thread (the one that invoked {@link MessageBroker#publish(Message)}) at the
   * point the message is published. Note that the JLS says that assignment to
   * variables of any type except long or double is atomic:
   * http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.7
   * 
   * @param message
   *          message to process
   */
  protected void onAsynchronousMessage(T message) {
    if (messageQueue.get() != null) {
      messageQueue.get().add(message);
    }
  }

  /**
   * Process a synchronous ({@link MonitorStyle#SYNC}) message on this thread
   * (the one that invoked {@link MessageBroker#runMessages()} at some point
   * after the message is published. Note that the JLS says that assignment to
   * variables of any type except long or double is atomic:
   * http://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html#jls-17.7
   * 
   * 
   * @param message
   *          message to process
   */
  protected void onSynchronousMessage(T message) {
  }

  private void initializeMessageSubscriber() {
    messageSubscriber = new Subscriber<T>() {
      @Override
      public void onMessage(T message) {
        MessageReceiver.this.onAsynchronousMessage(message);
      }
    };
    Injector.getMessageBroker().subscribe(messageClass, messageSubscriber);
  }

}