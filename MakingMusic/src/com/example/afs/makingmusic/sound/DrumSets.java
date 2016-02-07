// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.sound;

public class DrumSets {

	public static final Instrument largeDrumSet = new Instrument("Large Drum Set", 9, 0, 32, //
			Drum.acousticSnare, //
			Drum.sideStick, //
			Drum.closedHiHat, //
			Drum.lowFloorTom, //
			Drum.lowTom, //
			Drum.lowMidTom, //
			Drum.acousticBassDrum, //
			Drum.hiMidTom, //
			Drum.highTom, //
			Drum.highFloorTom, //
			Drum.crashCymbal1);

	public static final Instrument mediumDrumSet = new Instrument("Medium Drum Set", 9, 0, 32, //
			Drum.acousticSnare, //
			Drum.closedHiHat, //
			Drum.lowTom, //
			Drum.acousticBassDrum, //
			Drum.highTom, //
			Drum.crashCymbal1);

	public static final Instrument smallDrumSet = new Instrument("Small Drum Set", 9, 0, 32, //
			Drum.acousticSnare, //
			Drum.acousticBassDrum, //
			Drum.crashCymbal1);

}
