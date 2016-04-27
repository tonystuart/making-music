// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.utilities;

public class TimeUtilities {

  public static String getLabeledDuration(long elapsedTimeMillis) {
    String elapsedTime;
    long elapsedTimeSeconds = elapsedTimeMillis / 1000;
    int minutes = (int) ((elapsedTimeSeconds / 60) % 60);
    int hours = (int) (elapsedTimeSeconds / (60 * 60) % 24);
    int days = (int) (elapsedTimeSeconds / (60 * 60 * 24));
    if (days != 0) {
      elapsedTime = String.format("%dd %dh %dm", days, hours, minutes);
    } else if (hours != 0) {
      elapsedTime = String.format("%dh %dm", hours, minutes);
    } else {
      elapsedTime = String.format("%dm", minutes);
    }
    return elapsedTime;
  }

}
