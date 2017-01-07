
package com.constants;

public class Queries {
	
	private static String loginSql = null;
	//Added by Narendra
	
	//For Add Test
	private static String CorOpt = null;
	private static String insOpText = null;
	private static String lastOpid = null;
	private static String insQid = null;
	private static String insQtext = null;
	private static String lastQid = null;
	private static String lastCreatedTest = null;
	private static String qtnText = null;
	
	//For Edit Test
	private static String getQuestionId = null;
	private static String getQuestionText = null;
	private static String getOptDets=null;
	private static String getCorAns = null;	
	
	public static String getLoginSql(String username,String password){
		loginSql = "select count(*),rollId,sno from login "
				+ "where user = '"+ username +"' and password = '"+ password +"'";
		//System.out.println("Login Sql: " + loginSql);
		return loginSql;
	}
	//Added by Narendra
	public static String addUserFunction(String uname,String pwd){
		return "insert into login(user,password,rollId,isDelete) values('"+uname+"','"+pwd+"',"+1+","+2+")";		
	}
	public static String getQtnText(int testid){
		qtnText = "select qtname from questions where qtid = "
				+ "(select qtid from testquestions where testid = 1 limit 1)";
		return qtnText;
	}
	public static String getlastCreatedTest(){
		lastCreatedTest = "select max(testid) from tests ";
		return lastCreatedTest;
	}
	public static String getlastQid(){
		lastQid = "select max(qtId) from testquestions ";
		return lastQid;
	}
	public static String putinsQid(int tid,int qid){
		insQid="insert into testquestions(testid,qtId) values("+tid+","+qid+")";
		System.out.println(insQid);
		return insQid;
	}
	public static String putinsQtext(String qtext,int qid){
		insQtext="insert into questions(qtId,qtName) values("+qid+",'"+qtext+"')";
		System.out.println(insQtext);
		return insQtext;
	}
	public static String getlastOpid(){
		lastOpid="select max(opId) from qtoptions";
		return lastOpid;
	}
	public static String putinsOpt(String optext,int opid,int qid){
		insOpText="insert into qtoptions(opId,qtId,opName) values("+opid+","+qid+",'"+optext+"')";
		System.out.println(insOpText);
		return insOpText;
	}
	public static String putCorOpt(int qid,int opid){
		CorOpt="insert into qtans(qtId,opId) values("+qid+","+opid+")";
		System.out.println(CorOpt);
		return CorOpt;
	}
	public static String rollBackCorOp(int aques){
		return "delete from qtans where qtId in (select qtId from questions order by qtId desc limit "+aques+")";
	}
	public static String rollBackOps(int totalops){
		return "delete from qtoptions where opId in (select opId from qtoptions order by opId desc limit "+totalops+")";
	}
	public static String rollBackQts(int aques){
		return "delete from questions where qtId in (select qtId from questions order by qtId desc limit "+aques+")";
	}
	public static String rollBackTest(int tid){
		return "delete from tests where testid = "+tid;
	}
	public static String getQuestionIdProc(int testid){
		getQuestionId="select qtId from testquestions where testid = "+testid;
		return getQuestionId;
	}
	public static String getResult(int tid){
		return "select Sno,user,marks from login inner join user_tests_details on login.Sno=user_tests_details.userid where testid = "+tid;
	}
	
	public static String getQuestionTextProc(int qid){
		getQuestionText="select qtName from questions where qtId = "+qid;
		return getQuestionText;
	}
	
	
	public static String getOptDetsProc(int qid){
		getOptDets="select opId,opName from qtoptions where qtid = "+qid;
		return getOptDets;
	}
	
	
	public static String getCorAnsProc(int qid){
		getCorAns="select opId from qtans where qtid = "+qid;
		return getCorAns;
	}
	
	
	public static String insNewOpId(int opid,String opname){
		return "update qtoptions set opName='"+opname+"' where opId="+opid;
	}
	
	public static String insNewCorOpId(int qid,int opid){
		return "update qtans set opId="+opid+" where qtId = "+qid;
	}
	
	public static String insNewQtext(int qid,String qtext){
		return "update questions set qtName='"+qtext+"' where qtId="+qid;
	}
		
	public static void main(String args[]){
		System.out.println(getQtnText(1));
	}
}
