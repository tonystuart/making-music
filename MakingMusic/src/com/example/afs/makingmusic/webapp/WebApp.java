// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.webapp;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.constants.Durations;
import com.example.afs.makingmusic.process.CameraReader;
import com.example.afs.makingmusic.process.ImageCatcher;
import com.example.afs.makingmusic.process.ImageGenerator;
import com.example.afs.makingmusic.process.MetricsProcessor;
import com.example.afs.makingmusic.process.MotionDetector;
import com.example.afs.makingmusic.process.MusicGenerator;
import com.example.afs.makingmusic.process.ResetMessagePublisher;

public class WebApp {

  public static class RedirectingDefaultServlet extends DefaultServlet {
    private int pageCount;
    private int redirectCount;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      if (!request.getServerName().equals(request.getLocalAddr())) {
        LOG.info("requestURL=" + request.getRequestURL());
        String redirectLocation = "http://" + request.getLocalAddr() + ":" + PORT;
        response.setHeader("Location", redirectLocation);
        response.setStatus(Response.SC_TEMPORARY_REDIRECT);
        Injector.getMetrics().setRedirects(++redirectCount);
      } else {
        super.doGet(request, response);
        if ("/MakingMusic.html".equals(request.getRequestURI())) {
          Injector.getMetrics().setPages(++pageCount);
        }
      }
    }
  }

  private static final Logger LOG = Log.getLogger(WebApp.class);
  private static final int PORT = 8080;

  public static void main(String[] args) {
    new WebApp().start();
  }

  private Server server;

  public WebApp() {
    createProcess();
    createServer();
  }

  private void createProcess() {
    MetricsProcessor metricsProcessor = new MetricsProcessor(Durations.METRICS_INTERVAL);
    metricsProcessor.start();

    CameraReader cameraReader = new CameraReader(Durations.FRAME_INTERVAL);
    cameraReader.start();

    MotionDetector motionDetector = new MotionDetector(cameraReader.getOutputQueue());
    motionDetector.start();

    MusicGenerator musicGenerator = new MusicGenerator(motionDetector.getOutputQueue());
    musicGenerator.start();

    ImageGenerator imageGenerator = new ImageGenerator(musicGenerator.getOutputQueue());
    imageGenerator.start();

    ImageCatcher imageCatcher = new ImageCatcher(imageGenerator.getOutputQueue());
    imageCatcher.start();

    ResetMessagePublisher resetMessagePublisher = new ResetMessagePublisher(Durations.RESET_INTERVAL);
    resetMessagePublisher.start();
  }

  private void createServer() {
    server = new Server();
    SocketConnector connector = new SocketConnector();
    connector.setPort(PORT);
    server.setConnectors(new Connector[] { connector });
    DefaultServlet defaultServlet = new RedirectingDefaultServlet();
    ServletHolder defaultServletHolder = new ServletHolder(defaultServlet);
    defaultServletHolder.setInitParameter("resourceBase", getResourceBase());
    LOG.info("resourceBase=" + defaultServletHolder.getInitParameter("resourceBase"));
    ServletContextHandler context = new ServletContextHandler();
    context.setWelcomeFiles(new String[] { "MakingMusic.html" });
    context.addServlet(defaultServletHolder, "/");
    context.addServlet(CurrentFrameServlet.class, "/currentFrame.jpg");
    context.addServlet(RestServlet.class, "/rest/v1/*");
    HandlerCollection handlers = new HandlerCollection();
    handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
    server.setHandler(handlers);
  }

  private String getResourceBase() {
    String packageName = getClass().getPackage().getName() + ".client";
    String packageFolder = packageName.replace(".", "/");
    URL resource = getClass().getClassLoader().getResource(packageFolder);
    String resourceBase = resource.toExternalForm();
    return resourceBase;
  }

  private void start() {
    try {
      server.start();
      server.join();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
