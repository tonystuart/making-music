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
        doMessage(messageQueue.get().remove());
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
   * Process a message on this thread (the one that invoked
   * {@link #runMessages()}.
   * 
   * @param message
   *          message to process
   */
  protected void doMessage(T message) {
  }

  /**
   * Process a message on another thread (the one that invoked
   * {@link MessageBroker#publish(Message)}
   * 
   * @param message
   *          message to process
   */
  protected void onMessage(T message) {
    if (messageQueue.get() != null) {
      messageQueue.get().add(message);
    }
  }

  private void initializeMessageSubscriber() {
    messageSubscriber = new Subscriber<T>() {
      @Override
      public void onMessage(T message) {
        MessageReceiver.this.onMessage(message);
      }
    };
    Injector.getMessageBroker().subscribe(messageClass, messageSubscriber);
  }

}