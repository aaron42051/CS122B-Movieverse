
import java.io.PrintWriter;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DashboardServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html>\r\n" + 
				"<html lang=\"en\">\r\n" + 
				"\r\n" + 
				"<head>\r\n" + 
				"    <meta charset=\"utf-8\">\r\n" + 
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n" + 
				"    <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\r\n" + 
				"    <!-- Font Awesome -->\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n" + 
				"    <!-- Bootstrap core CSS -->\r\n" + 
				"    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\r\n" + 
				"    <!-- Material Design Bootstrap -->\r\n" + 
				"    <link href=\"css/mdb.min.css\" rel=\"stylesheet\">\r\n" + 
				"    <!-- Your custom styles (optional) -->\r\n" + 
				"    <link href=\"style.css\" rel=\"stylesheet\">\r\n" + 
				"    <script src=\"https://www.google.com/recaptcha/api.js\" async defer></script>\r\n" + 
				"    \r\n" + 
				"    \r\n" + 
				"</head>");
		out.println("<body>\r\n" + 
				"	<div class='background'></div>\r\n" + 
				"\r\n" + 
				"<!-- LOGIN PAGE -->\r\n" + 
				"<form id=\"login_form\" method=\"post\">\r\n" + 
				"      <h1 class=\"h1 text-center mb-4\">Employee Dashboard</h1>\r\n" + 
				"      <br>\r\n" + 
				"      <div class=\"md-form\">\r\n" + 
				"          <i class=\"fa fa-envelope prefix grey-text\"></i>\r\n" + 
				"          <input type=\"text\" id=\"defaultForm-email\" class=\"form-control\" name=\"email\">\r\n" + 
				"          <label for=\"defaultForm-email\">Your email</label>\r\n" + 
				"      </div>\r\n" + 
				"\r\n" + 
				"      <div class=\"md-form\">\r\n" + 
				"          <i class=\"fa fa-lock prefix grey-text\"></i>\r\n" + 
				"          <input type=\"password\" id=\"defaultForm-pass\" class=\"form-control\" name=\"pass\">\r\n" + 
				"          <label for=\"defaultForm-pass\">Your password</label>\r\n" + 
				"      </div>\r\n" + 
				"\r\n" + 
				"      <div class=\"text-center\">\r\n" + 
				"          <input id=\"login-button\" class=\"btn btn-primary my-3\" type=\"submit\" value=\"Login\">\r\n" + 
				"      </div>\r\n" + 
				"      <input class=\"employeeEmbed\" name=\"employee\" value=\"true\">\r\n" +
				"      <div class=\"g-recaptcha\" data-sitekey=\"6LeyBUoUAAAAAEJlJSXaI3hpXypMRI0m8j9xBBT5\"></div>\r\n" + 
				"      \r\n" + 
				"</form>");
		out.println("<script type=\"text/javascript\" src=\"js/jquery-3.2.1.min.js\"></script>\r\n" + 
				"    <!-- Bootstrap tooltips -->\r\n" + 
				"    <script type=\"text/javascript\" src=\"js/popper.min.js\"></script>\r\n" + 
				"    <!-- Bootstrap core JavaScript -->\r\n" + 
				"    <script type=\"text/javascript\" src=\"js/bootstrap.min.js\"></script>\r\n" + 
				"    <!-- MDB core JavaScript -->\r\n" + 
				"    <script type=\"text/javascript\" src=\"js/mdb.min.js\"></script>\r\n" + 
				"    <!-- my script -->\r\n" + 
				"    <script type=\"text/javascript\" src=\"dashboard.js\"></script>\r\n" + 
				"</body>\r\n" + 
				"\r\n" + 
				"</html>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
