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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.example.afs.makingmusic.constants.Midi;
import com.example.afs.makingmusic.constants.Property;
import com.example.afs.makingmusic.constants.Property.AssignmentMethod;
import com.example.afs.makingmusic.player.DrumPlayer;
import com.example.afs.makingmusic.player.KeyboardPlayer;
import com.example.afs.makingmusic.player.Player;
import com.example.afs.makingmusic.player.StringPlayer;
import com.example.afs.makingmusic.player.WindPlayer;
import com.example.afs.makingmusic.sound.Instrument;

public class MusicGenerator extends Step<Frame> {

  private static final List<Rect> EMPTY_LIST = Collections.emptyList();

  private AssignmentMethod assignmentMethod;
  private Set<String> instrumentNames = new ConcurrentHashSet<>();
  private List<Player> players = new ArrayList<>();
  private Synthesizer synthesizer = new Synthesizer();

  public MusicGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    initializeProperties();
    monitorPropertyChange(MonitorStyle.SYNC);
    monitorStateRequest(MonitorStyle.ASYNC);
  }

  @Override
  public void process(Frame frame) {
    if (players.size() > 0) {
      Map<Player, List<Rect>> playerItems = findPlayerItems(frame);
      play(frame, playerItems);
    }
  }

  @Override
  protected void onAsynchronousStateRequest(StateRequest stateRequest) {
    for (String name : instrumentNames) {
      stateRequest.addProperty(Property.Names.INSTRUMENT_PREFIX + name, Boolean.TRUE.toString());
    }
    stateRequest.addProperty(Property.Names.MAXIMUM_CONCURRENT_NOTES, Integer.toString(synthesizer.getMaximumConcurrentNotes()));
    stateRequest.addProperty(Property.Names.ASSIGNMENT_METHOD, assignmentMethod.name().toLowerCase());
  }

  @Override
  protected void onSynchronousPropertyChange(PropertyChange propertyChange) {
    switch (propertyChange.getName()) {
    case Property.Names.ASSIGNMENT_METHOD:
      assignmentMethod = AssignmentMethod.valueOf(propertyChange.getValue().toUpperCase());
      break;
    case Property.Names.MAXIMUM_CONCURRENT_NOTES:
      synthesizer.setMaximumConcurrentNotes(Integer.parseInt(propertyChange.getValue()));
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
      int channel = nextChannel++;
      if (nextChannel == Midi.DRUM_CHANNEL_INDEX) {
        nextChannel++;
      }
      Instrument instrument = instruments[i];
      Player player = createPlayer(instrument, channel);
      players.add(player);
    }
  }

  private void clearPlayers() {
    for (Player player : players) {
      player.clear();
    }
    players.clear();
  }

  private Player createPlayer(Instrument instrument, int channel) {
    Player player;
    switch (instrument.getType()) {
    case DRUM:
      player = new DrumPlayer(synthesizer, instrument);
      break;
    case KEYBOARD:
      player = new KeyboardPlayer(synthesizer, instrument, channel);
      break;
    case STRING:
      player = new StringPlayer(synthesizer, instrument, channel);
      break;
    case WIND:
      player = new WindPlayer(synthesizer, instrument, channel);
      break;
    default:
      throw new UnsupportedOperationException();
    }
    return player;
  }

  private Player findPlayer(Frame frame, Rect item) {
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

  private Map<Player, List<Rect>> findPlayerItems(Frame frame) {
    Map<Player, List<Rect>> playerItems = new HashMap<>();
    for (Player player : players) {
      playerItems.put(player, EMPTY_LIST);
    }
    for (Rect item : frame.getItems()) {
      Player player = findPlayer(frame, item);
      List<Rect> items = playerItems.get(player);
      if (items == EMPTY_LIST) {
        items = new LinkedList<>();
        playerItems.put(player, items);
      }
      items.add(item);
    }
    return playerItems;
  }

  private void initializeProperties() {
    assignmentMethod = Property.Defaults.ASSIGNMENT_METHOD;
    synthesizer.setMaximumConcurrentNotes(Property.Defaults.MAXIMUM_CONCURRENT_NOTES);
    updateActiveInstruments(Property.Defaults.INSTRUMENT, true);
  }

  private void play(Frame frame, Map<Player, List<Rect>> playerItems) {
    long tick = System.currentTimeMillis();
    for (Entry<Player, List<Rect>> entry : playerItems.entrySet()) {
      Player player = entry.getKey();
      List<Rect> items = entry.getValue();
      player.play(frame, items, tick);
    }
    Injector.getMetrics().setNotes(synthesizer.getTotalNoteCount());
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