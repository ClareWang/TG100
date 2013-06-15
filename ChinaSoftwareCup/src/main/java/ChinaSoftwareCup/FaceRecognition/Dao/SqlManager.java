package ChinaSoftwareCup.FaceRecognition.Dao;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;

import javax.persistence.criteria.Path;
import javax.sound.midi.Patch;

import ChinaSoftwareCup.FaceRecognition.log.Log;

public class SqlManager {
	private final int PSTM_TYPE = 0;
	private final int CALL_TYPE = 1;
	
	private static SqlManager manager = null; //support singleton pattern
	private PropertyResourceBundle bundle; //config property file
	private static String jdbcDrive = null; //JDBC driver type
	private String DBhost = ""; //db address
	private String DBname = ""; //db name
	private String DBprot = ""; //db port
	private String DBuser = ""; //db username
	private String DBpasswd = ""; //db password
	private String strcon = null; //connnecting string
	
	private Connection conn = null; //connecting object    
	private PreparedStatement pstm = null;
	private CallableStatement cstm = null;
	
	/**
	 * private constructor, cannot be instantiated
	 */
	private SqlManager(){
		try {
			String filePath = System.getProperty("user.dir") + "\\Config.properties";
			Log.info(filePath.toString());
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			bundle = new PropertyResourceBundle(in);
			this.DBhost = bundle.getString("DBhost");
			this.DBname = bundle.getString("DBname");
			this.DBprot = bundle.getString("DBport");
			this.DBuser = bundle.getString("DBuser");
			this.DBpasswd = bundle.getString("DBpassword");
			String database_type = bundle.getString("database-type");
			if(database_type != null){
				if (database_type.toLowerCase().equals("oracle"))
				{ // 设置oracle数据库的驱动程序和连接字符 
					jdbcDrive = "oracle.jdbc.driver.OracleDriver";
					strcon = "jdbc:oracle:thin:@" + DBhost + ":" + DBprot + ":" + DBname;
				}else if(database_type.toLowerCase().equals("mysql")){
					//set driver and connecting string to mysql database
					jdbcDrive = "com.mysql.jdbc.Driver";
					strcon = "jdbc:mysql://" + DBhost + ":" + DBprot + "/" + DBname;
				}else if(database_type.toLowerCase().equals("sqlserver2005")){
					//set driver and connecting string to sqlserver2005 database
					jdbcDrive = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
					strcon = "jdbc:sqlserver://" + DBhost + ":" + DBprot + ";DatabaseName=" + DBname;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("SqlManager Error!" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Singleton pattern to get instance
	 * @return SqlManager object
	 */
	public static SqlManager createInstance(){
		if(null == manager){
			manager = new SqlManager();
			manager.initDB();
		}
		return manager;
	}
	
	
	/**
	 *  Initialize connect parameters
	 */
	public void initDB(){
		try {
			Class.forName(jdbcDrive);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("initDB Error, can not load jdbc driver!" + e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("load jdbc driver class successfully!");
	}
	
	/**
	 *  connect to database
	 */
	public void connectDB(){
		try {
			conn = DriverManager.getConnection(strcon, DBuser, DBpasswd); //get connection
			conn.setAutoCommit(false);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("connectDB Error!" + e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("connect to database successfully");
	}
	
	
	/**
	 *  disconnect database
	 */
	public void closeDB(){
		try {
			if(pstm != null){
				pstm.close();
			}
			if(cstm != null){
				cstm.close();
			}
			if(conn != null){
				conn.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("closeDB Error!" + e.getMessage());
			e.printStackTrace();
		} finally{
			pstm = null;
			cstm = null;
			conn = null;
		}
		System.out.println("close database successfully");
	}
	
	
	
	/**
	 * set parameters in sql statement of PrepareStatement object
	 * @param sql , sql statement 
	 * @param params,  list of parameters
	 */
	private void setPrepareStatementParams(String sql, Object[] params){
		try {
			pstm = conn.prepareStatement(sql);
			if(params != null){
				for (int i = 0; i < params.length; i++) {
					pstm.setObject(i+1, params[i]);
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("setPrepareStatementParams Error!"+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * set parameters in sql statement of CallableStatementParams object
	 * @param sql , sql statement 
	 * @param params,  list of parameters
	 */
	private void setCallableStatementParams(String sql, Object[] params){
		try {
			cstm = conn.prepareCall(sql);
			if(params != null){
				for (int i = 0; i < params.length; i++)
				{
					cstm.setObject(i + 1, params[i]);
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("setCallableStatementParams Error!"+ e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * execute query
	 * @param sql , sql statement
	 * @param params , list of parameters
	 * @param type , type of sql statement
	 * @return result, type is ResultSet
	 */
	public ResultSet executeQuery(String sql, Object[] params, int type){
		ResultSet rs = null;
		try {
			switch (type) {
			case PSTM_TYPE:
				manager.setPrepareStatementParams(sql, params); //fill parameters
				rs = pstm.executeQuery(); // execute query
				break;
			case CALL_TYPE:
				manager.setCallableStatementParams(sql, params);
				rs = cstm.executeQuery();
				break;
			default:
				throw new Exception("this type does not exist");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("executeQuery Error!" + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	/**
	 * update database
	 * @param sql , sql statement
	 * @param params , list of parameters
	 * @param type , type of sql statement
	 * @return result of query
	 */
	public boolean executeUpdate(String sql, Object[] params, int type){
		boolean rs = false;
		try {
			switch (type) {
			case PSTM_TYPE:
				manager.setPrepareStatementParams(sql, params); //fill parameters
				pstm.executeUpdate();
				manager.commitChange();
				rs = true;
				break;
			case CALL_TYPE:
				manager.setCallableStatementParams(sql, params);
				cstm.executeUpdate();
				manager.commitChange();
				rs = true;
				break;
			default:
				throw new Exception("this type does not exist");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("executeUpdate Error!" + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	
	/**
	 * commit data to database
	 * @throws SQLException
	 */
	public void commitChange() throws SQLException{
		try
		{
			conn.commit();
			System.out.println("commit data successfully��");
		}
		catch (Exception e)
		{
			System.out.println("CommitChange Error!" + e.getMessage());
			e.printStackTrace();
		}
	}
}
