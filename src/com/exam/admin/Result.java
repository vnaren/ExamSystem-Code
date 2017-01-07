package com.exam.admin;

public class Result {
	int uid,marks;
	String uname;
	Result(int u,int m,String n){
		this.uid=u;
		this.marks=m;
		this.uname=n;
	}
	String getName(){
		return this.uname;
	}
	int getId(){
		return this.uid;
	}
	int getMarks(){
		return this.marks;
	}
}
