package com.exam.admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;

import com.constants.GlobalConstants;
import com.constants.Queries;

public class addQuestionsNew {
	private static JFrame f;
	private static JPanel heading,p1,p2,p3,p4;
	private static JPanel rbtf[];
	
	private static ButtonGroup bg;
	private static JRadioButton jb[];	
	private static JTextField tf[];	
	private static JTextArea qtn;
	private static JScrollPane sp;
	private static JButton submitbtn,backbtn;
	private static JLabel examdet,status;
	
	private static Connection con;
	private static Statement smt;
	private static ResultSet rs;
	
	private static String opts[];
	private static String qtext;
	private static int tques;
	private static int tid;
	private static int aques;
	private static int qid;
	private static int opid;
	private static int selected;	
	public static void manager(JFrame frame,String tname,int nques,int testid){
		tid=testid;
		tques=nques;
		f=frame;	
		f.setTitle("Add Questions");
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		heading=new JPanel();
		p1=new JPanel();		
		p2=new JPanel();
		p2.setLayout(new BoxLayout(p2,BoxLayout.Y_AXIS));		
		p3=new JPanel();
		p4=new JPanel();		
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
		
		submitbtn = new JButton("ADD QUESTION");
		backbtn = new JButton("GO BACK");
		p3.add(submitbtn);//p3.add(backbtn);	
		
		status=new JLabel();		
		p4.add(status);
		
		f.getContentPane().add(heading);f.getContentPane().add(p1);f.getContentPane().add(p2);f.getContentPane().add(p3);f.getContentPane().add(p4);
		f.setVisible(true);
		f.setSize(new Dimension(600, 450));
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent w){
				JOptionPane.showMessageDialog(f, "You did not add all questions", "Cannot Quit", JOptionPane.ERROR_MESSAGE);
				/*if(JOptionPane.showConfirmDialog(f, "Are You Sure You want to Exit?", 
						"Exit Test?", JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
						f.getContentPane().removeAll();f.repaint();f.dispose();
						adminManager.manager(f);
				}*/
			}
		});
		bindEvents();
	}
	
	private static void insQuestion(){		
		try{
			con=GlobalConstants.getConnection();
			con.setAutoCommit(false);
			smt=con.createStatement();
			rs=smt.executeQuery(Queries.getlastQid());
			if(rs.next()){
				qid = rs.getInt(1);
				System.out.println("question id : "+qid+" Test Id: "+tid);
				//smt.executeUpdate(Queries.putinsQid(tid,qid+1));
				//smt.executeUpdate(Queries.putinsQtext(qtext,qid+1));
				/* changed order of above 2 statements */
				smt.executeUpdate(Queries.putinsQtext(qtext,qid+1));
				smt.executeUpdate(Queries.putinsQid(tid,qid+1));
				rs=smt.executeQuery(Queries.getlastOpid());
				if(rs.next()){
					opid = rs.getInt(1);				
					for(int i=0;i<4;i++){
						smt.executeUpdate(Queries.putinsOpt(opts[i],opid+i+1,qid+1));					
						if(i==selected)
							smt.executeUpdate(Queries.putCorOpt(qid+1,opid+i+1));
					}
				}
				tques-=1;aques++;
				status.setText("Added : "+aques+" Remaining :"+tques);
				for(int i=0;i<4;i++)tf[i].setText("");
				qtn.setText("");
				bg.clearSelection();
				if(tques==0){
					aques=0;
					f.getContentPane().removeAll();f.repaint();
					adminManager.manager(f);
				}
			}			
			con.commit();
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				con.rollback();
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}	
	}
	private static void bindEvents(){
		backbtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(tques!=0){
					JOptionPane.showMessageDialog(f, "Enter All Questions before Quitting");
					return;
				}
				f.dispose();
				adminManager.manager(new JFrame());
			}
			
		});
		submitbtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				opts=new String[4];
				qtext = qtn.getText();
				selected=-1;
				for(int i=0;i<4;i++)
					opts[i]=tf[i].getText();
				for(int i=0;i<4;i++)
					if(jb[i].isSelected())
						selected=i;				
				System.out.println(selected);
				if(qtext.isEmpty()){
					JOptionPane.showMessageDialog(f, "Enter Question Text");
					return;
				}
				for(int k=0;k<4;k++){
					if(opts[k].isEmpty()){
						JOptionPane.showMessageDialog(f, "Enter Option Text");
						return;
					}
				}
				if(selected==-1){
					JOptionPane.showMessageDialog(f, "Select Correct Option");
					return;
				}
				try{
					con=GlobalConstants.getConnection();
					smt=con.createStatement();
					//rs=smt.executeQuery(Queries.getlastCreatedTest());
					//if(rs.next()){
						//int tid = rs.getInt(1);
						//System.out.println(tid);
						insQuestion();						
					//}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		manager(new JFrame(),"",20,1);
	}

}
