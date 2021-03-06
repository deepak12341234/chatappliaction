
package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DbConnect;


@SuppressWarnings("serial")
public class Logout extends HttpServlet {

    
    @SuppressWarnings("unused")
	protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	DbConnect db = null;
		try {
			db = new DbConnect();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
    HttpSession session=request.getSession();
        @SuppressWarnings("rawtypes")
		HashMap userDetails=(HashMap)session.getAttribute("userData");
     String e=   (String) userDetails.get("email");
        if(userDetails!=null){
        	
			
        	
				try {
					db.logOut(e);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
            session.invalidate();
            response.sendRedirect("home.jsp");
        }else{
            session.setAttribute("msg", "Plz login First!");
            response.sendRedirect("home.jsp");
        }
    }

}

