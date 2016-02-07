// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class Frame {
  private Mat image;
  private List<Rect> items = new LinkedList<>();

  public Frame(Mat image) {
    this.image = image;
  }

  public void addItem(Rect item) {
    items.add(item);
  }

  public Mat getImage() {
    return image;
  }

  public Iterable<Rect> getItems() {
    return items;
  }

}