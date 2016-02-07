// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.fluidsynth;

public class FluidSynth {

  public static final String NATIVE_LIBRARY_NAME = "fluidsynth_jni";

  public static native void deleteFluidAudioDriver(long driver);

  public static native void deleteFluidSettings(long settings);

  public static native void deleteFluidSynth(long synth);

  public static native void fluidSettingsSetstr(long settings, String name, String str);
  
  public static native int fluidSynthCc(long synth, int chan, int num, int val);

  public static native int fluidSynthNoteoff(long synth, int chan, int key);

  public static native int fluidSynthNoteon(long synth, int chan, int key, int vel);
  
  public static native int fluidSynthProgramChange(long synth, int chan, int program);
  
  public static native long fluidSynthSfload(long synth, String filename, int resetPresets);

  public static native long newFluidAudioDriver(long settings, long synth);

  public static native long newFluidSettings();

  public static native long newFluidSynth(long settings);

}
