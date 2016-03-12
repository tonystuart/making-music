// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.example.afs.makingmusic.common.MessageBroker.Subscriber;
import com.example.afs.makingmusic.constants.Properties;

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
            propertyChangeQueue.get().add(message);
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

  public void start(int period) {
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        runBody();
      }
    }, 0, period);
  }

  public void terminate() {
    terminated = true;
  }

  protected void cleanup() {
  }

  protected void doPropertyChange(PropertyChange propertyChange) {
    try {
      onPropertyChange(propertyChange);
    } catch (RuntimeException e) {
      // ignore invalid property change
    }
  }

  protected void initialize() {
  }

  protected void onPropertyChange(PropertyChange propertyChange) {
  }

  private void runBody() {
    try {
      T message;
      timeKeeper.beginLoop();
      if (inputQueue == null) {
        timeKeeper.beginProcess();
        message = process();
        timeKeeper.endProcess();
      } else {
        timeKeeper.beginInput();
        message = inputQueue.take();
        timeKeeper.endInput();
        timeKeeper.beginProperty();
        if (propertyChangeQueue.get() != null) {
          while (propertyChangeQueue.get().size() > 0) {
            onPropertyChange(propertyChangeQueue.get().remove());
          }
        }
        timeKeeper.endProperty();
        timeKeeper.beginProcess();
        process(message);
        timeKeeper.endProcess();
      }
      if (outputQueue != null) {
        timeKeeper.beginOutput();
        outputQueue.put(message);
        timeKeeper.endOutput();
      }
      timeKeeper.endLoop();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}