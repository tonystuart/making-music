// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.awt.image.BufferedImage;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.example.afs.makingmusic.common.Step;

public class ImageCatcher extends Step<Frame> {

  private static AtomicReference<BufferedImage> currentImage = new AtomicReference<>();

  public static BufferedImage getImage() {
    return currentImage.get();
  }

  public ImageCatcher(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
  }

  @Override
  public void process(Frame frame) {
    BufferedImage bufferedImage = frame.getBufferedImage();
    currentImage.set(bufferedImage);
  }
}
