package com.cqu.edu.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MaxListen {
	
	public static String name="";
	
	public static String getMax_Story() throws SQLException{
		
		String sql = "select name from story_click";
		Connection con = JdbcUtil.getDataSource().getConnection();
		ResultSet rs = con.createStatement().executeQuery(sql);
		 while (rs.next()) {  
			 name=rs.getString("name");
         } 
		 con.close();
		return name;
		
	}

	public static String getMax_Music() throws SQLException {
		String sql = "select name from music_click";
		Connection con = JdbcUtil.getDataSource().getConnection();
		ResultSet rs = con.createStatement().executeQuery(sql);
		 while (rs.next()) {  
			 name=rs.getString("name");
         } 
		 con.close();
		return name;
	}
}
