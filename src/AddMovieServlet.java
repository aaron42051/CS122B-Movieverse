import java.io.IOException;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/AddMovieServlet")
public class AddMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AddMovieServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("movie-title");
		String year = request.getParameter("movie-year");
		String star = request.getParameter("movie-stars");
		String birthYear = request.getParameter("star-birth-year");
		String genre = request.getParameter("movie-genre");
		String director = request.getParameter("movie-director");
		
		System.out.println("!!");
		
		if (title == null || year == null || star == null || birthYear == null || genre == null || director == null) {
			response.getWriter().write("{\"status\": \"failed\", \"message\": \"Must fill in all inputs correctly!\"}");
		}
		else {

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
	            
//	            Statement statement = connection.createStatement();

				
				
//				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
//				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");
	            
	            connection.setAutoCommit(false);
	            PreparedStatement pstatement;
	            
	            String useDB = "use cs122b;";
//	            statement.execute(useDB);
	            pstatement = connection.prepareStatement(useDB);
	            pstatement.execute();
	            
	            
	            String query = "CALL add_movie(\"" + title + "\", " + Integer.parseInt(year) + ", \"" + director + "\", \"" + genre + "\", \"" + star + "\", " + Integer.parseInt(birthYear) + ");";
//	            statement.execute(query);
	            pstatement = connection.prepareStatement(query);
	            pstatement.execute();
	            
	            connection.setAutoCommit(true);
	            connection.commit();
	        	connection.close();

	            response.getWriter().write("{\"status\": \"success\", \"message\":\"Adding " + title + " was a success!\"}");
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

}
