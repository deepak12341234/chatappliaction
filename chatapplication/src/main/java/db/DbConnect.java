
package db;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import servlets.User;


public class DbConnect {
        //variable declaration
    private static Connection c;
    private PreparedStatement insertUser, checkLogin,getPhoto,searchPeople,getPeopleByEmail,sendMessage,getMessages,getFile,changepassword,
    changephoto,editprofile,offlinemessage,forgetpassword,blockedperson,blockedlist,unblock;
    
    
        //constructor me database ke driver aur all sql queris
    public DbConnect() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        c=DriverManager.getConnection("jdbc:mysql://localhost:3306/friendzone","root","9795910490");
        c.createStatement();

        insertUser=c.prepareStatement("insert into userinfo values(?,?,?,?,?,?,?,?,?,?,?)");
        checkLogin=c.prepareStatement("select * from userinfo where email=? and pass=?");
        getPhoto=c.prepareStatement("select photo from userinfo where email=? ");
         searchPeople=c.prepareStatement("select name,email,phone,dob,gender from userinfo where  state=? and city=? and email!=? and status=? and area like ? ");
        getPeopleByEmail=c.prepareStatement("select * from userinfo where email=? ");
        sendMessage=c.prepareStatement("insert into peoplemsg  (sid,rid,filename,ufile,udate) values (?,?,?,?,now())");     //sender email,receive email
        getMessages=c.prepareStatement("select * from peoplemsg where sid=? and rid=? ");
        getFile=c.prepareStatement("select ufile from peoplemsg where pid=? ");
        changepassword=c.prepareStatement("update userinfo set pass=? where email=? and pass=?");
        changephoto=c.prepareStatement("update userinfo set photo=? where email=?");
        editprofile=c.prepareStatement("update userinfo set phone=?, name=?,gender=?,dob=?, state=?,city=?, area=? where email=?");
        forgetpassword=c.prepareStatement( "select pass from userinfo where email=? ");
        blockedperson=c.prepareStatement("update privouschat set block=? where usersmail=? and loginuser=?");
        blockedlist=c.prepareStatement("select * from privouschat where loginuser=? and block =1");
        unblock=c.prepareStatement("update privouschat set block=0 where usersmail=? and loginuser=?");
        } 
        
        
       //method se kam krege sara 
        
        @SuppressWarnings("rawtypes")
		public String reg(HashMap userDetail)throws SQLException{                        
            try{
            insertUser.setString(1, (String)userDetail.get("email"));
            insertUser.setString(2, (String)userDetail.get("pass"));
            insertUser.setString(3, (String)userDetail.get("name"));
            insertUser.setString(4, (String)userDetail.get("phone"));
            insertUser.setString(5, (String)userDetail.get("gender"));
            insertUser.setDate(6, (java.sql.Date)userDetail.get("dob"));
            insertUser.setString(7, (String)userDetail.get("state"));
            insertUser.setString(8, (String)userDetail.get("city"));
            insertUser.setString(9, (String)userDetail.get("area"));
            insertUser.setBinaryStream(10, (InputStream)userDetail.get("photo"));
            insertUser.setString(11, (String)userDetail.get("status"));
            int x=insertUser.executeUpdate();
        
        if(x!=0)
               return "Success";
            else
                return "Failed";
        }
            catch(java.sql.SQLIntegrityConstraintViolationException ex){
                return "Already";
                                  }
        }
        
        
       @SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap checkLogin(String e,String p) throws SQLException{
            checkLogin.setString(1, e);
            checkLogin.setString(2, p);
            ResultSet rs=checkLogin.executeQuery();     
            if(rs.next()){                                      //database se data nikala (get) lga hai
                HashMap userDetail=new HashMap();              //collection ke form me rkha
                userDetail.put("email", rs.getString("email"));
                userDetail.put("name", rs.getString("name"));
                userDetail.put("phone", rs.getString("phone"));
                userDetail.put("gender", rs.getString("gender"));
                userDetail.put("dob", rs.getDate("dob"));
                userDetail.put("state", rs.getString("state"));
                userDetail.put("city", rs.getString("city"));
                userDetail.put("area", rs.getString("area"));  
                changeUserStatus(e,servlets.User.ONLINE);
                //photo,password nhi liya
                return userDetail;
            }else{
                return null;
            }
        }
       
       public byte[] getPhoto(String ema ){
            try{
                getPhoto.setString(1, ema);
               
                ResultSet rs=getPhoto.executeQuery();
                if(rs.next()){
                    byte[] b=rs.getBytes("photo");
                    if(b.length!=0)
                        return b;
                    else
                        return null;
                }else{
                    return null;
                }  
            }catch(Exception ex){
                return null;
            }
        }
        
       
     @SuppressWarnings({ "unchecked", "rawtypes" })
	public  java.util.ArrayList<java.util.HashMap> searchPeople(String s,String c,String a,String e) throws SQLException{     
    searchPeople.setString(1, s);           //set kiya database me
    searchPeople.setString(2, c);
    searchPeople.setString(3, e);
    searchPeople.setString(4, User.ONLINE);
    searchPeople.setString(5, "%"+a+"%");
    ResultSet r=searchPeople.executeQuery();

            //jitne log us area me aayenge sbka data get kr liya
    java.util.ArrayList<java.util.HashMap> EveryBody=new java.util.ArrayList();
        
        while(r.next()){
        java.util.HashMap onlyone=new java.util.HashMap();
       onlyone.put("email",r.getString("email"));
       onlyone.put("name",r.getString("name"));
       onlyone.put("phone",r.getString("phone"));
       onlyone.put("gender",r.getString("gender"));
       onlyone.put("dob",r.getDate("dob"));
       EveryBody.add(onlyone);
        }
    return EveryBody;
        }
     
      @SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap getPeopleByEmail(String e){
            try{
                getPeopleByEmail.setString(1, e);
                ResultSet rs=getPeopleByEmail.executeQuery();
                if(rs.next()){
                HashMap talkuserDetails=new HashMap();
                talkuserDetails.put("email", rs.getString("email"));
                talkuserDetails.put("name", rs.getString("name"));
                talkuserDetails.put("phone", rs.getString("phone"));
                talkuserDetails.put("gender", rs.getString("gender"));
                talkuserDetails.put("dob", rs.getDate("dob"));
                talkuserDetails.put("state", rs.getString("state"));
                talkuserDetails.put("city", rs.getString("city"));
                talkuserDetails.put("area", rs.getString("area"));
                return talkuserDetails;
            }else{
                return null;
            }
            }catch(Exception ex){
                return null;
            }
        }
     
     
      
      public String sendMessage(String s,String r,String f,java.io.InputStream in) throws SQLException {        
           sendMessage.setString(1, s);
           sendMessage.setString(2, r);
           
           sendMessage.setString(3, f);
           sendMessage.setBinaryStream(4, in);
           int x=sendMessage.executeUpdate();
           if(x==1)
            return "Done";
           else 
            return "Error";
       }
    
       
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public java.util.ArrayList<java.util.HashMap> getMessages(String s,String r) throws SQLException{     
    getMessages.setString(1, s);
    getMessages.setString(2, r);
    ResultSet rs=getMessages.executeQuery();
    java.util.ArrayList<java.util.HashMap> senderandeceiverallMessage=new java.util.ArrayList();
    while(rs.next()){
        java.util.HashMap messagewithproperty=new java.util.HashMap();
        //messagewithproperty.put("message",rs.getString("msg"));
        messagewithproperty.put("datetime",rs.getString("udate"));
        messagewithproperty.put("filename",rs.getString("filename"));
        messagewithproperty.put("file",rs.getBytes("ufile"));
        messagewithproperty.put("pid",rs.getString("pid"));
        senderandeceiverallMessage.add(messagewithproperty);
        }
    return senderandeceiverallMessage;
        }
     
     
     public byte[] getFile(int e){
            try{
                getFile.setInt(1, e);
                ResultSet rs=getFile.executeQuery();
                if(rs.next()){
                    byte[] b=rs.getBytes("ufile");
                    if(b.length!=0)
                        return b;
                    else
                        return null;
                }else{
                    return null;
                }
            }catch(Exception ex){
                return null;
            }
        }
     
      public String changepassword(String np,String e,String p)throws SQLException{                        //zaroori nhi hai ki vhi lo
           
     
            changepassword.setString(1, np);
            changepassword.setString(2, e);
             changepassword.setString(3, p);
            int x=changepassword.executeUpdate();
            if(x==1)
             return "Success";
            else 
             return "Error";
        }
      
      
       @SuppressWarnings("rawtypes")
	public String changephoto(HashMap pic)throws SQLException{                          //zaroori nhi hai ki vhi lo
           
      changephoto.setBinaryStream(1,(InputStream)pic.get("photo"));
            changephoto.setString(2,(String)pic.get("Email") );
          
             
            int x=changephoto.executeUpdate();
            if(x==1)
             return "Success";
            else 
             return "Error";
        }
      
       
       
        public String edit(String A,String B,String C,java.sql.Date D,String E,String F,String G,String H)throws SQLException{                        //zaroori nhi hai ki vhi lo
           
            editprofile.setString(1, A);
            editprofile.setString(2, B);
            editprofile.setString(3, C);
            editprofile.setDate  (4, D);
            editprofile.setString(5, E);
            editprofile.setString(6, F);
            editprofile.setString(7, G);
            editprofile.setString(8, H);
            int x=editprofile.executeUpdate();
            if(x==1)
             return "Success";
            else 
             return "Error";
        }
        
        
        public String GetForgetPassword(String e){
            try{
                forgetpassword.setString(1, e);
                ResultSet rs=forgetpassword.executeQuery();
                if(rs.next()){
                        return rs.getString("pass");
                }else{
                    return null;
                }
            }catch(Exception ex){
                return null;
            }
        }
        public static void changeUserStatus(String email, String newStatus)throws SQLException {
    		
    		int rowsAffected;
    		String queryStr = "UPDATE userinfo SET status = ? where email = ?;";
    		
    		
    		
    		// Turn off auto-commit so we can use transactions
            c.setAutoCommit(false);

    		// Update the balance 
    		try (PreparedStatement updateCurBalStmt = c.prepareStatement(queryStr)) {
    			updateCurBalStmt.setString(1, newStatus);
    			updateCurBalStmt.setString(2, email);
    			
    			rowsAffected = updateCurBalStmt.executeUpdate();
    			
    			if(rowsAffected != 1){
    				dbErrorRollBackTx(c);
    				
    			}
    			c.commit(); /* Everything went OK */
    			
    		//	System.out.println("Status updated to " +newStatus);
    		} catch (SQLException ex) {
    			dbErrorRollBackTx(c);
    		}
    		
    	}
        
    	public static void dbErrorRollBackTx(Connection dbConn) {
    		try {
    			System.out.println("DB access error  rollback changes");
    			dbConn.rollback();
    		} catch (SQLException ex) {
    			System.out.println("Unable to rollback changes");
    			ex.printStackTrace();
    		}
    	}
    	public void logOut(String name) throws SQLException {
    		changeUserStatus(name, User.OFFLINE);
    		System.out.println("Logout Sucessfull !! ");
    		
    	}
    	
    	public void chathistory(String useremail,String friendsmail) {
    	PreparedStatement p = null;
    	PreparedStatement p1 = null;

		try {
			p1 = c.prepareStatement("select * from privouschat where usersmail=? and loginuser=?");

			p1.setString(1, friendsmail);
		
    	  int id=0;
			p1.setString(2,useremail);
			ResultSet  rs=p1.executeQuery();
			if(rs.next()) {
				 id=rs.getInt("idprivouschat");
			    
				PreparedStatement ps;
				
		        ps=c.prepareStatement("update privouschat set time=curtime() where idprivouschat=?");
		        ps.setInt(1, id);
		        ps.executeUpdate();
				return ;
			}
				
			p = c.prepareStatement("insert into privouschat (usersmail,loginuser,time,block) values(?,?,curtime(),0)");
		
    	
			p.setString(1, friendsmail);
		
    	
			p.setString(2,useremail);
		
			p.executeUpdate();
		
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		
		}
    
		public ArrayList<String> users(String mail) throws SQLException {
			PreparedStatement p= c.prepareStatement("select * from privouschat where loginuser=? and block!=1 order by time desc" );
			p.setString(1, mail);
			ResultSet rs=p.executeQuery();
			 ArrayList<String> userDetail = null;
			 userDetail=new ArrayList<>();
			while(rs.next()) {
				
				 userDetail.add(rs.getString("usersmail"));
				

			}
			return userDetail;
			
		}
		@SuppressWarnings("rawtypes")
		public ArrayList<HashMap> getname(String e) throws SQLException{
			PreparedStatement p= c.prepareStatement("select * from userinfo where email=?" );
			p.setString(1, e);
			ResultSet rs=p.executeQuery();
		    @SuppressWarnings("unchecked")
			java.util.ArrayList<java.util.HashMap> EveryBody=new java.util.ArrayList();

			while(rs.next()) {
				java.util.HashMap<String, String> onlyone=new HashMap<String, String>();
			       onlyone.put("email",rs.getString("email"));
			       onlyone.put("name",rs.getString("name"));
			       onlyone.put("gender", rs.getString("gender"));

			       EveryBody.add(onlyone);
			}
			return EveryBody;
			
		}
		public int insertoffilnemsg(String usermail ,String loginuser) throws SQLException {
			PreparedStatement select = c.prepareStatement("select * from privouschat where usersmail=? and loginuser=?");

			select.setString(1, usermail);
		
    	 int no = 0;
			select.setString(2,loginuser);
			ResultSet  rs=select.executeQuery();
			if(rs.next()) {
				//int no=rs.getInt("idprivouschat");
				 no=rs.getInt("offlinemessage");
	        offlinemessage=c.prepareStatement("update privouschat set offlinemessage=? where usersmail=? and loginuser=?");
            no=no+1;
			offlinemessage.setInt(1, no);
			offlinemessage.setString(2, usermail);
			offlinemessage.setString(3, loginuser);
			offlinemessage.executeUpdate();
			
		}
			return no;
		}
		public int getoffilnemsg(String usermail ,String loginuser) throws SQLException {
			PreparedStatement select = c.prepareStatement("select * from privouschat where usersmail=? and loginuser=?");

			select.setString(1, usermail);
		
    	 int no = 0;
			select.setString(2,loginuser);
			ResultSet  rs=select.executeQuery();
			if(rs.next()) {
				//int no=rs.getInt("idprivouschat");
				 no=rs.getInt("offlinemessage");
	    
			
		}
			return no;
		}
		public int deleteoffilnemsg(String usermail ,String loginuser) throws SQLException {
			
    	 int no=0;
			
		
	        offlinemessage=c.prepareStatement("update privouschat set offlinemessage=? where usersmail=? and loginuser=?");
            
			offlinemessage.setInt(1, no);
			offlinemessage.setString(2, usermail);
			offlinemessage.setString(3, loginuser);
			offlinemessage.executeUpdate();
			
		
			return no;
		}
		public void blockedperson(String blockedusername,String loginuser) throws SQLException {
			blockedperson.setInt(1,1);
			blockedperson.setString(2, blockedusername);
			blockedperson.setString(3, loginuser);
			blockedperson.executeUpdate();
		}
		public ArrayList<String> blockeduserlist(String loginuser) throws SQLException{
			blockedlist.setString(1,loginuser);
			ResultSet rs=blockedlist.executeQuery();
			ArrayList<String> list= new ArrayList<String>();
			while(rs.next()) {
				
				 list.add(rs.getString("usersmail"));
			}
			return list;
			
		}
		public void unblock(String blockedusername,String loginuser) throws SQLException {
			
			unblock.setString(1, blockedusername);
			unblock.setString(2, loginuser);
			unblock.executeUpdate();
		}
		public int blockmessage(String useremail,String friendsmail) throws SQLException {
	    	PreparedStatement p = null;
	    	PreparedStatement p1 = null;
                int block=0;
			
				p1 = c.prepareStatement("select block from privouschat where usersmail=? and loginuser=?");

				p1.setString(1, friendsmail);
			    p1.setString(2,useremail);
				ResultSet  rs=p1.executeQuery();
				if(rs.next()) {
					
				      block=rs.getInt("block");
					PreparedStatement ps;
					
					
				}
				return block;
			
		
    }
}