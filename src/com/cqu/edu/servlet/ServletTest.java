package com.cqu.edu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cqu.edu.service.SegmentService;

@SuppressWarnings("serial")
public class ServletTest extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ServletTest() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
		/*
		 * request.setCharacterEncoding("UTF-8");
		 * 
		 * System.out.println(request.getParameter("asdf")); //
		 * response.getOutputStream
		 * ().print("传的信息为："+request.getParameter("asdf").getBytes("gb2312"));
		 * response.setContentType("text/html;charset=UTF-8"); //
		 * response.getOutputStream().print("中文字"); //这行会出错 //
		 * response.getWriter().print("GET传的信息为：" + //
		 * request.getParameter("asdf")); // 换成这个就好了 String name =
		 * request.getParameter("asdf"); try { CreatIndex creat = new
		 * CreatIndex(); creat.creat(); StoryQuery search=new StoryQuery();
		 * String result = search.creatQuery(name); response.getWriter().print(
		 * "GET传的信息为：" + request.getParameter("asdf")); // 换成这个就好了
		 * response.getWriter().print("索引回的信息为"+result);
		 * response.getWriter().close();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

		request.setCharacterEncoding("UTF-8");
		String sentence = request.getParameter("sentence");
		String last_sentence =request.getParameter("lastsentence");
		System.out.println(last_sentence);
		response.setContentType("text/html;charset=UTF-8");
		try {
			SegmentService seg=new SegmentService();
			String result = seg.Segment(sentence,last_sentence);
			seg=null;
			response.getWriter().print(result);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		SegmentService seg=new SegmentService();
		try {
			String result = seg.Segment("",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
