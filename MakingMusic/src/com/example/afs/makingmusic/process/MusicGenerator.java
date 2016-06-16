// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.eclipse.jetty.util.ConcurrentHashSet;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

import com.example.afs.fluidsynth.Synthesizer;
import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.MessageReceiver.MonitorStyle;
import com.example.afs.makingmusic.common.MulDiv;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.StateRequest;
import com.example.afs.makingmusic.common.Step;
import com.example.afs.makingmusic.constants.Durations;
import com.example.afs.makingmusic.constants.Property;
import com.example.afs.makingmusic.constants.Property.AssignmentMethod;
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
          channels[i] = DRUM_CHANNEL_INDEX;
        } else {
          channels[i] = nextChannel++;
          if (nextChannel == DRUM_CHANNEL_INDEX) {
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

  public static final int DRUM_CHANNEL_INDEX = 9;

  private Set<ActiveSound> activeSounds;
  private AssignmentMethod assignmentMethod;
  private ChannelAssignments channelAssignments;
  private Set<String> instrumentNames;
  private int maximumConcurrentNotes;
  private int noteCount;
  private Synthesizer synthesizer;

  public MusicGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    activeSounds = new HashSet<>();
    instrumentNames = new ConcurrentHashSet<>();
    synthesizer = new Synthesizer();
    channelAssignments = new ChannelAssignments(synthesizer);
    monitorPropertyChange(MonitorStyle.SYNC);
    monitorStateRequest(MonitorStyle.ASYNC);
  }

  @Override
  public void process(Frame frame) {
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
        int index = getInstrumentIndex(frame, item);
        Instrument instrument = channelAssignments.getInstrument(index);
        int channel = channelAssignments.getChannel(index);
        Sound sound = MulDiv.scale(width, item.x, instrument.getSounds());
        MusicAnnotation musicAnnotation = new MusicAnnotation(instrument, sound);
        frame.addMusicAnnotation(musicAnnotation);
        int value = sound.getValue();
        ActiveSound activeSound = new ActiveSound(channel, value, tick + Durations.NOTE_DURATION);
        if (channel == DRUM_CHANNEL_INDEX || !activeSounds.contains(activeSound)) {
          activeSounds.add(activeSound);
          synthesizer.pressKey(channel, value, instrument.getVelocity());
          noteCount++;
        }
      }
      Injector.getMetrics().setNotes(noteCount);
      // System.out.println(expiringSounds.size() + "/" + activeSounds.size());
    }
  }

  @Override
  protected void onAsynchronousStateRequest(StateRequest stateRequest) {
    for (String name : instrumentNames) {
      stateRequest.addProperty(Property.Names.INSTRUMENT_PREFIX + name, Boolean.TRUE.toString());
    }
    stateRequest.addProperty(Property.Names.MAXIMUM_CONCURRENT_NOTES, Integer.toString(maximumConcurrentNotes));
    stateRequest.addProperty(Property.Names.ASSIGNMENT_METHOD, assignmentMethod.name().toLowerCase());
  }

  @Override
  protected void onSynchronousPropertyChange(PropertyChange propertyChange) {
    switch (propertyChange.getName()) {
    case Property.Names.ASSIGNMENT_METHOD:
      assignmentMethod = AssignmentMethod.valueOf(propertyChange.getValue().toUpperCase());
      break;
    case Property.Names.MAXIMUM_CONCURRENT_NOTES:
      maximumConcurrentNotes = Integer.parseInt(propertyChange.getValue());
      break;
    case Property.Names.RESET:
      instrumentNames.clear();
      initializeProperties();
      break;
    default:
      if (propertyChange.getName().startsWith(Property.Names.INSTRUMENT_PREFIX)) {
        updateActiveInstruments(propertyChange);
      }
      break;
    }
  }

  private int getInstrumentIndex(Frame frame, Rect item) {
    int index;
    if (assignmentMethod == null || assignmentMethod == AssignmentMethod.POSITION) {
      int height = frame.getImageMatrix().height();
      index = MulDiv.scale(height, item.y, channelAssignments.size());
    } else {
      Mat itemMat = frame.getImageMatrix().submat(item);
      Scalar mean = Core.mean(itemMat);
      float[] hsb = new float[3];
      Color.RGBtoHSB((int) mean.val[2], (int) mean.val[1], (int) mean.val[0], hsb);
      int hue = (int) (hsb[0] * 360);
      // Red hues are 0-30 and 330-360 so rotate by 30 to group together
      if (hue > 330) {
        hue = hue - 330;
      } else {
        hue += 30;
      }
      // RGBtoHSB hsb[0] is 0-1 inclusive for a total of 361 hue values
      index = MulDiv.scale(361, hue, channelAssignments.size());
    }
    return index;
  }

  private void initializeProperties() {
    assignmentMethod = Property.Defaults.ASSIGNMENT_METHOD;
    maximumConcurrentNotes = Property.Defaults.MAXIMUM_CONCURRENT_NOTES;
    updateActiveInstruments(Property.Defaults.INSTRUMENT, true);
  }

  private void updateActiveInstruments(PropertyChange propertyChange) {
    String name = propertyChange.getName().substring(Property.Names.INSTRUMENT_PREFIX.length());
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