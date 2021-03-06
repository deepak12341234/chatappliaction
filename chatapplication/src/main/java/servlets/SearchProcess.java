
package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public class SearchProcess extends HttpServlet {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
        HttpSession session=request.getSession();                           
        HashMap users=(HashMap)session.getAttribute("userData");             //session me hai esliye isko wha se utha liya(means pofile page me hai isliye whi se le liya)
         if(users!=null){                                                     //ye  3 lines aur niche ki pleaze login first sb me rhegi 
        String s=request.getParameter("state");
        String c=request.getParameter("city");
        String a=request.getParameter("area");
        
        db.DbConnect db=new db.DbConnect();                   //Connection bnaya
        
        
        //search......database
        
   ArrayList<HashMap> allUserDetails= db.searchPeople(s, c, a,(String)users.get("email"));            //Db connect ke searchPeople me data dala
        
        //Dbconnect se wapas aaya ab
        //jo krna hai so kro
        
      if(! allUserDetails.isEmpty()){
            java.util.HashMap address=new java.util.HashMap();
            address.put("state", s);                 //put kiya peoplesearch me dikhane ke liye
            address.put("city", c);
            address.put("area", a);
            session.setAttribute("EveryoneDetails",allUserDetails);
            session.setAttribute("address",address);
            response.sendRedirect("chatMainPage.jsp");
        }else{
            session.setAttribute("msg", "No Data Found!");
            response.sendRedirect("profile.jsp");
        }  
        }else{
            session.setAttribute("msg", "Plz login First!");
            response.sendRedirect("home.jsp");
        }
        } 
        catch (Exception ex) {
            
        }
        }
}
