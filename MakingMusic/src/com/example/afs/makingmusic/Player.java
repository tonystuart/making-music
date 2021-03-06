// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic;

import com.example.afs.makingmusic.constants.Durations;
import com.example.afs.makingmusic.process.CameraReader;
import com.example.afs.makingmusic.process.ImageGenerator;
import com.example.afs.makingmusic.process.ImageViewer;
import com.example.afs.makingmusic.process.MetricsProcessor;
import com.example.afs.makingmusic.process.MotionDetector;
import com.example.afs.makingmusic.process.MusicGenerator;
import com.example.afs.makingmusic.process.ResetMessagePublisher;

public class Player {

  public static void main(String[] args) {
    Player player = new Player();
    player.play();
  }

  public void play() {
    MetricsProcessor metricsProcessor = new MetricsProcessor(Durations.METRICS_INTERVAL);
    metricsProcessor.start();

    CameraReader cameraReader = new CameraReader(Durations.FRAME_INTERVAL);
    cameraReader.start();

    MotionDetector motionDetector = new MotionDetector(cameraReader.getOutputQueue());
    motionDetector.start();

    MusicGenerator musicGenerator = new MusicGenerator(motionDetector.getOutputQueue());
    musicGenerator.start();

    ImageGenerator imageGenerator = new ImageGenerator(musicGenerator.getOutputQueue());
    imageGenerator.start();

    ImageViewer imageViewer = new ImageViewer(imageGenerator.getOutputQueue());
    imageViewer.start();

    ResetMessagePublisher resetMessagePublisher = new ResetMessagePublisher(Durations.RESET_INTERVAL);
    resetMessagePublisher.start();
  }

}
