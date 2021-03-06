<%@page import="java.io.IOException"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import=" java.io.File"%>
<%
   HashMap users=(HashMap)session.getAttribute("userData");             //register aur login ke dwara  session me dali gyi value get kiya , pofile wale page se lekar jitne page hai sb me ye session rhega, perticular person ka hai
     if(users!=null){    
   String temail=request.getParameter("temail");   
   session.setAttribute("temail", temail);
   //fetch from peoplesearch.jsp talk button se
        db.DbConnect db=new db.DbConnect();                       //connection bnaya
        java.util.HashMap tUserDetails=db.getPeopleByEmail(temail);    
        //jo krna hai so kro
        //if(tUserDetails!=null){
        	String s1 = (String)tUserDetails.get("name");
        	session.setAttribute((String)users.get("name"),s1);  
        	db.deleteoffilnemsg(temail, (String)users.get("email"));

        	%>



<!DOCTYPE html>
<html lang="en">
   <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Chat Box9</title>
	<link rel="stylesheet" type="text/css" href="css/loginStyle.css">
	<link rel="stylesheet" href="css/chatbox.css">
	
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <script src="js/reconnectwebsocket.js"></script>
  <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
	<style type="text/css">
	.profile-userpic img {
  float: none;

  margin-left: 0px;
  width:120px;
  height: 120px;
  
  -webkit-border-radius: 60% !important;
  -moz-border-radius: 50% !important;
  border-radius: 50% !important;
}
	
	</style>
	<script>
	
	var webSocket = new  WebSocket("ws://localhost:8080/chatapplication/sendmsg");
	
       	webSocket.onopen = function() {
       console.log("open");
       		webSocket.send("<%=users.get("name")%>");
	  };
	  
	  webSocket.onerror = function(err) {
	   
	  };
	  var count=0;
	  webSocket.onmessage = function (message) {
		 $("#filename").load("hi.jsp");
		 if(message.data == "new_user_loggedin"){
			  loadOnlineUsers();
			  return;
		  }
		  var jsonData = JSON.parse(message.data);
		  console.log(jsonData)
	  var x= "\"<%=(String)tUserDetails.get("name")%>\"";
	  console.log(x);
	  var jsonmessage=JSON.stringify(jsonData.senderName);
	  if(jsonmessage==x){
		  $('.ex3').scrollTop($('.ex3')[0].scrollHeight);
		  var replysgs="recive:"+jsonData.message;
		 // alert("abcd");
	     // var oldChatHistorys = $("#chatHistorys").html();
	        $("#chatHistorys").append("<div class='mt-2 'style='line-height: 2'>"+
                 	"<br><div class='float-left shadow bg-success rounded'><span class='pl-2 pr-5'>"+replysgs+"</span><br>"+
         	"</div></div>");
	       
	  }
	 
	  else{
	  
	 // console.log(jsonmessage)
	  
		  if(jsonData.message != null)
		  {
			  if($("#"+jsonData.senderName).find("#chatHistory").val() != null){
			  }
			  else
			  {
				  startChat(jsonData.senderName);
			  }
			  
			  $("#"+jsonData.senderName).show();
			  
			  var replysg = jsonData.senderName+": "+jsonData.message;
			 // var oldChatHistory = $("#"+jsonData.senderName).find("#chatHistory").val();
			  $("#"+jsonData.senderName).find("#chatHistory").append(replysg+"\n");
			  
		 
		  }
	  }
	  };

  webSocket.onclose = function(message) {
	 
	};   
	

		function loadOnlineUsers(event){
		//	$("#boxTextDiv").load("peoplesearch.jsp");
		}
		
		
		
		
		$(document).ready(function(){
			$("#filename").load("hi.jsp");
			$('.ex3').scrollTop($('.ex3')[0].scrollHeight);
			
			$("#uploadBtn").on("click", function() {
				var url = "UploadDownloadFileServlet";
				var form = $("#sampleUploadFrm")[0];
				var data = new FormData(form);
				/* var data = {};
				data['key1'] = 'value1';
				data['key2'] = 'value2'; */
				$.ajax({
					xhr : function() {
						var xhr = new window.XMLHttpRequest();

						xhr.upload.addEventListener('progress', function(e) {

							if (e.lengthComputable) {

								console.log('Bytes Loaded: ' + e.loaded);
								console.log('Total Size: ' + e.total);
								console.log('Percentage Uploaded: ' + (e.loaded / e.total))

								var percent = Math.round((e.loaded / e.total) * 100);

								$('#progressBar').attr('aria-valuenow', percent).css('width', percent + '%').text(percent + '%');

							}

						});

						return xhr;
					},
				
					type : "POST",
					encType : "multipart/form-data",
					url : url,
					cache : false,
					processData : false,
					contentType : false,
					data : data,
					success : function(msg) {
						var response = JSON.parse(msg);
						var status = response.status;
						if (status == 1) {
							
							sendFile();
				            
				            alert("File has been uploaded successfully");
				            location.reload(true);
						} else {
							alert("Couldn't upload file");
						}
					},
					error : function(msg) {
						alert("Couldn't upload file");
					}
				});
				
			});
			
			var eventSource = new EventSource("ShoutServlet");

			eventSource.onmessage = function(event) {
			var ans;
			//document.getElementById('foo').innerHTML = event.data;
			var i;
			var jsonData = JSON.parse(event.data);
			
			
			
				var username="<%=tUserDetails.get("name")%>";
				
			if (jsonData.hasOwnProperty(username)) { 
				
				  $("#online").html("online");
				
				} else { 
					
					  $("#online").html("offline");
					
			} 
			//.innerHTML = ans; 
			
			};
	        
		});
		
		
		
		
		
		
		function startChat(userName1) {
			var userName;
			if(userName1.length > 0){
				userName = userName1;
				//alert("userName1:"+userName);
			}
			else
			{
				
					
			userName="<%=tUserDetails.get("name")%>"
				//userName = $("#boxTextDiv option:selected").text();
				//alert("userName:"+userName);

			}
			
			  
        
			if( $( "#"+userName).length ){
				if(! $( "#"+userName).is(":visible")  ){
					$( "#"+userName).show();
					
	
					 
				}
				return;
			}
			
             //main window

			//create chat window where id base div is user name.
			var chatWindow = "<div id='"+userName+"' class='chatDiv'>"+
			"<div class='chatDivTitle'>"+userName+" <a href='#' style='float:right' class='chatClose'>close</a></div>"+
				"<textarea rows='12' id='chatHistory' class='chatHistory' readonly='true' cols='30' ></textarea><br>"+
				"<textarea rows='3' id='chatBox' class='chatBox' cols='30' ></textarea>"+
			"</div>";
			$(".allChatDivs").append(chatWindow);
			
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
						 senderMsg.reciverid="<%=temail%>";;
				   var jsonMsgString= JSON.stringify(senderMsg);
						   
						   
					//var oldChatHistory = $("#"+userName).find("#chatHistory").val();
					$("#"+userName).find("#chatHistory").append("me: <br>"+message+"<br>");
					
					if(message!=null){
					
					webSocket.send(jsonMsgString);
					}
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
		
		function sendFile() {
			 var senderMsg = new Object();
			 senderMsg.message = " file";
			 senderMsg.senderId  ="<%=users.get("email")%>";
			 senderMsg.senderName ="<%=users.get("name")%>";
			 senderMsg.sendTo = "<%=tUserDetails.get("name")%>";
			 senderMsg.reciverid="<%=temail%>";
	         var jsonfileString= JSON.stringify(senderMsg);
			webSocket.send(jsonfileString);
			$("#filename").load("hi.jsp");
			}
		function sendmessage() {
			$('.ex3').scrollTop($('.ex3')[0].scrollHeight);
			
			 var message = document.getElementById("msg").value;
			 var senderMsg = new Object();
					 senderMsg.message = message;
					 senderMsg.senderId  ="<%=users.get("email")%>";
					 senderMsg.senderName ="<%=users.get("name")%>";
					 senderMsg.sendTo = "<%=tUserDetails.get("name")%>";
					 senderMsg.reciverid="<%=temail%>";
			   var jsonMsgString1= JSON.stringify(senderMsg);
			if(message!=""){
				webSocket.send(jsonMsgString1);
				var replysg=document.getElementById("msg").value;
			       var oldChatHistory = $("#chatHistorys").html();
			        
			       $("#chatHistorys").html(oldChatHistory+"<div class='mt-2 'style='line-height: 2'>"+
	                     	"<br><div class='float-right shadow bg-info rounded'><span class='pl-5 pr-2'>"+replysg+"</span><br>"+
	                     	"</div></div>");			}
				  
			        
			       
				document.getElementById('msg').value = ''
			}
		
	
		
		
	</script> 
 
  </head>
  
  <body  data-target="#my-navbar">

	<nav class="nav nav-pills nav-fill "style="background-color:#022b0d">
	
  <a class="nav-item nav-link text-white" href="profile.jsp">Friendzone</a>
  <a class="nav-item nav-link text-white" href="contact.html">Aboutus</a>
 
  

  <a class="nav-item nav-link text-white">Welcome: <%= users.get("name")%></a>
  <a class="nav-item nav-link text-white" href="profile.jsp">Home</a>
  <a class="nav-item nav-link text-white" href="logout" tabindex="-1" aria-disabled="true">Logout</a>
    <form action="blockperson">
  <input type="submit" value ="block" class=" nav-link text-blackte">
  
  </form>
</nav>
	             
	</br>
		<div class="container">
			<div class="row" >
                             
				<div class="col-md-4 profile-userpic">
					<img src="GetPhoto?Email=<%=tUserDetails.get("email")%>&Gender=<%=tUserDetails.get("gender") %>" width="120" height="150">
					 <div id="online"></div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
					<p for="email" class="control-label">Name: <font color="grey"><%=tUserDetails.get("name")%></font></p>
				       <label for="gender" class="control-label">Gender: <font color="grey"><%=tUserDetails.get("gender")%></font></label><br>
					<label for="phone" class="control-label">Phone: <font color="grey"><%=tUserDetails.get("phone")%></font></label><br>
					</div>
				</div>
				<div class="col-md-4">
					<div class="form-group">
	                                <label for="name" class="control-label">Email:<font color="grey"> <%=tUserDetails.get("email")%></font></label><br>
					<label for="dob" class="control-label">Date of Birth: <font color="grey"><%=tUserDetails.get("dob")%></font></label><br>
					<label for="address" class="control-label">Address: <font color="grey"><%=tUserDetails.get("area")%>,<%=tUserDetails.get("city")%>,<%=tUserDetails.get("state")%></font></label><br>
					</div>
				</div>
			</div>
		</div>
         



<div class="row ">
  <div class="col-lg-6 mb-3">
  <div class="container-fluid h-100">
			<div class="  h-100">
				
				<div class="chat">
					<div class="">
					  <div class="ex3" style=" background-color:;
                           height: 270px;
                          ;
    overflow-y:scroll;">
						<div class="card-body "style="line-height: " id="<%=tUserDetails.get("name")%>" >
						<div class="mb-5">
							<% 

                     try {
                    	  String sendername=(String)tUserDetails.get("name");
                    	 //String sendername="<script>document.writeln(v)</script>";
                    	// String webAppRoot ="/home/speaosol/backupcwp/";
                     String webAppRoot = System.getProperty( "catalina.base" );
                     String s = File.separator;
                    // String configDir =  "/home/speaosol/public_html/test/" ;
                    String configDir = webAppRoot + s + "wtpwebapps" +s+"ROOT"+s ;
                     String txtFilePath = configDir+users.get("email")+s+sendername+".txt" ;
                     //out.print("value="+sendername);

                     BufferedReader reader = new BufferedReader(new FileReader(txtFilePath));
                     //   String line = reader.readLine();
                     String data;
                  
                     while((data= reader.readLine())!= null)
                     	
                     {
                    	 if (data.length() == 0)
                    		 continue;
                    	 
                    	 if(data.charAt(0)=='y'){
                     	%>
                     	<div class="mt-2 "style="line-height: 2">
                     	<br><div class="float-right shadow bg-info rounded"><span class="pl-5 pr-2"><%=data.substring(4) %></span><br></div>
                     	</div>
                     	<%
                    // out.println(data+"<br>");
                    	 }
                    	 else{
                    		 %>
                    		 <div class="mt-2 "style="line-height: 2">
                          	<br><div class="float-left  shadow bg-success rounded"><span class="pl-2 pr-5 "><%=data %></span><br></div>
                          	</div>
                          	<% 
                    	 }
                     }

                     } catch (IOException e) {

                     e.printStackTrace();
                     }
                     %>
                     
                     <div  id="chatHistorys" ></div><br>
                     </div>
                     </div>
							</div>
							</div>
						<br>
						</div>
						<div class="">
							<div class="input-group">
								
								<textarea name="" id="msg" type="text" class="form-control type_msg text-dark border border-primary" placeholder="Type your message..."></textarea>
								<button type="submit" class="btn btn-primary" onclick="sendmessage()">Send</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		

          <div class="col-lg-6 ">  
          
                           <div class="ex3  border border-primary" style=" background-color:;
                           height: 270px;
                           overflow: auto;">
                           <div id="filename">	
                    
                    
                  	</div>
                  	</div>
                  	<br>
<!--                   	<form action="SendMessage" data-toggle="validator" method='post' enctype='multipart/form-data' class="form-horizontal"> -->
<!--                    <div class="row"> -->
<!--                                 <div class="col-lg-4"> -->
                   
<!-- 									<input type="file" name="ufile" class="form-control" id=""/> -->
<!-- 								</div> -->
<!-- 								<div class="col-lg-2"> -->
<%--                                                                          <input type="hidden" name="temail" value="<%= temail%>"/> --%>
<!-- 									<button type="submit" class="btn " onclick="sendFile()">Send</button> -->
<!-- 									</div>  -->
<!-- 									</div>      -->
<!-- 	

								</form>  -->
								
								<div class="row">
<div class="col-sm-10">
			<form id="sampleUploadFrm" method="POST" action="#" enctype="multipart/form-data">
				
						 <input type="file" name="file" class="form-control"placeholder='Choose a file...' /> 
						
				
			</form>
		</div>
									<button type="button" class="btn btn-primary pull-right"id="uploadBtn">Submit</button>
		
	        </div>   
	        <div class="progress">
  <div id="progressBar" class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
    0%
  </div>
</div> 	  
	</div>
</div>

<div class="allChatDivs">	
	</div>

	<%@ include file="footer.jsp" %>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/jquery-2.2.2.min.js"></script>
    <script src="js/validator.js"></script>
    <script type="text/javascript">


</script>
  </body>
</html>
<%       
   
     }else{
        session.setAttribute("msg", "Plz login First!");
        response.sendRedirect("home.jsp");
    }
%>
       