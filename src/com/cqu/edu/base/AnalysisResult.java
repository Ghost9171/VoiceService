package com.cqu.edu.base;
/**
 * �ڼ���״̬
 * ��8�֣�
 * 1.���䣺��ͨ���󷶸ʵϡ����� ���޷��õ��κ���Ϣ���������ʾ��䣩0 0 0
 * 2.���䣺��㽲������/���׸������޹ؼ��ֵ������ͺͿ��ƣ� 0 1 1
 * 3.���䣺�������ҹ������ҹ���ֻ�ڹ��������У���һ���ؼ���һ�����ͣ����ùؼ���ֻ��Ӧһ�����ͣ� 1 1 2
 * 4.���䣺��������ѩ�����������ѩ�������ǹ������Ǹ�������һ���ؼ��ֶ������ͣ�1 �� 2    !!!��Ҫ��һ���Ự
 * 5.���䣺�������ҹ���Ĺ��£�������¡�Ҳ��һ���ؼ��ʣ��������ؼ���һ�����ͣ� 2 1 2
 * 6.���䣺��������ѩ�����Ĺ��£������ؼ��ֶ������ͣ� 2 �� 2
 * 7.���䣺��������ѩ�����ͻҹ�������������Ŀ���ԣ��� �� ��>2
 * 8.���䣺�������ҹ���ĸ裨ƥ�䲻���� 2 1 2
 * 9.���䣺��˹�ٷҹ�ʦ�����£�ֻ�������޹ؼ���Ҳ�޿��ƣ�1 1 1 !!!��Ҫ��һ���Ự
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
