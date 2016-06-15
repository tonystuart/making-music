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
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.example.afs.makingmusic.common.Step;

public class ImageGenerator extends Step<Frame> {

  private static final Scalar GREEN = new Scalar(0, 255, 0);
  private static final Scalar RED = new Scalar(0, 0, 255);

  public ImageGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
  }

  @Override
  public void process(Frame frame) {
    Mat image = frame.getImageMatrix();
    Iterator<MusicAnnotation> annotationIterator = frame.getMusicAnnotations().iterator();
    for (Rect item : frame.getItems()) {
      if (annotationIterator.hasNext()) {
        annotationIterator.next();
        Imgproc.rectangle(image, item.br(), item.tl(), GREEN, 1);
      } else {
        Imgproc.rectangle(image, item.br(), item.tl(), RED, 1);
      }
    }
    BufferedImage bufferedImage = toBufferedImage(image);
    frame.setBufferedImage(bufferedImage);
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