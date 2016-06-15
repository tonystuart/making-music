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
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.PropertyChange;
import com.example.afs.makingmusic.common.StateRequest;
import com.example.afs.makingmusic.utilities.FileUtilities;

public class RestServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private Matchers matchers = new Matchers();

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (matchers.isMatch("^/instruments$", request.getPathInfo())) {
      writeProperties(response);
    } else if (matchers.isMatch("^/metrics$", request.getPathInfo())) {
      FileUtilities.writeJson(response, Injector.getMetrics());
    } else if (matchers.isMatch("^/settings$", request.getPathInfo())) {
      writeProperties(response);
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String[] matches;
    String pathInfo = request.getPathInfo();
    if ((matches = matchers.getMatches("/properties/([^/]+)/(.+)", pathInfo)).length > 0) {
      String name = matches[0];
      String value = matches[1];
      Injector.getMessageBroker().publish(new PropertyChange(name, value));
    }
  }

  private void writeProperties(HttpServletResponse response) {
    StateRequest stateRequest = new StateRequest(new ConcurrentHashMap<String, String>());
    Injector.getMessageBroker().publish(stateRequest);
    FileUtilities.writeJson(response, stateRequest);
  }
}
