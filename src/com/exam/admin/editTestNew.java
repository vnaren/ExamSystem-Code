package com.exam.admin;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.constants.*;
import com.constants.QuestionsAdmin.options;

import java.sql.*;

public class editTestNew {
	private static JFrame f;
	private static JPanel heading,p1,p2,p3;
	private static JPanel rbtf[];
	
	private static ButtonGroup bg;
	private static JRadioButton jb[];	
	private static JTextField tf[];	
	private static JTextArea qtn;
	private static JScrollPane sp;
	private static JButton quit,next,previous;
	private static JLabel examdet;
	
	private static Connection con;
	private static Statement smtq,smto,smtt,smta,smtu;
	private static ResultSet rsq,rst,rso,rsa;	
	private static int tid;
	
	private static Integer count=0,current=0;
	private static String opts[];
	
	private static LinkedHashMap<Integer, QuestionsAdmin> questions;
	private static QuestionsAdmin qobject;
	private static void layout(JFrame frame,int testid,String tname){
		tid=testid;
		opts=new String[4];
		f=frame;	f.setTitle("Edit Test Questions");
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		heading=new JPanel();
		p1=new JPanel();		
		p2=new JPanel();
		p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));		
		p3=new JPanel();			
		rbtf=new JPanel[4];
		jb=new JRadioButton[4];
		bg=new ButtonGroup();
		tf=new JTextField[4];
		
		examdet=new JLabel("Exam Name : "+tname);	
		heading.add(examdet);
		
		qtn=new JTextArea(3,45);		
		sp=new JScrollPane(qtn);		
		p1.add(sp);
		
		for(int i=0;i<4;i++){
			jb[i]=new JRadioButton();
			tf[i]=new JTextField(20);			
			rbtf[i]=new JPanel();
			rbtf[i].setLayout(new FlowLayout());			
			bg.add(jb[i]);	
			rbtf[i].add(jb[i]);
			rbtf[i].add(tf[i]);			
			p2.add(rbtf[i]);
		}
		
		quit = new JButton("UPDATE AND EXIT");quit.setPreferredSize(new Dimension(180,30));
		next = new JButton("UPDATE AND GO TO NEXT QUESTION");
		previous = new JButton("UPDATE AND GO TO PREVIOUS QUESTION");
		p3.add(previous);p3.add(next);p3.add(quit);	
		
		f.getContentPane().add(heading);f.getContentPane().add(p1);
		f.getContentPane().add(p2);f.getContentPane().add(p3);
		
		f.setVisible(true);
		f.setSize(new Dimension(600, 500));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	public static boolean checkTotal(JFrame f,int tid){
		try{
			con=GlobalConstants.getConnection();			
			Statement smt=con.createStatement();
			ResultSet rs=smt.executeQuery("select count(qtId) from testquestions where testid="+tid);
			if(rs.next()){
				int count=rs.getInt(1);
				if(count==0)
				{
					JOptionPane.showMessageDialog(null, "No Questions for this test - Quitting");
					f.getContentPane().removeAll();f.repaint();
					editTestId.manager(f);
					return false;
				}
			}			
		}
		catch(Exception e){e.printStackTrace();}
		return true;
	}
	public static void manager(JFrame frame,int testid,String tname){
		if(checkTotal(frame,testid)){
			layout(frame,testid,tname);
			loadQuestions();
			bindEvents();
		}
	}
	private static void loadQuestions(){
		questions = new LinkedHashMap<Integer,QuestionsAdmin>();		
		QuestionsAdmin.options opts=null;
		try{
			Integer qcount=0;
			con=GlobalConstants.getConnection();
			smtq=con.createStatement();
			smtt=con.createStatement();
			smto=con.createStatement();
			smta=con.createStatement();
			
			rsq=smtq.executeQuery(Queries.getQuestionIdProc(tid));
			while(rsq.next()){
				int qid = rsq.getInt(1);
				//System.out.println("Question Id : "+rsq.getInt(1));
				
				
				rst=smtt.executeQuery(Queries.getQuestionTextProc(qid));
				rst.next();
				String qtext =rst.getString(1);
				//System.out.println("Question text : "+qtext);
				
				rso=smto.executeQuery(Queries.getOptDetsProc(qid));				
				
				rsa=smta.executeQuery(Queries.getCorAnsProc(qid));
				rsa.next();
				int cans = rsa.getInt(1);
				//System.out.println("Correct Optiond Id : "+cans);
				qobject = new QuestionsAdmin();
				qobject.setQtId(qid);
				qobject.setQtName(qtext);
				qobject.setSelectedOption(cans);				
				qobject.opt = new LinkedHashMap<Integer,options>();
				
				Integer c=1;				
				while(rso.next()){
					opts = qobject.new options();
					int oid = rso.getInt(1);
					//System.out.println("Option Id : "+oid);					
					opts.setOpId(oid);
					
					String oname=rso.getString(2);
					//System.out.println("Option Name : "+oname);
					opts.setOpName(oname);
					
					qobject.opt.put(c, opts);									
					c++;
				}				
				questions.put(++qcount, qobject);	
				//System.out.println("\n\n");				
			}			
		}
		catch(Exception e){	e.printStackTrace();}		
		count=questions.size();
		current=1;
		System.out.println(count);
		if(count!=0)setNewData();
		else{
			JOptionPane.showMessageDialog(f, "No Questions for this test");
			f.dispose();
			editTestId.manager(new JFrame());			
		}
		for(Integer i=1;i<=count;i++){
			System.out.println(questions.get(i).getQtName());				
			for(Integer j=1;j<=4;j++){
				System.out.println(questions.get(i).opt.get(j).getOpName()+" "+questions.get(i).opt.get(j).getOpId());
			}
		}		
	}
	private static void bindEvents(){

		quit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(updateQuestion()){
					f.dispose();
					adminManager.manager(new JFrame());
				}
			}
		});
		
		next.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub				
					if(updateQuestion()){	
						current++;
						setNewData();
					}
					//if(current==count)next.setEnabled(false);
			}
			
		});
		
		previous.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(updateQuestion()){	
					current--;
					setNewData();
				}
				//if(current==1)previous.setEnabled(false);
			}
			
		});
	}
	public static void setNewData(){
		qtn.setText((questions.get(current).getQtName()));
		for(int i=0;i<4;i++){
			tf[i].setText((questions.get(current).opt.get(i+1).getOpName()));
			if( (questions.get(current).opt.get(i+1).getOpId()) == (questions.get(current).getSelectedOption()) ){
				jb[i].setSelected(true);
			}
		}
		if(current==1)
			previous.setEnabled(false);
		else
			previous.setEnabled(true);
		if(current==count)
			next.setEnabled(false);
		else
			next.setEnabled(true);
	}
	public static boolean updateQuestion(){
		int selected=-1;
		String qtext=qtn.getText();
		if(qtext.isEmpty())
		{
			JOptionPane.showMessageDialog(f, "Enter Question Text proceeding");
			return false;
		}
		for(int i=0;i<4;i++)
		{
			opts[i]=tf[i].getText();
			if(jb[i].isSelected())
				selected=i;
			if(opts[i].isEmpty()){
				JOptionPane.showMessageDialog(f, "Enter All Options before proceeding");
				return false;
			}
		}
		if(selected==-1){
			JOptionPane.showMessageDialog(f, "Select Correct Option");
			return false;
		}
		
		int opid[]=new int[4];
		int qid=(questions.get(current)).getQtId();
		int newCorOpId = (questions.get(current)).opt.get(selected+1).getOpId();
		//System.out.println(newCorOpId);
		for(int i=1;i<=4;i++){
			opid[i-1]=(questions.get(current)).opt.get(i).getOpId();
			//System.out.println(opid[i-1]);
		}
		try{
			con=GlobalConstants.getConnection();
			con.setAutoCommit(false);
			smtu=con.createStatement();
			for(int i=0;i<4;i++){
				smtu.executeUpdate(Queries.insNewOpId(opid[i], opts[i]));
				(questions.get(current)).opt.get(i+1).setOpName(opts[i]);
			}
			smtu.executeUpdate(Queries.insNewCorOpId(qid, newCorOpId));
			(questions.get(current)).setSelectedOption(newCorOpId);
			
			smtu.executeUpdate(Queries.insNewQtext(qid, qtext));
			(questions.get(current)).setQtName(qtext);
			
			con.commit();
		}
		catch(Exception e){
			try{con.rollback();}
			catch(SQLException se){se.printStackTrace();}
			e.printStackTrace();
			return false;
		}		
		return true;
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		manager(new JFrame(),30,"test6");
	}

}
