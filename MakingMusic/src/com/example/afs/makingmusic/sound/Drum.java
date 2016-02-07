// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.sound;

public class Drum extends Sound {

	public static final Drum acousticBassDrum = new Drum("Acoustic Bass Drum", 35);
	public static final Drum acousticSnare = new Drum("Acoustic Snare", 38);
	public static final Drum closedHiHat = new Drum("Closed Hi-Hat", 42);
	public static final Drum crashCymbal1 = new Drum("Crash Cymbal 1", 49);
	public static final Drum highFloorTom = new Drum("High Floor Tom", 43);
	public static final Drum highTom = new Drum("High Tom", 50);
	public static final Drum hiMidTom = new Drum("Hi-Mid Tom", 48);
	public static final Drum lowFloorTom = new Drum("Low Floor Tom", 41);
	public static final Drum lowMidTom = new Drum("Low-Mid Tom", 47);
	public static final Drum lowTom = new Drum("Low Tom", 45);
	public static final Drum sideStick = new Drum("Side Stick", 37);

	public Drum(String name, int value) {
		super(name, value);
	}

}
