// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.constants;

import java.util.concurrent.TimeUnit;

// All intervals are in milliseconds unless otherwise noted.

public class Durations {

  public static final long FRAME_INTERVAL = TimeUnit.SECONDS.toMillis(1) / 8;
  public static final long METRICS_INTERVAL = TimeUnit.SECONDS.toMillis(5);
  public static final long NOTE_DURATION = TimeUnit.SECONDS.toMillis(1);
  public static final long RESET_INTERVAL = TimeUnit.MINUTES.toMillis(5);

}
