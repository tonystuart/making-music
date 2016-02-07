// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

public class ActiveSound {

	private int channel;
	private long offTime;
	private int value;

	public ActiveSound(int channel, int value, long offTime) {
		this.channel = channel;
		this.value = value;
		this.offTime = offTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ActiveSound other = (ActiveSound) obj;
		if (channel != other.channel) {
			return false;
		}
		if (value != other.value) {
			return false;
		}
		return true;
	}

	public int getChannel() {
		return channel;
	}

	public long getOffTime() {
		return offTime;
	}

	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + channel;
		result = prime * result + value;
		return result;
	}

}