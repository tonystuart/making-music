// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.constants;

import com.example.afs.makingmusic.common.Injector;

public class Property {

  public enum AssignmentMethod {
    COLOR, POSITION
  }

  public static class Defaults {
    public static final AssignmentMethod ASSIGNMENT_METHOD = AssignmentMethod.POSITION;
    public static final String INSTRUMENT = Injector.getInstruments().getDefaultInstrumentName();
    public static final int MAXIMUM_CONCURRENT_NOTES = 20;
  }

  public static class Names {
    public static final String ASSIGNMENT_METHOD = "enum-assignment-method";
    public static final String INSTRUMENT_PREFIX = "instrument-";
    public static final String MAXIMUM_CONCURRENT_NOTES = "maximum-concurrent-notes";
    public static final String RESET = "reset";
  }
}
