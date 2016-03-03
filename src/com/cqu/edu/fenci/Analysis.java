package com.cqu.edu.fenci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.lionsoul.jcseg.core.ADictionary;
import org.lionsoul.jcseg.core.DictionaryFactory;
import org.lionsoul.jcseg.core.ILexicon;
import org.lionsoul.jcseg.core.JcsegTaskConfig;

import com.cqu.edu.DB.MaxListen;
import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.CommonConstant;

public class Analysis {
	/** �����ֵ� */
	private static ADictionary story_dic;
	private static ADictionary song_dic;
	private static ADictionary poetry_dic;
	private static ADictionary konwledge_dic;
	/** �����ֵ� */
	private static ADictionary random_dic;
	private static ADictionary best_dic;

	private static JcsegTaskConfig config_none;
	// ��������
	private int controlType = 0;
	// 0��ʼ(����)��1���£�2���֣�3ʫ�裬4֪ʶ,5���(��δ������)
	private int type = 0;
	private String types = "";
	private String keyword = "";

	/**
	 * ���ݷִʽ�����з���
	 * 
	 * @param typeMap
	 * @param control
	 * @param last_result
	 *            ��һ�仰�ķ������
	 * @return
	 */
	public AnalysisResult startAnalysis(JSONObject typeMap, String control,
			AnalysisResult last_result) {

		AnalysisResult result = new AnalysisResult();
		// �ؼ���
		String keyword = "";
		// ����
		String analysis_type = "";
		JSONArray keywordList = null;
		JSONArray typeList = null;
		// control=control1;

		// Ҫ��λỰ���ܵó��������Ҫ��һ�εķ������
		if (last_result != null && last_result.getStatu() == 4) {// ����ж�λỰ{"statu":4,"keyword":"��ѩ����","type":null,"control":"ָ������"}
			keywordList = typeMap.getJSONArray("keyword");
			if (typeMap.size() == 1) {//��Ȼֻ�ҵ�һ���ؼ���
				if (keywordList.contains("����") || keywordList.contains("����")
						|| keywordList.contains("��") || keywordList.contains("ʫ")) {
					result.setStatu(6);
					result.setKeyword(last_result.getKeyword());
					result.setType(keywordList.getString(0));
					result.setControl(last_result.getControl());
				} else {// û�еõ���Ҫ�Ľ�����ظ�!!!�п���Ҫ�Ż����������޹أ�
					result.setStatu(4);
					result.setKeyword(last_result.getKeyword());
					result.setControl(last_result.getControl());
				}
			} else {
				result = getResultWhenMapSizeis2(keywordList, typeMap, control);
			}
		} else if (last_result != null && last_result.getStatu() == 9) {// ��ֻ�������޹ؼ���Ҳ�޿��ƣ�
		
			if (typeMap.size() == 0) {//û�йؼ��֣�ֻ�п��ƴʻ�û��
				if(control==null||control.equals("")||control.equals("null")){//�޿��ƴ�
					result.setStatu(9);
					result.setType(analysis_type);
					//result.setControl(control);
				}else{//�п��ƴ�
					result.setStatu(2);
					result.setType(last_result.getType());
					result.setControl(control);
				}
			}else if (typeMap.size() == 1) {//��Ȼֻ�ҵ�һ���ؼ���
				keywordList = typeMap.getJSONArray("keyword");
				//������Ҫ�Ĺؼ���
				if (keywordList.contains("����") || keywordList.contains("����")
						|| keywordList.contains("��")
						|| keywordList.contains("ʫ")) {
					// ֻ��'����'��Щһ���ؼ��֣��޹ؼ��ֵ������ͣ�
					analysis_type = keywordList.getString(0);
					//�ж��Ƿ��п��ƴ�
					if(control==null||control.equals("")||control.equals("null")){//�޿��ƴ�
						result.setStatu(9);
						result.setType(analysis_type);
					}else{//�п��ƴ�
						result.setStatu(2);
						result.setType(analysis_type);
						result.setControl(control);
					}
				} else {//����ؼ�������Ҫ��(�ؼ���)
					keyword = keywordList.getString(0);
					control = "ָ������";
					typeList = typeMap.getJSONArray(keyword);
					if (typeList.size() == 1) {// ��һ���ؼ���һ�����ͣ����ùؼ���ֻ��Ӧһ�����ͣ�
						result.setStatu(3);
						result.setKeyword(keyword);
						result.setType(typeList.getString(0));
						result.setControl(last_result.getControl());
					} else {// ��һ���ؼ��ֶ������ͣ�����
						result.setStatu(4);
						result.setKeyword(last_result.getKeyword());
						result.setControl(last_result.getControl());
					}
				}
			} else {
				keywordList = typeMap.getJSONArray("keyword");
				result = getResultWhenMapSizeis2(keywordList, typeMap, control);
			}
		}// ����Ҫ���ж�λỰ
		else if (typeMap.size() == 2)// ֻ��һ���ؼ���(�����¡����֡��衢ʫ�衢ʫ��){[aaa,bbb]}
		{
			keywordList = typeMap.getJSONArray("keyword");
			result = getResultWhenMapSizeis2(keywordList, typeMap, control);
		} else if (typeMap.size() == 0) {// δ�ҵ��ؼ���
			result.setStatu(1);
		} else if (typeMap.size() > 2) {// �������������Ŀ���ԣ�
			result.setStatu(7);
		} else if (typeMap.size() == 1) {
			if (!control.equals("")) {// �����Ʋ��ţ�
				result.setStatu(2);
				keywordList = typeMap.getJSONArray("keyword");
				result.setType(keywordList.getString(0));
				result.setControl(control);
			} else {// ֻ�������޹ؼ���Ҳ�޿��� !!!��Ҫ��һ���Ự
				result.setStatu(9);
				keywordList = typeMap.getJSONArray("keyword");
				result.setType(keywordList.getString(0));
				// result.setControl(control);
			}
		}
		return result;

	}

	private AnalysisResult getResultWhenMapSizeis2(JSONArray keywordList,
			JSONObject typeMap, String control) {
		AnalysisResult result = new AnalysisResult();
		// �ؼ���
		String keyword = "";
		// ����
		String analysis_type = "";
		JSONArray typeList = null;
		if (keywordList.size() == 1) {// {[aaa]}
			if (keywordList.contains("����") || keywordList.contains("����")
					|| keywordList.contains("��") || keywordList.contains("ʫ")) {
				// ֻ��'����'��Щһ���ؼ��֣��޹ؼ��ֵ������ͣ�
				analysis_type = keywordList.getString(0);
				result.setStatu(2);
				result.setType(analysis_type);
				result.setControl(control);
			} else {
				keyword = keywordList.getString(0);
				control = "ָ������";
				typeList = typeMap.getJSONArray(keyword);
				if (typeList.size() == 1) {// ��һ���ؼ���һ�����ͣ����ùؼ���ֻ��Ӧһ�����ͣ�
					result.setStatu(3);
					result.setKeyword(keyword);
					result.setType(typeList.getString(0));
					result.setControl(control);
				} else {// ��һ���ؼ��ֶ������ͣ�����
					result.setStatu(4);
					result.setKeyword(keyword);
					result.setControl(control);
				}
			}
		} else {// {[aaa,bbb]}
			keyword = keywordList.getString(0);
			control = "ָ������";
			if (keyword.equals("����") || keyword.equals("����")
					|| keyword.equals("��") || keyword.equals("ʫ")
					|| keyword.equals("ʫ��")) {
				analysis_type = keywordList.getString(0);
				keyword = keywordList.getString(1);
				typeList = typeMap.getJSONArray(keyword);
				if (typeList.size() == 1) {// �������ؼ���һ�����ͣ�
					result.setStatu(5);
					result.setKeyword(keyword);
					result.setType(analysis_type);
					result.setControl(control);
				} else {// �������ؼ��ֶ������ͣ�
					result.setStatu(6);
					result.setKeyword(keyword);
					result.setType(analysis_type);
					result.setControl(control);
				}
			}
			analysis_type = keywordList.getString(1);
			keyword = keywordList.getString(0);
			typeList = typeMap.getJSONArray(keyword);
			if (checkType(keyword, analysis_type) == false) {
				result.setKeyword(keyword);
				result.setType(analysis_type);
				result.setStatu(8);
			} else {
				if (typeList.size() == 1) {// �������ؼ���һ�����ͣ�
					result.setStatu(5);
					result.setKeyword(keyword);
					result.setType(analysis_type);
					result.setControl(control);
				} else {// �������ؼ��ֶ������ͣ�
					result.setStatu(6);
					result.setKeyword(keyword);
					result.setType(analysis_type);
					result.setControl(control);
				}
			}
		}
		return result;
	}

	private boolean checkType(String keyword, String analysis_type) {

		if (analysis_type.equals(CommonConstant.STORY)) {
			if (story_dic.get(ILexicon.CJK_WORD, keyword) != null)
				return true;
		}
		if (analysis_type.equals(CommonConstant.MUSIC)
				|| analysis_type.equals("��")) {
			if (song_dic.get(ILexicon.CJK_WORD, keyword) != null)
				return true;
		}
		if (analysis_type.equals(CommonConstant.POETRY)
				|| analysis_type.equals("ʫ")) {
			if (poetry_dic.get(ILexicon.CJK_WORD, keyword) != null)
				return true;
		}
		if (analysis_type.equals(CommonConstant.KNOWLEDGE)) {
			if (konwledge_dic.get(ILexicon.CJK_WORD, keyword) != null)
				return true;
		}
		return false;
	}

	/**
	 * ��������еĶ���
	 * 
	 * @param resultList
	 * @return
	 */
	public ArrayList<String> getVerb(ArrayList<String> resultList) {
		// �ȸ����յ�Ŀ¼���ټ�����Ҫ���ֵ�
		config_none = new JcsegTaskConfig(this.getClass()
				.getResource(CommonConstant.JCSEG_PROPERTIES_NONE).getPath());
		config_none.setMaxLength(10);
		ADictionary verb_dic = DictionaryFactory
				.createDefaultDictionary(config_none);

		File Verb_file = new File(this.getClass()
				.getResource(CommonConstant.VERB_LEX).getPath());
		verb_dic.loadFromLexiconFile(Verb_file);
		ArrayList<String> verbList = new ArrayList<String>();
		// ��ʼ�ж�
		for (String word : resultList) {
			if (verb_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.VERB + ":" + word);
				verbList.add(word);
			}
		}
		return verbList;
	}

	/**
	 * ���ض������ͣ����¡����֡�ʫ�衣����
	 * 
	 * @param resultList
	 * @return
	 */
	public JSONObject getType(ArrayList<String> resultList) {

		loadBasicLex();

		// Map<String,ArrayList<String>> typeMap = new HashMap<String,
		// ArrayList<String>>();
		// type=0;//0��ʼ(����)��1���£�2���֣�3ʫ�裬4֪ʶ,5���(��δ������)
		// String types="";
		JSONObject typeMap = new JSONObject();
		JSONArray typeArray = new JSONArray();
		JSONArray keywordArray = new JSONArray();
		// ArrayList<String> typeList = new ArrayList<String>();
		// ��ʼ�ж�

		for (String word : resultList) {
			typeArray.clear();
			if (story_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.STORY + ":" + word);
				type = CommonConstant.story;
				types = CommonConstant.STORY;
				// typeList.add(types);
				if (!keywordArray.contains(word))
					keywordArray.add(word);// ����ؼ����б�
				typeArray.add(types);// ��ǰ�ؼ�����������
				/*
				 * if(type!=0&&type!=CommonConstant.story) {
				 * type=CommonConstant.mixed; types=CommonConstant.MIXED;
				 * }//�ȴ����� else { type=CommonConstant.story;
				 * types=CommonConstant.STORY;
				 * 
				 * }
				 */
			}
			if (song_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.MUSIC + ":" + word);
				type = CommonConstant.music;
				types = CommonConstant.MUSIC;
				if (!keywordArray.contains(word))
					keywordArray.add(word);// ����ؼ����б�
				typeArray.add(types);// ��ǰ�ؼ�����������
				/*
				 * if(type!=0&&type!=CommonConstant.music)
				 * {type=CommonConstant.mixed;}//�ȴ����� else
				 * {type=CommonConstant.music;types=CommonConstant.MUSIC;}
				 */
			}
			if (poetry_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.POETRY + ":" + word);
				type = CommonConstant.poetry;
				types = CommonConstant.POETRY;
				if (!keywordArray.contains(word))
					keywordArray.add(word);// ����ؼ����б�
				typeArray.add(types);// ��ǰ�ؼ�����������
				/*
				 * if(type!=0&&type!=CommonConstant.poetry)
				 * {type=CommonConstant.mixed;}//�ȴ����� else
				 * {type=CommonConstant.poetry;types=CommonConstant.POETRY;}
				 */
			}
			if (konwledge_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.KNOWLEDGE + ":" + word);
				type = CommonConstant.knowledge;
				types = CommonConstant.KNOWLEDGE;
				type = CommonConstant.poetry;
				types = CommonConstant.POETRY;
				if (!keywordArray.contains(word))
					keywordArray.add(word);// ����ؼ����б�
				typeArray.add(types);// ��ǰ�ؼ�����������
				/*
				 * if(type!=0&&type!=CommonConstant.knowledge)
				 * {type=CommonConstant.mixed;}//�ȴ����� else
				 * {type=CommonConstant.knowledge
				 * ;types=CommonConstant.KNOWLEDGE;}
				 */
			}
			if (!typeArray.isEmpty() && !word.equals("����") && !word.equals("ʫ")
					&& !word.equals("ʫ��") && !word.equals("����")
					&& !word.equals("��")) {
				typeMap.put(word, typeArray);
			}
		}
		if (keywordArray.size() != 0)
			typeMap.put("keyword", keywordArray);
		return typeMap;
	}

	private void getkeyword(ArrayList<String> resultList) {
		if (type != 0 || type != 5) {
			for (String word : resultList) {
				if (story_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword = word;
				}
				if (song_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword = word;
				}
				if (poetry_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword = word;
				}
				if (konwledge_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword = word;
				}
			}
		} else
			keyword = "";
	}

	/**
	 * �õ��������ͣ�������Ż���Ѳ��š�����
	 * 
	 * @param resultList
	 * @return
	 * @throws Exception
	 */
	public String getControl(ArrayList<String> resultList) throws Exception {
		loadControlLex();

		int type1 = 0;// 0��ʼ(����)��1�����2��ѣ�3���(��δ������)
		String results = "";
		// ��ʼ�ж�
		for (String word : resultList) {
			if (random_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.CONTROL + ":" + word);
				if (type1 != 0 && type1 != CommonConstant.random) {
					type1 = CommonConstant.mixed;
					results = CommonConstant.MIXED;
				}// �ȴ�����
				else {
					type1 = CommonConstant.random;
					results = CommonConstant.RANDOM;
				}
			}
			if (best_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.CONTROL + ":" + word);
				if (type1 != 0 && type1 != CommonConstant.best) {
					type1 = CommonConstant.mixed;
					results = CommonConstant.MIXED;
				}// �ȴ�����
				else {
					type1 = CommonConstant.best;
					results = CommonConstant.BEST;
				}
			}
		}
		return results;
	}

	// ���ȡ����//0��ʼ(����)��1���£�2���֣�3ʫ�裬4֪ʶ,5���(��δ������)
	public String getRandom(String typess) throws Exception {

		if (typess.equals(CommonConstant.STORY)) {
			return getContent(CommonConstant.STORY_LEX);
		} else if (typess.equals(CommonConstant.MUSIC) || typess.equals("��")) {
			return getContent(CommonConstant.SONG_LEX);
		}
		return "null";
	}

	public String getBest(String typess) throws SQLException {
		if (typess.equals(CommonConstant.STORY)) {
			return MaxListen.getMax_Story();
		} else if (typess.equals(CommonConstant.MUSIC) || typess.equals("��")) {
			return MaxListen.getMax_Music();
		}
		return "null";
	}

	private String getContent(String tp) throws IOException {
		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				this.getClass().getResource(tp).getPath()), "UTF-8"));
		System.out.println(this.getClass().getResource(tp).getPath());
		String tempString = null;
		int number = new Random().nextInt(19) + 1;// �ӵڶ��п�ʼ
		// System.out.println("�����="+number);
		// һ�ζ�һ�У�����nullʱ�ļ�����
		int line = 0;
		while ((tempString = reader.readLine()) != null) {
			// �ѵ�ǰ�к���ʾ����
			if (line == number) {
				// System.out.println("��"+line+"��");
				return tempString;
			}
			line++;
		}
		reader.close();
		return tempString;
	}

	/**
	 * ���ػ����ֵ�
	 * 
	 */
	public void loadBasicLex() {

		// �ȸ����յ�Ŀ¼���ټ�����Ҫ���ֵ�
		config_none = new JcsegTaskConfig(this.getClass()
				.getResource(CommonConstant.JCSEG_PROPERTIES_NONE).getPath());

		story_dic = DictionaryFactory.createDefaultDictionary(config_none);
		song_dic = DictionaryFactory.createDefaultDictionary(config_none);
		poetry_dic = DictionaryFactory.createDefaultDictionary(config_none);
		konwledge_dic = DictionaryFactory.createDefaultDictionary(config_none);

		String path = getClass().getClassLoader()
				.getResource(CommonConstant.STORY_LEX).getPath();
		File Story_file = new File(path);
		story_dic.loadFromLexiconFile(Story_file);

		File Song_file = new File(this.getClass()
				.getResource(CommonConstant.SONG_LEX).getPath());
		song_dic.loadFromLexiconFile(Song_file);

		File Poetry_file = new File(this.getClass()
				.getResource(CommonConstant.POETRY_LEX).getPath());
		poetry_dic.loadFromLexiconFile(Poetry_file);

		File Konwledge_file = new File(this.getClass()
				.getResource(CommonConstant.KNOWLEDGE_LEX).getPath());
		konwledge_dic.loadFromLexiconFile(Konwledge_file);
	}

	/**
	 * ���ؿ����ֵ�
	 * 
	 */
	public void loadControlLex() {
		// �ȸ����յ�Ŀ¼���ټ�����Ҫ���ֵ�
		config_none = new JcsegTaskConfig(this.getClass()
				.getResource(CommonConstant.JCSEG_PROPERTIES_NONE).getPath());

		random_dic = DictionaryFactory.createDefaultDictionary(config_none);
		best_dic = DictionaryFactory.createDefaultDictionary(config_none);

		File Story_file = new File(this.getClass()
				.getResource(CommonConstant.RANDOM_LEX).getPath());
		random_dic.loadFromLexiconFile(Story_file);

		File Song_file = new File(this.getClass()
				.getResource(CommonConstant.BEST_LEX).getPath());
		best_dic.loadFromLexiconFile(Song_file);
	}

}
