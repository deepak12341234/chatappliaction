
package servlets;
import java.io.IOException;
import java.io.InputStream;
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
public class changePhoto extends HttpServlet {

     
   
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
           HttpSession session=request.getSession();
        
         try {
             String e=request.getParameter("Email");                 //fetch from URI of email
             Part part=request.getPart("photo");
            InputStream is=null;
            if(part!=null){
                is=part.getInputStream();
            }
            
            HashMap pico=new HashMap();            //collection me rkha
            pico.put("photo", is);
           pico.put("Email", e);
           
            db.DbConnect db=new db.DbConnect();              //Connection bnaya
           
            
              String m=db.changephoto(pico);          //db connect ke changephoto nam ke method me dal diya
              
               if(m.equalsIgnoreCase("Success")){
                
               session.setAttribute("msg", "Photo Succesfully Change");
                response.sendRedirect("editprofile.jsp");
            }else if(m.equalsIgnoreCase("Error")){
                session.setAttribute("msg", "please Again try");
                 response.sendRedirect("editprofile.jsp");
            }
            
           } catch (Exception ex) {
            
        }
       
    }
}

