package servlets;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class SendMail extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		     HttpSession session=request.getSession();
	            String e=request.getParameter("email");
	            db.DbConnect db=new db.DbConnect();
	            String p=db.GetForgetPassword(e);
	            if(p!=null) {
			sendmsg(e,p);
			session.setAttribute("msg","Mail Sent successfully.");
            response.sendRedirect("forgetpassword.jsp");
			
		 }else{
             session.setAttribute("msg", "Wrong Emial ID");
             response.sendRedirect("forgetpassword.jsp");
         }
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	public void sendmsg(String recipient,String p)  {
		Properties pro = new Properties();
		pro.put("mail.smtp.auth","true");
		pro.put("mail.smtp.starttls.enable",true);
		pro.put("mail.smtp.starttls.enable", "true");
		pro.put("mail.smtp.host","smtp.gmail.com");
		pro.put("mail.smtp.port","587");
		final String myAccount ="1234.dsp.ds@gmail.com";
		final String password = "9795910490";
		Session session = Session.getInstance(pro,new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(myAccount,password);
			}
			
			
		});
		Message message = prepareMessage(session,myAccount,recipient,p );
		try {
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static Message prepareMessage(Session session, String myAccount ,String recipient,String p) {
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(myAccount));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject("Your Password is Here from friendzoneforever!");
			message.setText("Your Email Id: "+recipient+" and Password: "+p);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
		
	}
}
