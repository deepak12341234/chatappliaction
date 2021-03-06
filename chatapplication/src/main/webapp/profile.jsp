<%@page import="db.DbConnect"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%
    HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya
     if(users!=null){                                                    //to sb jgah jha jha dikhana hai dikhao
       //searchprocess.java se aaya hu
      session.setAttribute("EveryoneDetails",null);  //in dono ko null isliye kiya kyoki in dono session ki zaroort 
        session.setAttribute("address",null);          //profile page pr nhi hai, overloading km krne ke liye
%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>friendzone</title>
    <!-- Bootstrap -->
    <link href="css/profile.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
	<link href="css/custom.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/loginStyle.css">
		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	 <script src="js/reconnectwebsocket.js"></script>
	<script type="text/JavaScript" src='js/state.js'></script>
	<script type="text/javascript">
     $(document).ready(function(){
	  $("#chathistry").load("friends.jsp");
	});
  </script>
  <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		
	<script>
	

	var webSocket = new  WebSocket("ws://localhost:8080/chatapplication/sendmsg");

		
       	webSocket.onopen = function() {
       		
       	webSocket.send("<%=users.get("name")%>");
       
	  };
	  
	  webSocket.onerror = function(err) {
	      
	  };
	  var count=0;
	    
	  webSocket.onmessage = function (message) {
		 
		  if(message.data == "new_user_loggedin"){
			  
			  loadOnlineUsers();
			  return;
		  }
		  var jsonData = JSON.parse(message.data);
		  var v= jsonData.senderName;
		  
		  if(jsonData.message != null)
		  {
			  $('#passwordsNoMatchRegister').show();
			  
			  var replysg = jsonData.senderName+": "+jsonData.message;
			  if($("#"+jsonData.senderName).find("#chatHistory").val() != null){
			  }
			  else
			  {
				  
				  startChat(jsonData.senderName,jsonData.senderId);
			  }
			 
			  $("#"+jsonData.senderName).show();

			   var oldChatHistory = $("#"+jsonData.senderName).find("#chatHistory").val();
			   $("#"+jsonData.senderName).find("#chatHistory").val(oldChatHistory +replysg+"\n");
			  
		  }
	  };

	  webSocket.onclose = function(message) {

		 
		};	  
				  
		function loadOnlineUsers(event){
			$("#boxTextDiv").load("peoplesearch.jsp");
		}
		
		$(document).ready(function(){
			$("#boxTextDiv").load("peoplesearch.jsp");
			var variable = $('#boxTextDiv').html();
			$("#textSubmit").click(loadOnlineUsers);
			$('#boxTextDiv').dblclick(startChat);
		});
		
		
		
		
		//............................................................
		
		
		function startChat(userName1,reciverid1) {
			
			var userName;
			var reciverid;
			if(userName1.length > 0){
				//chat window after get message
				userName = userName1;
				//alert("userName1:"+userName);
				reciverid=reciverid1;
			}
			else
			{
				//click karne k baad jochat window khulega
			//username=document.getElementById("name").value;
				//userName = $("#boxTextDiv option:selected").text();
				//alert("userName:"+userName);

			}
			
			
			if( $( "#"+userName).length ){
				if(! $( "#"+userName).is(":visible")  ){
					$( "#"+userName).show();
				}
				return;
			}
			
			//create chat window where id base div is user name.
			var chatWindow = "<div id='"+userName+"' class='chatDiv'>"+
			"<div class='chatDivTitle'>"+userName+" <a href='#' style='float:right' class='chatClose'>close</a></div>"+
				"<textarea rows='13' id='chatHistory' class='chatHistory' readonly='true' cols='30' ></textarea><br>"+
				"<textarea rows='2' id='chatBox' class='chatBox' cols='30' ></textarea>"+
			"</div>";
			$(".allChatDivs").append(chatWindow);
		    
			 //event on close button for newly created chat window
			$("#"+userName).find(".chatClose").click(function(event){
				$(this).closest(".chatDiv").hide();
			});
			 
			//set event keyup on chat box text area of this chat window
			$("#"+userName).find("#chatBox").keyup(function(event){
				
				 var keycode = (event.keyCode ? event.keyCode : event.which);
				if(keycode == '13'){
					var message=$(this).val();
						
				 var senderMsg = new Object();
						 senderMsg.message = message;
						 senderMsg.senderId  ="<%=users.get("email")%>";
						 senderMsg.senderName ="<%=users.get("name")%>";
						 senderMsg.sendTo = userName;
						 senderMsg.reciverid = reciverid;
						 
						 
				         var jsonMsgString= JSON.stringify(senderMsg);
						   
						   
					var oldChatHistory = $("#"+userName).find("#chatHistory").val();
					$("#"+userName).find("#chatHistory").val(oldChatHistory +"me: "+message);
					
					//alert(jsonMsgString);
					webSocket.send(jsonMsgString);
					
					$(this).val('');
					
					//set scroller down chatHistory text area when somthing typed and appended to the history
					var $charHistory = $("#"+userName).find("#chatHistory");
					$charHistory .scrollTop($charHistory.prop("scrollHeight"));

			}
				//if esc pressed in the chat box then hide the chat window 
				if (event.keyCode == '27') {
					$(this).closest(".chatDiv").hide();
				} 
			
			});
			
		};
		
	</script> 
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
					<li><div class="navbar-text"><p>Welcome: <%= users.get("name") %></p></div></li>
					<li><a href="profile.jsp">Home</a></li>
					<li><a href="blockedpeople.jsp">blocked people</a></li>
					<li><a href="logout">Logout</a><li>
				</ul>			
			</div>
		</div>
	</nav>
	
		<div class="container-fluid">
		
			<div class="row">
			
				 <div class="col-sm-3">
      		<div class="profile-sidebar">
				<!-- SIDEBAR USERPIC -->
				<div class="profile-userpic">
						<img src="GetPhoto?Email=<%=users.get("email")%>&Gender=<%= users.get("gender") %>">   
				</div>
				
				<div class="profile-usertitle">
					<div class="form-group">
							<label for="email" class="control-label">Name: <font color="grey"><%= users.get("name") %></font></label>
						</div>
					
				</div>
				<!-- END SIDEBAR USER TITLE -->
				<!-- SIDEBAR BUTTONS -->
				<div class=" form-group">
								<a href="editprofile.jsp" class="btn  btn-info">Edit Profile</a>
                                <a href="changepassword.jsp" class="btn btn-success">Change Password</a>
															
							</div>
				<!-- END SIDEBAR BUTTONS -->
				<!-- SIDEBAR MENU -->
				<div class="profile-usermenu">
					<ul class="nav">
						<li class="active">
														<label for="name" class="control-label">Email:<font color="grey"> <%= users.get("email")%></font></label>

						</li>
						<li>
														<label for="gender" class="control-label">Gender: <font color="grey"><%= users.get("gender") %></font></label>

						</li>
						<li>
														<label for="dob" class="control-label">Date of Birth: <font color="grey"><%= users.get("dob") %></font></label>

						</li>
						<li>
														<label for="state" class="control-label">Address: <font color="grey"><%= users.get("area") %>,<%= users.get("city") %>,<%= users.get("state") %></font></label>

						</li>
					</ul>
				</div>
				<!-- END MENU -->
			</div>
    </div>
    
				<div class="col-sm-5">
				                            							<div class="alert alert-info col-lg-10 col-sm-offset-2" role="alert">Search People</div>
				
                                     <%
            String msg=(String)session.getAttribute("msg");               //No data found ke liye
            if(msg!=null)  
            {
        %>
        <div class="panel panel-danger">
            <div class="panel-heading text-center">
                <p><%=msg%></p>
            </div>
        </div>
        <%
            session.setAttribute("msg", null);
            }
        %>
					
                            
						
							<form action="SearchProcess" class="form-horizontal">
								<div class="form-group">
									<label for="state" class="col-lg-3 control-label">State:</label>
									<div class="col-lg-9">
										<select name="state" class="form-control" id="listBox" onchange='selct_district(this.value)'>
											
										</select>
									</div>
								</div><!--end form group-->
								<div class="form-group">
									<label for="city" class="col-lg-3 control-label">City:</label>
										<div class="col-lg-9">
											<select name="city" class="form-control" id='secondlist'>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="area" class="col-lg-3 control-label">Area:</label>
									<div class="col-lg-9">
										
										<input type="text" name="area" class="form-control" id="area" placeholder="Enter your Area" />
									</div>
								</div>
								<div class="form-group">
									<div class="col-lg-10 col-sm-offset-2">
										<button type="search" class="btn btn-primary" >Search</button>
									</div>
								</div>
							</form>
					
				</div>
				<div class="col-sm-4">
				<div class="alert alert-success" role="alert">your friends</div>
                                <div id="chathistry"></div>
    </div>
			</div>
			
		
	</div>
	<div class="allChatDivs">
	<div id="chathistory">	
	</div>	
	</div>
	<!--footer-->
	<%@ include file="footer.jsp" %>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    	
  </body>
</html>
<%       
    }else{
        session.setAttribute("msg", "Plz login First!");
        response.sendRedirect("home.jsp");
    }
%>