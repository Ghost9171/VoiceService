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
	 * �����ִ�
	 * 
	 * @param str
	 *            ���ִ����
	 * @param config 
	 * @return �ִʽ��List
	 * @throws Exception
	 */
	public ArrayList<String> fullsegment(String str, JcsegTaskConfig config) throws Exception {
		
		dic = DictionaryFactory.createDefaultDictionary(config, true);
		// �����ִ�
		seg = SegmentFactory.createJcseg(JcsegTaskConfig.COMPLEX_MODE,new Object[] { config, dic });

		// ��ʼ�ִ�
		seg.reset(new StringReader(str));

		// ��ȡ�ִʽ��
		IWord word = null;
		ArrayList<String> res_list = new ArrayList<String>();
		System.out.println("�ִʽ��:");
		while ((word = seg.next()) != null) {
			System.out.print(word.getValue() + "	");
			res_list.add(word.getValue());
		}
		word = null;
		System.out.println();
		// ������ʱ

		return res_list;
	}

}
