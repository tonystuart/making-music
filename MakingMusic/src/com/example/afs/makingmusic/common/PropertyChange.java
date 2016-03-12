// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import com.example.afs.makingmusic.common.MessageBroker.Message;

public class PropertyChange implements Message {

  private String name;
  private String value;

  public PropertyChange(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "PropertyChange [name=" + name + ", value=" + value + "]";
  }

}
