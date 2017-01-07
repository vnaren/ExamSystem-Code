
package com.exam.client;

public class clientConstatns {
	
	private static int testID = 0;
	private static int timeOut = 0;
	
	void clearConstants(){
		testID = 0;
		timeOut = 0;
	}
	
	public static void setTestID(int ID){
		testID = ID;
	}
	
	public static int getTestID(){
		return testID;
	}
	
	public static void setTimeOut(int timeout){
		timeOut = timeout;
	}
	
	public static int getTimeOut(){
		return timeOut;
	}
}
