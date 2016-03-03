package com.cqu.edu.fenci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.lionsoul.jcseg.core.ADictionary;
import org.lionsoul.jcseg.core.DictionaryFactory;
import org.lionsoul.jcseg.core.ILexicon;
import org.lionsoul.jcseg.core.JcsegTaskConfig;

import com.cqu.edu.DB.MaxListen;
import com.cqu.edu.base.AnalysisResult;
import com.cqu.edu.base.AnalysisResult2;
import com.cqu.edu.base.CommonConstant;

public class Analysis2 {

	/** 基本字典 */
	private static ADictionary story_dic;
	private static ADictionary song_dic;
	private static ADictionary poetry_dic;
	private static ADictionary konwledge_dic;
	/** 控制字典 */
	private static ADictionary random_dic;
	private static ADictionary best_dic;

	private static JcsegTaskConfig config_none;
	
	/**
	 * 先判断控制词在判断关键词
	 * @param type
	 * @param control
	 * @param keyword 
	 * @param last_result
	 * @return
	 */
	public AnalysisResult startAnalysis(String type, String control,ArrayList<String> keyword, AnalysisResult2 last_result) {
		if(control!=null&&!control.equals("")){
			System.out.println("控制："+control);
			
		}else if(keyword!=null){
			System.out.println("关键词："+keyword.toString());
			
		}
		return null;
	}
	
	public ArrayList<String> getKeyword(String type, ArrayList<String> resultList) {
		loadBasicLex();
		ArrayList<String> keyword=new ArrayList<String>();
		switch (type) {
		case "story":
			for (String word : resultList) {
				if (story_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword.add(word);
					}
				}
			break;
		case "song":
			for (String word : resultList) {
				if (song_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword.add(word);
					}
				}
			break;
		case "poetry":
			for (String word : resultList) {
				if (poetry_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword.add(word);
					}
				}
			break;
		case "konwledge":
			for (String word : resultList) {
				if (konwledge_dic.get(ILexicon.CJK_WORD, word) != null) {
					keyword.add(word);
					}
				}
			break;
		default:
			break;
		}
		return keyword;
	}
	
	public String getControl(ArrayList<String> resultList) {
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
