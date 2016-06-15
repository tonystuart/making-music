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
import java.util.concurrent.ConcurrentHashMap;

import com.example.afs.makingmusic.common.MessageBroker.Message;

public class StateRequest implements Message {

  private Map<String, String> properties = new ConcurrentHashMap<>();

  public StateRequest(Map<String, String> properties) {
    this.properties = properties;
  }

  public void addProperty(String name, String value) {
    properties.put(name, value);
  }

  public Map<String, String> getProperties() {
    return properties;
  }
}
