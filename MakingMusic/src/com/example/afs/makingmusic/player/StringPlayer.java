// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.player;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.sound.Instrument;

public class StringPlayer extends ProgramPlayer {

  public StringPlayer(Synthesizer synthesizer, Instrument instrument, int channel) {
    super(synthesizer, instrument, channel);
  }

}