// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import com.example.afs.makingmusic.band.Drums;
import com.example.afs.makingmusic.band.Instruments;

// See Josh Bloch, Effective Java, Item 3: Static Factory Method

public class Injector {
  private static final Drums drums = new Drums();
  private static final Instruments instruments = new Instruments();
  private static final MessageBroker messageBroker = new MessageBroker();
  private static final PropertyCache propertyCache = new PropertyCache();

  public static Drums getDrums() {
    return drums;
  }

  public static Instruments getInstruments() {
    return instruments;
  }

  public static MessageBroker getMessageBroker() {
    return messageBroker;
  }

  public static PropertyCache getPropertyCache() {
    return propertyCache;
  }

  private Injector() {
  }
}
