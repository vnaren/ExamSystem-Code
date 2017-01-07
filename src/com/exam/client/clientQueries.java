package com.exam.client;

import com.constants.GlobalConstants;

public class clientQueries {
	
	private static String testSql = null;
	
	private static String questSql = null;
	
	private static String qtOptionsSql = null;
	
	private static String submitOptions = null;
	
	private static String checkTest = null;
	
	static String getCheckTest(int id){
		checkTest = "select testid, userid from user_tests_selected_options "
				+ "where testid = "+ id +" and userid = "+ GlobalConstants.getUserID();
		return checkTest;
	}
	
	public static String getTestSql(int testId){
		testSql = "select testid,testname,timeout from tests "
				+ "where testid = " + testId + "";
		return testSql;
	}
	
	static String getQuestSql(int id){
		questSql = "select tq.qtId as qtId,q.qtName as qtName from testquestions tq "
				+ "left join questions q on tq.qtId = q.qtId "
				+ "where tq.testid = "+ id;
		//System.out.println("Questions Sql: " + questSql);
		return questSql;
	}
	
	public static String getQtOptionsSql(int qtId){
		qtOptionsSql = "select opId,opName from qtoptions where qtId = " + qtId;
		//System.out.println("Options Sql: " + qtOptionsSql);
		return qtOptionsSql;
	}
	
	static String submitOptions(int testId,int userId,int qtId, int opId){

		submitOptions = "insert into user_tests_selected_options(testid,userid,qtId,opId)"
						+ " values(" + testId + ","
						+ userId +","+ qtId +","+ opId +")";
		return submitOptions;
	}
	
	// Admin Functions
	
	
}
