// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.band;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.afs.makingmusic.sound.Drum;
import com.example.afs.makingmusic.sound.Instrument;

public class Drums {

  private Instrument drumKit1;
  private Instrument drumKit2;
  private Instrument drumKit3;
  private Map<String, Drum> map = new LinkedHashMap<>();

  public Drums() {
    add("bass-drum-2", 35);
    add("bass-drum-1", 36);
    add("side-stick/rimshot", 37);
    add("snare-drum-1", 38);
    add("hand-clap", 39);
    add("snare-drum-2", 40);
    add("low-tom-2", 41);
    add("closed-hi-hat", 42);
    add("low-tom-1", 43);
    add("pedal-hi-hat", 44);
    add("mid-tom-2", 45);
    add("open-hi-hat", 46);
    add("mid-tom-1", 47);
    add("high-tom-2", 48);
    add("crash-cymbal-1", 49);
    add("high-tom-1", 50);
    add("ride-cymbal-1", 51);
    add("chinese-cymbal", 52);
    add("ride-bell", 53);
    add("tambourine", 54);
    add("splash-cymbal", 55);
    add("cowbell", 56);
    add("crash-cymbal-2", 57);
    add("vibra-slap", 58);
    add("ride-cymbal-2", 59);
    add("high-bongo", 60);
    add("low-bongo", 61);
    add("mute-high-conga", 62);
    add("open-high-conga", 63);
    add("low-conga", 64);
    add("high-timbale", 65);
    add("low-timbale", 66);
    add("high-agogô", 67);
    add("low-agogô", 68);
    add("cabasa", 69);
    add("maracas", 70);
    add("short-whistle", 71);
    add("long-whistle", 72);
    add("short-güiro", 73);
    add("long-güiro", 74);
    add("claves", 75);
    add("high-wood-block", 76);
    add("low-wood-block", 77);
    add("mute-cuíca", 78);
    add("open-cuíca", 79);
    add("mute-triangle", 80);
    add("open-triangle", 81);
  }

  public void add(String name, int value) {
    map.put(name, new Drum(name, value));
  }

  public Drum getDrum(String name) {
    return map.get(name);
  }

  public Instrument getDrumKit1() {
    if (drumKit1 == null) {
      drumKit1 = new Instrument("drum-kit-1", 9, 0, 127, //
          getDrum("low-wood-block"), //
          getDrum("hand-clap"), //
          getDrum("cowbell"), //
          getDrum("tambourine"), //
          getDrum("high-wood-block"), //
          getDrum("open-triangle")); //
    }
    return drumKit1;
  }

  public Instrument getDrumKit2() {
    if (drumKit2 == null) {
      drumKit2 = new Instrument("drum-kit-2", 9, 0, 127, //
          getDrum("snare-drum-1"), //
          getDrum("low-tom-1"), //
          getDrum("mid-tom-1"), //
          getDrum("high-tom-1"), //
          getDrum("ride-cymbal-1")); //
    }
    return drumKit2;
  }

  public Instrument getDrumKit3() {
    if (drumKit3 == null) {
      drumKit3 = new Instrument("drum-kit-3", 9, 0, 127, //
          getDrum("bass-drum-1"));
    }
    return drumKit3;
  }

}
