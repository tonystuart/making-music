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
import com.example.afs.makingmusic.sound.Instrument.Type;
import com.example.afs.makingmusic.theory.Keys;

public class Instruments {

  private Map<String, Instrument> map = new LinkedHashMap<>();

  public Instruments() {
    add("acoustic-grand-piano", Type.KEYBOARD, 1);
    add("bright-acoustic-piano", Type.KEYBOARD, 2);
    add("electric-grand-piano", Type.KEYBOARD, 3);
    add("honky-tonk-piano", Type.KEYBOARD, 4);
    add("electric-piano-1", Type.KEYBOARD, 5);
    add("electric-piano-2", Type.KEYBOARD, 6);
    add("harpsichord", Type.KEYBOARD, 7);
    add("clavinet", Type.KEYBOARD, 8);
    add("celesta", Type.KEYBOARD, 9);
    add("glockenspiel", Type.KEYBOARD, 10);
    add("music-box", Type.KEYBOARD, 11);
    add("vibraphone", Type.KEYBOARD, 12);
    add("marimba", Type.KEYBOARD, 13);
    add("xylophone", Type.KEYBOARD, 14);
    add("tubular-bells", Type.KEYBOARD, 15);
    add("dulcimer", Type.KEYBOARD, 16);
    add("drawbar-organ", Type.KEYBOARD, 17);
    add("percussive-organ", Type.KEYBOARD, 18);
    add("rock-organ", Type.KEYBOARD, 19);
    add("church-organ", Type.KEYBOARD, 20);
    add("reed-organ", Type.KEYBOARD, 21);
    add("accordion", Type.KEYBOARD, 22);
    add("harmonica", Type.KEYBOARD, 23);
    add("tango-accordion", Type.KEYBOARD, 24);
    add("acoustic-guitar-nylon", Type.STRING, 25);
    add("acoustic-guitar-steel", Type.STRING, 26);
    add("electric-guitar-jazz", Type.STRING, 27);
    add("electric-guitar-clean", Type.STRING, 28);
    add("electric-guitar-muted", Type.STRING, 29);
    add("overdriven-guitar", Type.STRING, 30);
    add("distortion-guitar", Type.STRING, 31);
    add("guitar-harmonics", Type.STRING, 32);
    add("acoustic-bass", Type.STRING, 33);
    add("electric-bass-finger", Type.STRING, 34);
    add("electric-bass-pick", Type.STRING, 35);
    add("fretless-bass", Type.STRING, 36);
    add("slap-bass-1", Type.STRING, 37);
    add("slap-bass-2", Type.STRING, 38);
    add("synth-bass-1", Type.STRING, 39);
    add("synth-bass-2", Type.STRING, 40);
    add("violin", Type.STRING, 41);
    add("viola", Type.STRING, 42);
    add("cello", Type.STRING, 43);
    add("contrabass", Type.STRING, 44);
    add("tremolo-strings", Type.STRING, 45);
    add("pizzicato-strings", Type.STRING, 46);
    add("orchestral-harp", Type.STRING, 47);
    add("timpani", Type.STRING, 48);
    add("string-ensemble-1", Type.STRING, 49);
    add("string-ensemble-2", Type.STRING, 50);
    add("synth-strings-1", Type.STRING, 51);
    add("synth-strings-2", Type.STRING, 52);
    add("choir-aahs", Type.KEYBOARD, 53);
    add("voice-oohs", Type.KEYBOARD, 54);
    add("synth-choir", Type.KEYBOARD, 55);
    add("orchestra-hit", Type.KEYBOARD, 56);
    add("trumpet", Type.WIND, 57);
    add("trombone", Type.WIND, 58);
    add("tuba", Type.WIND, 59);
    add("muted-trumpet", Type.WIND, 60);
    add("french-horn", Type.WIND, 61);
    add("brass-section", Type.WIND, 62);
    add("synth-brass-1", Type.WIND, 63);
    add("synth-brass-2", Type.WIND, 64);
    add("soprano-sax", Type.WIND, 65);
    add("alto-sax", Type.WIND, 66);
    add("tenor-sax", Type.WIND, 67);
    add("baritone-sax", Type.WIND, 68);
    add("oboe", Type.WIND, 69);
    add("english-horn", Type.WIND, 70);
    add("bassoon", Type.WIND, 71);
    add("clarinet", Type.WIND, 72);
    add("piccolo", Type.WIND, 73);
    add("flute", Type.WIND, 74);
    add("recorder", Type.WIND, 75);
    add("pan-flute", Type.WIND, 76);
    add("blown-bottle", Type.WIND, 77);
    add("shakuhachi", Type.WIND, 78);
    add("whistle", Type.WIND, 79);
    add("ocarina", Type.WIND, 80);
    add("lead-1-square", Type.KEYBOARD, 81);
    add("lead-2-sawtooth", Type.KEYBOARD, 82);
    add("lead-3-calliope", Type.KEYBOARD, 83);
    add("lead-4-chiff", Type.KEYBOARD, 84);
    add("lead-5-charang", Type.KEYBOARD, 85);
    add("lead-6-voice", Type.KEYBOARD, 86);
    add("lead-7-fifths", Type.KEYBOARD, 87);
    add("lead-8-bass-+-lead", Type.KEYBOARD, 88);
    add("pad-1-new-age", Type.KEYBOARD, 89);
    add("pad-2-warm", Type.KEYBOARD, 90);
    add("pad-3-polysynth", Type.KEYBOARD, 91);
    add("pad-4-choir", Type.KEYBOARD, 92);
    add("pad-5-bowed", Type.KEYBOARD, 93);
    add("pad-6-metallic", Type.KEYBOARD, 94);
    add("pad-7-halo", Type.KEYBOARD, 95);
    add("pad-8-sweep", Type.KEYBOARD, 96);
    add("fx-1-rain", Type.KEYBOARD, 97);
    add("fx-2-soundtrack", Type.KEYBOARD, 98);
    add("fx-3-crystal", Type.KEYBOARD, 99);
    add("fx-4-atmosphere", Type.KEYBOARD, 100);
    add("fx-5-brightness", Type.KEYBOARD, 101);
    add("fx-6-goblins", Type.KEYBOARD, 102);
    add("fx-7-echoes", Type.KEYBOARD, 103);
    add("fx-8-sci-fi", Type.KEYBOARD, 104);
    add("sitar", Type.KEYBOARD, 105);
    add("banjo", Type.KEYBOARD, 106);
    add("shamisen", Type.KEYBOARD, 107);
    add("koto", Type.KEYBOARD, 108);
    add("kalimba", Type.KEYBOARD, 109);
    add("bagpipe", Type.KEYBOARD, 110);
    add("fiddle", Type.KEYBOARD, 111);
    add("shanai", Type.KEYBOARD, 112);
    add("tinkle-bell", Type.KEYBOARD, 113);
    add("agogo", Type.KEYBOARD, 114);
    add("steel-drums", Type.KEYBOARD, 115);
    add("woodblock", Type.KEYBOARD, 116);
    add("taiko-drum", Type.KEYBOARD, 117);
    add("melodic-tom", Type.KEYBOARD, 118);
    add("synth-drum", Type.KEYBOARD, 119);
    add("reverse-cymbal", Type.KEYBOARD, 120);
    add("guitar-fret-noise", Type.KEYBOARD, 121);
    add("breath-noise", Type.KEYBOARD, 122);
    add("seashore", Type.KEYBOARD, 123);
    add("bird-tweet", Type.KEYBOARD, 124);
    add("telephone-ring", Type.KEYBOARD, 125);
    add("helicopter", Type.KEYBOARD, 126);
    add("applause", Type.KEYBOARD, 127);
    add("gunshot", Type.KEYBOARD, 128);

    add("drum-kit-1", Injector.getDrums().getDrumKit1());
    add("drum-kit-2", Injector.getDrums().getDrumKit2());
    add("drum-kit-3", Injector.getDrums().getDrumKit3());
  }

  public boolean contains(String name) {
    return map.containsKey(name);
  }

  public Instrument find(String name) {
    return map.get(name);
  };

  public String getDefaultInstrumentName() {
    return map.keySet().iterator().next();
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

  private void add(String name, Type type, int programNumber) {
    map.put(name, new Instrument(type, name, programNumber, Keys.CMajorMiddle.getSounds()));
  }

}
