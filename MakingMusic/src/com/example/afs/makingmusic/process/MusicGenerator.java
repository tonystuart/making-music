// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Rect;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.common.MulDiv;
import com.example.afs.makingmusic.common.Step;
import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.InstrumentSets;
import com.example.afs.makingmusic.sound.Sound;

public class MusicGenerator extends Step<Frame> {

  private Set<ActiveSound> activeSounds;
  private Instrument[] instruments;
  private Synthesizer synthesizer;

  public MusicGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    activeSounds = new HashSet<>();
    synthesizer = new Synthesizer();
    setInstruments(InstrumentSets.medium);
  }

  @Override
  public void process(Frame frame) {
    int height = frame.getImage().height();
    int width = frame.getImage().width();
    long tick = System.currentTimeMillis();
    List<ActiveSound> expiringSounds = new ArrayList<>(activeSounds.size());
    for (ActiveSound activeSound : activeSounds) {
      if (activeSound.getOffTime() < tick) {
        synthesizer.releaseKey(activeSound.getChannel(), activeSound.getValue());
        expiringSounds.add(activeSound);
      }
    }
    activeSounds.removeAll(expiringSounds);
    for (Rect item : frame.getItems()) {
      Instrument instrument = MulDiv.scale(height, item.y, instruments);
      Sound sound = MulDiv.scale(width, item.x, instrument.getSounds());
      int channel = instrument.getChannel();
      int value = sound.getValue();
      ActiveSound activeSound = new ActiveSound(channel, value, tick + 1000);
      if (channel == 9 || !activeSounds.contains(activeSound)) {
        activeSounds.add(activeSound);
        synthesizer.pressKey(channel, value, instrument.getVelocity());
      }
    }
  }

  public void setInstruments(Instrument[] instruments) {
    this.instruments = instruments;
    for (Instrument instrument : instruments) {
      int channel = instrument.getChannel();
      if (channel != 9) {
        int program = instrument.getProgram();
        synthesizer.changeProgram(channel, program);
      }
    }
  }

}