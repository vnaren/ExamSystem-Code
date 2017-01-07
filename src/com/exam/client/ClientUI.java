
package com.exam.client;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.*;

import com.constants.GlobalConstants;
import com.constants.Questions;


class ClientUI {
	JFrame frame;
	JPanel p;
	JLabel l,timeLabel;
	ButtonGroup bg;
	JRadioButton jb[];
	JButton next,previous,submit;
	
	JComboBox<Object> qList;
	
	Object timer;
	
	int count = 0, current = 1;
	int timeOut;
	
	Hashtable<Integer, Questions> questions;
	
	ClientUI(JFrame frame) {
		this.frame = frame;
		p = new JPanel();
		p.setLayout(null);
		
		l = new JLabel();
		l.setText("Question");
		p.add(l);
		
		timeLabel = new JLabel();
		timeLabel.setText("Time :");
		p.add(timeLabel);
		timeOut = clientConstatns.getTimeOut();
		
		bg = new ButtonGroup();
		
		jb = new JRadioButton[4];
		for(int i=0;i<4;i++){
			jb[i] = new JRadioButton();
			p.add(jb[i]);
			bg.add(jb[i]);
		}
		
		next = new JButton("Next");
		previous = new JButton("Previous");
		submit = new JButton("Submit");
		qList = new JComboBox<Object>();
		
		qList.setBounds(30, 40, 450, 25);
		l.setBounds(30, 70, 450, 20);
		timeLabel.setBounds(250, 10, 100, 25);
		jb[0].setBounds(70, 100, 100, 20);
		jb[1].setBounds(70, 130, 100, 20);
		jb[2].setBounds(70, 160, 100, 20);
		jb[3].setBounds(70, 190, 100, 20);
		
		previous.setBounds(120, 260, 100, 30);
		next.setBounds(250, 260, 100, 30);
		submit.setBounds(180, 330, 100, 30);
		
		p.add(previous);
		p.add(next);
		p.add(submit);
		p.add(qList);
		
		this.frame.add(p);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(500, 500);
		this.frame.setLocationRelativeTo(null);
		this.frame.toFront();
	}
	
	void bindEvents(){
		
		previous.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				current--;
				set();
			}
		});
		
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				current++;
				set();
			}
		});
		
		submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				((countDownMin)timer).stopTimer();
				timerFinished();
			}
		});
		
		qList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				current = qList.getSelectedIndex() + 1;
				set();
			}
		});
		
		
		class radioAction implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent ae){
				int optId = -1;
				for(int i=0;i<4;i++){
					if(jb[i].isSelected()){
						optId = i;
						break;
					}
				}
				Questions temp = questions.get(current);
				temp.setSelectedOption(optId + 1);
			}
		}
		radioAction radioEvent = new radioAction();
		
		for(int i=0;i<4;i++){
			jb[i].addActionListener(radioEvent);
		}
	}
	
	void timerCountDown(){
		timer = new countDownMin(timeOut, frame, timeLabel, this);
	}
	
	void timerFinished(){
		frame.setEnabled(false);
		frame.remove(p);
		frame.add(new JLabel("Submitting details"));
		frame.repaint();
		frame.setEnabled(true);
		frame.setSize(300, 200);
		frame.setVisible(true);
		submitResult();
	}
	
	void makeFrameVisible(){
		
		this.frame.setVisible(true);
		this.frame.toFront();
	}
	
	void loadQuestions(){
		try{
			Connection con = GlobalConstants.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			
			rs = stmt.executeQuery(clientQueries.getQuestSql(clientConstatns.getTestID()));
			
			questions = new Hashtable<Integer, Questions>();
			
			while(rs.next()){
				count++;
				int quId = Integer.parseInt(rs.getString(1));
				
				Questions qDetail = new Questions();
				qDetail.setQtId(quId);
				qDetail.setQtName(rs.getString(2));
				
				Statement stmtOpt = con.createStatement();
				ResultSet rsOpt = stmtOpt.executeQuery(clientQueries.getQtOptionsSql(quId));
				for(int i=0;i<4;i++){
					rsOpt.next();
					Questions.options temp = qDetail.new options();
					temp.setOpId(Integer.parseInt(rsOpt.getString(1)));
					temp.setOpName(rsOpt.getString(2));
					qDetail.opt.put(i+1, temp);
				}
				
				questions.put(count, qDetail);
				
				rsOpt.close();
				stmtOpt.close();
			}
			rs.close();
			stmt.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	void loadComboBox(){
		for(int i=1;i<=count;i++){
			Questions temp = questions.get(i);
			qList.addItem(temp);
		}
	}
	
	void set(){
		bg.clearSelection();
		
		Questions temp = questions.get(current);
		String text = "Ques " + current + " : " + temp.getQtName();
		l.setText("<html>"+text+"</html>");
		
		for(int i=0;i<4;i++){
			Questions.options opText = temp.opt.get(i+1);
			jb[i].setText("<html>"+opText.getOpName()+"</html>");
		}
		
		if(temp.getSelectedOption() != -1)
			jb[temp.getSelectedOption()-1].setSelected(true);
		
		previous.setEnabled(true);
		if(current == 1)
			previous.setEnabled(false);
		
		next.setEnabled(true);
		if(current == count)
			next.setEnabled(false);
	}
	
	void printTotal(){
		for(Map.Entry<Integer, Questions> m:questions.entrySet()){
			print(m.getKey());
		}
	}
	
	void print(int key){
		System.out.println("Inteface Question Id: " + key);
		Questions ques = questions.get(key);
		System.out.println("Database Question Id: " + ques.getQtId());
		System.out.println("Question: " + ques.getQtName());
			
		for(Map.Entry<Integer, Questions.options> op:ques.opt.entrySet()){
			System.out.println("Interface Option Id: " + op.getKey());
			Questions.options temp = op.getValue();
			System.out.println("Database Option Id: " + temp.getOpId());
			System.out.println("Option: " + temp.getOpName());
		}
		System.out.println();
	}
	
	void submitResult(){
		Connection con = null;
		Statement stmt = null;
		try{
			con = GlobalConstants.getConnection();
			
			int userID = GlobalConstants.getUserID();
			int testID = clientConstatns.getTestID();
			
			con.setAutoCommit(false);
			
			for(int i=1;i<=count;i++){
				stmt = con.createStatement();
				String query = new String();
				Questions temp = questions.get(i);
				
				int qtIdS = temp.getQtId();
				System.out.println("QtId: " + qtIdS + "\n" + i + ". " + temp.getQtName());
				
				int optIdS = (temp.getSelectedOption()==-1)?-1:temp.opt.get(temp.getSelectedOption()).getOpId();
				
				if(optIdS != -1){
					System.out.println("Option Id: " + optIdS);
					System.out.println("Option Name: " + temp.opt.get(temp.getSelectedOption()).getOpName());
				}
				else{
					System.out.println("Option Not Selected");
				}
				
				query = clientQueries.submitOptions(testID, userID, qtIdS, optIdS);
				stmt.executeUpdate(query);
				stmt.close();
			}
			con.commit();
		}
		catch(Exception e){
			try{
				if(con != null){
					con.rollback();
				}
			}
			catch(Exception et){
				et.printStackTrace();
			}
		}
		finally{
			try{
				stmt.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	void manager(){
		loadQuestions();
		//printTotal();
		loadComboBox();
		timerCountDown();
		set();
		bindEvents();
		//makeFrameVisible();
	}
	
	public static void main(String []args){
		new ClientUI(new JFrame("Exam")).manager();
	}
	
}
