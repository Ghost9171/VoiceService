package com.cqu.edu.Info;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cqu.edu.DB.DBContent;
import com.cqu.edu.base.AnalysisResult;

public class GetContent {
	/**
	 * 得到内容
	 * @param result 
	 * 
	 * @throws Exception
	 */
	public static String getContent(JSONObject typeMap, AnalysisResult analysisResult) {

		int statu=analysisResult.getStatu();
		// 关键词
		String keyword = analysisResult.getKeyword();
		// 类型
		String type=analysisResult.getType();
		String content = "";
		
		if(statu==1){//（无法得到任何信息，需给出提示语句）
			content="";
			return content;
		}else if(statu==2){//（无关键字但有类型
			content=DBContent.getContent(type, keyword);
			return content;
		}else if(statu==3){
			content=DBContent.getContent(type, keyword);
			return content;
		}else if(statu==4){
			return content;
		}else if(statu==5){
			content=DBContent.getContent(type, keyword);
			return content;
		}else if(statu==6){
			content=DBContent.getContent(type, keyword);
			return content;
		}else if(statu==7){
			return content;
		}else if(statu==8){
			return content;
		}
		return content;
	}

	public static StringBuffer getContent(String type, AnalysisResult result) {
		// TODO Auto-generated method stub
		return null;
	}
}
