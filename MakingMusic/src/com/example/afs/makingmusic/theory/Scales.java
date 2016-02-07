// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.theory;

public class Scales {

  // C  C# D  D# E  F  F# G  G# A  A# B
  // 60 61 62 63 64 65 66 67 68 69 70 71

  public static final Scale harmonicMinor = new Scale("Harmonic Minor", 2, 1, 2, 2, 1, 3, 1);
  public static final Scale major = new Scale("Major", 2, 2, 1, 2, 2, 2, 1);
  public static final Scale melodicMinor = new Scale("Melodic Minor", 2, 1, 2, 2, 2, 2, 1);
  public static final Scale minor = new Scale("Natural Minor", 2, 1, 2, 2, 1, 2, 2);
}
