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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
    BufferedImage bufferedImage = frame.getBufferedImage();
    ImageIcon imageIcon = new ImageIcon(bufferedImage);
    window.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
    label.setIcon(imageIcon);
    label.repaint();
  }

}
