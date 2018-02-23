

import java.io.IOException;
import java.sql.*;

import javax.servlet.http.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;



@WebServlet("/BrowseServlet")
public class BrowseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public BrowseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("GET GENRES");
		
		try {
			
			// establish new connection to MySQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		   // AWS VERSION
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","ajching", "ajching");
	        
			// LOCAL VERSION
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");

			System.out.println("Connection valid: " + connection.isValid(10));
			
            Statement statement = connection.createStatement();
            
            // AWS VERSION
//          String useDB = "use moviedb;";
          
            // LOCAL VERSION
            String useDB = "use cs122b;";
            
            String query = "SELECT name FROM genres;";
            
            statement.execute(useDB);
            
            ResultSet rs = statement.executeQuery(query);
            
            String responseObject = "{";
            int num = 1;
            while(rs.next()) {
            	responseObject += "\"genre" + Integer.toBinaryString(num) + "\": \"" + rs.getString(1) + "\"";
            	if (!rs.isLast()) {
            		responseObject += ", ";
            	}
            	num++;
            }
            responseObject += "}";
            
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

}
