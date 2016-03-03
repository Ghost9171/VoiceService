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
	 * ��servlet���������г����ִʣ�
	 * �Էִʽ�����з�����
	 * ����Json����
	 * @param talk ��Ҫ���зִʵ��������
	 * @param last_talk 
	 * @return ƴװJSON��
	 * @throws Exception 
	 */
	 public StringBuffer Segment(String type,String talk, String last_talk); 
}
