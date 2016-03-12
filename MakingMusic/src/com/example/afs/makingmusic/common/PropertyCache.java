// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.afs.makingmusic.common.MessageBroker.Subscriber;
import com.example.afs.makingmusic.constants.Constants;
import com.google.gson.Gson;

public class PropertyCache {
  private Map<String, String> properties = new LinkedHashMap<String, String>() {
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
      return size() == Constants.MAX_PROPERTIES;
    }
  };

  public PropertyCache() {
    Injector.getMessageBroker().subscribe(PropertyChange.class, new Subscriber<PropertyChange>() {
      @Override
      public void onMessage(PropertyChange message) {
        properties.put(message.getName(), message.getValue());
      }
    });
  }

  public String getJsonProperties() {
    Gson gson = new Gson();
    String jsonString = gson.toJson(this);
    return jsonString;
  }

  public Map<String, String> getProperties() {
    return properties;
  }
}
