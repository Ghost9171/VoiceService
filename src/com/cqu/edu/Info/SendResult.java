package com.cqu.edu.Info;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.CommonConstant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SendResult {
	
	public static String send(AnalysisResult result, ArrayList<String> resultList, String return_str, String content_str) throws IOException{
		//拼装
				JSONArray typeArray = new JSONArray();
				JSONArray verbArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				//类型
				//jsonObject.put(CommonConstant.TYPES, typeList.get(1));
				//关键词
				jsonObject.put("关键字", result.getKeyword());
				//分词结果
				for (String type:resultList) {
					if (!type.equals(""))
						typeArray.add(type);
				}
				jsonObject.put(CommonConstant.RESULT, typeArray);
				//控制词
				jsonObject.put(CommonConstant.CONTROL, result.getControl());
				//分析结果
				ObjectMapper objectMapper = new ObjectMapper();
				//JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
				String res = objectMapper.writeValueAsString(result); 
				objectMapper.readValue(res, AnalysisResult.class);
				jsonObject.put("分析", res);
				
				jsonObject.put("结果", return_str);
				if(content_str.equals("")){
					jsonObject.put("内容", return_str);
				}else{
					jsonObject.put("内容", content_str);
				}
				
			return jsonObject.toString();
	}

	public static StringBuffer send(AnalysisResult result,
			ArrayList<String> resultList, String return_str,
			StringBuffer content_str) {
		// TODO Auto-generated method stub
		return null;
	}
}
