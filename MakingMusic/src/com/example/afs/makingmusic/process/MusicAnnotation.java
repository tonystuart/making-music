// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.Sound;

public class MusicAnnotation {
  private Instrument instrument;
  private Sound sound;

  public MusicAnnotation(Instrument instrument, Sound sound) {
    this.instrument = instrument;
    this.sound = sound;
  }

  public Instrument getInstrument() {
    return instrument;
  }

  public Sound getSound() {
    return sound;
  }

  @Override
  public String toString() {
    return "MusicAnnotation [instrument=" + instrument + ", sound=" + sound + "]";
  }
}