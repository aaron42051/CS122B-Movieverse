

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
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


//		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
//		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
//		boolean valid = VerifyUtils.verify(gRecaptchaResponse);


		
		// get the email and password sent from js form
		String email =  request.getParameter("email");
		String pass =  request.getParameter("pass");
		String employee = request.getParameter("employee");
		
		System.out.println("employee: " + employee);
		
		// connection to MySQL details
	
		response.setContentType("text/html");

		
		
		try {
			
			
            Context initCtx = new InitialContext();
            if (initCtx == null)
                System.out.println("initCtx is NULL");
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
            	System.out.println("envCtx is NULL");

            // Look up our data source
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            
            if (ds == null)
            	System.out.println("ds is null.");

            Connection connection = ds.getConnection();
            if (connection == null)
            	System.out.println("dbcon is null.");
            
            Statement statement = connection.createStatement();

			
			// establish new connection to MySQL
//			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		   // AWS VERSION
//	       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","ajching", "ajching");
	        
			// LOCAL VERSION
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");

			

            connection.setAutoCommit(false);
            PreparedStatement pstatement;
            
            // write query
            
            
            // AWS VERSION
//            String useDB = "use moviedb;";
            
            // LOCAL VERSION
            String useDB = "use cs122b;";
            
            String query = "SELECT * FROM customers c WHERE c.email = \"" + email + "\"" + " and c.password=\"" + pass + "\";";
            String usernameQuery = "SELECT * FROM customers c WHERE c.email = \"" + email + "\";";
            
//            statement.execute(useDB);
            pstatement = connection.prepareStatement(useDB);
            pstatement.execute();
            connection.commit();
            
            // result of query
            String responseObject = "{";
//    		if(!valid) {
//    			System.out.println("INVALID RECAPTCHA");
//    			responseObject = setAttribute(responseObject, "status", "failed", true);
//    			responseObject = setAttribute(responseObject, "message", "ReCaptcha failed", false);
////    			responseObject += "}";
////            	response.getWriter().write(responseObject);
//    		}
//    		else {
    		System.out.println("HOLA MI AMIGOS: " + email + pass);
    		if (employee != null) {
    			System.out.println("employee confirmed");
                query = "SELECT * FROM employees e WHERE e.email = \"" + email + "\"" + " and e.password=\"" + pass + "\";";
                usernameQuery = "SELECT * FROM employees e WHERE e.email = \"" + email + "\";";    		
            }
            System.out.println("EXECUTING QUERY: " + "\n\n" + query + "\n");
            pstatement = connection.prepareStatement(query);
            
//    		ResultSet rs = statement.executeQuery(query);
            ResultSet rs = pstatement.executeQuery();
            try {
                rs.next();
                if (employee != null) {
                	System.out.println("Employee: ?");
                
                }
                else {
                    System.out.println("User: " + rs.getString(2));
                    
                    int id = rs.getInt("id");
                    
                    System.out.println("ID: " + id);
                }

//            	JSONObject responseObject = new JSONObject();
                responseObject = setAttribute(responseObject, "status", "success", true);
            	responseObject = setAttribute(responseObject, "message", "Login Successful!", false);
                
            }
            catch (Exception e) {
                System.out.println("EXECUTING QUERY: " + "\n\n" + usernameQuery + "\n");

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
            

        	connection.close();

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
