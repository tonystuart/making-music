// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Streams {

  public static String read(InputStream inputStream) {
    try {
      int rc;
      byte[] buffer = new byte[4096];
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while ((rc = inputStream.read(buffer)) != -1) {
        baos.write(buffer, 0, rc);
      }
      return baos.toString("utf-8");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
