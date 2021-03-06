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
        session.setAttribute("address",null);          //profile page pr nhi hai, overloading km krne ke liye
    %>
<!DOCTYPE html>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<section>
                             <%
                             DbConnect db = new   DbConnect();
                             
                             ArrayList<String> alluserdetail= db.users((String)users.get("email"));
                        Iterator<String> i=alluserdetail.iterator();
                        while(i.hasNext()){
                       ArrayList al= db.getname((String)users.get("email"));
                       Iterator ii=al.iterator();
                       while(i.hasNext()){
                           HashMap peopleDetail=(HashMap)ii.next();
                        %>
			<div class="row">
				<div class="col-lg-3">
					<img src="GetPhoto?Email=<%=i.next()%>&Gender=<%= peopleDetail.get("gender") %>" width="135" height="150">
				</div>
				<div class="col-lg-7">
						<div class="form-group">
							<label for="email" class="control-label">Name: <font color="grey"><span id="name"><%= peopleDetail.get("name")%></span></font></label><br>
							<label for="name" class="control-label">Email:<font color="grey"> <%= peopleDetail.get("email")%></font></label><br>
1						</div>
				</div>
				<form action="talk.jsp" class="form-horizontal">
					<div class="col-lg-2">
						<div class="form-group">
						</br>
						</br>    
                                                <%--Talk Button pr jisse bat krna hai uska Email Id hidden roop me bheja--%>
                                                        <input type="hidden" name="temail" value="<%= peopleDetail.get("email")%>"/>
							<button type="search" class="btn btn-primary">Talk</button>
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