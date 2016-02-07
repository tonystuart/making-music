// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.common;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Step<T> extends Thread {

	private BlockingQueue<T> inputQueue;
	private BlockingQueue<T> outputQueue;
	private boolean terminated;
	private TimeKeeper timeKeeper;

	public Step() {
		this(null);
	}

	public Step(BlockingQueue<T> inputQueue) {
		this.inputQueue = inputQueue;
		setName(getClass().getSimpleName());
		timeKeeper = new TimeKeeper(getName());
	}

	public BlockingQueue<T> getOutputQueue() {
		if (outputQueue == null) {
			outputQueue = new ArrayBlockingQueue<>(1);
		}
		return outputQueue;
	}

	public T process() throws InterruptedException {
		return null;
	}

	public void process(T message) throws InterruptedException {
	}

	@Override
	public void run() {
		while (!terminated) {
			runBody();
		}
	}

	public void start(int period) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runBody();
			}
		}, 0, period);
	}

	public void terminate() {
		terminated = true;
	}

	private void runBody() {
		try {
			T message;
			timeKeeper.beginLoop();
			if (inputQueue == null) {
				timeKeeper.beginProcess();
				message = process();
				timeKeeper.endProcess();
			} else {
				timeKeeper.beginInput();
				message = inputQueue.take();
				timeKeeper.endInput();
				timeKeeper.beginProcess();
				process(message);
				timeKeeper.endProcess();
			}
			if (outputQueue != null) {
				timeKeeper.beginOutput();
				outputQueue.put(message);
				timeKeeper.endOutput();
			}
			timeKeeper.endLoop();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}