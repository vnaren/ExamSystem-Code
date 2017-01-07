
package com.constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm  {
	
	JFrame frame;
	JPanel p;
	JLabel lb1,lb2;
	JTextField tf1;
	JPasswordField tf2;
	JButton submit;
	
	public LoginForm(JFrame fr){		
		frame = fr;
		frame = new JFrame("Login Details");
		p = new JPanel();
		lb1 = new JLabel("UserName : ");
		lb2 = new JLabel("Password : ");
		tf1 = new JTextField(10);
		tf2 = new JPasswordField(10);
		submit = new JButton();
		submit.setText("Submit");
		
		//p.setLayout(new GridLayout(3,2));
		p.setLayout(null);
		p.add(lb1);p.add(tf1);
		p.add(lb2);p.add(tf2);
		p.add(submit);
		
		lb1.setBounds(120, 120, 70, 30);
		lb2.setBounds(120, 170, 70, 30);
		tf1.setBounds(220, 120, 150, 25);
		tf2.setBounds(220, 170, 150, 25);
		submit.setBounds(220, 220, 80, 25);
		
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setLocationRelativeTo(null);
		frame.add(p);
		
		try{
			Connection con = GlobalConstants.getConnection();
			if(con==null){
				JOptionPane.showMessageDialog(frame, "No Database to Connect.Please follow instructions to create a working database");
				System.exit(0);
			}
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(frame, "No Database to Connect.Please follow instructions to create a working database");
		}		
	}
	
	void bindEvents(){
		submit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setEnabled(false);
				tf1.setEditable(false);
				tf2.setEditable(false);
				String passwordU = String.copyValueOf(tf2.getPassword());
				//System.out.println("Password: " + passwordU);
				if(tf1.getText().equals("") || passwordU.equals("") ){
					JOptionPane.showMessageDialog(frame, "Enter Username & Password");
				}
				else{
					if( Authentication.Authenticate(tf1.getText().trim(), passwordU.toString().trim()) == true ){
						frame.remove(p);
						frame.repaint();
						JOptionPane.showMessageDialog(frame, "Login Succesful");
						Authentication.sendRedirect(frame);
						frame.setEnabled(true);
						return;
					}
					JOptionPane.showMessageDialog(frame, "Invalid Username & Password");
				}
				frame.setEnabled(true);
				tf1.setEditable(true);tf1.setText("");
				tf2.setEditable(true);tf2.setText("");
				frame.toFront();
			}
		});
	}
	
	void makeVisible(){
		frame.setVisible(true);
	}
	
	public void manager(){
		bindEvents();
		makeVisible();
	}
	
	public static void main(String []args){
		new LoginForm(new JFrame()).manager();
	}
}
