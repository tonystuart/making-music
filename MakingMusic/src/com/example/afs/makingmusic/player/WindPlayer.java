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

  private static final long DELAY = 10;
  private static final float GAIN_BEND = 0.1f;
  private static final float GAIN_CENTER = 0.2f;
  private static final int PITCH_BEND = 1000;
  private static final int PITCH_CENTER = 8192;
  private static final int PITCH_INCREMENT = 40;

  private int channel;
  private Rect containingItem;
  private int currentValue;
  private long expirationTick;
  private Instrument instrument;
  private int nextValue;
  private Sound sound;
  private Synthesizer synthesizer;

  public WindPlayer(Synthesizer synthesizer, Instrument instrument, int channel) {
    this.synthesizer = synthesizer;
    this.instrument = instrument;
    this.channel = channel;
    int programIndex = instrument.getProgramIndex();
    synthesizer.changeProgram(channel, programIndex);
  }

  @Override
  public void play(Frame frame, List<Rect> items, long tick) {
    if (items.size() == 0) {
      stopExpiringSounds(tick);
      if (currentValue != 0) {
        frame.addMusicAnnotation(new MusicAnnotation(containingItem, instrument, sound, Type.ACTIVE));
      }
    } else {
      containingItem = getContainingItem(items);
      sound = getSound(frame, containingItem);
      if (sound.getValue() == currentValue) {
        frame.addMusicAnnotation(new MusicAnnotation(containingItem, instrument, sound, Type.DUPLICATE));
      } else {
        nextValue = sound.getValue();
        int bend;
        int increment;
        if (nextValue > currentValue) {
          bend = PITCH_CENTER - PITCH_BEND;
          increment = PITCH_INCREMENT;
        } else {
          bend = PITCH_CENTER + PITCH_BEND;
          increment = -PITCH_INCREMENT;
        }
        startNext();
        long millis = System.currentTimeMillis();
        int iterations = PITCH_BEND / PITCH_INCREMENT;
        float gain = GAIN_CENTER - GAIN_BEND;
        float gainDelta = GAIN_BEND / iterations;
        for (int i = 0; i < iterations; i++) {
          bend += increment;
          gain += gainDelta;
          synthesizer.bendPitch(channel, bend);
          synthesizer.setGain(gain);
          System.out.println("currentValue=" + currentValue + ", bend=" + bend + ", increment=" + increment + ", gain=" + gain);
          try {
            Thread.sleep(DELAY);
          } catch (InterruptedException e) {
            // ignore
          }
        }
        System.out.println("Elapsed time: " + (System.currentTimeMillis() - millis));
        frame.addMusicAnnotation(new MusicAnnotation(containingItem, instrument, sound, Type.NEW));
      }
      expirationTick = tick + getDuration();
    }
  }

  @Override
  public void terminate() {
    stopCurrent();
  }

  @Override
  public String toString() {
    return "WindPlayer [instrument=" + instrument + ", channel=" + channel + "]";
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
    return 1000;
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