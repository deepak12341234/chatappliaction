
package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public class changepassword extends HttpServlet {

   
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
      
       HttpSession session=request.getSession();                           
        @SuppressWarnings("rawtypes")
		HashMap users=(HashMap)session.getAttribute("userData");             //session me hai esliye isko wha se utha liya(means pofile page me hai isliye whi se le liya)
         if(users!=null){ 
      String email=(String)users.get("email"); 
     String old=request.getParameter("oldpassword");
     String nw=request.getParameter("newpassword");
     String conf=request.getParameter("confirmpassword");
        
      
        
        if(nw.equals(conf)){
            try{  
          db.DbConnect db=new db.DbConnect();                             //connection bnaya
          String r=db.changepassword(nw,email,old);
         //Dbconnect se wapas aaya
          if(r.equalsIgnoreCase("Success")){
                    session.setAttribute("msg", "Password Change Successfully!");
                     response.sendRedirect("changepassword.jsp");
          }
          if(r.equalsIgnoreCase("Error")){
                    session.setAttribute("msg", "You have Enter Wrong Old Password!");
                     response.sendRedirect("changepassword.jsp");
          }
        }
         catch(Exception e){}
        }else{
         session.setAttribute("msg", "Old Password and New Password Not Match!");
                  response.sendRedirect("changepassword.jsp");
         }
         }
         else{
                session.setAttribute("msg", "Plz login First!");
                response.sendRedirect("home.jsp");
            }
    
}
   
}

  


