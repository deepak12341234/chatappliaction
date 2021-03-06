<%@page import="java.io.IOException"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Arrays"%>
<%@page import="db.DbConnect"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import=" java.io.File"%>
<%
DbConnect db;

HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya , pofile wale page se lekar jitne page hai sb me ye session rhega, perticular person ka hai
if(users!=null){
String temail=(String)session.getAttribute("temail");
db = new DbConnect();		    
HashMap tUserDetails=db.getPeopleByEmail(temail);    

String ruser = (String)tUserDetails.get("name");

String sname=(String)users.get("name");
//String webAppRoot = "/home/speaosol/public_html/test/";
//String s = File.separator;
//String UPLOAD_DIRECTORY = webAppRoot +sname+ruser;
//String UPLOAD_DIRECTORY1= webAppRoot +ruser+sname;
 String webAppRoot = System.getProperty( "catalina.base" );
 String s = File.separator;
 String UPLOAD_DIRECTORY = webAppRoot + s + "wtpwebapps" +s+"ROOT"+s+sname+ruser;
 String UPLOAD_DIRECTORY1= webAppRoot + s + "wtpwebapps" +s+"ROOT"+s+ruser+sname;
File  f2 = new File(UPLOAD_DIRECTORY);

File f3 = new File(UPLOAD_DIRECTORY1);


	if(!(f2.exists())) {
		f2.mkdir();
		
		}
	if(!(f3.exists())) {
		
			f3.mkdir();
		
	
        	 
	}
%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

		
		<div class="container text-center ">
			<div class="panel panel-default">
				<div class="panel-body text-center">
					<div class="row">
						<div class="col-lg-6">
							<div class="panel panel-default">
								<div class="panel-heading text-left">
									<h5><%= users.get("name") %>'s file</h5>
								</div>
		

          <% 
          List<String> results = new ArrayList<String>();
      File[] files = new File(UPLOAD_DIRECTORY).listFiles();
      if(files!=null){
   // Arrays.sort(files, Comparator.comparing(File::lastModified));

           for (File file : files) {
             if (file.isFile()) {
              results.add(file.getName());
              
               %>  
                     <div class="panel-body text-left">
									<div class="row  ">
										<div class="col-lg-6">               
                                         <a href="DownloadFile?filename=<%=file.getName()%>">
                                          <%=file.getName() %>
                                        </a>									
                                        </div>
										
									</div>
								</div>
                                              <%
                                                 }
                                                  }
}
                                                  %>                       
							</div>
						</div>
						<div class="col-lg-6">
							<div class="panel panel-default">
								<div class="panel-heading text-left">
									<h5><%= tUserDetails.get("name") %>'s file</h5>
								</div>
                                                                <% 
          List<String> result = new ArrayList<String>();
      File[] file1 = new File(UPLOAD_DIRECTORY1).listFiles();
      if(file1!=null){
     // Arrays.sort(file1, Comparator.comparing(File::lastModified));
      
           for (File file: file1) {
             if (file.isFile()) {
              result.add(file.getName());
               %>  
                     <div class="panel-body text-left">
									<div class="row  ">
										<div class="col-lg-6">               
                                         <a href="DownloadFile?filename=<%=file.getName()%>">
                                          <%=file.getName() %>
                                        </a>									
                                        </div>
										
									</div>
								</div>
                                              <%
                                                 }
                                                  }
      }
                                                  %>  
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	<hr>
	
	<!--footer-->
</body>
<%       
    // }
     }else{
        session.setAttribute("msg", "Plz login First!");
        response.sendRedirect("home.jsp");
    }
%>
</html>