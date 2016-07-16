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

public class Instrument {

  // See https://en.wikipedia.org/wiki/Hornbostel%E2%80%93Sachs
  public enum Type {
    DRUM, KEYBOARD, STRING, WIND
  }

  private String name;
  private int programIndex;
  private Sound[] sounds;
  private Type type;

  public Instrument(String name, Drum... drums) {
    this.type = Type.DRUM;
    this.name = name;
    this.sounds = drums;
  }

  public Instrument(Type type, String name, int programNumber, Sound... sounds) {
    this.type = type;
    this.name = name;
    this.programIndex = programNumber - 1;
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

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Instrument [type=" + type + ", name=" + name + ", programIndex=" + programIndex + ", sounds=" + Arrays.toString(sounds) + "]";
  }

}
