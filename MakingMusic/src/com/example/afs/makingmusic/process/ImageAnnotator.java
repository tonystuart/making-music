// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.util.concurrent.BlockingQueue;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.example.afs.makingmusic.common.Step;

public class ImageAnnotator extends Step<Frame> {

  public ImageAnnotator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
  }

  @Override
  public void process(Frame frame) {
    Mat image = frame.getImage();
    for (Rect item : frame.getItems()) {
      Imgproc.rectangle(image, item.br(), item.tl(), new Scalar(0, 255, 0), 1);
    }
  }

}