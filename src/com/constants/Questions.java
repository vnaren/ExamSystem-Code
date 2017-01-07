package com.constants;

import java.util.Hashtable;

public class Questions{
	int qtId,selectedOption;
	String qtName;
	public Hashtable<Integer, options> opt;
	/** Constructor **/
	public Questions(){
		selectedOption = -1;
		opt = new Hashtable<Integer, options>();
	}
	
	/** set question id **/
	public void setQtId(int qtId){
		this.qtId = qtId;
	}
	
	/** get question id **/
	public int getQtId(){
		return this.qtId;
	}
	
	/** set option selected **/
	public void setSelectedOption(int selectedOption){
		this.selectedOption = selectedOption;
	}
	
	/** get option selected **/
	public int getSelectedOption(){
		return this.selectedOption;
	}
	
	/** set question text **/
	public void setQtName(String qtName){
		this.qtName = qtName;
	}
	
	/** get question text **/
	public String getQtName(){
		return this.qtName;
	}
	
	public String toString(){
		return this.qtName;
	}
	public class options{
		int opId;
		String opName;
		
		public void setOpId(int opId){
			this.opId = opId;
		}
		
		public int getOpId(){
			return this.opId;
		}
		
		public void setOpName(String opName){
			this.opName = opName;
		}
		
		public String getOpName(){
			return this.opName;
		}
	}
	
}
