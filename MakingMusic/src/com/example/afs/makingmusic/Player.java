// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic;

import org.opencv.core.Core;

import com.example.afs.makingmusic.process.BackgroundDetector;
import com.example.afs.makingmusic.process.CameraReader;
import com.example.afs.makingmusic.process.ImageAnnotator;
import com.example.afs.makingmusic.process.ImageViewer;
import com.example.afs.makingmusic.process.MusicGenerator;

public class Player {

  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }

  public static void main(String[] args) {
    Player player = new Player();
    player.play();
  }

  public void play() {

    CameraReader cameraReader = new CameraReader();
    cameraReader.start(125);

    BackgroundDetector backgroundDetector = new BackgroundDetector(cameraReader.getOutputQueue());
    backgroundDetector.start();

    MusicGenerator musicGenerator = new MusicGenerator(backgroundDetector.getOutputQueue());
    musicGenerator.start();

    ImageAnnotator imageAnnotator = new ImageAnnotator(musicGenerator.getOutputQueue());
    imageAnnotator.start();

    ImageViewer imageViewer = new ImageViewer(imageAnnotator.getOutputQueue());
    imageViewer.start();
  }

}
