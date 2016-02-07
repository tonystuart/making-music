// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

// Return the nth item associated with position of offset in range
// See https://blogs.msdn.microsoft.com/oldnewthing/20120514-00/?p=7633
// Note that using a constant integer divisor leads to cumulative rounding errors.
// range = 640 (e.g. 0 to 639)
// offset = 639
// values.length = 14
// integer divisor = 640 / 14 = 45.714 = 45
// 639 / 45 = 14.2 = 14 = index out of bounds
public class MulDiv {

  public static final int scale(int range, int offset, int itemCount) {
    return (offset * itemCount) / range;
  }

  public static final <T> T scale(int range, int offset, T[] values) {
    return values[scale(range, offset, values.length)];
  }

}
