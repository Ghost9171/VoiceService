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
		//ƴװ
				JSONArray typeArray = new JSONArray();
				JSONArray verbArray = new JSONArray();
				JSONObject jsonObject = new JSONObject();
				//����
				//jsonObject.put(CommonConstant.TYPES, typeList.get(1));
				//�ؼ���
				jsonObject.put("�ؼ���", result.getKeyword());
				//�ִʽ��
				for (String type:resultList) {
					if (!type.equals(""))
						typeArray.add(type);
				}
				jsonObject.put(CommonConstant.RESULT, typeArray);
				//���ƴ�
				jsonObject.put(CommonConstant.CONTROL, result.getControl());
				//�������
				ObjectMapper objectMapper = new ObjectMapper();
				//JsonGenerator jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(System.out, JsonEncoding.UTF8);
				String res = objectMapper.writeValueAsString(result); 
				objectMapper.readValue(res, AnalysisResult.class);
				jsonObject.put("����", res);
				
				jsonObject.put("���", return_str);
				if(content_str.equals("")){
					jsonObject.put("����", return_str);
				}else{
					jsonObject.put("����", content_str);
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
