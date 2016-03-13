// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.server;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.afs.makingmusic.common.TimeKeeper;
import com.example.afs.makingmusic.process.ImageCatcher;

public class CurrentFrameServlet extends HttpServlet {

  private static TimeKeeper timeKeeper = new TimeKeeper(CurrentFrameServlet.class.getSimpleName());

  public synchronized static void getImage(HttpServletResponse resp) throws ServletException, IOException {
    timeKeeper.beginLoop();
    BufferedImage bufferedImage = ImageCatcher.getImage();
    if (bufferedImage != null) {
      ImageIO.write(bufferedImage, "JPEG", resp.getOutputStream());
    }
    timeKeeper.endLoop();
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ImageIO.setUseCache(false);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    getImage(resp);
  }

}
