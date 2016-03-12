// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.sound;

public class Instrument {

  private boolean isDrumKit;
  private String name;
  private int programIndex;
  private Sound[] sounds;
  private int velocity;

  public Instrument(String name, int channel, int programNumber, int velocity, Drum... drums) {
    this.name = name;
    this.programIndex = programNumber - 1;
    this.velocity = velocity;
    this.sounds = drums;
    this.isDrumKit = true;
  }

  public Instrument(String name, int channel, int programNumber, int velocity, Sound... sounds) {
    this.name = name;
    this.programIndex = programNumber - 1;
    this.velocity = velocity;
    this.sounds = sounds;
  }

  public String getName() {
    return name;
  }

  public int getProgramIndex() {
    return programIndex;
  }

  public Sound[] getSounds() {
    return sounds;
  }

  public int getVelocity() {
    return velocity;
  }

  public boolean isDrumKit() {
    return isDrumKit;
  }

  @Override
  public String toString() {
    return "Instrument [name=" + name + ", programIndex=" + programIndex + ", velocity=" + velocity + ", isDrums=" + isDrumKit + "]";
  }

}
