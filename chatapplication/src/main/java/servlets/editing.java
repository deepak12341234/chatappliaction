
package servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class editing extends HttpServlet {

   
    protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    
        try{
        HttpSession session=request.getSession();                           
        HashMap users=(HashMap)session.getAttribute("userData");             //session me hai esliye isko wha se utha liya(means pofile page me hai isliye whi se le liya)
         if(users!=null){  
             
     
      //get from editprofile.jsp
     String n=request.getParameter("name");
     String ph=request.getParameter("phone");
     String g=request.getParameter("gender");
     String s=request.getParameter("state");
     String c=request.getParameter("city");
     String a=request.getParameter("area");
     String dt=request.getParameter("dob");
     //code to conver String to Date
      
      //util form me cast kiya
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
     Date date=sdf.parse(dt);
            //sql form me change kiya
      java.sql.Date d=new java.sql.Date(date.getTime());
     
   
      db.DbConnect db=new db.DbConnect();       //connection bnaya
                
  //Db connect ke edit nam ke method me data dala    
        String m=db.edit(ph,n,g,d,s,c,a,(String)users.get("email"));
        
        //Dbconnect se wapas aao
        //edit string type ka tha ab usne jo jo return kiya hai uske according hm apna kam krenge
      
         if(m.equalsIgnoreCase("Success")){
             session.setAttribute("msg", "DATA Updated Successfully");
                response.sendRedirect("editprofile.jsp");
            }else if(m.equalsIgnoreCase("error")){
                session.setAttribute("msg", "Updation failed");
                response.sendRedirect("editprofile.jsps");
            }
         
        }
        }catch (Exception ex) {
           
        }
        
        
        
                
 }
}
