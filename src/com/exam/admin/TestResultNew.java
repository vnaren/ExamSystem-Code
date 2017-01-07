package com.exam.admin;

import java.util.ArrayList;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import com.constants.GlobalConstants;
import com.constants.Queries;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

public class TestResultNew {
	private static JFrame frame;	
	private static JLabel totMarks;
	private static JButton submit,save,back;
	private static JTextField tname;
	private static JPanel testpanel,submitpanel,totMarksPanel,markspanel,accesspanel;
	private static JTable table;
	private static JScrollPane p;
	private static Border eb = new EmptyBorder(10,10,10,10);
	
	private static ArrayList<Result> result = new ArrayList<Result>();
	private static Connection con=GlobalConstants.getConnection();
	private static Statement smt,smtcount;
	private static ResultSet rs,rscount;
	private static int outof;
	private static String t;
	public static String logo = "resources/jntulogo.jpg";
	private static void genTable(ArrayList<Result> r){		
		/** Display Marks **/
		table = new JTable();
		DefaultTableModel model=new DefaultTableModel();
		model.setColumnIdentifiers(new Object[]{"User-ID","User Name","Marks Obtained"});
		table.setModel(model);
		table.setRowHeight(30);
		
		p = new JScrollPane(table);
		p.setPreferredSize(new Dimension(200,200));p.setMinimumSize(new Dimension(200,200));
		
		markspanel=new JPanel();
		markspanel.setLayout(new BoxLayout(markspanel, BoxLayout.Y_AXIS));
		markspanel.setBorder(eb);
		
		for(int i=0;i<result.size();i++)
		{
			Object row[]=new Object[3];
			Result res = r.get(i);
			row[0]=res.getId();
			row[1]=res.getName();
			row[2]=res.getMarks();
			model.addRow(row);
		}		
		markspanel.add(p);	
		
		/** Total Marks **/
		totMarks.setText(" Total Marks : "+outof);
		
		/** Display Access panel **/		
		accesspanel.setVisible(true);
		
		frame.add(totMarksPanel);
		frame.add(markspanel);		
		frame.add(accesspanel);
		frame.pack();frame.setLocationRelativeTo(null);
	}
	
	private static void btnManager(){
		t = tname.getText();
		result.clear();
		frame.remove(markspanel);
		//System.out.println(t);
		try{			
			smt=con.createStatement();
			smtcount=con.createStatement();
			rs=smt.executeQuery("select testid from tests where testname='"+t+"'");
			if(rs.next()){
				int tid=rs.getInt(1);				
				rs = smt.executeQuery(Queries.getResult(tid));				
				rscount = smtcount.executeQuery("select count(qtId) from testquestions where testid="+tid);
				rscount.next();
				outof=rscount.getInt(1);				
			}
			else {
				JOptionPane.showMessageDialog(null, "No Test with such name");
				return;
			}
			if(rs.isBeforeFirst()){
				while(rs.next()){
					Result r = new Result(rs.getInt(1),rs.getInt(3),rs.getString(2));
					result.add(r);
				}
				genTable(result);								
			}
			else {
				JOptionPane.showMessageDialog(null, "This test hasn't been taken yet");
				return;
			}			
		}
		catch(Exception e){e.printStackTrace();}
	}
		
	
	public static void manager(JFrame f){
		frame=f;f.setTitle("Retrieve Scores");
		
		/** Display Text Box **/
		JLabel tname_label = new JLabel("Enter Test Name : ");
		tname=new JTextField(20);
		Dimension tfd = new Dimension(400,30);
		tname.setPreferredSize(tfd);
		tname.setMaximumSize(tfd);
		testpanel=new JPanel();
		testpanel.setBorder(new EmptyBorder(10,10,5,10));
		testpanel.setLayout(new BoxLayout(testpanel, BoxLayout.X_AXIS));
		testpanel.add(tname_label);testpanel.add(tname);
		
		
		/** Submit button and it's ActionListener **/
		submit = new JButton("GET RESULTS");
		back = new JButton("GO BACK");
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnManager();
			}
			
		});
		back.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.getContentPane().removeAll();frame.repaint();
				adminManager.manager(frame);
			}
			
		});
		submitpanel = new JPanel();
		submitpanel.add(submit);submitpanel.add(back);
		
		/** Marks Panel **/
		markspanel=new JPanel();
		markspanel.setPreferredSize(new Dimension(200,200));markspanel.setMinimumSize(new Dimension(200,200));
		markspanel.setBorder(eb);
		
		/** Display total no.of marks for a given test **/
		totMarksPanel=new JPanel();
		totMarks = new JLabel();
		totMarks.setFont(new Font("Tahoma", Font.BOLD, 14));
		totMarks.setForeground(Color.DARK_GRAY);
		totMarksPanel.add(totMarks);
		
		/** Display  access panel **/
		save = new JButton("SAVE RESULT");
		save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try 
				{
					PdfWriter w = new PdfWriter("C:\\Users\\Narendra\\Desktop\\Result.pdf");
					PdfDocument d = new PdfDocument(w);
					Document doc = new Document(d);							
					doc.add(new Paragraph("Test Name : "+t).setTextAlignment(TextAlignment.CENTER));
					doc.add(new Paragraph("Maximum Marks : "+outof).setTextAlignment(TextAlignment.CENTER));
					doc.add(new Paragraph("RESULTS").setBold().setTextAlignment(TextAlignment.CENTER));
					PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_OBLIQUE);
					
					Table t = new Table(3);
					t.setWidthPercent(70);
					t.setHorizontalAlignment(HorizontalAlignment.CENTER);
					t.setFont(font);
					Cell cell = new Cell().add("User-ID").setTextAlignment(TextAlignment.CENTER).setFont(font);
					t.addCell(cell);
					cell = new Cell().add("User-Name").setTextAlignment(TextAlignment.CENTER).setFont(font);
					t.addCell(cell);
					cell = new Cell().add("Marks").setTextAlignment(TextAlignment.CENTER).setFont(font);
					t.addCell(cell);
					
					PdfFont font1 = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);			
					t.setFont(font1);
					for(int i=0;i<result.size();i++){
						Result r = result.get(i);						
						cell=new Cell().add(String.valueOf(r.getId())).setTextAlignment(TextAlignment.CENTER);
						t.addCell(cell);
						cell=new Cell().add(r.getName()).setTextAlignment(TextAlignment.CENTER);
						t.addCell(cell);
						cell=new Cell().add(String.valueOf(r.getMarks())).setTextAlignment(TextAlignment.CENTER);
						t.addCell(cell);
					}
					doc.add(t);
					doc.close();
					JOptionPane.showMessageDialog(f, "Result Saved to File on Desktop");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}});
		accesspanel=new JPanel();
		accesspanel.setLayout(new BoxLayout(accesspanel,BoxLayout.X_AXIS));
		accesspanel.add(save);
		accesspanel.setBorder(eb);
		accesspanel.setVisible(false);
		
		
		
		frame.add(testpanel);
		frame.add(submitpanel);
		frame.add(markspanel);
		
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));		
		frame.pack();	
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		manager(new JFrame());
	}

}
