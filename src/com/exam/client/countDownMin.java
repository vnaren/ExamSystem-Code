package com.exam.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class countDownMin implements ActionListener{
	
	JFrame frame;
	JLabel timeLabel;
	ClientUI obj;
	Timer timer;
	int timeOut;
	
	countDownMin(int time,JFrame frame,JLabel label,ClientUI obj){
		
		this.timeOut = time;
		this.frame = frame;
		this.timeLabel = label;
		this.obj = obj;
		timer = new Timer(1000, this);
		timer.setInitialDelay(1);
		timer.setRepeats(true);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e){
		int hours = timeOut/60;
		int minutes = timeOut % 60;
		timeOut--;
		if(hours > -1 && minutes > -1)
			timeLabel.setText("Time :" + hours + "M:" + minutes + "S");
		else{
			stopTimer();
		}
	}
	
	public void stopTimer(){
		timer.stop();
		timeLabel.setText("Time : --:--");
		obj.timerFinished();
	}
}
