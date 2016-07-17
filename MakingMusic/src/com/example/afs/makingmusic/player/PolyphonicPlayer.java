// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opencv.core.Rect;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.common.MulDiv;
import com.example.afs.makingmusic.process.ActiveSound;
import com.example.afs.makingmusic.process.Frame;
import com.example.afs.makingmusic.process.MusicAnnotation;
import com.example.afs.makingmusic.process.MusicAnnotation.Type;
import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.Sound;

public abstract class PolyphonicPlayer implements Player {

  protected Set<ActiveSound> activeSounds = new HashSet<>();
  protected int channel;
  protected Instrument instrument;
  protected Synthesizer synthesizer;

  public PolyphonicPlayer(Synthesizer synthesizer, Instrument instrument, int channel) {
    this.synthesizer = synthesizer;
    this.instrument = instrument;
    this.channel = channel;
  }

  @Override
  public void play(Frame frame, List<Rect> items, long tick) {
    stopExpiredSounds(tick);
    Set<Sound> frameSounds = new HashSet<>();
    for (Rect item : items) {
      Sound sound = getSound(frame, item);
      play(frameSounds, frame, item, tick, sound);
    }
  }

  @Override
  public void terminate() {
    stopActiveSounds();
  }

  protected long getDuration() {
    return 1000;
  }

  protected Sound getSound(Frame frame, Rect item) {
    int width = frame.getImageMatrix().width();
    Sound sound = MulDiv.scale(width, item.x, instrument.getSounds());
    return sound;
  }

  protected boolean isPlayable() {
    return true; // default is unlimited polyphony
  }

  protected void play(Set<Sound> frameSounds, Frame frame, Rect item, long tick, Sound sound) {
    if (frameSounds.add(sound)) { // only adds if not already present
      ActiveSound activeSound = new ActiveSound(sound, tick + getDuration());
      if (!activeSounds.contains(activeSound)) {
        if (isPlayable()) {
          startActiveSound(activeSound);
          frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.NEW));
        } else {
          frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.OVERFLOW));
        }
      } else {
        frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.ACTIVE));
      }
    } else {
      frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.DUPLICATE));
    }
  }

  protected void startActiveSound(ActiveSound activeSound) {
    int value = activeSound.getSound().getValue();
    synthesizer.pressKey(channel, value, 96);
    activeSounds.add(activeSound);
  }

  protected void stopExpiredSounds(long tick) {
    List<ActiveSound> expiringSounds = new ArrayList<>(activeSounds.size());
    for (ActiveSound activeSound : activeSounds) {
      if (activeSound.getOffTime() < tick) {
        synthesizer.releaseKey(channel, activeSound.getSound().getValue());
        expiringSounds.add(activeSound);
      }
    }
    activeSounds.removeAll(expiringSounds);
  }

  private void stopActiveSounds() {
    for (ActiveSound activeSound : activeSounds) {
      synthesizer.releaseKey(channel, activeSound.getSound().getValue());
    }
    activeSounds.clear();
  }

}