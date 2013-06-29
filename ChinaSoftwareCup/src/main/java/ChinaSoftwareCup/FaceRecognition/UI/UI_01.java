

package ChinaSoftwareCup.FaceRecognition.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.*;

public class UI_01 extends javax.swing.JFrame {
	
	private Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Determine user's screen size
	
    private javax.swing.JLabel lbUsername;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbVideoPath;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnGetVideoPath;
    private javax.swing.JTextField tbUserID;
    private javax.swing.JPasswordField tbPassword;
    private javax.swing.JTextField tbVideoPath;
    
//    public static player player = null;
//    private capturedeviceinfo di = null;
//    private medialocator ml = null;
    
    /** Creates new form NewAccount */
    public UI_01() {
        initComponents();
    }
    
    private void initComponents()
    	{
    	setResizable (false);
    	setLocation (d.width / 2 - getWidth() / 2, d.height / 2 - getHeight() / 2);
    	lbUsername = new javax.swing.JLabel();
    	lbPassword = new javax.swing.JLabel();
    	lbVideoPath = new javax.swing.JLabel();
    	tbUserID = new javax.swing.JTextField();
    	tbPassword = new javax.swing.JPasswordField();
    	tbVideoPath = new javax.swing.JTextField();
        btnOk = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();      
        btnGetVideoPath = new javax.swing.JButton();

        getContentPane().setLayout(null);
        
        setTitle("人脸识别登录系统");
 
        //Username label
        lbUsername.setForeground (Color.black);
        lbUsername.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbUsername.setFont(new java.awt.Font("Tahoma", 1, 11));
        lbUsername.setText("Username :");
        getContentPane().add(lbUsername);
        lbUsername.setBounds(10, 375, 75, 25);
        
        //Password label
        lbPassword.setForeground (Color.black);
        lbPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbPassword.setFont(new java.awt.Font("Tahoma", 1, 11));
        lbPassword.setText("Password :");
        getContentPane().add(lbPassword);
        lbPassword.setBounds(10, 410, 75, 25);
        
        lbVideoPath.setForeground (Color.black);
        lbVideoPath.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbVideoPath.setFont(new java.awt.Font("Tahoma", 1, 11));
        lbVideoPath.setText("VideoPath :");
        getContentPane().add(lbVideoPath);
        lbVideoPath.setBounds(10, 340, 75, 25);

        getContentPane().add(tbUserID);
        getContentPane().add(tbPassword);
        getContentPane().add(tbVideoPath);
        getContentPane().setBackground(Color.white);
        tbUserID.setBounds(80, 375, 150, 25);        
        tbPassword.setBounds(80, 410, 150, 25);
        tbVideoPath.setBounds(80, 340, 260, 25);
        
        getContentPane().add(btnOk);
        getContentPane().add(btnRegister);
        getContentPane().add(btnGetVideoPath);
        
        //Login button
        btnOk.setText("Login");
        btnOk.setFont(new java.awt.Font("Tahoma", 1, 9));
        btnOk.setBackground(Color.white);
        btnOk.setBounds (236, 442, 90, 25);
        btnOk.setToolTipText("Click to login");
		btnOk.addActionListener(new ActionListener(){
  			public void actionPerformed(ActionEvent evt)
			{
  				//绑定按钮消息事件
  				Login();
			}
        });
		
		//Register button
		btnRegister.setText("Register");
		btnRegister.setFont(new java.awt.Font("Tahoma", 1, 9));
		btnRegister.setBackground(Color.white);
		btnRegister.setBounds (130, 442, 90, 25);
		btnRegister.setToolTipText("Click to register");
		btnRegister.addActionListener(new ActionListener(){
  			public void actionPerformed(ActionEvent evt)
			{
  				//绑定按钮消息事件
  				Register();
			}
        });
		
		//GetVideoPath button
		btnGetVideoPath.setText("ImportVideo");
		btnGetVideoPath.setFont(new java.awt.Font("Tahoma", 1, 9));
		btnGetVideoPath.setBackground(Color.white);
		btnGetVideoPath.setBounds (10, 442, 110, 25);
		btnGetVideoPath.setToolTipText("Click to get video path");
		btnGetVideoPath.addActionListener(new ActionListener(){
  			public void actionPerformed(ActionEvent evt)
			{
  				//绑定按钮消息事件
  				GetVideoPath();
			}
        });
		
        pack();
		setSize (370, 500);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		}
    
   
    /*public static void main(String args[]) {
    	UI_01 ui=new UI_01();
    }
    */

    void Login(){

    	//登录失败 消息框
    	//JOptionPane.showMessageDialog((Component) null, "登陆失败 ", "Login Error", JOptionPane.INFORMATION_MESSAGE);
    	//登录成功消息框
    	//JOptionPane.showMessageDialog((Component) null, "登陆成功", "Login Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    void Register(){
    
    }
    
    void GetVideoPath(){
    	FileDialog fd=new FileDialog(new Frame(),"测试",FileDialog.LOAD);
   	 	FilenameFilter ff=new FilenameFilter(){
   	 		public boolean accept(File dir, String name) {
   	 			if (name.endsWith("jpg")){
   	 				return true;
   	 				}
   	 			return false;
   	 			}
   	 		};
   	 		fd.setFilenameFilter(ff);
   	 		fd.setVisible(true);
   	 		System.out.println(fd.getDirectory()+"\\"+fd.getFile()); 	
   	 		tbVideoPath.setText(fd.getDirectory()+"\\"+fd.getFile());
    }
    

	void VerifyLogin(){
		Connection con=null;
		String url="jdbc:odbc:DobiTest";
		Statement st=null;
		
		try
		{
			String val1=tbUserID.getText();
			val1 = val1.trim();
			String val2 =  (String)tbPassword.toString();
			val2 = val2.trim();					
					
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
					
			con=DriverManager.getConnection(url);
					
			st = con.createStatement();
					
			ResultSet rs=st.executeQuery("Select password from staff where username='"+val1+"'");
				
			while(rs.next())
			{
				String user = rs.getString(1);
				boolean b=user.equals(val2);				
				
				if(b)
				{
					setVisible(false);
					JOptionPane.showMessageDialog((Component) null, "Wow! U got it here too! Hihi =B", "Mmuahaha", JOptionPane.PLAIN_MESSAGE);
				}
				 else
				{
					JOptionPane.showMessageDialog((Component) null, "Invalid password. Please try again. ", "Login Error", JOptionPane.INFORMATION_MESSAGE);
					tbPassword.setText("");
					tbPassword.requestFocus();
				}
				}
			  }
			  catch(SQLException ex)
			   {
			    System.out.println("Unable to access the database");
			   }
			  catch(ClassNotFoundException ex)
			   {
			    System.out.println("Class not found");
			   }
			  catch(Exception ex)
			  {
               System.out.println("Exception raised is:"+ex);
			  }
			  finally {
			  con=null;
			  }
		}	
}