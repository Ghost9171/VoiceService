package com.cqu.edu.service;


import java.util.ArrayList;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.lionsoul.jcseg.core.JcsegTaskConfig;

import com.cqu.edu.Info.GetContent;
import com.cqu.edu.Info.SendResult;
import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.CommonConstant;
import com.cqu.edu.context.ContextGenerater;
import com.cqu.edu.fenci.Segmentation;
import com.cqu.edu.fenci.Analysis;

public class SegmentService {

	public static JcsegTaskConfig config = new JcsegTaskConfig();
	
	/**
	 * ��servlet���������г����ִʣ�
	 * �Էִʽ�����з�����
	 * ����Json����
	 * @param talk ��Ҫ���зִʵ��������
	 * @param last_talk 
	 * @return ƴװJSON��
	 * @throws Exception 
	 */
	 public String Segment(String talk, String last_talk) throws Exception{
		 String[] path=new String[1] ;
			String path1=null;
			path1=this.getClass().getResource(CommonConstant.JCSEG_PROPERTIES_COMPLETE).getPath();
			path[0]=path1.substring(1, path1.length()-1);
			config.setLexiconPath(path);
			config.setMaxLength(10);
			path1=null;
			path=null;
		 //��ʼ��ʱ
		 long startTime=System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
		 //�����ִ�
		//��ʼ��ʱ
		 long startTime1=System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
		 Segmentation seg=new Segmentation();
		 
		 ArrayList<String> resultList=seg.fullsegment(talk,config);
		 
		 long endTime1=System.currentTimeMillis();
			System.out.println("�ִʺ�ʱΪ��"+(endTime1-startTime1)+"����");
		Analysis analysis=new Analysis();
		
		//�õ�����	
		ArrayList<String> verbList=analysis.getVerb(resultList);
		 
		//�õ���������
		String control=analysis.getControl(resultList);
		
		//һ�仰�������������Ǵ���������ų�
		
		//��ʱ����һ�仰ֻ����һ������
		JSONObject TypeMap=analysis.getType(resultList);
		//��һ�仰�ķ���
		AnalysisResult last_result=null;
		if(last_talk!=null&&!last_talk.equals("")){
		ObjectMapper objectMapper = new ObjectMapper();
		last_result =objectMapper.readValue(last_talk, AnalysisResult.class);
		}
		//��ǰ����
		AnalysisResult result = analysis.startAnalysis(TypeMap,control,last_result);
		
		String return_str = ContextGenerater.getDialog(verbList,TypeMap,result); 
		System.out.println(return_str);
		
		String content_str = GetContent.getContent(TypeMap,result); 
		
		String str=SendResult.send(result,resultList,return_str,content_str);
		
		//������ʱ
		long endTime=System.currentTimeMillis();
		System.out.println("�ܺ�ʱΪ��"+(endTime-startTime)+"����");

		return str;
		/*�����Լ���
		   * CosineSimilarAlgorithm compare =new CosineSimilarAlgorithm(); 
	        double similarity1 = compare.getSimilarity("��ѧ����", "��ѩ����");
	        double similarity2 = compare.getSimilarity("���Ѽ", "��СѼ");
	        System.out.println("similarity1"+similarity1);
	        System.out.println("similarity2"+similarity2);*/
		// String[] story =new String[3];
		
	 }
}
