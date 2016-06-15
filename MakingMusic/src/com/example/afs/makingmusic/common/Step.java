// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.example.afs.makingmusic.common.MessageBroker.Message;
import com.example.afs.makingmusic.common.MessageReceiver.MonitorStyle;

public class Step<T> extends Thread {

  private BlockingQueue<T> inputQueue;
  private BlockingQueue<T> outputQueue;
  private MessageReceiver<PropertyChange> propertyChangeReceiver;
  private MessageReceiver<StateRequest> stateRequestReceiver;
  private boolean terminated;
  private TimeKeeper timeKeeper;

  public Step() {
    this(null);
  }

  public Step(BlockingQueue<T> inputQueue) {
    this.inputQueue = inputQueue;
    setName(getClass().getSimpleName());
    timeKeeper = new TimeKeeper(getName());
    propertyChangeReceiver = new MessageReceiver<PropertyChange>(PropertyChange.class) {
      @Override
      public void doMessage(PropertyChange message) {
        doPropertyChange(message);
      }
    };
    stateRequestReceiver = new MessageReceiver<StateRequest>(StateRequest.class) {
      @Override
      public void onMessage(StateRequest message) {
        onStateRequest(message);
      }
    };
  }

  public BlockingQueue<T> getOutputQueue() {
    if (outputQueue == null) {
      outputQueue = new ArrayBlockingQueue<>(1);
    }
    return outputQueue;
  }

  public void monitorPropertyChange(MonitorStyle monitorStyle) {
    propertyChangeReceiver.setMonitorStyle(monitorStyle);
  }

  public void monitorStateRequest(MonitorStyle monitorStyle) {
    stateRequestReceiver.setMonitorStyle(monitorStyle);
  }

  public T process() throws InterruptedException {
    return null;
  }

  public void process(T message) throws InterruptedException {
  }

  @Override
  public void run() {
    initialize();
    while (!terminated) {
      runBody();
    }
    cleanup();
  }

  public void terminate() {
    terminated = true;
  }

  protected void cleanup() {
  }

  /**
   * Process a message on this thread (the one that invoked
   * {@link #runMessages()}.
   * 
   * @param message
   *          message to process
   */

  protected void doPropertyChange(PropertyChange propertyChange) {
  }

  protected void initialize() {
  }

  /**
   * Process a message on another thread (the one that invoked
   * {@link MessageBroker#publish(Message)}
   * 
   * @param message
   *          message to process
   */
  protected void onStateRequest(StateRequest stateRequest) {
  }

  protected void runBody() {
    try {
      T message;
      timeKeeper.beginLoop();
      if (inputQueue == null) {
        runPropertyChanges();
        message = runProcess();
      } else {
        message = runInput();
        runPropertyChanges();
        runProcess(message);
      }
      if (outputQueue != null) {
        runOutput(message);
      }
      timeKeeper.endLoop();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  protected T runInput() throws InterruptedException {
    T message;
    timeKeeper.beginInput();
    message = inputQueue.take();
    timeKeeper.endInput();
    return message;
  }

  protected void runOutput(T message) throws InterruptedException {
    timeKeeper.beginOutput();
    outputQueue.put(message);
    timeKeeper.endOutput();
  }

  protected T runProcess() throws InterruptedException {
    T message;
    timeKeeper.beginProcess();
    message = process();
    timeKeeper.endProcess();
    return message;
  }

  protected void runProcess(T message) throws InterruptedException {
    timeKeeper.beginProcess();
    process(message);
    timeKeeper.endProcess();
  }

  protected void runPropertyChanges() {
    timeKeeper.beginProperty();
    propertyChangeReceiver.runMessages();
    timeKeeper.endProperty();
  }

}