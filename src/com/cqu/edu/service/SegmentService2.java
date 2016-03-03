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
import com.cqu.edu.fenci.Analysis2;
import com.cqu.edu.fenci.Segmentation;
import com.cqu.edu.fenci.Analysis;

public interface SegmentService2 {

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
	 public StringBuffer Segment(String type,String talk, String last_talk); 
}
