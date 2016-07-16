// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.player;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.process.ActiveSound;
import com.example.afs.makingmusic.sound.Instrument;

public class WindPlayer extends ProgramPlayer {

  public WindPlayer(Synthesizer synthesizer, Instrument instrument, int channel) {
    super(synthesizer, instrument, channel);
  }

  public void play(ActiveSound activeSound) {
    super.play(activeSound);
    startPitchBender();
  }

  @Override
  protected long getDuration() {
    return 2000;
  }

  
  @Override
  protected boolean isPlayable() {
    return activeSounds.size() == 0;
  }

  private void startPitchBender() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        long timeRemaining = getDuration();
        long interval = timeRemaining / 16;
        int bendIncrement = 4096 / 16;
        int bend = 8192;
        while (timeRemaining > 0) {
          try {
            System.out.println("timeRemaining=" + timeRemaining + ", interval=" + interval);
            Thread.sleep(interval);
          } catch (InterruptedException e) {
            // ignore
          }
          timeRemaining -= interval;
          System.out.println("channel=" + channel + ", bend=" + bend);
          synthesizer.bendPitch(channel, bend);
          bend += bendIncrement;
        }
        System.out.println("terminating");
      }
    }).start();
  }

}