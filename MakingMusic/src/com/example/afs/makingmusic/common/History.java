// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.LinkedList;

public class History {
  private LinkedList<Metrics> metrics;

  public LinkedList<Metrics> getMetrics() {
    return metrics;
  }

  public void setMetrics(LinkedList<Metrics> metrics) {
    this.metrics = metrics;
  }
}
