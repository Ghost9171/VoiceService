package com.cqu.edu.base;
/**
 * 第几种状态
 * 共8种：
 * 1.例句：热通过大范甘迪。。。 （无法得到任何信息，需给出提示语句）0 0 0
 * 2.例句：随便讲个故事/放首歌听（无关键字但有类型和控制） 0 1 1
 * 3.例句：我想听灰姑娘（假设灰姑娘只在故事类型中）（一个关键字一种类型，即该关键字只对应一种类型） 1 1 2
 * 4.例句：我想听白雪公主（这里白雪公主既是故事又是歌曲）（一个关键字多种类型）1 多 2    !!!需要进一步会话
 * 5.例句：我想听灰姑娘的故事（这里‘故事’也是一个关键词）（两个关键字一种类型） 2 1 2
 * 6.例句：我想听白雪公主的故事（两个关键字多种类型） 2 多 2
 * 7.例句：我想听白雪公主和灰姑娘（错误情况，无目的性）多 多 多>2
 * 8.例句：我想听灰姑娘的歌（匹配不到） 2 1 2
 * 9.例句：阿斯蒂芬哈师大会故事（只有类型无关键字也无控制）1 1 1 !!!需要进一步会话
 */
public class AnalysisResult {
	
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
