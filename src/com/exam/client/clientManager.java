
package com.exam.client;

import javax.swing.*;

public class clientManager {
	
	public static void checkTest(JFrame frame){
		new setTest(frame).manager();
	}
	
	static void showClient(JFrame frame){
		new ClientUI(frame).manager();
	}
}
