

import java.io.IOException;
import java.sql.*;

import javax.servlet.http.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;



@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());


	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("LOGIN POST");



		// get the email and password sent from js form
		String email =  request.getParameter("email");
		String pass =  request.getParameter("pass");
		
		System.out.println("email: " + email);
		
		// connection to MySQL details
	
		response.setContentType("text/html");
		
		try {
			
			// establish new connection to MySQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		   // AWS VERSION
	       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","ajching", "ajching");
	        
			// LOCAL VERSION
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");

			System.out.println("Connection valid: " + connection.isValid(10));
			
            Statement statement = connection.createStatement();

            // write query
            
            
            // AWS VERSION
            String useDB = "use moviedb;";
            
            // LOCAL VERSION
//            String useDB = "use cs122b;";
            
            String query = "SELECT * FROM customers c WHERE c.email = \"" + email + "\"" + " and c.password=\"" + pass + "\";";
            String usernameQuery = "SELECT * FROM customers c WHERE c.email = \"" + email + "\";";
            
            statement.execute(useDB);
            
            // result of query
            System.out.println("EXECUTING QUERY: " + "\n\n" + query + "\n");
            String responseObject = "{";

            ResultSet rs = statement.executeQuery(query);
            try {
                rs.next();
                System.out.println("User: " + rs.getString(2));
                
                int id = rs.getInt("id");
                
                System.out.println("ID: " + id);
//            	JSONObject responseObject = new JSONObject();
                responseObject = setAttribute(responseObject, "status", "success", true);
            	responseObject = setAttribute(responseObject, "message", "Login Successful!", false);
                
            }
            catch (Exception e) {
                ResultSet usernameResult = statement.executeQuery(usernameQuery);
                if(usernameResult.next()) {
                   
//                    	responseObject.addProperty("status", "fail");
//                    	responseObject.addProperty("message", "Incorrect password");         
                    	
                    responseObject = setAttribute(responseObject, "status", "fail", true);
                    responseObject = setAttribute(responseObject, "message", "Incorrect password", false);
                    
                }
                else {
                	responseObject = setAttribute(responseObject, "status", "fail", true);
                	responseObject = setAttribute(responseObject, "message", "No such username", false);
                }
            }

            
              
            responseObject += " }";
            System.out.println(responseObject);
        	response.getWriter().write(responseObject);

		}
        catch (SQLException ex) {
            while (ex != null) {
                  System.out.println ("SQL Exception:  " + ex.getMessage ());
                  ex = ex.getNextException ();
              }  
          }  
        catch(java.lang.Exception ex)
        {
            while (ex != null) {
                System.out.println ("Exception:  " + ex.getMessage ());
            }          
        }
	}
	
	public String setAttribute(String json, String attribute, String value, boolean first) {
		if(!first) {
			json += ", ";
		}
		json += "\"" + attribute + "\": \"" + value+ "\"";
		return json;
	}    

}
