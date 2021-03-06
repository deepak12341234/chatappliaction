
package servlets;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class GetPhoto extends HttpServlet {

   
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         try {
        
             String e=request.getParameter("Email");                 //fetch from URI of email
            String g=request.getParameter("Gender");             //fetch from URI of gender
           
            db.DbConnect db=new db.DbConnect();              //Connection bnaya
            byte[] b=db.getPhoto(e);                         //email dala , now go to dB connect
            
             //agar photo kisi ne dala hai to
            if(b!=null){
             response.getOutputStream().write(b);
            }
            
            else if(b==null){
                if(g.equals("male")){
                FileInputStream fin=new FileInputStream("C:\\Users\\BADAL\\Desktop\\Advanced java\\Netbean  jsp\\PeopleTalk\\web\\img\\xyz.jpg");
                b=new byte[3500];
                fin.read(b);
                 response.getOutputStream().write(b);
                 fin.close();
                 }
          else{
           FileInputStream fin=new FileInputStream("C:\\Users\\BADAL\\Desktop\\Advanced java\\Netbean  jsp\\PeopleTalk\\web\\img\\abc.jpg");
            b=new byte[3500];
             fin.read(b);
                 response.getOutputStream().write(b);
                 fin.close();
                 }
        }
         }
        catch (Exception ex) {
            ex.printStackTrace();
            FileInputStream fin=new FileInputStream("C:\\Users\\BADAL\\Desktop\\Advanced java\\Netbean  jsp\\PeopleTalk\\web\\img\\def.png");
            byte []b=new byte[3500];
            fin.read(b);
            response.getOutputStream().write(b);
            fin.close();
        }
       
    }
}

