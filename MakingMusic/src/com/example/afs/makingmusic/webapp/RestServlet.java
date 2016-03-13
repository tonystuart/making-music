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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.afs.makingmusic.common.Injector;
import com.example.afs.makingmusic.common.PropertyChange;

public class RestServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private Matchers matchers = new Matchers();

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (matchers.isMatch("^/properties$", request.getPathInfo())) {
      response.addHeader("Content-Type", "application/json");
      response.getOutputStream().write(Injector.getPropertyCache().getJsonProperties().getBytes());
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
}
