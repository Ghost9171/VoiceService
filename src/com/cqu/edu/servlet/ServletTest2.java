package com.cqu.edu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cqu.edu.proxy.TimeInvoHandler;
import com.cqu.edu.service.SegmentService2;
import com.cqu.edu.service.impl.SegmentServiceImpl;

@SuppressWarnings("serial")
public class ServletTest2 extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ServletTest2() {
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
		String type = request.getParameter("type");
		String sentence = request.getParameter("sentence");
		String last_sentence =request.getParameter("lastsentence");
		System.out.println(type+last_sentence);
		response.setContentType("text/html;charset=UTF-8");
		try {
			
			SegmentServiceImpl seg = new SegmentServiceImpl();
			SegmentService2 seg2 = (SegmentService2)TimeInvoHandler.factory(seg);
			StringBuffer result = seg2.Segment(type,sentence,last_sentence);
			/*SegmentServiceImpl seg = new SegmentServiceImpl();
			StringBuffer result =seg.Segment(type,sentence,last_sentence);*/
			
			//SegmentService2 seg=new SegmentService2();
			//String result = seg.Segment(type,sentence,last_sentence);
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
		SegmentServiceImpl seg1 = new SegmentServiceImpl();
		try {
			StringBuffer result = seg1.Segment("","", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
