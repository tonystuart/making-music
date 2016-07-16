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

import com.example.afs.makingmusic.common.Step;
import com.example.afs.makingmusic.process.MusicAnnotation.Type;

public class ImageGenerator extends Step<Frame> {

  public static final Scalar BLUE = new Scalar(255, 0, 0);
  public static final Scalar CYAN = new Scalar(255, 255, 0);
  public static final Scalar GREEN = new Scalar(0, 255, 0);
  public static final Scalar MAGENTA = new Scalar(255, 0, 255);
  public static final Scalar RED = new Scalar(0, 0, 255);
  public static final Scalar YELLOW = new Scalar(0, 255, 255);

  public ImageGenerator(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
  }

  @Override
  public void process(Frame frame) {
    Mat image = frame.getImageMatrix();
    for (MusicAnnotation musicAnnotation : frame.getMusicAnnotations()) {
      Rect item = musicAnnotation.getItem();
      Type type = musicAnnotation.getType();
      switch (type) {
      case NEW:
        Imgproc.rectangle(image, item.br(), item.tl(), GREEN, 1);
        break;
      case ACTIVE:
        Imgproc.rectangle(image, item.br(), item.tl(), BLUE, 1);
        break;
      case DUPLICATE:
        Imgproc.rectangle(image, item.br(), item.tl(), YELLOW, 1);
        break;
      case OVERFLOW:
        Imgproc.rectangle(image, item.br(), item.tl(), RED, 1);
        break;
      default:
        throw new UnsupportedOperationException();
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