// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.ScheduledStep;

public class CameraReader extends ScheduledStep<Frame> {

  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }

  private VideoCapture camera;
  private int frameCount;

  public CameraReader(long intervalMillis) {
    super(intervalMillis);
    this.camera = new VideoCapture(0);
  }

  @Override
  public Frame process() throws InterruptedException {
    Mat image = new Mat();
    while (!camera.read(image)) {
      System.err.println("Cannot read image. Is the camera plugged in?");
      sleep(100);
      camera.release();
      camera = new VideoCapture(0);
    }
    Injector.getMetrics().setFrames(++frameCount);
    return new Frame(image);
  }

}