package servlets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParserFactory;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import db.DbConnect;


@ServerEndpoint("/sendmsg")
public class sendmsg {
  
	static Set<Session> chatRoomUsers = Collections.synchronizedSet(new HashSet<Session>());
	String reciverid;
	String	senderId;
//	static String currUserName;
	
	static Map<String,Session> chatRoomUsersMap= Collections.synchronizedMap(new HashMap<String,Session>());;
	static Map<String,String> status= Collections.synchronizedMap(new HashMap<String,String>());;
	Map<Session, String> map = new HashMap<Session, String>();
	static int userCount=0;
	static Map<String, String> msgJsonMap;
	private boolean initial= true;
	User u = new User();
	
	int count=0;
	@OnOpen
	public void onOpen(Session userSession) throws IOException{
		userCount++;
		chatRoomUsers.add(userSession);
		initial = true;
		

		broadcastAMsg("new_user_loggedin");
	
	}
	
	@OnMessage
	public void handleMessage(String msgJsonStr, Session userSession) throws Exception    
  	{
		if(initial){
			//User u = new User(); 
			
			System.out.println("User is connected with name : "+ msgJsonStr);
		String currUserName = msgJsonStr; // First time it will send user name - deepak
			status.put(currUserName,"online");
			chatRoomUsersMap.put( currUserName,userSession);
			map.put(userSession,msgJsonStr);
			u.setData(status);
			System.out.println(u.getData());
			System.out.println(status);
			initial = false;
		}
		else{
			msgJsonMap = parseJsonMsg(msgJsonStr);
			String message =  msgJsonMap.get("message");
			 senderId =  msgJsonMap.get("senderId");
			String senderName =  msgJsonMap.get("senderName");
			String sendTo = msgJsonMap.get("sendTo");
			System.out.println(sendTo);
             reciverid= msgJsonMap.get("reciverid");
			/*
			 * try { Message newMsg = new Message(message,senderId);
			 * ChatDbOperations.sendMessege(newMsg); } catch (SQLException | ChatDbFailure
			 * e) { e.printStackTrace(); }
			 */
			foldercreate( sendTo, message,senderId);
			writeincomingmsg(senderName, message, reciverid);
			
			DbConnect db ;
			
				db = new DbConnect();
			
			
			db.chathistory(senderId, reciverid);
			db.chathistory(reciverid, senderId);
			int block=db.blockmessage(reciverid, senderId);
			
			System.out.println("hi");
			Session receiverSession = chatRoomUsersMap.get(sendTo);
			
		
			                                
				try {
					if((receiverSession!=null)&&block==0) {
					receiverSession.getBasicRemote().sendText(buildJSONData(sendTo,message,senderId,senderName,reciverid));
					}
					else {
						try {
						//offlinemessage++;
						db.insertoffilnemsg( senderId, reciverid);
						System.out.println("inserted");
						}catch (Exception e) {
							System.out.println(e);
						}
					}
					
				} catch (IOException e) {
					db.insertoffilnemsg( senderId, reciverid);
					System.out.println("inserted");
				}
				//System.out.println(receiverSession);
			
			
				//DbConnect db = new DbConnect();
				
					//db.insertoffilnemsg(offlinemessage, reciverid, senderId);
				
				//offlinemessage++;
			

		}
		 
		}
    
  	private String buildJSONData(String sendTo, String message, String senderId, String senderName,String reciverid) {
		JsonObject jsonObj = (Json.createObjectBuilder()
				.add("sendTo",sendTo)
				.add("message",message)
				.add("senderId",senderId)
				.add("reciverid",reciverid)
				.add("senderName",senderName)).build();
		         
		StringWriter srtingWriter = new StringWriter();
		try(JsonWriter jsonWriter = Json.createWriter(srtingWriter))
		{
			jsonWriter.write(jsonObj);
		}
	//	System.out.println("Here is ready "+srtingWriter.toString());
		return srtingWriter.toString();
	}
	//@SuppressWarnings("unlikely-arg-type")
	//@SuppressWarnings("unchecked")
	@OnClose
  	public String handleClose( Session userSession) throws Exception
  	{
		
		  String name=map.get(userSession); // broadcastAMsg(name+"offline");
		  if(name!=null) { status.remove(name); u.setData(status);
		  System.out.println(u.getData()); }
		 
		userCount--;
		//System.out.print(userCount);
  		chatRoomUsers.remove(userSession);
  		System.out.print("Connectin is closed");
  		
  		return("a_user_leaving");
  	}
  	@OnError
  	public void handleError(Throwable e) throws Exception
  	{
  		DbConnect db ;
		
		db = new DbConnect();
  		db.insertoffilnemsg( senderId, reciverid);
		System.out.println("inserted");
  		System.out.println("hrloo");
  		//e.printStackTrace();
  	}
  	
  	private Map<String,String> parseJsonMsg(String message){
	  	
  		JsonReader reader = Json.createReader(new StringReader(message));
  		JsonObject inf = reader.readObject();
  		
  		System.out.println("Reading "+inf);
  		JsonParserFactory factory = Json.createParserFactory(null);
	  	JsonParser parser = factory.createParser(new StringReader(message));
	  	Map<String, String> map = new HashMap<String, String>();
	  	
	  	while (parser.hasNext()) {
	  		Event event = parser.next();

		  	switch (event) {
		    case KEY_NAME: 
		    	if(parser.getString().equals("message"))
		    	{
	  	    		event = parser.next();
	  	    		String msgStr = parser.getString();
	  	    		//System.out.print("message =" + parser.getString());
	  	    		
					map.put("message",msgStr);
	  	    	}
		    	else if(parser.getString().equals("senderId"))
		    	{
	  	    		event = parser.next();
	  	    		String msgStr = parser.getString();
	  	    	//	System.out.print("senderId =" + parser.getString());
	  	    		
					map.put("senderId",msgStr);
	  	    	}
		    	else if(parser.getString().equals("senderName"))
		    	{
	  	    		event = parser.next();
	  	    		String msgStr = parser.getString();
	  	    		//System.out.print("senderName =" + parser.getString());
	  	    		
					map.put("senderName",msgStr);
	  	    	}
	  	    	else if(parser.getString().equals("sendTo"))
		    	{
	  	    		event = parser.next();
	  	    		String msgStr = parser.getString();
	  	    		//System.out.print("sendTo =" + parser.getString());
	  	    		
					map.put("sendTo",msgStr);
	  	    	}
	  	    	else if(parser.getString().equals("reciverid"))
		    	{
	  	    		event = parser.next();
	  	    		String msgStr = parser.getString();
	  	    		//System.out.print("sendTo =" + parser.getString());
	  	    		
					map.put("reciverid",msgStr);
	  	    	}
		    	break;
		   default:
			   break;
	  	  }
	  	 
	  	}
	  	 return map;
  	}
  	

  	
  	private void broadcastAMsg(String name) throws IOException{
  		Iterator<Session> iterator = chatRoomUsers.iterator();
		while(iterator.hasNext()){
			iterator.next().getBasicRemote().sendText(name);
		}
  	}
  	
 
  	 public void foldercreate(String rname,String msg,String id) throws IOException {
  		 String webAppRoot = System.getProperty( "catalina.base" );
			String s = File.separator;
			String configDir = webAppRoot + s + "wtpwebapps" +s+"ROOT"+s ;
  		//String configDir = "/home/speaosol/public_html/test/";
  		System.out.println(configDir);
	    	File  f2 = new File(configDir+id);
	    	
	    	File f3 = new File(f2,rname+".txt");
	    	
	    	
	    		if(!(f2.exists())) {
	    			f2.mkdirs();
	    			
	    			}
	    		if(!(f3.exists())) {
	    			try {
	    				f3.createNewFile();
	    			} catch (IOException e) {
	    			
	    				e.printStackTrace();
	    			}
	    		
	    		}
	    		PrintWriter pw = null;
	    		try {
	    			pw = new PrintWriter(new FileWriter(f3, true));
	    		} catch (IOException e) {
	    		
	    			e.printStackTrace();
	    		}
	    		
	    		
	    		pw.println("you"+"-"+msg);
	    		
	    		pw.flush();  
	            pw.close();  
	    	
	    }
	
	  public void writeincomingmsg(String sname,String msg,String reciverid) throws IOException { 
		 String webAppRoot = System.getProperty( "catalina.base" );
			String s = File.separator;
			String configDir = webAppRoot + s + "wtpwebapps" +s+"ROOT"+s ;
		 // String configDir = "/home/speaosol/public_html/test/";
		  File f2 = new File(configDir+reciverid);
	  
	  File f3 = new File(f2,sname+".txt");
	  
	  
	  if(!(f2.exists())) { 
		  f2.mkdirs();
	  
	  } 
	  if(!(f3.exists())) {
		  try {
			  f3.createNewFile(); 
		  
		  } 
		  
		  catch (IOException e) {
	  
	  e.printStackTrace(); }
	  
	  } 
	  PrintWriter pw = null; 
	  try {
		  pw = new PrintWriter(new FileWriter(f3, true)); 
		  } catch (IOException e) {
	  
	  e.printStackTrace(); }
	  
	  pw.println("recive:"+msg);
	  
	  pw.flush(); pw.close(); }
	   

	/*
	 * public void readdata() throws IOException { File f2 = new
	 * File(System.getProperty("user.home"),"logs");
	 * 
	 * File f3 = new File(f2,Supername+".txt"); try { BufferedReader br = new
	 * BufferedReader(new FileReader(f3)); String line = br.readLine(); while
	 * (line!=null) { Label l = new Label();
	 * 
	 * l.setText(line); chatbox.getChildren().add(l); line = br.readLine();
	 * 
	 *  
	 * } br.close(); } catch (FileNotFoundException e) { Label l = new Label();
	 * 
	 * l.setText("start chat"); chatbox.getChildren().add(l);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * }
	 * 
	 * 
	 */
  	
  	
  	
  	
  	
  	
}
