

import java.io.IOException;
import java.sql.*;

import javax.servlet.http.*;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet("/FullTextServlet")
public class FullTextServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public FullTextServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String title = request.getParameter("title");
		System.out.println("FULL TEXT SEARCH ON TITLE: " + title);
		
		String[] keywords = title.split("\\s");
		
		try {
			
			// establish new connection to MySQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
		   // AWS VERSION
//	       Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","ajching", "ajching");
	        
			// LOCAL VERSION
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");

			System.out.println("Connection valid: " + connection.isValid(10));
			
            Statement statement = connection.createStatement();
            String responseObject = "{";
            String baseQuery = "SELECT m.id, m.title, m.year, m.director FROM movies m WHERE ";
            for(int i = 0; i < keywords.length; i++) {
            	ResultSet rs = statement.executeQuery(baseQuery + "MATCH (title) AGAINST ('"+ keywords[i] + "');");
            	while(rs.next()) {
                	
    				String resultID = rs.getString(1);
                	String resultTitle = rs.getString(2);
                	String resultYear = Integer.toString(rs.getInt(3));
                	String resultDirector = rs.getString(4);
//                	new Movie object in JSON, with ID as the key
                	responseObject += "\"" + resultID + "\"" + ": {";
                	
//                	add attributes in movie object
                	responseObject = setAttribute(responseObject, "title", resultTitle, true);
                	responseObject = setAttribute(responseObject, "year", resultYear, false);
                	responseObject = setAttribute(responseObject, "director", resultDirector, false);
                	
                	
//                	add genres as list of objects
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
                	
                	
//                	close genres and open stars object
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
                	
//                	close stars and entire Movie object in JSON
                	responseObject += "} }";
                	if(!rs.isLast()) {
                		responseObject += ", \n";
                	}

                }
            	
                responseObject += "}";
                
            	response.getWriter().write(responseObject);
            }
            
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
