<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%
    HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya , pofile wale page se lekar jitne page hai sb me ye session rhega, perticular person ka hai
     if(users!=null){    
 java.util.ArrayList<java.util.HashMap> allUserDetails=(java.util.ArrayList<java.util.HashMap>)session.getAttribute("EveryoneDetails");
        HashMap address=(HashMap)session.getAttribute("address");
        if(allUserDetails!=null){
%>





<!DOCTYPE html>
<html lang="en">
   <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>friendzone</title>


    <link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/talk.css" rel="stylesheet">

  
  </head>
 
  <body data-spy="scroll" data-target="#my-navbar">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="profile.jsp">friendzone</a>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
                                    <li><div class="navbar-text"><p>Welcome: <%= users.get("name") %> </p></div></li>
					<li><a href="profile.jsp">Home</a></li>
					<li><a href="logout">Logout</a><li>
				</ul>			
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="panel panel-default text center">
			<div class="panel-heading text-center">
				<h3>Search Results for:<%= address.get("state")%>/<%= address.get("city")%>/<%= address.get("area")%></h3>
			</div>
		</div>
	</div>
	</br>
	</br>
		<div class="container">
			<section>
                             <%
                        Iterator i=allUserDetails.iterator();
                        while(i.hasNext()){
                            HashMap peopleDetail=(HashMap)i.next();
                        %>
			<div class="row">
				<div class="col-lg-3">
					<img src="GetPhoto?Email=<%=peopleDetail.get("email")%>&Gender=<%= peopleDetail.get("gender") %>" width="135" height="150">
				</div>
				<div class="col-lg-7">
						<div class="form-group">
							<label for="email" class="control-label">Name: <font color="grey"><span id="name"><%= peopleDetail.get("name")%></span></font></label><br>
							<label for="name" class="control-label">Email:<font color="grey"> <%= peopleDetail.get("email")%></font></label><br>
							<label for="gender" class="control-label">Gender: <font color="grey"><%= peopleDetail.get("gender")%></font></label><br>
							<label for="dob" class="control-label">Date of Birth: <font color="grey"><%= peopleDetail.get("dob")%></font></label><br>
							<label for="phone" class="control-label">Phone: <font color="grey"><%= peopleDetail.get("phone")%></font></label><br>										
						
						</div>
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
                        %>
		</div>
		</section>
	</div>
	</br>
	<!--footer-->
	<%@ include file="footer.jsp" %>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
<%       
        }else{
         session.setAttribute("msg", "Search Again!");
            response.sendRedirect("profile.jsp");
    }
        }else{
        session.setAttribute("msg", "Plz login First!");
        response.sendRedirect("home.jsp");
    }
%>