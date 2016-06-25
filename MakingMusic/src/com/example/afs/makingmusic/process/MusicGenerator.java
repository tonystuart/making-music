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
import com.example.afs.makingmusic.process.MusicAnnotation.Type;
import com.example.afs.makingmusic.sound.Instrument;
import com.example.afs.makingmusic.sound.Sound;

public class MusicGenerator extends Step<Frame> {

  public static final int DRUM_CHANNEL_INDEX = 9;

  public class Player {

    private Set<ActiveSound> activeSounds = new HashSet<>();
    private int channel;
    private Set<Sound> frameSounds = new HashSet<>();
    private Instrument instrument;
    private Set<Rect> items = new HashSet<>();

    public Player(Instrument instrument, int channel) {
      this.instrument = instrument;
      this.channel = channel;
      int programIndex = instrument.getProgramIndex();
      if (channel != DRUM_CHANNEL_INDEX) {
        synthesizer.changeProgram(channel, programIndex);
      }
    }

    public void add(Frame frame, Rect item) {
      items.add(item);
    }

    public void clear() {
      for (ActiveSound activeSound : activeSounds) {
        synthesizer.releaseKey(activeSound.getChannel(), activeSound.getValue());
        activeSounds.remove(activeSound);
      }
    }

    public void play(Frame frame, long tick) {
      int width = frame.getImageMatrix().width();
      for (Rect item : items) {
        Sound sound = MulDiv.scale(width, item.x, instrument.getSounds());
        if (!frameSounds.contains(sound)) {
          frameSounds.add(sound);
          ActiveSound activeSound = new ActiveSound(channel, sound.getValue(), tick + Durations.NOTE_DURATION);
          if (channel == DRUM_CHANNEL_INDEX || !activeSounds.contains(activeSound)) {
            if (activeSounds.size() < maximumConcurrentNotes) {
              frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.NEW));
              int value = sound.getValue();
              synthesizer.pressKey(channel, value, instrument.getVelocity());
              activeSounds.add(activeSound);
              noteCount++;
            } else {
              frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.OVERFLOW));
            }
          } else {
            frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.OLD));
          }
        } else {
          frame.addMusicAnnotation(new MusicAnnotation(item, instrument, sound, Type.DUPLICATE));
        }
      }
    }

    public void prepare(Frame frame, long tick) {
      releaseExpiredSounds(tick);
      items.clear();
      frameSounds.clear();
    }

    private void releaseExpiredSounds(long tick) {
      List<ActiveSound> expiringSounds = new ArrayList<>(activeSounds.size());
      for (ActiveSound activeSound : activeSounds) {
        if (activeSound.getOffTime() < tick) {
          synthesizer.releaseKey(activeSound.getChannel(), activeSound.getValue());
          expiringSounds.add(activeSound);
        }
      }
      activeSounds.removeAll(expiringSounds);
    }

  }

  private AssignmentMethod assignmentMethod;
  private Set<String> instrumentNames = new ConcurrentHashSet<>();
  private int maximumConcurrentNotes;
  private int noteCount;
  private List<Player> players = new ArrayList<>();
  private Synthesizer synthesizer = new Synthesizer();

  public MusicGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    monitorPropertyChange(MonitorStyle.SYNC);
    monitorStateRequest(MonitorStyle.ASYNC);
  }

  @Override
  public void process(Frame frame) {
    long tick = System.currentTimeMillis();
    if (players.size() > 0) {
      for (Player player : players) {
        player.prepare(frame, tick);
      }
      for (Rect item : frame.getItems()) {
        Player player = getPlayer(frame, item);
        player.add(frame, item);
      }
      for (Player player : players) {
        player.play(frame, tick);
      }
      Injector.getMetrics().setNotes(noteCount);
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

  private void assignPlayers(Instrument[] instruments) {
    int nextChannel = 0;
    for (int i = 0; i < instruments.length; i++) {
      int channel;
      Instrument instrument = instruments[i];
      if (instruments[i].isDrumKit()) {
        channel = DRUM_CHANNEL_INDEX;
      } else {
        channel = nextChannel++;
        if (nextChannel == DRUM_CHANNEL_INDEX) {
          nextChannel++;
        }
      }
      players.add(new Player(instrument, channel));
    }
  }

  private void clearPlayers() {
    for (Player player : players) {
      player.clear();
    }
    players.clear();
  }

  private Player getPlayer(Frame frame, Rect item) {
    int index;
    if (assignmentMethod == null || assignmentMethod == AssignmentMethod.POSITION) {
      int height = frame.getImageMatrix().height();
      index = MulDiv.scale(height, item.y, players.size());
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
      index = MulDiv.scale(361, hue, players.size());
    }
    return players.get(index);
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
      clearPlayers();
      Instrument[] instruments = Injector.getInstruments().getInstruments(instrumentNames);
      assignPlayers(instruments);
    }
  }

}