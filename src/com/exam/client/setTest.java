
package com.exam.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

import com.constants.GlobalConstants;

class setTest {
	
	JFrame frame;
	JPanel p;
	JLabel l;
	JTextField tf;
	JButton submit;
	
	Connection con;
	Statement stmt;
	ResultSet rs;
	
	setTest(JFrame frame){
		this.frame = frame;
		
		p = new JPanel();
		p.setLayout(null);
		
		l = new JLabel();
		l.setText("Enter Test ID:");
		p.add(l);
		
		tf = new JTextField(10);
		p.add(tf);
		
		submit = new JButton("Submit");
		p.add(submit);
		
		l.setBounds(90, 150, 100, 25);
		tf.setBounds(200, 150, 50, 25);
		submit.setBounds(250, 150, 75, 25);
		p.setBounds(0, 0, 0, 0);
		
		this.frame.add(p);
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(500, 500);
		this.frame.setLocationRelativeTo(null);
		this.frame.toFront();
		
	}
	
	void bindEvents(){
		submit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int id;
				String sql;
				try{
					frame.setEnabled(false);
					
					id = Integer.parseInt(tf.getText());
					
					con = GlobalConstants.getConnection();
					stmt = con.createStatement();
					
					sql = clientQueries.getTestSql(id);
					rs = stmt.executeQuery(sql);
					
					Statement stmt1 = con.createStatement();
					ResultSet rs1 = stmt1.executeQuery(clientQueries.getCheckTest(id));
					
					if(!rs1.next()){
						if(rs.next()){
							clientConstatns.setTestID(Integer.parseInt(rs.getString(1)));
							clientConstatns.setTimeOut(Integer.parseInt(rs.getString(3))*60);
							rs.close();
							stmt.close();
							frame.remove(p);
							frame.repaint();
							clientManager.showClient(frame);
							frame.setEnabled(true);
						}
						else{
							JOptionPane.showMessageDialog(frame, "Invalid Test Id");
						}
					}
					else{
						JOptionPane.showMessageDialog(frame, "Test Attempted Already");
					}
					rs.close();
					stmt.close();
				}
				catch(Exception er){
					er.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Enter Correct Test Id");
				}
				frame.setEnabled(true);
			}
		});
	}
	
	void manager(){
		bindEvents();
	}
	
}
