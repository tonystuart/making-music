// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.constants.Properties;
import com.example.afs.makingmusic.process.CameraReader;
import com.example.afs.makingmusic.process.ImageGenerator;
import com.example.afs.makingmusic.process.ImageViewer;
import com.example.afs.makingmusic.process.MotionDetector;
import com.example.afs.makingmusic.process.MusicGenerator;

public class Player {

  public static void main(String[] args) {
    Player player = new Player();
    player.play();
  }

  public void play() {

    CameraReader cameraReader = new CameraReader(125);
    cameraReader.start();

    MotionDetector motionDetector = new MotionDetector(cameraReader.getOutputQueue());
    motionDetector.start();

    MusicGenerator musicGenerator = new MusicGenerator(motionDetector.getOutputQueue());
    musicGenerator.start();

    ImageGenerator imageGenerator = new ImageGenerator(musicGenerator.getOutputQueue());
    imageGenerator.start();

    ImageViewer imageViewer = new ImageViewer(imageGenerator.getOutputQueue());
    imageViewer.start();

    Injector.getMessageBroker().publish(new PropertyChange(Properties.MAXIMUM_CONCURRENT_NOTES, "10"));
    Injector.getMessageBroker().publish(new PropertyChange("instrument-acoustic-grand-piano", "true"));
  }

}
