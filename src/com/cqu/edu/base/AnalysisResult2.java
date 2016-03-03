package com.cqu.edu.base;
/**
 * 第几种状态
 * 共4种：
 * 1.#￥&*%&*&&*…    （无法得到任何信息，需给出提示语句）
 * 2.随便讲个（故事）				（只有控制词）
 * 3.我想听灰姑娘（的故事）（假设灰姑娘只在故事类型中）			（只有关键词）
 * 4.我想听白雪公主和灰姑娘（错误情况，无目的性）
 */
public class AnalysisResult2 {
	
	private int statu;
	
	private String keyword;
	
	private String type;
	
	private String control;

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}
	
	
	
}
