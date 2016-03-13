// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.server;

import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.constants.Properties;
import com.example.afs.makingmusic.process.CameraReader;
import com.example.afs.makingmusic.process.ImageCatcher;
import com.example.afs.makingmusic.process.ImageGenerator;
import com.example.afs.makingmusic.process.MotionDetector;
import com.example.afs.makingmusic.process.MusicGenerator;

public class WebApp {

  public static void main(String[] args) throws Exception {

    CameraReader cameraReader = new CameraReader();
    cameraReader.start(125);

    MotionDetector motionDetector = new MotionDetector(cameraReader.getOutputQueue());
    motionDetector.start();

    MusicGenerator musicGenerator = new MusicGenerator(motionDetector.getOutputQueue());
    musicGenerator.start();

    ImageGenerator imageGenerator = new ImageGenerator(musicGenerator.getOutputQueue());
    imageGenerator.start();

    ImageCatcher imageCatcher = new ImageCatcher(imageGenerator.getOutputQueue());
    imageCatcher.start();

    Injector.getMessageBroker().publish(new PropertyChange(Properties.MAXIMUM_CONCURRENT_NOTES, "10"));
    Injector.getMessageBroker().publish(new PropertyChange("instrument-acoustic-grand-piano", "true"));

    Server server = new Server();
    SocketConnector connector = new SocketConnector();
    connector.setPort(8080);
    server.setConnectors(new Connector[] {
      connector
    });
    DefaultServlet defaultServlet = new DefaultServlet();
    ServletHolder defaultServletHolder = new ServletHolder(defaultServlet);
    defaultServletHolder.setInitParameter("resourceBase", new File("html").getCanonicalPath());
    System.out.println("resourceBase=" + defaultServletHolder.getInitParameter("resourceBase"));
    ServletContextHandler context = new ServletContextHandler();
    context.addServlet(defaultServletHolder, "/");
    context.addServlet(CurrentFrameServlet.class, "/currentFrame.jpg");
    context.addServlet(RestServlet.class, "/rest/v1/*");
    HandlerCollection handlers = new HandlerCollection();
    handlers.setHandlers(new Handler[] {
        context,
        new DefaultHandler()
    });
    server.setHandler(handlers);
    server.start();
    server.join();
  }
}
