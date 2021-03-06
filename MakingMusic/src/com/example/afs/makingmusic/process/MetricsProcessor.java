// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.util.LinkedList;

import com.example.afs.makingmusic.common.History;
import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.MessageReceiver.MonitorStyle;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.ScheduledStep;
import com.example.afs.makingmusic.constants.FileNames;
import com.example.afs.makingmusic.constants.Limits;
import com.example.afs.makingmusic.constants.Property;
import com.example.afs.makingmusic.utilities.FileUtilities;
import com.example.afs.makingmusic.utilities.TimeUtilities;

public class MetricsProcessor extends ScheduledStep<Void> {

  private History history;
  private int instruments;
  private int properties;
  private long startTimeMillis;

  public MetricsProcessor(long intervalMillis) {
    super(intervalMillis);
    startTimeMillis = System.currentTimeMillis();
    restoreMetrics();
    monitorPropertyChange(MonitorStyle.ASYNC);
  }

  @Override
  public Void process() throws InterruptedException {
    long currentTimeMillis = System.currentTimeMillis();
    long elapsedTimeMillis = currentTimeMillis - startTimeMillis;
    Injector.getMetrics().setUptime(TimeUtilities.getLabeledDuration(elapsedTimeMillis));
    Injector.getMetrics().setCpu(FileUtilities.read(FileNames.LOADAVG));
    Injector.getMetrics().setTemperature(Integer.parseInt(FileUtilities.read(FileNames.TEMPERATURE).trim()));
    FileUtilities.writeJson(FileNames.METRICS, history);
    return null;
  }

  @Override
  protected void onAsynchronousPropertyChange(PropertyChange propertyChange) {
    if (propertyChange.getName().startsWith(Property.Names.INSTRUMENT_PREFIX)) {
      Injector.getMetrics().setInstruments(++instruments);
    } else if (!propertyChange.getName().equals(Property.Names.RESET)) {
      Injector.getMetrics().setProperties(++properties);
    }
  }

  private void restoreMetrics() {
    try {
      history = FileUtilities.readJson(FileNames.METRICS, History.class);
      while (history.getMetrics().size() >= Limits.HISTORY_SIZE) {
        history.getMetrics().removeFirst();
      }
    } catch (RuntimeException e) {
      history = new History();
      history.setMetrics(new LinkedList<>());
    }
    history.getMetrics().add(Injector.getMetrics());
  }

}