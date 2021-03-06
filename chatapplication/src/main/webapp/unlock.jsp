<%@page import="org.json.simple.JSONArray"%>
<%@page import="java.util.List"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.Map"%>
<%@page import="db.DbConnect"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%
    HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya
     if(users!=null){                                                    //to sb jgah jha jha dikhana hai dikhao
       //searchprocess.java se aaya hu
      session.setAttribute("EveryoneDetails",null);  //in dono ko null isliye kiya kyoki in dono session ki zaroort 
        session.setAttribute("address",null);    
      //profile page pr nhi hai, overloading km krne ke liye
List<String> name = new ArrayList<>();
 
    %>
<!DOCTYPE html>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
  <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>

</head>
<body>

<section>
                             <%
                            
                             DbConnect db = new   DbConnect();
                             
                             ArrayList<String> alluserdetail= db.blockeduserlist((String)users.get("email"));
                        Iterator<String> i=alluserdetail.iterator();
                        while(i.hasNext()){    
                        	
                        	String mail=i.next();
                       ArrayList al= db.getname(mail);
                       Iterator it=al.iterator();
                       while(it.hasNext()){
                          HashMap peopleDetail=(HashMap)it.next();
                          
                            %>
			<div class="row">
				<div class="col-lg-3 profile-userpich">
					<img src="GetPhoto?Email=<%=mail%>&Gender=<%= peopleDetail.get("gender") %>">
				</div>
				<div class="col-lg-7">
						<div class="form-group">
							<label for="email" class="control-label">Name: <font color="grey"><span id="name"><%= peopleDetail.get("name")%></span></font></label>
							
							<label for="name" class="control-label">Email:<font color="grey"> <%= peopleDetail.get("email")%></font></label><br>
							<div id="#<%= peopleDetail.get("name") %>"></div>
							
				</div>
				</div>
				<form action="unblock" class="form-horizontal">
					<div class="col-lg-2">
						<div class="form-group">
						  
                                                <%--Talk Button pr jisse bat krna hai uska Email Id hidden roop me bheja--%>
                                                        <input type="hidden" name="temail" value="<%= peopleDetail.get("email")%>"/>
							<button type="search" class="btn btn-success">unblock</button>
						</div>
					</div>
				</form>
			</div>
			<hr>
		      <%
                       }
                        }
                       
                        %>
	
		</section>
</body>


</html>
<%       
    }else{
        session.setAttribute("msg", "Plz login First!");
        response.sendRedirect("home.jsp");
    }
%>