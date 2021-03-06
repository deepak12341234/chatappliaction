
package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


@SuppressWarnings("serial")
@WebServlet(name = "Register", urlPatterns = {"/Register"})
@MultipartConfig
public class Register extends HttpServlet {

   
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void service(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
    
         HttpSession session=request.getSession();
        try {
            //PrintWriter out = response.getWriter();
            
     String n=request.getParameter("name");
     String ph=request.getParameter("phone");
     String e=request.getParameter("email");
     String g=request.getParameter("gender");
     String s=request.getParameter("state");
     String c=request.getParameter("city");
     String a=request.getParameter("area");
     String p=request.getParameter("password");
     
     String dt=request.getParameter("dob");
     //code to conver String to Date
      
      //util form me cast kiya
     SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            Date date=sdf.parse(dt);
            //sql form me change kiya
            java.sql.Date d=new java.sql.Date(date.getTime());
     
     // file(pdf,images) ke liye coding
      Part part=request.getPart("photo");
            InputStream is=null;
            if(part!=null){
                is=part.getInputStream();
            }
            
     //Uper text field se li gyi chezzo ka userDetails nam ka collection bnaya       
     HashMap userDetails=new HashMap();
            userDetails.put("email", e);
            userDetails.put("name", n);
            userDetails.put("pass", p);
            userDetails.put("phone", ph);
            userDetails.put("gender", g);
            userDetails.put("dob", d);
            userDetails.put("state", s);
            userDetails.put("city", c);
            userDetails.put("area", a);
            userDetails.put("photo", is);         
            userDetails.put("status", User.ONLINE);     
       //connection bnaya 
        db.DbConnect db=new db.DbConnect();   
                
  //Db connect ke reg nam ke method me data dala    
        String m=db.reg(userDetails);
        
        //Dbconnect se wapas aao
        //reg string type ka tha ab usne jo jo return kiya hai uske according hm apna kam krenge
      
         if(m.equalsIgnoreCase("Success")){
                userDetails.remove("pass");
                userDetails.remove("photo");
                
                session.setAttribute("msg", "you are registered susscessfully");
                response.sendRedirect("home.jsp");
            }else if(m.equalsIgnoreCase("Already")){
                session.setAttribute("msg", "Email ID Already Exist!");
                response.sendRedirect("home.jsp");
            }else {
                session.setAttribute("msg", "Registration Failed!");
                response.sendRedirect("home.jsp");
            }
        } catch (Exception ex) {
            session.setAttribute("msg", "Registration Failed: "+ex);
            response.sendRedirect("home.jsp");
        }
                
 }

    

}
