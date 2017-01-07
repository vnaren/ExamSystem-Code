
package com.exam.admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import com.constants.GlobalConstants;
import com.constants.LoginForm;
import com.constants.Queries;

import java.sql.*;

public class adminManager {
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;
	private static JFrame f;
	private static JPanel p;
	private static JButton addUser,exitTest,addTest,editTest,resultTest;	
	public static void manager(JFrame frame){
		f=frame;
		f.setSize(400,350);
		p = new JPanel();
		//p.removeAll();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		Dimension d = new Dimension(100,30);
		addUser = new JButton("ADD USER");addUser.setMaximumSize(d);addUser.setPreferredSize(d);
		exitTest = new JButton("LOG OUT");exitTest.setMaximumSize(d);exitTest.setPreferredSize(d);
		addTest =  new JButton("ADD TEST");addTest.setMaximumSize(d);addTest.setPreferredSize(d);
		editTest = new JButton("EDIT TEST");editTest.setMaximumSize(d);editTest.setPreferredSize(d);
		resultTest = new JButton("RESULTS");resultTest.setMaximumSize(d);resultTest.setPreferredSize(d);
		p.add(Box.createRigidArea(new Dimension(0,25)));
		p.add(addUser);		
		p.add(Box.createRigidArea(new Dimension(0,25)));
		p.add(addTest);
		p.add(Box.createRigidArea(new Dimension(0,25)));
		p.add(editTest);
		p.add(Box.createRigidArea(new Dimension(0,25)));
		p.add(resultTest);
		p.add(Box.createRigidArea(new Dimension(0,25)));
		p.add(exitTest);
		f.setTitle("Select Option");
		f.setLayout(new FlowLayout());
		f.add(p);
		f.setVisible(true);		
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bindEvents();
	}
	public static void addUserFrame(JFrame f){		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel parentPanel = new JPanel();
		
		JLabel l1 = new JLabel("Enter User-Name");
		JLabel l2 = new JLabel("Enter Password");
		
		JTextField t1 = new JTextField(10);
		JPasswordField t2 = new JPasswordField(10);
		
		JButton btn = new JButton("Submit");
		JButton back = new JButton("Go Back");
		
		p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));
		p3.setLayout(new FlowLayout(FlowLayout.CENTER));
		parentPanel.setLayout(new BoxLayout(parentPanel,BoxLayout.Y_AXIS));
		
		p1.setSize(200,100);p2.setSize(200,100);p3.setSize(200,100);
		p1.add(l1);p1.add(t1);
		p2.add(l2);p2.add(t2);
		p3.add(btn);p3.add(back);
		parentPanel.add(p1);parentPanel.add(p2);parentPanel.add(p3);
		f.add(parentPanel);
		
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//f.remove(parentPanel);
				f.getContentPane().removeAll();
				f.repaint();				
				manager(f);				
			}
		});
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					con = GlobalConstants.getConnection();
					stmt = con.createStatement();
					String username=t1.getText();
					String password=String.copyValueOf(t2.getPassword());
					if(!username.isEmpty()&&!password.isEmpty()){
						rs=stmt.executeQuery("select user from login where user='"+username+"'");
						if(!rs.next()){
							String q = Queries.addUserFunction(username,password);
							System.out.println(q);
							stmt.executeUpdate(q);
						}
						else{
							JOptionPane.showMessageDialog(f, "User Already exists");
							return;
						}
					}
					else {
						JOptionPane.showMessageDialog(f, "Enter Values for All Fields");
						return;
					}
					stmt.close();con.close();
					f.getContentPane().removeAll();f.repaint();
					manager(f);
					/*f.add(p);f.setSize(300,800);					
					f.setLocationRelativeTo(null);
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);*/
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		});
		
	}
	public static void bindEvents(){
		addUser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				f.getContentPane().removeAll();
				f.repaint();	
				addUserFrame(f);
				f.setSize(400, 150);
				f.setTitle("Add User");
				f.setVisible(true);				
			}
		});
		exitTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				f.getContentPane().removeAll();f.dispose();
				new LoginForm(new JFrame()).manager();
			}
		});
		addTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				f.getContentPane().removeAll();f.repaint();
				addNewTest.manager(f);
			}
		});
		resultTest.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				f.remove(p);f.repaint();
				TestResultNew.manager(f);
			}
			
		});
		editTest.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				f.remove(p);f.repaint();
				editTestId.manager(f);
			}
		});
	}
	
	public static void main(String []args){
		manager(new JFrame());
	}
}
