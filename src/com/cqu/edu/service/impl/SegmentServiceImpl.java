package com.cqu.edu.service.impl;

import java.util.ArrayList;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
import org.lionsoul.jcseg.core.JcsegTaskConfig;

import com.cqu.edu.Info.GetContent;
import com.cqu.edu.Info.SendResult;
import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.AnalysisResult2;
import com.cqu.edu.base.CommonConstant;
import com.cqu.edu.context.ContextGenerater;
import com.cqu.edu.fenci.Analysis2;
import com.cqu.edu.fenci.Segmentation;
import com.cqu.edu.fenci.Analysis;
import com.cqu.edu.service.SegmentService2;

public class SegmentServiceImpl implements SegmentService2 {

	public static JcsegTaskConfig config = new JcsegTaskConfig();

	/**
	 * 将servlet传的语句进行初步分词， 对分词结果进行分析， 返回Json数据
	 * 
	 * @param talk
	 *            需要进行分词的中文语句
	 * @param last_talk
	 * @return 拼装JSON串
	 * @throws Exception
	 */
	public StringBuffer Segment(String type, String talk, String last_talk) {
		String[] path = new String[1];
		String path1 = null;
		path1 = this.getClass()
				.getResource(CommonConstant.JCSEG_PROPERTIES_COMPLETE)
				.getPath();
		path[0] = path1.substring(1, path1.length() - 1);
		config.setLexiconPath(path);
		config.setMaxLength(10);
		path1 = null;
		path = null;
		
		Segmentation seg = new Segmentation();
		Analysis2 analysis = new Analysis2();
		// 上一句话的分析
		AnalysisResult2 last_result = null;
		try {
			// 完整分词
			ArrayList<String> resultList = seg.fullsegment(talk, config);
			
			// 得到控制类型
			String control = analysis.getControl(resultList);
			// 得到控制类型
			ArrayList<String> keyword = analysis.getKeyword(type,resultList);
			
			//会话未完成
			if (last_talk != null && !last_talk.equals("")) {
				ObjectMapper objectMapper = new ObjectMapper();
				last_result = objectMapper.readValue(last_talk,AnalysisResult2.class);
			}
			// 当前分析
			AnalysisResult result = analysis.startAnalysis(type, control,keyword,last_result);

			String return_str = ContextGenerater.getDialog(type,result);
			System.out.println(return_str);

			StringBuffer content_str = GetContent.getContent(type, result);

			StringBuffer str = SendResult.send(result, resultList, return_str,content_str);

			return str;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * 相似性计算 CosineSimilarAlgorithm compare =new CosineSimilarAlgorithm();
		 * double similarity1 = compare.getSimilarity("白学公主", "白雪公主"); double
		 * similarity2 = compare.getSimilarity("丑大鸭", "丑小鸭");
		 * System.out.println("similarity1"+similarity1);
		 * System.out.println("similarity2"+similarity2);
		 */
		// String[] story =new String[3];
		return null;

	}
}
