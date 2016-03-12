// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.server;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matchers {
  private Map<String, Pattern> patterns = new HashMap<>();

  public Matcher getMatcher(String regularExpression, String input) {
    Pattern pattern = patterns.get(regularExpression);
    if (pattern == null) {
      pattern = Pattern.compile(regularExpression);
      patterns.put(regularExpression, pattern);
    }
    Matcher matcher = pattern.matcher(input);
    return matcher;
  }

  public String[] getMatches(String regularExpression, String input) {
    String[] matches;
    Matcher matcher = getMatcher(regularExpression, input);
    if (matcher.matches()) {
      int groupCount = matcher.groupCount();
      matches = new String[groupCount];
      for (int i = 0; i < groupCount; i++) {
        matches[i] = matcher.group(i + 1);
      }
    } else {
      matches = new String[0];
    }
    return matches;
  }

  public boolean isMatch(String regularExpression, String input) {
    Matcher matcher = getMatcher(regularExpression, input);
    return matcher.matches();
  }
}