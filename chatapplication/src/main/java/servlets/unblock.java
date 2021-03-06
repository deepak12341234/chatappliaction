package servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DbConnect;

/**
 * Servlet implementation class unblock
 */
public class unblock extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		  String loginuser = null;
		  //String blockedusername = null;
		String temail=request.getParameter("temail");
		HashMap users=(HashMap)session.getAttribute("userData");   
		 if(users!=null){
	        loginuser=(String)users.get("email");
	        try {
				DbConnect db = new DbConnect();
				db.unblock(temail, loginuser);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		 }
		 response.sendRedirect("blockedpeople.jsp");
		 }

}
