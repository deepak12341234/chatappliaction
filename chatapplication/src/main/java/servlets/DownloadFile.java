
package servlets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.DbConnect;


@SuppressWarnings("serial")

public class DownloadFile extends HttpServlet {
	private String filepath;
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
    		DbConnect db;
   		 HttpSession session=request.getSession();


    		   @SuppressWarnings("rawtypes")
			HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya , pofile wale page se lekar jitne page hai sb me ye session rhega, perticular person ka hai

    		String temail=(String)session.getAttribute("temail");
    		db = new DbConnect();		    
    	    HashMap tUserDetails=db.getPeopleByEmail(temail);    

    	 	String ruser = (String) tUserDetails.get("name");

    	    String sname=(String)users.get("name");
    	    String webAppRoot = System.getProperty( "catalina.base" );
    		String s = File.separator;
    		 filepath = webAppRoot + s + "wtpwebapps" +s+"ROOT"+s+sname+ruser;
    		 String  fileName=request.getParameter("filename");
    		
    		if(fileName == null || fileName.equals("")){
    			throw new ServletException("File Name can't be null or empty");
    		}
    		File file = new File(filepath+File.separator+fileName);
    		if(!file.exists()){
    			try {
        		 filepath = webAppRoot + s + "wtpwebapps" +s+"ROOT"+s+ruser+sname;
        		 file = new File(filepath+File.separator+fileName);
    			}
    			catch (Exception e) {
        			throw new ServletException("File doesn't exists on server.");
				}
    		}
    		
    		System.out.println("File location on server::"+file.getAbsolutePath());
    		InputStream fis = new FileInputStream(file);
    	
    		ServletOutputStream os       = response.getOutputStream();
    		byte[] bufferData = new byte[4096];
    		int read;
    		response.setContentType("application/octet-stream");
    		
    		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    		response.setContentLength((int) file.length());
    		while((read = fis.read(bufferData))!= -1){
    			os.write(bufferData, 0, read);
    		}
    	
    		System.out.println("File downloaded at client successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}