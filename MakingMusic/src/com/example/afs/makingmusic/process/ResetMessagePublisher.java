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
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.ScheduledStep;
import com.example.afs.makingmusic.constants.Constants;
import com.example.afs.makingmusic.constants.Properties;

public class ResetMessagePublisher extends ScheduledStep<Void> {

  public ResetMessagePublisher(long intervalMillis) {
    super(intervalMillis);
    setMonitorPropertyChanges(true);
  }

  @Override
  public Void process() throws InterruptedException {
    Injector.getPropertyCache().clear();
    Injector.getMessageBroker().publish(new PropertyChange(Properties.RESET, null));
    Injector.getMessageBroker().publish(new PropertyChange(Properties.MAXIMUM_CONCURRENT_NOTES, Integer.toString(Constants.DEFAULT_MAXIMUM_CONCURRENT_NOTES)));
    Injector.getMessageBroker().publish(new PropertyChange(Properties.INSTRUMENT_PREFIX + Injector.getInstruments().getDefaultInstrumentName(), Boolean.TRUE.toString()));
    return null;
  }

  @Override
  protected void onPropertyChange(PropertyChange propertyChange) {
    if (!propertyChange.getName().equals(Properties.RESET)) {
      resetTimer();
    }
  }

}