package com.cqu.edu.context;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.CommonConstant;
import com.cqu.edu.fenci.Analysis;

public class ContextGenerater {
	/**
	 * ���ɻỰ
	 * @param result2 
	 * 
	 * @throws Exception
	 */
	public static String getDialog(ArrayList<String> resultList,JSONObject typeMap, AnalysisResult analysisResult)
			throws Exception {
		String result = "";
		int statu=analysisResult.getStatu();
		// �ؼ���
		String keyword = analysisResult.getKeyword();
		// ����
		String type=analysisResult.getType();
		//����
		String control =analysisResult.getControl();
		
		if(statu==1){//���޷��õ��κ���Ϣ���������ʾ��䣩
			result="û������޷�ʶ��,���ظ�һ��!";
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
			result="�ؼ���ֻ����һ��";
			return result;
		}else if(statu==8){
			result="û��"+keyword+"��"+type;
			return result;
		}else if(statu==9){
			result="û����,��Ҫ��ʲô"+type+"?";
			return result;
		}
		
		
		/*
		if(typeMap.size() == 2)// ֻ��һ���ؼ���(�����¡����֡��衢ʫ�衢ʫ��){[aaa,bbb]}
		{
			keywordList=typeMap.getJSONArray("keyword");
			if(keywordList.size()==1){//{[aaa]}
				if(keywordList.contains("����")||keywordList.contains("����")||keywordList.contains("��")||keywordList.contains("ʫ")){
					//����ȷ������
					type=keywordList.getString(0);
					result=checkControl(type,control);
					return result;
				}else{
					keyword=keywordList.getString(0);
					//TODO
					control="ָ������";
					typeList=typeMap.getJSONArray(keyword);
					result=directDisplay(typeList,keyword);
				}
			}else {//{[aaa,bbb]}
				keyword=keywordList.getString(0);
				if(keyword.equals("����")||keyword.equals("����")||keyword.equals("��")||keyword.equals("ʫ")||keyword.equals("ʫ��")){
					type=keywordList.getString(0);
					result=checkControl(type,control);
					return result;
				}
				//TODO
				control="ָ������";
				type=keywordList.getString(1);
				result=directDisplay(type,keyword);
			}
		}else if(typeMap.size() == 0){//δ�ҵ��ؼ���
			result="���ظ�һ�飡";
			return result;
		}else if(typeMap.size() >2){
			result="�ؼ���ֻ����һ��";
			return result;
		}*/
		/*if (typeMap.size() == 1) {// ֻ��һ���ؼ���
			if (typeList.size() == 1) {
				// ָ������
				if (!keyword.equals("")) {
					result = "������" + keyword + "��";
				}
				// ��������
				if (control.equals(CommonConstant.RANDOM)) {
					Analysis typeClassify = new Analysis();
					keyword = typeClassify.getRandom(typeList.get(1));
					result = "���������" + keyword;
					// typeList.set(0, keyword);
				} else if (control.equals(CommonConstant.BEST)) {
					Analysis typeClassify = new Analysis();
					keyword = typeClassify.getBest(typeList.get(1));
					result = "������" + keyword;
					// typeList.set(0, keyword);
				}
			} 
		}*/
		return result;
	}

	private static String directDisplay(String type, String keyword) {
		String result = "";
		if(result.equals(""))
			result = "������" + keyword + "��";
			return result;
	}

	private static String directDisplay(JSONArray typeList, String keyword) {
		// ����
		String type="";
		String result = "";
		if(typeList.size()==1)//ֻ��һ������
		{
			type=typeList.getString(0);
			if(result.equals(""))
			result = "������" + keyword + "��";
			return result;
		}
		else{//��������
			for(int i=0;i<typeList.size();i++){
				type=(String) typeList.get(i);
				if(i==0)
					result=type;
				else if(i==typeList.size()-1)
					{
						result="��Ҫ��"+keyword+"��"+result+"����"+type+"��";
						return result;
					}
				else 
					result=result+"��"+type;
			}
		}
		return result;
	}

	private static String checkControl(String type, String control) throws Exception {
		if (control.equals(CommonConstant.RANDOM)) {
			Analysis typeClassify = new Analysis();
			String keyword = typeClassify.getRandom(type);
			//String result = "���������" + keyword;
			return keyword;
		} else if (control.equals(CommonConstant.BEST)) {
			Analysis typeClassify = new Analysis();
			String keyword = typeClassify.getBest(type);
			//String result = "������" + keyword;
			return keyword;
		}
		return "";
	}

	//�µķ�ʽ
	public static String getDialog(String type, AnalysisResult result) {
		// TODO Auto-generated method stub
		return null;
	}
}
