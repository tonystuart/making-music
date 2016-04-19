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

public class Time {

  public static final long FRAME_RATE_MILLIS = TimeUnit.SECONDS.toMillis(1) / 8;
  public static final long RESET_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(5);

}
