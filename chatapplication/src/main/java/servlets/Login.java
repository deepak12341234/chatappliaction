
package servlets;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public class Login extends HttpServlet {

   
    protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
         HttpSession session=request.getSession();
         
        
        try {
             String e=request.getParameter("email");
            String p=request.getParameter("password");
            request.setAttribute("e", e);
             db.DbConnect db=new db.DbConnect();                   //conection bnaya
               
                @SuppressWarnings("rawtypes")
				HashMap userDetails=db.checkLogin(e, p);             //checklogin me data data
                
              //Db connect se collection ke form me value return aayi
               //hme jo krana hai krao
             if(userDetails!=null){
            	
            	 session.setAttribute("userData", userDetails);
                response.sendRedirect("profile.jsp");
            }else{
                session.setAttribute("msg", "Wrong Entries!");
                response.sendRedirect("home.jsp");
                }
             
        } catch (Exception ex) {
            session.setAttribute("msg", "Login Failed: "+ex);
            response.sendRedirect("home.jsp");
        }
        
  

    }
    


    }
