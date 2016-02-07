// Copyright 2016 Anthony F. Stuart - All rights reserved.
//
// This program and the accompanying materials are made available
// under the terms of the GNU General Public License. For other license
// options please contact the copyright owner.
//
// This program is made available on an "as is" basis, without
// warranties or conditions of any kind, either express or implied.

package com.example.afs.makingmusic.process;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;

import com.example.afs.makingmusic.common.Step;

public class BackgroundDetector extends Step<Frame> {

	public static final double MINIMUM_AREA = 100;
	
	private BackgroundSubtractorMOG2 backgroundSubtractor;
	private Mat foregroundMask;

	public BackgroundDetector(BlockingQueue<Frame> inputQueue) {
		super(inputQueue);
		backgroundSubtractor = Video.createBackgroundSubtractorMOG2(5, 16, false);
		foregroundMask = new Mat();
	}

	@Override
	public void process(Frame frame) {
		Mat image = frame.getImage();
		Core.flip(image, image, 1);
		backgroundSubtractor.apply(image, foregroundMask);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(foregroundMask.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		int contourCount = contours.size();
		for (int contourIndex = 0; contourIndex < contourCount; contourIndex++) {
			MatOfPoint contour = contours.get(contourIndex);
			double contourArea = Imgproc.contourArea(contour);
			if (contourArea > BackgroundDetector.MINIMUM_AREA) {
				Rect item = Imgproc.boundingRect(contour);
				frame.addItem(item);
			}
		}
	}

}