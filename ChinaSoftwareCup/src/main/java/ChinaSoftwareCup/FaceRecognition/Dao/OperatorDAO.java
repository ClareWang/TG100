package ChinaSoftwareCup.FaceRecognition.Dao;

import java.sql.ResultSet;

import ChinaSoftwareCup.FaceRecognition.Bean.Operator;

public class OperatorDAO {
	SqlManager manage=null;
	
	public OperatorDAO(){
		manage=SqlManager.createInstance();
		manage.connectDB();
	}
	
	/**
	 * check user name and password
	 * @param username 
	 * @param password 
	 * @return result of query
	 */
	public boolean loginCheck(String username, String password){
		boolean result=false;
		try {
			String sql="select * from db_users where username=? and password=?";
			Object[] params=new Object[]{username,password};
			ResultSet rs=manage.executeQuery(sql, params, Constants.PSTM_TYPE);
			while(rs.next()){
				result=true;
			}
			manage.closeDB();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("loginCheck error!"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * add new operator to database
	 * @param operator , Operator object
	 * @return result
	 */
	public boolean addOperator(Operator operator){
		boolean result=false;
		try {
			String sql="insert into db_users(username,password) values (?,?)";
			Object[] params=new Object[]{operator.getUserName(),operator.getPassword()};
			result=manage.executeUpdate(sql, params, Constants.PSTM_TYPE);
			manage.closeDB();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("addOperator error!"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * delete opreator
	 * @param username , name of the delete opreator 
	 * @return result of delete
	 */
	public boolean deleteOperator(String username){
		boolean result=false;
		try
		{
			String sql="delete from db_users where username=?";
			Object[] params= new Object[]{username};
			result=manage.executeUpdate(sql, params, Constants.PSTM_TYPE);
			manage.closeDB();
		}
		catch (Exception e)
		{
			System.out.println("deleteOperator error!"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * modify password
	 * @param username
	 * @param password
	 * @return result of modify
	 */
	public boolean modifyPassword(String username,String password)
	{
		boolean result=false;
		try
		{
			String sql="update db_users set password=? where username=?";
			Object[] params=new Object[]{password,username};
			result=manage.executeUpdate(sql, params, Constants.PSTM_TYPE);
			manage.closeDB();
		}
		catch (Exception e)
		{
			System.out.println("deleteOperator error!"+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
     * get user information
     * @param userName
     * @return  result of query
     */
    public  Operator getOperatorInfo(String userName)
    {
    	Operator result=new Operator();
    	try
		{
			String sql="select * from db_users where username=?";
			Object[] params=new Object[]{userName};
			ResultSet rs=manage.executeQuery(sql, params, Constants.PSTM_TYPE);
			if(rs.next())
			{
				result.setUserName(userName);
				result.setPassword(rs.getString("password"));
			}
		}
		catch (Exception e)
		{
			System.out.println("getOperatorInfo error!"+e.getMessage());
			e.printStackTrace();
		}
		return result;
    }
}
