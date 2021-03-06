
package servlets;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
@SuppressWarnings("serial")
@MultipartConfig
public class SendMessage extends HttpServlet {

   
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try{
            HttpSession session=request.getSession();
             @SuppressWarnings("rawtypes")
			HashMap users=(HashMap)session.getAttribute("userData"); 
            if(users!=null){
                String temail=request.getParameter("temail");
                String semail=(String)users.get("email");              //session se hi nikal liya sende email
                
                Part p=request.getPart("ufile");
                
                
                 java.io.InputStream in=null;
                 String fname="";
                if(p!=null){
                    fname=p.getSubmittedFileName();
                    in = p.getInputStream();
                }
             
                
                db.DbConnect db=new db.DbConnect();                             //connection bnaya
                String r=db.sendMessage(semail, temail, fname, in);    //Dbconnect ke send Message me data dala
                
            //jo krna hai so kro
            if(r.equalsIgnoreCase("Done")){
                    session.setAttribute("msg", "Message Sent Successfully!");
                }else{
                    session.setAttribute("msg", "Message Sending Failed!");
                }
            
                response.sendRedirect("talk.jsp?temail="+temail);
            }else{
                session.setAttribute("msg", "Plz login First!");
                response.sendRedirect("home.jsp");
            }
        }catch(Exception ex){
            
        }
    }
            
     }

    


