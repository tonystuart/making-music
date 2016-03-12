// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TimeKeeper {

  private static class Observer {

    private long beginTime;
    private int totalObservations;
    private long totalTime;

    public void begin() {
      beginTime = System.currentTimeMillis();
    }

    public int getTotalObservations() {
      return totalObservations;
    }

    public long getTotalTime() {
      return totalTime;
    }

    private void end() {
      long endTime = System.currentTimeMillis();
      long elapsedTime = endTime - beginTime;
      totalTime += elapsedTime;
      totalObservations++;
      beginTime = 0;
    }

  }

  public static final String INPUT = "1 Input";
  public static final String LOOP = "5 Loop";
  public static final String OUTPUT = "4 Output";
  public static final String PROCESS = "3 Process";
  public static final String PROPERTY = "2 Property";

  public static final long REPORT_INTERVAL_MILLIS = 10000;

  private boolean isEnabled = false;
  private String name;
  private Map<String, TimeKeeper.Observer> observers = new HashMap<>();
  private long reportIntervalMillis;
  private long reportTime;

  public TimeKeeper() {
    this(null);
  }

  public TimeKeeper(String name) {
    this(name, REPORT_INTERVAL_MILLIS);
  }

  public TimeKeeper(String name, long reportIntervalMillis) {
    this.name = name;
    this.reportIntervalMillis = reportIntervalMillis;
  }

  public void begin(String sampleName) {
    if (isEnabled) {
      TimeKeeper.Observer observer = getObserver(sampleName);
      observer.begin();
    }
  }

  public void beginInput() {
    begin(INPUT);
  }

  public void beginLoop() {
    if (isEnabled) {
      begin(LOOP);
      if (reportTime == 0 && reportIntervalMillis != 0) {
        reportTime = System.currentTimeMillis() + reportIntervalMillis;
      }
    }
  }

  public void beginOutput() {
    begin(OUTPUT);
  }

  public void beginProcess() {
    begin(PROCESS);
  }

  public void beginProperty() {
    begin(PROPERTY);
  }

  public void clear() {
    observers.clear();
  }

  public void end(String sampleName) {
    if (isEnabled) {
      TimeKeeper.Observer observer = getObserver(sampleName);
      observer.end();
    }
  }

  public void endInput() {
    end(INPUT);
  }

  public void endLoop() {
    if (isEnabled) {
      end(LOOP);
      if (reportIntervalMillis == 0 || System.currentTimeMillis() >= reportTime) {
        report();
        clear();
        reportTime = 0;
      }
    }
  }

  public void endOutput() {
    end(OUTPUT);
  }

  public void endProcess() {
    end(PROCESS);
  }

  public void endProperty() {
    end(PROPERTY);
  }

  private TimeKeeper.Observer getObserver(String sampleName) {
    TimeKeeper.Observer observer = observers.get(sampleName);
    if (observer == null) {
      observer = new Observer();
      observers.put(sampleName, observer);
    }
    return observer;
  }

  private void report() {
    ArrayList<String> sampleNames = new ArrayList<>(observers.keySet());
    Collections.sort(sampleNames);
    // System.out.printf("%-32s %10s %10s\n", "Name", "Total #", "Avg ms");
    for (String sampleName : sampleNames) {
      TimeKeeper.Observer observer = getObserver(sampleName);
      if (observer.getTotalObservations() > 0) {
        String lineItemName = name == null ? sampleName : name + " " + sampleName;
        System.out.printf("%-32s %10d %10d\n", lineItemName, observer.getTotalObservations(), observer.getTotalTime() / observer.getTotalObservations());
      }
    }
  }

}