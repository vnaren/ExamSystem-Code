package com.exam.admin;
import com.constants.*;
import java.sql.*;
import javax.swing.*;

import javax.swing.border.EmptyBorder;

import com.constants.GlobalConstants;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class addNewTest {
	private static JFrame f;
	private static JPanel secondPanel,firstPanel;
	private static JTextField t1,t2,t3;
	private static JLabel l1,l2,l3;
	private static JButton btn,back;
	public static void manager(JFrame frame){
		f=frame;f.setTitle("Enter Test Details");	
		createGUI();
	}
	public static void createGUI(){		
		firstPanel = new JPanel();
		secondPanel = new JPanel();
		firstPanel.setLayout(new GridLayout(0, 2, 5, 5));
		secondPanel.setLayout(new FlowLayout());
		
		l1 = new JLabel("Enter Test Name :");
		l2 = new JLabel("Enter Number of Questions : ");
		l3 = new JLabel("Enter Total Time : ");
		
		t1 = new JTextField(10);
		t2 = new JTextField(10);
		t3 = new JTextField(10);
		
		btn = new JButton("SUBMIT");
		back = new JButton("GO BACK");
		
		firstPanel.add(l1);firstPanel.add(t1);
		firstPanel.add(l2);firstPanel.add(t2);
		firstPanel.add(l3);firstPanel.add(t3);
		firstPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		secondPanel.add(btn);secondPanel.add(back);
		secondPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		f.add(firstPanel);f.add(secondPanel);
		f.setVisible(true);
		f.setSize(400, 200);		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		bindEvents();
	}
	public static void bindEvents(){
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				f.getContentPane().removeAll();f.repaint();f.revalidate();				
				adminManager.manager(f);
			}
		});
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
						if(t1.getText().isEmpty() || t2.getText().isEmpty() || t3.getText().isEmpty())
						{
							JOptionPane.showMessageDialog(f, "Enter Values for all fields");
							return;
						}
						String tname = t1.getText();						
						Integer time = Integer.parseInt(t3.getText());
						Integer tques = Integer.parseInt(t2.getText());						
						Connection con = GlobalConstants.getConnection();
						con.setAutoCommit(false);
						Statement smt = con.createStatement();
						ResultSet rs=smt.executeQuery("select testname from tests where testname = '"+tname+"'");
						if(!rs.next())
						{
							System.out.println("insert into tests(testname,timeout) values('"+tname+"',"+time+")");
							smt.executeUpdate("insert into tests(testname,timeout) values('"+tname+"',"+time+")");							
							f.remove(firstPanel);f.remove(secondPanel);f.repaint();
							rs=smt.executeQuery(Queries.getlastCreatedTest());
							if(rs.next())
								addQuestionsNew.manager(f,tname,tques,rs.getInt(1));
							else {
								JOptionPane.showMessageDialog(f, "Unknown Error In Selected Last Created Test", "Error", 0);
								return;
							}
							con.commit();
						}
						else {
							try{
								con.rollback();
							}
							catch(Exception e2){}
							JOptionPane.showMessageDialog(f, "Test with same name exists", "Error", 0);
						}
				}
				catch(Exception e1){					
					e1.printStackTrace();					
					JOptionPane.showMessageDialog(f, "Enter Proper Values for all Fields", "Error", 0);
				}
			}
		});
	}
	public static void main(String args[]){
		manager(new JFrame());
	}
}
