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
 * Servlet implementation class blockperson
 */
public class blockperson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public blockperson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			  HttpSession session=request.getSession();
			  String loginuser = null;
			  String blockedusername = null;
			 HashMap users=(HashMap)session.getAttribute("userData");   
			 if(users!=null){
		 loginuser=		 (String)users.get("email");
		 blockedusername= (String) session.getAttribute("temail");
			 }
			DbConnect db = new DbConnect();
			db.blockedperson(blockedusername, loginuser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.sendRedirect("profile.jsp");
	} 

}
