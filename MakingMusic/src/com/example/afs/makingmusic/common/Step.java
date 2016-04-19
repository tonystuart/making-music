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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.example.afs.makingmusic.common.MessageBroker.Subscriber;

public class Step<T> extends Thread {

  private BlockingQueue<T> inputQueue;
  private BlockingQueue<T> outputQueue;
  private AtomicReference<ConcurrentLinkedQueue<PropertyChange>> propertyChangeQueue = new AtomicReference<ConcurrentLinkedQueue<PropertyChange>>();
  private Subscriber<PropertyChange> propertyChangeSubscriber;
  private boolean terminated;
  private TimeKeeper timeKeeper;

  public Step() {
    this(null);
  }

  public Step(BlockingQueue<T> inputQueue) {
    this.inputQueue = inputQueue;
    setName(getClass().getSimpleName());
    timeKeeper = new TimeKeeper(getName());
  }

  public BlockingQueue<T> getOutputQueue() {
    if (outputQueue == null) {
      outputQueue = new ArrayBlockingQueue<>(1);
    }
    return outputQueue;
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

  public void setMonitorPropertyChanges(boolean isMonitorPropertyChanges) {
    if (isMonitorPropertyChanges) {
      if (propertyChangeQueue.get() == null) {
        propertyChangeSubscriber = new Subscriber<PropertyChange>() {
          @Override
          public void onMessage(PropertyChange message) {
            onPropertyChange(message);
          }
        };
        propertyChangeQueue.set(new ConcurrentLinkedQueue<PropertyChange>());
        Injector.getMessageBroker().subscribe(PropertyChange.class, propertyChangeSubscriber);
      }
    } else {
      if (propertyChangeQueue.get() != null) {
        Injector.getMessageBroker().unsubscribe(PropertyChange.class, propertyChangeSubscriber);
        propertyChangeQueue.set(null);
        propertyChangeSubscriber = null;
      }
    }
  }

  public void terminate() {
    terminated = true;
  }

  protected void cleanup() {
  }

  protected void doPropertyChange(PropertyChange propertyChange) {
  }

  protected void initialize() {
  }

  /**
   * Handle a property change message in a thread safe manner.
   * 
   * @param message
   *          property change to handle
   */
  protected void onPropertyChange(PropertyChange message) {
    propertyChangeQueue.get().add(message);
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
    if (propertyChangeQueue.get() != null) {
      while (propertyChangeQueue.get().size() > 0) {
        try {
          doPropertyChange(propertyChangeQueue.get().remove());
        } catch (RuntimeException e) {
          // ignore invalid property change
        }
      }
    }
    timeKeeper.endProperty();
  }

}