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
import java.util.Map.Entry;
import java.util.Set;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.theory.Keys;

public class Instruments {

  private Map<String, Instrument> map = new LinkedHashMap<>();

  public Instruments() {
    add("acoustic-grand-piano", 1);
    add("bright-acoustic-piano", 2);
    add("electric-grand-piano", 3);
    add("honky-tonk-piano", 4);
    add("electric-piano-1", 5);
    add("electric-piano-2", 6);
    add("harpsichord", 7);
    add("clavinet", 8);
    add("celesta", 9);
    add("glockenspiel", 10);
    add("music-box", 11);
    add("vibraphone", 12);
    add("marimba", 13);
    add("xylophone", 14);
    add("tubular-bells", 15);
    add("dulcimer", 16);
    add("drawbar-organ", 17);
    add("percussive-organ", 18);
    add("rock-organ", 19);
    add("church-organ", 20);
    add("reed-organ", 21);
    add("accordion", 22);
    add("harmonica", 23);
    add("tango-accordion", 24);
    add("acoustic-guitar-nylon", 25);
    add("acoustic-guitar-steel", 26);
    add("electric-guitar-jazz", 27);
    add("electric-guitar-clean", 28);
    add("electric-guitar-muted", 29);
    add("overdriven-guitar", 30);
    add("distortion-guitar", 31);
    add("guitar-harmonics", 32);
    add("acoustic-bass", 33);
    add("electric-bass-finger", 34);
    add("electric-bass-pick", 35);
    add("fretless-bass", 36);
    add("slap-bass-1", 37);
    add("slap-bass-2", 38);
    add("synth-bass-1", 39);
    add("synth-bass-2", 40);
    add("violin", 41);
    add("viola", 42);
    add("cello", 43);
    add("contrabass", 44);
    add("tremolo-strings", 45);
    add("pizzicato-strings", 46);
    add("orchestral-harp", 47);
    add("timpani", 48);
    add("string-ensemble-1", 49);
    add("string-ensemble-2", 50);
    add("synth-strings-1", 51);
    add("synth-strings-2", 52);
    add("choir-aahs", 53);
    add("voice-oohs", 54);
    add("synth-choir", 55);
    add("orchestra-hit", 56);
    add("trumpet", 57);
    add("trombone", 58);
    add("tuba", 59);
    add("muted-trumpet", 60);
    add("french-horn", 61);
    add("brass-section", 62);
    add("synth-brass-1", 63);
    add("synth-brass-2", 64);
    add("soprano-sax", 65);
    add("alto-sax", 66);
    add("tenor-sax", 67);
    add("baritone-sax", 68);
    add("oboe", 69);
    add("english-horn", 70);
    add("bassoon", 71);
    add("clarinet", 72);
    add("piccolo", 73);
    add("flute", 74);
    add("recorder", 75);
    add("pan-flute", 76);
    add("blown-bottle", 77);
    add("shakuhachi", 78);
    add("whistle", 79);
    add("ocarina", 80);
    add("lead-1-square", 81);
    add("lead-2-sawtooth", 82);
    add("lead-3-calliope", 83);
    add("lead-4-chiff", 84);
    add("lead-5-charang", 85);
    add("lead-6-voice", 86);
    add("lead-7-fifths", 87);
    add("lead-8-bass-+-lead", 88);
    add("pad-1-new-age", 89);
    add("pad-2-warm", 90);
    add("pad-3-polysynth", 91);
    add("pad-4-choir", 92);
    add("pad-5-bowed", 93);
    add("pad-6-metallic", 94);
    add("pad-7-halo", 95);
    add("pad-8-sweep", 96);
    add("fx-1-rain", 97);
    add("fx-2-soundtrack", 98);
    add("fx-3-crystal", 99);
    add("fx-4-atmosphere", 100);
    add("fx-5-brightness", 101);
    add("fx-6-goblins", 102);
    add("fx-7-echoes", 103);
    add("fx-8-sci-fi", 104);
    add("sitar", 105);
    add("banjo", 106);
    add("shamisen", 107);
    add("koto", 108);
    add("kalimba", 109);
    add("bagpipe", 110);
    add("fiddle", 111);
    add("shanai", 112);
    add("tinkle-bell", 113);
    add("agogo", 114);
    add("steel-drums", 115);
    add("woodblock", 116);
    add("taiko-drum", 117);
    add("melodic-tom", 118);
    add("synth-drum", 119);
    add("reverse-cymbal", 120);
    add("guitar-fret-noise", 121);
    add("breath-noise", 122);
    add("seashore", 123);
    add("bird-tweet", 124);
    add("telephone-ring", 125);
    add("helicopter", 126);
    add("applause", 127);
    add("gunshot", 128);

    add("drum-kit-1", Injector.getDrums().getDrumKit1());
    add("drum-kit-2", Injector.getDrums().getDrumKit2());
    add("drum-kit-3", Injector.getDrums().getDrumKit3());
  }

  public boolean contains(String name) {
    return map.containsKey(name);
  };

  public Instrument find(String name) {
    return map.get(name);
  }

  public Instrument[] getInstruments(Set<String> names) {
    int instrumentIndex = 0;
    Instrument[] instruments = new Instrument[names.size()];
    for (Entry<String, Instrument> entry : map.entrySet()) {
      if (names.contains(entry.getKey())) {
        instruments[instrumentIndex++] = entry.getValue();
      }
    }
    return instruments;
  }

  private void add(String name, Instrument instrument) {
    map.put(name, instrument);
  }

  private void add(String name, int programNumber) {
    map.put(name, new Instrument(name, 0, programNumber, 127, Keys.CMajorFull.getSounds()));
  }

}
