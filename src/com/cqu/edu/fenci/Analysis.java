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
	/** 基本字典 */
	private static ADictionary story_dic;
	private static ADictionary song_dic;
	private static ADictionary poetry_dic;
	private static ADictionary konwledge_dic;
	/** 控制字典 */
	private static ADictionary random_dic;
	private static ADictionary best_dic;

	private static JcsegTaskConfig config_none;
	// 控制类型
	private int controlType = 0;
	// 0初始(其他)，1故事，2音乐，3诗歌，4知识,5混合(尚未做处理)
	private int type = 0;
	private String types = "";
	private String keyword = "";

	/**
	 * 根据分词结果进行分析
	 * 
	 * @param typeMap
	 * @param control
	 * @param last_result
	 *            上一句话的分析结果
	 * @return
	 */
	public AnalysisResult startAnalysis(JSONObject typeMap, String control,
			AnalysisResult last_result) {

		AnalysisResult result = new AnalysisResult();
		// 关键词
		String keyword = "";
		// 类型
		String analysis_type = "";
		JSONArray keywordList = null;
		JSONArray typeList = null;
		// control=control1;

		// 要多次会话才能得出结果，需要上一次的分析结果
		if (last_result != null && last_result.getStatu() == 4) {// 需进行多次会话{"statu":4,"keyword":"白雪公主","type":null,"control":"指定播放"}
			keywordList = typeMap.getJSONArray("keyword");
			if (typeMap.size() == 1) {//仍然只找到一個关键字
				if (keywordList.contains("故事") || keywordList.contains("音乐")
						|| keywordList.contains("歌") || keywordList.contains("诗")) {
					result.setStatu(6);
					result.setKeyword(last_result.getKeyword());
					result.setType(keywordList.getString(0));
					result.setControl(last_result.getControl());
				} else {// 没有得到想要的结果，重复!!!有可能要优化（上下文无关）
					result.setStatu(4);
					result.setKeyword(last_result.getKeyword());
					result.setControl(last_result.getControl());
				}
			} else {
				result = getResultWhenMapSizeis2(keywordList, typeMap, control);
			}
		} else if (last_result != null && last_result.getStatu() == 9) {// （只有类型无关键字也无控制）
		
			if (typeMap.size() == 0) {//没有关键字，只有控制词或没有
				if(control==null||control.equals("")||control.equals("null")){//无控制词
					result.setStatu(9);
					result.setType(analysis_type);
					//result.setControl(control);
				}else{//有控制词
					result.setStatu(2);
					result.setType(last_result.getType());
					result.setControl(control);
				}
			}else if (typeMap.size() == 1) {//仍然只找到一個关键字
				keywordList = typeMap.getJSONArray("keyword");
				//不是需要的关键字
				if (keywordList.contains("故事") || keywordList.contains("音乐")
						|| keywordList.contains("歌")
						|| keywordList.contains("诗")) {
					// 只有'故事'这些一个关键字（无关键字但有类型）
					analysis_type = keywordList.getString(0);
					//判断是否有控制词
					if(control==null||control.equals("")||control.equals("null")){//无控制词
						result.setStatu(9);
						result.setType(analysis_type);
					}else{//有控制词
						result.setStatu(2);
						result.setType(analysis_type);
						result.setControl(control);
					}
				} else {//但这关键字是需要的(关键词)
					keyword = keywordList.getString(0);
					control = "指定播放";
					typeList = typeMap.getJSONArray(keyword);
					if (typeList.size() == 1) {// （一个关键字一种类型，即该关键字只对应一种类型）
						result.setStatu(3);
						result.setKeyword(keyword);
						result.setType(typeList.getString(0));
						result.setControl(last_result.getControl());
					} else {// （一个关键字多种类型）！！
						result.setStatu(4);
						result.setKeyword(last_result.getKeyword());
						result.setControl(last_result.getControl());
					}
				}
			} else {
				keywordList = typeMap.getJSONArray("keyword");
				result = getResultWhenMapSizeis2(keywordList, typeMap, control);
			}
		}// 不需要进行多次会话
		else if (typeMap.size() == 2)// 只有一个关键词(除故事、音乐、歌、诗歌、诗外){[aaa,bbb]}
		{
			keywordList = typeMap.getJSONArray("keyword");
			result = getResultWhenMapSizeis2(keywordList, typeMap, control);
		} else if (typeMap.size() == 0) {// 未找到关键字
			result.setStatu(1);
		} else if (typeMap.size() > 2) {// （错误情况，无目的性）
			result.setStatu(7);
		} else if (typeMap.size() == 1) {
			if (!control.equals("")) {// （控制播放）
				result.setStatu(2);
				keywordList = typeMap.getJSONArray("keyword");
				result.setType(keywordList.getString(0));
				result.setControl(control);
			} else {// 只有类型无关键字也无控制 !!!需要进一步会话
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
		// 关键词
		String keyword = "";
		// 类型
		String analysis_type = "";
		JSONArray typeList = null;
		if (keywordList.size() == 1) {// {[aaa]}
			if (keywordList.contains("故事") || keywordList.contains("音乐")
					|| keywordList.contains("歌") || keywordList.contains("诗")) {
				// 只有'故事'这些一个关键字（无关键字但有类型）
				analysis_type = keywordList.getString(0);
				result.setStatu(2);
				result.setType(analysis_type);
				result.setControl(control);
			} else {
				keyword = keywordList.getString(0);
				control = "指定播放";
				typeList = typeMap.getJSONArray(keyword);
				if (typeList.size() == 1) {// （一个关键字一种类型，即该关键字只对应一种类型）
					result.setStatu(3);
					result.setKeyword(keyword);
					result.setType(typeList.getString(0));
					result.setControl(control);
				} else {// （一个关键字多种类型）！！
					result.setStatu(4);
					result.setKeyword(keyword);
					result.setControl(control);
				}
			}
		} else {// {[aaa,bbb]}
			keyword = keywordList.getString(0);
			control = "指定播放";
			if (keyword.equals("故事") || keyword.equals("音乐")
					|| keyword.equals("歌") || keyword.equals("诗")
					|| keyword.equals("诗歌")) {
				analysis_type = keywordList.getString(0);
				keyword = keywordList.getString(1);
				typeList = typeMap.getJSONArray(keyword);
				if (typeList.size() == 1) {// （两个关键字一种类型）
					result.setStatu(5);
					result.setKeyword(keyword);
					result.setType(analysis_type);
					result.setControl(control);
				} else {// （两个关键字多种类型）
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
				if (typeList.size() == 1) {// （两个关键字一种类型）
					result.setStatu(5);
					result.setKeyword(keyword);
					result.setType(analysis_type);
					result.setControl(control);
				} else {// （两个关键字多种类型）
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
				|| analysis_type.equals("歌")) {
			if (song_dic.get(ILexicon.CJK_WORD, keyword) != null)
				return true;
		}
		if (analysis_type.equals(CommonConstant.POETRY)
				|| analysis_type.equals("诗")) {
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
	 * 返回语句中的动词
	 * 
	 * @param resultList
	 * @return
	 */
	public ArrayList<String> getVerb(ArrayList<String> resultList) {
		// 先给个空的目录，再加载想要的字典
		config_none = new JcsegTaskConfig(this.getClass()
				.getResource(CommonConstant.JCSEG_PROPERTIES_NONE).getPath());
		config_none.setMaxLength(10);
		ADictionary verb_dic = DictionaryFactory
				.createDefaultDictionary(config_none);

		File Verb_file = new File(this.getClass()
				.getResource(CommonConstant.VERB_LEX).getPath());
		verb_dic.loadFromLexiconFile(Verb_file);
		ArrayList<String> verbList = new ArrayList<String>();
		// 开始判断
		for (String word : resultList) {
			if (verb_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.VERB + ":" + word);
				verbList.add(word);
			}
		}
		return verbList;
	}

	/**
	 * 返回动作类型，故事、音乐、诗歌。。。
	 * 
	 * @param resultList
	 * @return
	 */
	public JSONObject getType(ArrayList<String> resultList) {

		loadBasicLex();

		// Map<String,ArrayList<String>> typeMap = new HashMap<String,
		// ArrayList<String>>();
		// type=0;//0初始(其他)，1故事，2音乐，3诗歌，4知识,5混合(尚未做处理)
		// String types="";
		JSONObject typeMap = new JSONObject();
		JSONArray typeArray = new JSONArray();
		JSONArray keywordArray = new JSONArray();
		// ArrayList<String> typeList = new ArrayList<String>();
		// 开始判断

		for (String word : resultList) {
			typeArray.clear();
			if (story_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.STORY + ":" + word);
				type = CommonConstant.story;
				types = CommonConstant.STORY;
				// typeList.add(types);
				if (!keywordArray.contains(word))
					keywordArray.add(word);// 加入关键词列表
				typeArray.add(types);// 当前关键词所属类型
				/*
				 * if(type!=0&&type!=CommonConstant.story) {
				 * type=CommonConstant.mixed; types=CommonConstant.MIXED;
				 * }//等待处理 else { type=CommonConstant.story;
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
					keywordArray.add(word);// 加入关键词列表
				typeArray.add(types);// 当前关键词所属类型
				/*
				 * if(type!=0&&type!=CommonConstant.music)
				 * {type=CommonConstant.mixed;}//等待处理 else
				 * {type=CommonConstant.music;types=CommonConstant.MUSIC;}
				 */
			}
			if (poetry_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.POETRY + ":" + word);
				type = CommonConstant.poetry;
				types = CommonConstant.POETRY;
				if (!keywordArray.contains(word))
					keywordArray.add(word);// 加入关键词列表
				typeArray.add(types);// 当前关键词所属类型
				/*
				 * if(type!=0&&type!=CommonConstant.poetry)
				 * {type=CommonConstant.mixed;}//等待处理 else
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
					keywordArray.add(word);// 加入关键词列表
				typeArray.add(types);// 当前关键词所属类型
				/*
				 * if(type!=0&&type!=CommonConstant.knowledge)
				 * {type=CommonConstant.mixed;}//等待处理 else
				 * {type=CommonConstant.knowledge
				 * ;types=CommonConstant.KNOWLEDGE;}
				 */
			}
			if (!typeArray.isEmpty() && !word.equals("故事") && !word.equals("诗")
					&& !word.equals("诗歌") && !word.equals("音乐")
					&& !word.equals("歌")) {
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
	 * 得到控制类型，随机播放或最佳播放。。。
	 * 
	 * @param resultList
	 * @return
	 * @throws Exception
	 */
	public String getControl(ArrayList<String> resultList) throws Exception {
		loadControlLex();

		int type1 = 0;// 0初始(其他)，1随机，2最佳，3混合(尚未做处理)
		String results = "";
		// 开始判断
		for (String word : resultList) {
			if (random_dic.get(ILexicon.CJK_WORD, word) != null) {
				System.out.println(CommonConstant.CONTROL + ":" + word);
				if (type1 != 0 && type1 != CommonConstant.random) {
					type1 = CommonConstant.mixed;
					results = CommonConstant.MIXED;
				}// 等待处理
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
				}// 等待处理
				else {
					type1 = CommonConstant.best;
					results = CommonConstant.BEST;
				}
			}
		}
		return results;
	}

	// 随机取数据//0初始(其他)，1故事，2音乐，3诗歌，4知识,5混合(尚未做处理)
	public String getRandom(String typess) throws Exception {

		if (typess.equals(CommonConstant.STORY)) {
			return getContent(CommonConstant.STORY_LEX);
		} else if (typess.equals(CommonConstant.MUSIC) || typess.equals("歌")) {
			return getContent(CommonConstant.SONG_LEX);
		}
		return "null";
	}

	public String getBest(String typess) throws SQLException {
		if (typess.equals(CommonConstant.STORY)) {
			return MaxListen.getMax_Story();
		} else if (typess.equals(CommonConstant.MUSIC) || typess.equals("歌")) {
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
		int number = new Random().nextInt(19) + 1;// 从第二行开始
		// System.out.println("随机数="+number);
		// 一次读一行，读入null时文件结束
		int line = 0;
		while ((tempString = reader.readLine()) != null) {
			// 把当前行号显示出来
			if (line == number) {
				// System.out.println("第"+line+"行");
				return tempString;
			}
			line++;
		}
		reader.close();
		return tempString;
	}

	/**
	 * 加载基本字典
	 * 
	 */
	public void loadBasicLex() {

		// 先给个空的目录，再加载想要的字典
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
	 * 加载控制字典
	 * 
	 */
	public void loadControlLex() {
		// 先给个空的目录，再加载想要的字典
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
