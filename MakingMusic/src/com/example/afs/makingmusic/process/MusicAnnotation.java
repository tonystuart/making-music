// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import org.opencv.core.Rect;

import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.Sound;

public class MusicAnnotation {

  public enum Type {
    DUPLICATE, NEW, OLD, OVERFLOW
  }

  private Instrument instrument;
  private Rect item;
  private Sound sound;
  private Type type;

  public MusicAnnotation(Rect item, Instrument instrument, Sound sound, Type type) {
    this.item = item;
    this.instrument = instrument;
    this.sound = sound;
    this.type = type;
  }

  public Instrument getInstrument() {
    return instrument;
  }

  public Rect getItem() {
    return item;
  }

  public Sound getSound() {
    return sound;
  }

  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return "MusicAnnotation [instrument=" + instrument + ", sound=" + sound + ", type=" + type + "]";
  }
}