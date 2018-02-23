
import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/StarServlet")
public class StarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public StarServlet() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("GETTING STAR INFO");
		
		String name = request.getParameter("star");
		
		try {
			
			// establish new connection to MySQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		   // AWS VERSION
//	       Connection connection = DriverManager.getConnection("jdbc:mysql://18.220.104.176:3306?autoReconnect=true&useSSL=false","ajching", "ajching");
	        
			// LOCAL VERSION
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");

			System.out.println("Connection valid: " + connection.isValid(10));
			
            Statement statement = connection.createStatement();
            Statement statement2 = connection.createStatement();
            
            // AWS VERSION
//          String useDB = "use moviedb;";
          
            // LOCAL VERSION
            String useDB = "use cs122b;";
            
            String query = "SELECT s.name, s.birthYear FROM stars s WHERE s.name = \"" + name + "\";";
            String movieQuery = "SELECT m.title FROM stars s, movies m, stars_in_movies sm WHERE s.id = sm.starId and m.id = sm.movieId and s.name = \"" + name + "\";";
            
            statement.execute(useDB);

            
            ResultSet rs = statement.executeQuery(query);
            System.out.println(query);

            rs.next();
            
            String responseObject = "{ \"name\": \"" + rs.getString(1) + "\", \"birthYear\": " + Integer.toString(rs.getInt(2)) + ", \"movies\": { ";
            
            ResultSet rsm = statement2.executeQuery(movieQuery);
            System.out.println(movieQuery);


            int num = 1;
            while(rsm.next()) {
            	responseObject += "\"movie" + Integer.toBinaryString(num) + "\": \"" + rsm.getString(1) + "\"";
            	if (!rsm.isLast()) {
            		responseObject += ", ";
            	}
            	num++;
            }
            responseObject += "} }";
            System.out.println("RESULT JSON: " + responseObject);
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
