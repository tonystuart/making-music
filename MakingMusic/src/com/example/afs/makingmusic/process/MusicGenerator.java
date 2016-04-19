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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Rect;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.MulDiv;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.Step;
import com.example.afs.makingmusic.constants.Constants;
import com.example.afs.makingmusic.constants.Properties;
import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.Sound;

public class MusicGenerator extends Step<Frame> {

  public static class ChannelAssignments {

    private int[] channels;
    private Instrument[] instruments;
    private Synthesizer synthesizer;

    public ChannelAssignments(Synthesizer synthesizer) {
      this.synthesizer = synthesizer;
    }

    public void assignChannels(Instrument[] instruments) {
      this.instruments = instruments;
      this.channels = new int[instruments.length];
      int nextChannel = 0;
      for (int i = 0; i < instruments.length; i++) {
        if (instruments[i].isDrumKit()) {
          channels[i] = Constants.DRUM_CHANNEL_INDEX;
        } else {
          channels[i] = nextChannel++;
          if (nextChannel == Constants.DRUM_CHANNEL_INDEX) {
            nextChannel++;
          }
          int programIndex = instruments[i].getProgramIndex();
          synthesizer.changeProgram(channels[i], programIndex);
        }
      }
    }

    public int getChannel(int index) {
      return channels[index];
    }

    public Instrument getInstrument(int index) {
      return instruments[index];
    }

    public int size() {
      return instruments == null ? 0 : instruments.length;
    }

  }

  private Set<ActiveSound> activeSounds;
  private ChannelAssignments channelAssignments;
  private Set<String> instrumentNames;
  private int maximumConcurrentNotes;
  private Synthesizer synthesizer;

  public MusicGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    setMonitorPropertyChanges(true);
    activeSounds = new HashSet<>();
    instrumentNames = new HashSet<>();
    synthesizer = new Synthesizer();
    channelAssignments = new ChannelAssignments(synthesizer);
  }

  @Override
  public void process(Frame frame) {
    int height = frame.getImageMatrix().height();
    int width = frame.getImageMatrix().width();
    long tick = System.currentTimeMillis();
    List<ActiveSound> expiringSounds = new ArrayList<>(activeSounds.size());
    for (ActiveSound activeSound : activeSounds) {
      if (activeSound.getOffTime() < tick) {
        synthesizer.releaseKey(activeSound.getChannel(), activeSound.getValue());
        expiringSounds.add(activeSound);
      }
    }
    activeSounds.removeAll(expiringSounds);
    if (channelAssignments.size() > 0) {
      Iterator<Rect> itemIterator = frame.getItems().iterator();
      while (itemIterator.hasNext() && activeSounds.size() < maximumConcurrentNotes) {
        Rect item = itemIterator.next();
        int index = MulDiv.scale(height, item.y, channelAssignments.size());
        Instrument instrument = channelAssignments.getInstrument(index);
        int channel = channelAssignments.getChannel(index);
        Sound sound = MulDiv.scale(width, item.x, instrument.getSounds());
        int value = sound.getValue();
        ActiveSound activeSound = new ActiveSound(channel, value, tick + Constants.NOTE_DURATION_MILLIS);
        if (channel == Constants.DRUM_CHANNEL_INDEX || !activeSounds.contains(activeSound)) {
          activeSounds.add(activeSound);
          synthesizer.pressKey(channel, value, instrument.getVelocity());
        }
      }
      // System.out.println(expiringSounds.size() + "/" + activeSounds.size());
    }
  }

  @Override
  protected void doPropertyChange(PropertyChange propertyChange) {
    switch (propertyChange.getName()) {
    case Properties.MAXIMUM_CONCURRENT_NOTES:
      maximumConcurrentNotes = Integer.parseInt(propertyChange.getValue());
      break;
    case Properties.RESET:
      maximumConcurrentNotes = Constants.LOWER_MAX_NOTES_LIMIT;
      instrumentNames.clear();
      updateActiveInstruments(Injector.getInstruments().getDefaultInstrumentName(), true);
      break;
    default:
      if (propertyChange.getName().startsWith(Properties.INSTRUMENT_PREFIX)) {
        updateActiveInstruments(propertyChange);
      }
      break;
    }
  }

  private void updateActiveInstruments(PropertyChange propertyChange) {
    String name = propertyChange.getName().substring(Properties.INSTRUMENT_PREFIX.length());
    boolean isActive = Boolean.parseBoolean(propertyChange.getValue());
    updateActiveInstruments(name, isActive);
  }

  private void updateActiveInstruments(String name, boolean isActive) {
    if (Injector.getInstruments().contains(name)) {
      if (isActive) {
        instrumentNames.add(name);
      } else {
        instrumentNames.remove(name);
      }
      Instrument[] instruments = Injector.getInstruments().getInstruments(instrumentNames);
      channelAssignments.assignChannels(instruments);
    }
  }

}