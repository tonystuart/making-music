// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.sound;

public class Note extends Sound {

  public static String[] NAMES = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

  public static String getName(int value) {
    String note = NAMES[value % 12];
    int octave = (value / 12) + 1;
    String name = note + octave + " (" + value + ")";
    return name;
  }

  public Note(int value) {
    super(getName(value), value);
  }
}
