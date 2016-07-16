// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import com.example.afs.makingmusic.sound.Sound;

public class ActiveSound {

  private long offTime;
  private Sound sound;

  public ActiveSound(Sound value, long offTime) {
    this.sound = value;
    this.offTime = offTime;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ActiveSound other = (ActiveSound) obj;
    if (sound == null) {
      if (other.sound != null) {
        return false;
      }
    } else if (!sound.equals(other.sound)) {
      return false;
    }
    return true;
  }

  public long getOffTime() {
    return offTime;
  }

  public Sound getSound() {
    return sound;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sound == null) ? 0 : sound.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "ActiveSound [value=" + sound + ", offTime=" + offTime + "]";
  }

}