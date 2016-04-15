// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A step with a minimum interval between the start of each iteration. If a
 * previous iteration is not able to complete within this interval, the next
 * iteration is started immediately, and any missed iterations are ignored.
 * Contrast this with the fixed rate behavior of {@link Timer} which can get
 * further and further behind and run as an infinite loop until caught up.
 */
public class ScheduledStep<T> extends Step<T> {

  private AtomicLong previousMillis = new AtomicLong();
  private long intervalMillis;

  public ScheduledStep(long intervalMillis) {
    this.intervalMillis = intervalMillis;
  }

  @Override
  protected void runBody() {
    long sleepMillis;
    while ((sleepMillis = (previousMillis.get() + intervalMillis) - System.currentTimeMillis()) > 0) {
      try {
        // System.out.println("Sleeping for " + sleepMillis + " milliseconds.");
        Thread.sleep(sleepMillis);
      } catch (InterruptedException e) {
        // Recalculate and restart sleep
      }
    }
    resetTimer();
    super.runBody();
  }

  public void resetTimer() {
    previousMillis.set(System.currentTimeMillis());
  }

}
