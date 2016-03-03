package com.cqu.edu.context;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.CommonConstant;
import com.cqu.edu.fenci.Analysis;

public class ContextGenerater {
	/**
	 * 生成会话
	 * @param result2 
	 * 
	 * @throws Exception
	 */
	public static String getDialog(ArrayList<String> resultList,JSONObject typeMap, AnalysisResult analysisResult)
			throws Exception {
		String result = "";
		int statu=analysisResult.getStatu();
		// 关键词
		String keyword = analysisResult.getKeyword();
		// 类型
		String type=analysisResult.getType();
		//控制
		String control =analysisResult.getControl();
		
		if(statu==1){//（无法得到任何信息，需给出提示语句）
			result="没听清或无法识别,请重复一遍!";
			return result;
		}else if(statu==2){
			keyword=checkControl(type, control);
			result=directDisplay(type, keyword);
			analysisResult.setKeyword(keyword);
			return result;
		}else if(statu==3){
			result=directDisplay(type, keyword);
			return result;
		}else if(statu==4){
			JSONArray typeList = null;
			typeList=typeMap.getJSONArray(keyword);
			result=directDisplay(typeList, keyword);
			return result;
		}else if(statu==5){
			result=directDisplay(type, keyword);
			return result;
		}else if(statu==6){
			result=directDisplay(type, keyword);
			return result;
		}else if(statu==7){
			result="关键字只能有一个";
			return result;
		}else if(statu==8){
			result="没有"+keyword+"的"+type;
			return result;
		}else if(statu==9){
			result="没听清,你要听什么"+type+"?";
			return result;
		}
		
		
		/*
		if(typeMap.size() == 2)// 只有一个关键词(除故事、音乐、歌、诗歌、诗外){[aaa,bbb]}
		{
			keywordList=typeMap.getJSONArray("keyword");
			if(keywordList.size()==1){//{[aaa]}
				if(keywordList.contains("故事")||keywordList.contains("音乐")||keywordList.contains("歌")||keywordList.contains("诗")){
					//可以确定类型
					type=keywordList.getString(0);
					result=checkControl(type,control);
					return result;
				}else{
					keyword=keywordList.getString(0);
					//TODO
					control="指定播放";
					typeList=typeMap.getJSONArray(keyword);
					result=directDisplay(typeList,keyword);
				}
			}else {//{[aaa,bbb]}
				keyword=keywordList.getString(0);
				if(keyword.equals("故事")||keyword.equals("音乐")||keyword.equals("歌")||keyword.equals("诗")||keyword.equals("诗歌")){
					type=keywordList.getString(0);
					result=checkControl(type,control);
					return result;
				}
				//TODO
				control="指定播放";
				type=keywordList.getString(1);
				result=directDisplay(type,keyword);
			}
		}else if(typeMap.size() == 0){//未找到关键字
			result="请重复一遍！";
			return result;
		}else if(typeMap.size() >2){
			result="关键字只能有一个";
			return result;
		}*/
		/*if (typeMap.size() == 1) {// 只有一个关键词
			if (typeList.size() == 1) {
				// 指定播放
				if (!keyword.equals("")) {
					result = "将播放" + keyword + "。";
				}
				// 其他播放
				if (control.equals(CommonConstant.RANDOM)) {
					Analysis typeClassify = new Analysis();
					keyword = typeClassify.getRandom(typeList.get(1));
					result = "将随机播放" + keyword;
					// typeList.set(0, keyword);
				} else if (control.equals(CommonConstant.BEST)) {
					Analysis typeClassify = new Analysis();
					keyword = typeClassify.getBest(typeList.get(1));
					result = "将播放" + keyword;
					// typeList.set(0, keyword);
				}
			} 
		}*/
		return result;
	}

	private static String directDisplay(String type, String keyword) {
		String result = "";
		if(result.equals(""))
			result = "将播放" + keyword + "。";
			return result;
	}

	private static String directDisplay(JSONArray typeList, String keyword) {
		// 类型
		String type="";
		String result = "";
		if(typeList.size()==1)//只有一种类型
		{
			type=typeList.getString(0);
			if(result.equals(""))
			result = "将播放" + keyword + "。";
			return result;
		}
		else{//多种类型
			for(int i=0;i<typeList.size();i++){
				type=(String) typeList.get(i);
				if(i==0)
					result=type;
				else if(i==typeList.size()-1)
					{
						result="你要听"+keyword+"的"+result+"还是"+type+"。";
						return result;
					}
				else 
					result=result+"、"+type;
			}
		}
		return result;
	}

	private static String checkControl(String type, String control) throws Exception {
		if (control.equals(CommonConstant.RANDOM)) {
			Analysis typeClassify = new Analysis();
			String keyword = typeClassify.getRandom(type);
			//String result = "将随机播放" + keyword;
			return keyword;
		} else if (control.equals(CommonConstant.BEST)) {
			Analysis typeClassify = new Analysis();
			String keyword = typeClassify.getBest(type);
			//String result = "将播放" + keyword;
			return keyword;
		}
		return "";
	}

	//新的方式
	public static String getDialog(String type, AnalysisResult result) {
		// TODO Auto-generated method stub
		return null;
	}
}
