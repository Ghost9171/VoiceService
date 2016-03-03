package com.cqu.edu.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.CommonConstant;

public class DBContent {
	/**
	 * 得到内容
	 * @param result 
	 * 
	 * @throws Exception
	 */
	public static String getContent(String type,String keyword) {
		String content="";
		String table="";
		if(type.equals(CommonConstant.STORY)){
			table="story";
		}if(type.equals(CommonConstant.MUSIC)){
			table="music";
		}if(type.equals(CommonConstant.POETRY)){
			table="poetry";
		}if(type.equals(CommonConstant.KNOWLEDGE)){
			table="knowledge";
		}
		String sql = "select content from "+table+" where name = '"+ keyword + "'";
		try {
			Connection con = JdbcUtil.getDataSource().getConnection();
			ResultSet rs = con.createStatement().executeQuery(sql);
			while (rs.next()) {
				content = rs.getString("content");
			}
			con.close();
		} catch (SQLException e) {
			System.out.println("未在数据库中找到相应记录！");
			content = "未找到内容！";
		}
		return content;
	}
}
