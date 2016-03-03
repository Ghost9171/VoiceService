package com.cqu.edu.fenci;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import org.lionsoul.jcseg.core.ADictionary;
import org.lionsoul.jcseg.core.DictionaryFactory;
import org.lionsoul.jcseg.core.ISegment;
import org.lionsoul.jcseg.core.IWord;
import org.lionsoul.jcseg.core.JcsegTaskConfig;
import org.lionsoul.jcseg.core.SegmentFactory;


public class Segmentation {
//new JcsegTaskConfig();
	private static ADictionary dic; //= DictionaryFactory.createDefaultDictionary(config, true);
	private static ISegment seg = null;

	/**
	 * 完整分词
	 * 
	 * @param str
	 *            待分词语句
	 * @param config 
	 * @return 分词结果List
	 * @throws Exception
	 */
	public ArrayList<String> fullsegment(String str, JcsegTaskConfig config) throws Exception {
		
		dic = DictionaryFactory.createDefaultDictionary(config, true);
		// 完整分词
		seg = SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE,new Object[] { config, dic });

		// 开始分词
		seg.reset(new StringReader(str));

		// 获取分词结果
		IWord word = null;
		ArrayList<String> res_list = new ArrayList<String>();
		System.out.println("分词结果:");
		while ((word = seg.next()) != null) {
			System.out.print(word.getValue() + "	");
			res_list.add(word.getValue());
		}
		word = null;
		System.out.println();
		// 结束计时

		return res_list;
	}

}
