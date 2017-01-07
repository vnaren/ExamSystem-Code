package com.exam.admin;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.constants.GlobalConstants;

import java.sql.*;

public class editTestId {
	private static JFrame frame;
	private static JPanel inputpanel,btnpanel;
	private static JLabel label;
	private static JTextField input;
	private static JButton btn,back;
	public static void manager(JFrame f){		
		frame=f;f.setTitle("Select Test to Edit");
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		label=new JLabel("Enter Test Name : ");
		input = new JTextField();
		input.setMaximumSize(new Dimension(200,50));
		btn=new JButton("EDIT TEST");
		back=new JButton("GO BACK");
		inputpanel = new JPanel();
		inputpanel.setLayout(new BoxLayout(inputpanel, BoxLayout.X_AXIS));
		inputpanel.add(label);inputpanel.add(input);
		inputpanel.setBorder(new EmptyBorder(20,10,10,10));
		btnpanel = new JPanel();
		btnpanel.add(btn);
		btnpanel.add(back);
		frame.getContentPane().add(inputpanel);
		frame.getContentPane().add(btnpanel);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setSize(500, 150);
		bindEvents();
	}
	public static void bindEvents(){
		back.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.getContentPane().removeAll();
				adminManager.manager(frame);
			}			
		});
		btn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					String tname=input.getText();
					if(tname.isEmpty()){
						JOptionPane.showMessageDialog(frame, "Enter Test Name");
						return;
					}
					else {
						Connection con = GlobalConstants.getConnection();
						Statement smt = con.createStatement();
						ResultSet rs = smt.executeQuery("select testid from tests where testname='"+tname+"'");
						if(rs.next()){
							frame.getContentPane().removeAll();frame.repaint();
							editTestNew.manager(frame,rs.getInt(1),tname);
						}
						else{
							JOptionPane.showMessageDialog(frame, "Enter Valid Test Name");
							return;
						}
					}
				}
				catch(Exception e1){e1.printStackTrace();}
			}
		});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			manager(new JFrame());
	}

}
