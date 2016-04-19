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
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.Step;
import com.example.afs.makingmusic.constants.Constants;
import com.example.afs.makingmusic.constants.Properties;

public class ImageGenerator extends Step<Frame> {

  private static final Scalar GREEN = new Scalar(0, 255, 0);
  private static final Scalar RED = new Scalar(0, 0, 255);
  private int maximumConcurrentNotes;

  public ImageGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    setMonitorPropertyChanges(true);
  }

  @Override
  public void process(Frame frame) {
    int count = 0;
    Mat image = frame.getImageMatrix();
    for (Rect item : frame.getItems()) {
      Scalar color = count++ < maximumConcurrentNotes ? GREEN : RED;
      Imgproc.rectangle(image, item.br(), item.tl(), color, 1);
    }
    BufferedImage bufferedImage = toBufferedImage(image);
    frame.setBufferedImage(bufferedImage);
  }

  @Override
  protected void doPropertyChange(PropertyChange propertyChange) {
    switch (propertyChange.getName()) {
    case Properties.MAXIMUM_CONCURRENT_NOTES:
      maximumConcurrentNotes = Integer.parseInt(propertyChange.getValue());
      break;
    case Properties.RESET:
      maximumConcurrentNotes = Constants.LOWER_MAX_NOTES_LIMIT;
      break;
    }
  }

  private BufferedImage toBufferedImage(Mat matrix) {
    int width = matrix.cols();
    int height = matrix.rows();
    int channels = matrix.channels();
    int bitmapSize = height * width * channels;
    byte[] bitmap = new byte[bitmapSize];
    matrix.get(0, 0, bitmap);
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
    WritableRaster raster = image.getRaster();
    DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
    System.arraycopy(bitmap, 0, dataBuffer.getData(), 0, bitmap.length);
    return image;
  }

}