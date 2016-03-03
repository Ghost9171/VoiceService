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
	 * 将servlet传的语句进行初步分词，
	 * 对分词结果进行分析，
	 * 返回Json数据
	 * @param talk 需要进行分词的中文语句
	 * @param last_talk 
	 * @return 拼装JSON串
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
		 //开始计时
		 long startTime=System.currentTimeMillis();// 当前时间对应的毫秒数
		 //完整分词
		//开始计时
		 long startTime1=System.currentTimeMillis();// 当前时间对应的毫秒数
		 Segmentation seg=new Segmentation();
		 
		 ArrayList<String> resultList=seg.fullsegment(talk,config);
		 
		 long endTime1=System.currentTimeMillis();
			System.out.println("分词耗时为："+(endTime1-startTime1)+"毫秒");
		Analysis analysis=new Analysis();
		
		//得到动词	
		ArrayList<String> verbList=analysis.getVerb(resultList);
		 
		//得到控制类型
		String control=analysis.getControl(resultList);
		
		//一句话包含多种类型是错误情况，排除
		
		//暂时考虑一句话只包含一种类型
		JSONObject TypeMap=analysis.getType(resultList);
		//上一句话的分析
		AnalysisResult last_result=null;
		if(last_talk!=null&&!last_talk.equals("")){
		ObjectMapper objectMapper = new ObjectMapper();
		last_result =objectMapper.readValue(last_talk, AnalysisResult.class);
		}
		//当前分析
		AnalysisResult result = analysis.startAnalysis(TypeMap,control,last_result);
		
		String return_str = ContextGenerater.getDialog(verbList,TypeMap,result); 
		System.out.println(return_str);
		
		String content_str = GetContent.getContent(TypeMap,result); 
		
		String str=SendResult.send(result,resultList,return_str,content_str);
		
		//结束计时
		long endTime=System.currentTimeMillis();
		System.out.println("总耗时为："+(endTime-startTime)+"毫秒");

		return str;
		/*相似性计算
		   * CosineSimilarAlgorithm compare =new CosineSimilarAlgorithm(); 
	        double similarity1 = compare.getSimilarity("白学公主", "白雪公主");
	        double similarity2 = compare.getSimilarity("丑大鸭", "丑小鸭");
	        System.out.println("similarity1"+similarity1);
	        System.out.println("similarity2"+similarity2);*/
		// String[] story =new String[3];
		
	 }
}
