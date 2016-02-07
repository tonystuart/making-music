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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;

import com.example.afs.makingmusic.common.Step;

public class ImageViewer extends Step<Frame> {

  private JLabel label;
  private JFrame window;

  public ImageViewer(BlockingQueue<Frame> inputQueue) {
    super(inputQueue);
    window = new JFrame("Making Music");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    label = new JLabel();
    window.setContentPane(label);
    window.setVisible(true);
  }

  @Override
  public void process(Frame frame) {
    Mat image = frame.getImage();
    BufferedImage bufferedImage = toBufferedImage(image);
    ImageIcon imageIcon = new ImageIcon(bufferedImage);
    window.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
    label.setIcon(imageIcon);
    label.repaint();
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
