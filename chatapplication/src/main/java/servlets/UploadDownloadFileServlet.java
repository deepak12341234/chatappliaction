package servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import db.DbConnect;

@WebServlet("/UploadDownloadFileServlet")
public class UploadDownloadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ServletFileUpload uploader = null;
	@Override
	public void init() throws ServletException{
		
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DbConnect db = null;
 		 HttpSession session=request.getSession();


  		   @SuppressWarnings("rawtypes")
			HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya , pofile wale page se lekar jitne page hai sb me ye session rhega, perticular person ka hai

  		String temail=(String)session.getAttribute("temail");
  		try {
			db = new DbConnect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		    
  	    HashMap tUserDetails=db.getPeopleByEmail(temail);    

  	 	String ruser = (String) tUserDetails.get("name");

  	    String sname=(String)users.get("name");
  	  
		  String webAppRoot = System.getProperty( "catalina.base" );
		  String s =File.separator;
		  String filepath = webAppRoot + s + "wtpwebapps"+s+"ROOT"+s+sname+ruser;
		 
  	   // String webAppRoot =  "/home/speaosol/public_html/test/";
  		//String s = File.separator;
  		//String filepath = webAppRoot+sname+ruser;
  		 
		if(ServletFileUpload.isMultipartContent(request)){
			try {
                List<FileItem> multiparts = new ServletFileUpload(
                                         new DiskFileItemFactory()).parseRequest(request);
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                    	
                        String name = new File(item.getName()).getName();
                        item.write( new File(filepath + File.separator + name));
                    }
                }
			} catch (Exception e) {
				
			}
			
			PrintWriter out = response.getWriter();
			out.print("{\"status\":1}");
		}
		
	}
	
	

}
