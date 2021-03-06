
			 <%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="javax.websocket.OnMessage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%
    HashMap users=(HashMap)session.getAttribute("userData");  
//register aur login ke dwara  session me dali gyi value get kiya , pofile wale page se lekar jitne page hai sb me ye session rhega, perticular person ka hai
  int count=0;
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Chat Box9</title>
	<link rel="stylesheet" type="text/css" href="css/loginStyle.css">
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		 <script src="js/reconnectwebsocket.js"></script>
	<script>
	
	var webSocket = new  WebSocket("ws://localhost:8080/final-project/sendmsg");
		
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
	<body>
	<div class="alert alert-error hide" id="passwordsNoMatchRegister">
  <span>
    <p>Looks like the passwords you entered don't match!</p>
  </span>
</div>
	<div id="center">

		<div class="onlineUsersDIV" >
		
			<div  class="onlineuserlist"  id="boxTextDiv">
			</div>
			
			
		</div>
	</div>
	<div class="allChatDivs">
	<div id="chathistory">	
	</div>	
	</div>
	<script type="text/javascript">	 

	  </script>
	
	</body>
</html>
