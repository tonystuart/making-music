// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.fluidsynth;

import java.util.concurrent.atomic.AtomicInteger;

public class Synthesizer {

  public static class Settings {
    private long settings;

    public Settings() {
      settings = FluidSynth.newFluidSettings();
    }

    public long getSettings() {
      return settings;
    }

    public void set(String name, String value) {
      FluidSynth.fluidSettingsSetstr(settings, name, value);
    }
  }

  static {
    System.loadLibrary(FluidSynth.NATIVE_LIBRARY_NAME);
  }

  private static Settings createDefaultSettings() {
    Settings settings = new Settings();
    settings.set("audio.driver", "alsa");
    // settings.set("synth.chorus.active", "no");
    // settings.set("synth.reverb.active", "no");
    return settings;
  }

  private AtomicInteger activeNoteCount = new AtomicInteger();
  private int maximumConcurrentNotes;
  private long synth;
  private AtomicInteger totalNoteCount = new AtomicInteger();

  public Synthesizer() {
    this(createDefaultSettings());
  }

  public Synthesizer(Settings settings) {
    synth = FluidSynth.newFluidSynth(settings.getSettings());
    FluidSynth.newFluidAudioDriver(settings.getSettings(), synth);
    FluidSynth.fluidSynthSfload(synth, "/usr/share/sounds/sf2/FluidR3_GM.sf2", 1);
  }

  /**
   * Modifies the pitch on the channel.
   * 
   * @param channel
   *          channel whose pitch is to be modified
   * @param val
   *          adjustment to pitch in four semitone range (0-16383 with 8192
   *          being center)
   */
  public void bendPitch(int channel, int val) {
    FluidSynth.fluidSynthPitchBend(synth, channel, val);
  }

  public void changeProgram(int channel, int program) {
    FluidSynth.fluidSynthProgramChange(synth, channel, program);
  }

  public int getActiveNoteCount() {
    return activeNoteCount.get();
  }

  public int getMaximumConcurrentNotes() {
    return maximumConcurrentNotes;
  }

  public int getTotalNoteCount() {
    return totalNoteCount.get();
  }

  public void pressKey(int channel, int key, int velocity) {
    totalNoteCount.incrementAndGet();
    if (activeNoteCount.incrementAndGet() <= maximumConcurrentNotes) {
      FluidSynth.fluidSynthNoteon(synth, channel, key, velocity);
    }
  }

  public void releaseKey(int channel, int key) {
    activeNoteCount.decrementAndGet();
    FluidSynth.fluidSynthNoteoff(synth, channel, key);
  }

  /**
   * Modifies the gain for the synthesizer.
   * 
   * @param gain
   *          value in the range 0.0 to 10.0
   */
  public void setGain(float gain) {
    FluidSynth.fluidSynthSetGain(synth, gain);
  }

  public void setMaximumConcurrentNotes(int maximumConcurrentNotes) {
    this.maximumConcurrentNotes = maximumConcurrentNotes;
  }
}
