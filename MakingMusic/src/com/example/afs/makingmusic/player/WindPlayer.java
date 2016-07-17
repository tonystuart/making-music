// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.player;

import java.util.List;

import org.opencv.core.Rect;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.common.MulDiv;
import com.example.afs.makingmusic.process.Frame;
import com.example.afs.makingmusic.process.MusicAnnotation;
import com.example.afs.makingmusic.process.MusicAnnotation.Type;
import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.Sound;

public class WindPlayer implements Player {

  private class PitchBender extends Thread {

    private static final int CENTER = 8192;
    private boolean terminated;

    public PitchBender(String instrumentName) {
      super("PitchBender " + instrumentName);
    }

    @Override
    public void run() {
      while (!terminated) {
        if (nextValue == 0) {
          if (currentValue != 0) {
            // play with current sound;
          } else {
            // nothing is playing
          }
        } else {
          if (currentValue == 0) {
            // nothing is playing, just start new sound
            startNext();
          } else {
            // something is playing, transition to next note
            int bend = CENTER;
            int increment = nextValue > currentValue ? 10 : -10;
            for (int i = 0; i < 50; i++) {
              System.out.println("counter=" + i + ", bend=" + bend + ", increment=" + increment + ", current=" + currentValue);
              synthesizer.bendPitch(channel, bend);
              bend += increment;
              pause(2);
            }
            startNext();
          }
        }
        pause(100);
      }
      stopCurrent();
      System.out.println("Terninating...");
    }

    public void terminate() {
      terminated = true;
    }

    private void pause(long millis) {
      try {
        sleep(millis);
      } catch (InterruptedException e) {
        // ignore
      }
    }
  }

  private int channel;
  private int currentValue;
  private long expirationTick;
  private Instrument instrument;
  private int nextValue;
  private PitchBender pitchBender;
  private Synthesizer synthesizer;

  public WindPlayer(Synthesizer synthesizer, Instrument instrument, int channel) {
    this.synthesizer = synthesizer;
    this.instrument = instrument;
    this.channel = channel;
    int programIndex = instrument.getProgramIndex();
    synthesizer.changeProgram(channel, programIndex);
    pitchBender = new PitchBender(instrument.getName());
    pitchBender.start();
  }

  @Override
  public void play(Frame frame, List<Rect> items, long tick) {
    if (items.size() == 0) {
      stopExpiringSounds(tick);
    } else {
      Rect containingItem = getContainingItem(items);
      Sound sound = getSound(frame, containingItem);
      if (sound.getValue() == currentValue) {
        frame.addMusicAnnotation(new MusicAnnotation(containingItem, instrument, sound, Type.ACTIVE));
      } else {
        nextValue = sound.getValue();
        frame.addMusicAnnotation(new MusicAnnotation(containingItem, instrument, sound, Type.NEW));
      }
      expirationTick = tick + getDuration();
    }
  }

  @Override
  public void terminate() {
    stopCurrent();
    pitchBender.terminate();
  }

  private Rect getContainingItem(List<Rect> items) {
    int x1 = Integer.MAX_VALUE;
    int y1 = Integer.MAX_VALUE;
    int x2 = 0;
    int y2 = 0;
    for (Rect item : items) {
      x1 = Math.min(x1, item.x);
      y1 = Math.min(y1, item.y);
      x2 = Math.max(x2, item.x + item.width);
      y2 = Math.max(y2, item.y + item.height);
    }
    Rect containingItem = new Rect(x1, y1, x2 - x1, y2 - y1);
    return containingItem;
  }

  private long getDuration() {
    return 3000;
  }

  private Sound getSound(Frame frame, Rect item) {
    int width = frame.getImageMatrix().width();
    Sound sound = MulDiv.scale(width, item.x + (item.width / 2), instrument.getSounds());
    return sound;
  }

  private void startNext() {
    stopCurrent();
    synthesizer.pressKey(channel, nextValue, 96);
    currentValue = nextValue;
    nextValue = 0;
  }

  private void stopCurrent() {
    if (currentValue != 0) {
      synthesizer.releaseKey(channel, currentValue);
      currentValue = 0;
    }
  }

  private void stopExpiringSounds(long tick) {
    if (currentValue != 0 && tick > expirationTick) {
      stopCurrent();
    }
  }

}