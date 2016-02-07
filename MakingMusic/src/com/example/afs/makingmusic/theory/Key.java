// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.theory;

import com.example.afs.makingmusic.sound.Note;
import com.example.afs.makingmusic.sound.Sound;

public class Key {

  public static Sound[] createSounds(int tonic, int octaveCount, Scale scale) {
    int noteCount = octaveCount * 7;
    Sound[] sounds = new Sound[noteCount];
    int[] intervals = scale.getIntervals();
    for (int i = 0, value = tonic; i < noteCount; i++) {
      Note note = new Note(value);
      sounds[i] = note;
      int scaleIndex = i % intervals.length;
      int halfStepsToNextNote = intervals[scaleIndex];
      value += halfStepsToNextNote;
    }
    return sounds;
  }

  private String name;
  private int octaveCount;
  private Scale scale;
  private Sound[] sounds;
  private int tonic;

  public Key(String name, int tonic, int octaveCount, Scale scale) {
    this.name = name;
    this.tonic = tonic;
    this.octaveCount = octaveCount;
    this.scale = scale;
    this.sounds = createSounds(tonic, octaveCount, scale);
  }

  public String getName() {
    return name;
  }

  public int getOctaveCount() {
    return octaveCount;
  }

  public Scale getScale() {
    return scale;
  }

  public Sound[] getSounds() {
    return sounds;
  }

  public int getTonic() {
    return tonic;
  }

  @Override
  public String toString() {
    return "Key [name=" + name + ", tonic=" + tonic + ", octaveCount=" + octaveCount + ", scale=" + scale + "]";
  }

}
