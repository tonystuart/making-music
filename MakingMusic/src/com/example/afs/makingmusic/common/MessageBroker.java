// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageBroker {

  public interface Message {
  }

  public interface Subscriber<M extends Message> {
    void onMessage(M message);
  }

  private Map<Class<? extends Message>, Queue<Subscriber<? extends Message>>> subscribers;

  public MessageBroker() {
    subscribers = new ConcurrentHashMap<>();
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> void publish(T message) {
    Queue<Subscriber<? extends Message>> queue = subscribers.get(message.getClass());
    if (queue != null) {
      for (Subscriber<? extends Message> subscriber : queue) {
        ((Subscriber<T>) subscriber).onMessage(message);
      }
    }
  }

  public <T extends Message> void subscribe(Class<T> type, Subscriber<T> subscriber) {
    synchronized (subscribers) {
      Queue<Subscriber<? extends Message>> queue = subscribers.get(type);
      if (queue == null) {
        queue = new ConcurrentLinkedQueue<Subscriber<? extends Message>>();
        subscribers.put(type, queue);
      }
      queue.add(subscriber);
    }
  }

  public <T extends Message> void unsubscribe(Class<T> type, Subscriber<T> subscriber) {
    synchronized (subscribers) {
      Queue<Subscriber<? extends Message>> queue = subscribers.get(type);
      if (queue != null) {
        queue.remove(subscriber);
        if (queue.size() == 0) {
          subscribers.remove(type);
        }
      }
    }
  }
}
