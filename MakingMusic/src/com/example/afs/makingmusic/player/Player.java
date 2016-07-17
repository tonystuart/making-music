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

import com.example.afs.makingmusic.process.Frame;

public interface Player {

  public void play(Frame frame, List<Rect> items, long tick);

  public void terminate();

}