// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.MessageReceiver.MonitorStyle;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.ScheduledStep;
import com.example.afs.makingmusic.constants.Property;

public class ResetMessagePublisher extends ScheduledStep<Void> {

  public ResetMessagePublisher(long intervalMillis) {
    super(intervalMillis);
    resetTimer();
    monitorPropertyChange(MonitorStyle.ASYNC);
  }

  @Override
  public Void process() throws InterruptedException {
    Injector.getMessageBroker().publish(new PropertyChange(Property.Names.RESET, null));
    return null;
  }

  @Override
  protected void onAsynchronousPropertyChange(PropertyChange propertyChange) {
    if (!propertyChange.getName().equals(Property.Names.RESET)) {
      resetTimer();
    }
  }

}