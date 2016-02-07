// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.sound;

import java.util.Arrays;

import com.example.afs.makingmusic.theory.Keys;

public class Instrument {

  public static final Instrument flute = new Instrument("Flute", 0, 74 - 1, 64, Keys.CMajorMiddle.getSounds());
  public static final Instrument frenchHorn = new Instrument("French Horn", 0, 61 - 1, 64, Keys.CMajorLow.getSounds());
  public static final Instrument piano = new Instrument("Piano", 0, 1 - 1, 64, Keys.CMajorFull.getSounds());
  public static final Instrument sax = new Instrument("Sax", 0, 65 - 1, 64, Keys.CMajorMiddle.getSounds());
  public static final Instrument synthBass1 = new Instrument("Synth Bass 1", 0, 39 - 1, 64, Keys.CMajorLow.getSounds());
  public static final Instrument violin = new Instrument("Violin", 0, 41 - 1, 64, Keys.CMajorMiddle.getSounds());

  private int channel;
  private String name;
  private int program;
  private Sound[] sounds;
  private int velocity;

  public Instrument(String name, int channel, int program, int velocity, Sound... sounds) {
    this.name = name;
    this.channel = channel;
    this.program = program;
    this.velocity = velocity;
    this.sounds = sounds;
  }

  public int getChannel() {
    return channel;
  }

  public String getName() {
    return name;
  }

  public int getProgram() {
    return program;
  }

  public Sound[] getSounds() {
    return sounds;
  }

  public int getVelocity() {
    return velocity;
  }

  @Override
  public String toString() {
    return "Instrument [name=" + name + ", channel=" + channel + ", program=" + program + ", velocity=" + velocity + ", sounds=" + Arrays.toString(sounds) + "]";
  }

}
