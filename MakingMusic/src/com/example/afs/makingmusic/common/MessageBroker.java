// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBroker {

  public interface Message {

  }

  public interface Subscriber<M extends Message> {

    void onMessage(M message);

  }

  private ConcurrentHashMap<Class<? extends Message>, List<Subscriber<? extends Message>>> subscribers;

  public MessageBroker() {
    subscribers = new ConcurrentHashMap<>();
  }

  @SuppressWarnings("unchecked")
  public <T extends Message> void publish(T message) {
    List<Subscriber<? extends Message>> list = subscribers.get(message.getClass());
    if (list != null) {
      for (Subscriber<? extends Message> subscriber : list) {
        ((Subscriber<T>) subscriber).onMessage(message);
      }
    }
  }

  public <T extends Message> void subscribe(Class<T> type, Subscriber<T> subscriber) {
    List<Subscriber<? extends Message>> list = subscribers.get(type);
    if (list == null) {
      list = new LinkedList<Subscriber<? extends Message>>();
      subscribers.put(type, list);
    }
    list.add(subscriber);
  }

  public <T extends Message> void unsubscribe(Class<T> type, Subscriber<T> subscriber) {
    List<Subscriber<? extends Message>> list = subscribers.get(type);
    if (list != null) {
      list.remove(subscriber);
    }
  }
}
