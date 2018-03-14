

import java.io.IOException;
import java.sql.*;

import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;



@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SEARCH 1");
		System.out.println("SEARCH GET");
		System.out.println("SEARCH GET");
		System.out.println("SEARCH GET");
		System.out.println("SEARCH GET");
		System.out.println("SEARCH GET");
		System.out.println("SEARCH GET");
		System.out.println("SEARCH GET");
		
		String title = request.getParameter("movie-title");
		
		

		int year = 0;
		try {
			year = Integer.parseInt(request.getParameter("movie-year"));
		}
				
		catch(NumberFormatException ex) {
			System.out.println("Not a numerical year!");
		}
		
		System.out.println("year: " + year);

		String director = request.getParameter("movie-director");
		
		System.out.println("director: " + director);
		
		String star = request.getParameter("movie-stars");
		
		System.out.println("star: " + star);
		
		System.out.println();
		
		String search = request.getParameter("search");
		
		String genre = request.getParameter("genre");
		
		String browse = request.getParameter("browse");


		
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
			
//			Class.forName("com.mysql.jdbc.Driver").newInstance();

//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","ajching", "ajching");

            Statement statement = connection.createStatement();
            
            // AWS VERSION
//          String useDB = "use moviedb;";
          
            // LOCAL VERSION
            String useDB = "use cs122b;";
            
            statement.execute(useDB);
            
            String baseQuery = "SELECT m.id, m.title, m.year, m.director FROM movies m WHERE ";
            
            boolean first = true;
            if (star != null && star != "") {

            	first = false;
            	baseQuery = "SELECT m.id, m.title, m.year, m.director FROM movies m, stars s, stars_in_movies sm WHERE m.id = sm.movieId AND s.id = sm.starId AND s.name LIKE \"%" + star + "%\" ";
            }
            if (title != null && title != "") {
            	System.out.println("!");
            	if(!first) {
            		baseQuery += " AND ";
            	}
            	if (browse != null) {
                	baseQuery += "m.title LIKE \"" + title + "%\"";
            	}
            	else {
                	baseQuery += "m.title LIKE \"%" + title + "%\"";
            	}
            	first = false;
            }
            if (year != 0) {
            	if(!first) {
            		baseQuery += " AND ";
            	}
            	baseQuery += "m.year = " + year;
            	first = false;
            }
            if (director != null && director != "") {
            	if(!first) {
            		baseQuery += " AND ";
            	}
            	first = false;
            	baseQuery += " m.director LIKE \"%" + director + "%\"";
            }

            baseQuery += ";";
        	if(search != null) {
        		baseQuery = "SELECT m.id, m.title, m.year, m.director FROM movies m WHERE m.title = \""+ title + "\"";
        	}
        	if(genre != null) {
        		baseQuery = "SELECT m.id, m.title, m.year, m.director FROM genres g, movies m, genres_in_movies gm WHERE g.id = gm.genreId AND m.id = gm.movieId AND g.name =\"" + genre + "\";";
        	}
        	System.out.println("CONSTRUCTED: ");
            System.out.println(baseQuery);
            ResultSet rs = statement.executeQuery(baseQuery);
            
            System.out.println("EXECUTING...");

//          open up new JSON
            String responseObject = "{";
            
            while(rs.next()) {
            	
				String resultID = rs.getString(1);
            	String resultTitle = rs.getString(2);
            	String resultYear = Integer.toString(rs.getInt(3));
            	String resultDirector = rs.getString(4);
//            	new Movie object in JSON, with ID as the key
            	responseObject += "\"" + resultID + "\"" + ": {";
            	
//            	add attributes in movie object
            	responseObject = setAttribute(responseObject, "title", resultTitle, true);
            	responseObject = setAttribute(responseObject, "year", resultYear, false);
            	responseObject = setAttribute(responseObject, "director", resultDirector, false);
            	
            	
//            	add genres as list of objects
            	responseObject += ", \"genres\": {";
            	
            	String genreQuery = "SELECT g.name FROM genres g, movies m, genres_in_movies gm WHERE g.id = gm.genreId AND m.id = gm.movieId AND m.id = \"" + resultID + "\"";
                Statement gstatement = connection.createStatement();
            	ResultSet grs = gstatement.executeQuery(genreQuery);
            	int genreNum = 1;
            	
            	String att = "genre" + Integer.toString(genreNum);
            	grs.next();
            	String gen = grs.getString(1);
            	responseObject = setAttribute(responseObject, att, gen, true);

            	while(grs.next()) {
            		genreNum++;
            		responseObject = setAttribute(responseObject, "genre"+genreNum, grs.getString(1), false);
            	}
            	
            	
//            	close genres and open stars object
            	responseObject += "}, \"stars\": {";
            	
            	String starQuery = "SELECT s.name FROM stars s, movies m, stars_in_movies sm WHERE s.id = sm.starId AND m.id = sm.movieId AND m.id = \"" + resultID + "\"";
                Statement sstatement = connection.createStatement();
   	
            	ResultSet srs = sstatement.executeQuery(starQuery);
            	int starNum = 1;
            	srs.next();
            	responseObject = setAttribute(responseObject, "star" + starNum, srs.getString(1), true);
            	
            	while(srs.next()) {
            		starNum++;
            		responseObject = setAttribute(responseObject, "star"+starNum, srs.getString(1), false);
            	}
            	
//            	close stars and entire Movie object in JSON
            	responseObject += "} }";
            	if(!rs.isLast()) {
            		responseObject += ", \n";
            	}

            }
            
//          close entire JSON response
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
	
	public String setAttribute(String json, String attribute, String value, boolean first) {
		if(!first) {
			json += ", ";
		}
		json += "\"" + attribute + "\": \"" + value+ "\"";
		return json;
	} 

}
