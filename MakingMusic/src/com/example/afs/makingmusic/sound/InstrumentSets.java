// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.sound;

public class InstrumentSets {

  public static Instrument[] large = new Instrument[] {
      Instrument.piano,
      Instrument.violin,
      Instrument.flute,
      Instrument.sax,
      Instrument.frenchHorn,
      Instrument.synthBass1,
      DrumSets.smallDrumSet
  };

  public static Instrument[] medium = new Instrument[] {
      Instrument.piano,
      DrumSets.smallDrumSet
  };

  public static Instrument[] small = new Instrument[] {
    Instrument.piano,
  };
}
