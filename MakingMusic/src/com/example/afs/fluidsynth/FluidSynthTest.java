// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.fluidsynth;

public class FluidSynthTest {

  public static void main(String[] args) throws Exception {
    System.loadLibrary(FluidSynth.NATIVE_LIBRARY_NAME);
    long settings = FluidSynth.newFluidSettings();
    long synth = FluidSynth.newFluidSynth(settings);
    FluidSynth.fluidSettingsSetstr(settings, "audio.driver", "alsa");
    long adriver = FluidSynth.newFluidAudioDriver(settings, synth);
    FluidSynth.fluidSynthSfload(synth, "/usr/share/sounds/sf2/FluidR3_GM.sf2", 1);
    for (int i = 0; i < 12; i++) {
      FluidSynth.fluidSynthNoteon(synth, 0, 60 + i, 64);
      Thread.sleep(500);
      FluidSynth.fluidSynthNoteoff(synth, 0, 60 + i);
    }
    FluidSynth.deleteFluidAudioDriver(adriver);
    FluidSynth.deleteFluidSynth(synth);
    FluidSynth.deleteFluidSettings(settings);
  }
}
