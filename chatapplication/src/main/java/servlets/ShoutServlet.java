package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
@WebServlet("/ShoutServlet")
public class ShoutServlet extends HttpServlet {

/**
	 * 
	 */
	private static final long serialVersionUID = 5242456642901610633L;

public void doGet(HttpServletRequest request, HttpServletResponse res)
throws ServletException, IOException {

	 res.setContentType("text/event-stream;charset=UTF-8");
     res.setHeader("Cache-Control",
             "no-cache,no-store,max-age=0,max-stale=0");
     res.setHeader("Connection", "keep-alive");

PrintWriter writer = res.getWriter();
User u=new User();
if(u.getData()!=null) {
JSONObject obj  = new JSONObject(u.getData());
writer.write("data: "+ obj +"\n\n");

}
}
}