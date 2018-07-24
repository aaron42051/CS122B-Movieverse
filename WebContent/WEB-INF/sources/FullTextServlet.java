

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;
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
		long serveStart = System.nanoTime();
		long endTime = 0;

		String title = request.getParameter("title");
		String ajax = request.getParameter("ajax");
		String android = "false";
		try {
			android = request.getParameter("android");
		} catch(Exception e) {
			System.out.println("android is null, set to false");
		}
		System.out.println("FULL TEXT SEARCH ON TITLE: " + title);
		System.out.println("AUTOCOMPLETE: " + ajax);
		System.out.println("ANDROID: " + android);
		String[] keywords = title.split("\\s");
		

		long jdbcStart = System.nanoTime();
		
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

			
            Statement starstatement = connection.createStatement();
            
            String useDB = "use cs122b;";
            statement.execute(useDB);


            int movieMax = 0;
            int starMax = 0;
            
            String responseObject = "{";
            
            ArrayList movieList = new ArrayList();
            ArrayList starList = new ArrayList();
            
            if (ajax.equals("true")) {
            	responseObject = "[";
            }

            String baseQuery = "SELECT m.id, m.title, m.year, m.director FROM movies m WHERE ";
            String starBaseQuery = "SELECT s.id, s.name FROM stars s WHERE ";
            for(int i = 0; i < keywords.length; i++) {
            	ResultSet rs = statement.executeQuery(baseQuery + "MATCH (title) AGAINST ('"+ keywords[i] + "*' IN BOOLEAN MODE);");
            	endTime = System.nanoTime();
            	if (ajax.equals("true")) {
                	ResultSet starsRS = starstatement.executeQuery(starBaseQuery + "MATCH (name) AGAINST ('"+ keywords[i] + "*' IN BOOLEAN MODE);");
        			while(starsRS.next()) {
                    	starList.add("{\"value\": \"" + starsRS.getString(2) + "\", \"data\":\"" + starsRS.getString(1) + "\"}");
        			}
            	}
            	while(rs.next()) {

            			String resultID = rs.getString(1);
                    	String resultTitle = rs.getString(2);
                    	String resultYear = Integer.toString(rs.getInt(3));
                    	String resultDirector = rs.getString(4);

//                    	add genres as list of objects
                    	
                    	String genreQuery = "SELECT g.name FROM genres g, movies m, genres_in_movies gm WHERE g.id = gm.genreId AND m.id = gm.movieId AND m.id = \"" + resultID + "\"";
                        Statement gstatement = connection.createStatement();
                    	ResultSet grs = gstatement.executeQuery(genreQuery);
                    	int genreNum = 1;
                    	
                    	String att = "genre" + Integer.toString(genreNum);
                    	grs.next();
                    	String gen = grs.getString(1);

//                    	add stars as list of objects
                    	String starQuery = "SELECT s.name FROM stars s, movies m, stars_in_movies sm WHERE s.id = sm.starId AND m.id = sm.movieId AND m.id = \"" + resultID + "\"";
                        Statement sstatement = connection.createStatement();
           	
                    	ResultSet srs = sstatement.executeQuery(starQuery);
                    	int starNum = 1;
                    	srs.next();

                    	// construct responseObject based on regular search or autocomplete
                    	if(ajax.equals("true")) {
                			String new_movie = "{\"value\": \"" + rs.getString(2) + "\", \"data\":\"" + rs.getString(1) + "\"";
                			new_movie = setAttribute(new_movie, "year", resultYear, false);
                			new_movie = setAttribute(new_movie, "director", resultDirector, false);
                			new_movie += ", \"genres\": [";
                    		//new_movie += "{\"genre"+genreNum+"\": \"" + grs.getString(1) + "\"}";
                			new_movie += "\"" + grs.getString(1) + "\""; 
                    		if (!grs.isLast()) {
                    			new_movie += ", ";
                    		}
                        	while(grs.next()) {
                        		genreNum++;
//                        		new_movie += "{\"genre"+genreNum+"\": \"" + grs.getString(1) + "\"}";
                    			new_movie += "\"" + grs.getString(1) + "\""; 

                        		if (!grs.isLast()) {
                        			new_movie += ", ";
                        		}

                        	}
                        	grs.first();
                        	new_movie += "], \"stars\": [";
//                    		new_movie += "{\"star" + starNum + "\": \"" + srs.getString(1) + "\"}";
                        	new_movie += "\"" + srs.getString(1) + "\"";
                    		if (!srs.isLast()) {
                    			new_movie += ", ";
                    		}
                        	while(srs.next()) {
                        		starNum++;
//                        		new_movie += "{\"star" + starNum + "\": \"" + srs.getString(1) + "\"}";
                            	new_movie += "\"" + srs.getString(1) + "\"";

                        		if (!srs.isLast()) {
                        			new_movie += ", ";
                        		}

                        	}
                        	srs.first();
                        	new_movie += "] }";
                        	movieList.add(new_movie);
                    	}
                    	else {
                        	responseObject += "\"" + resultID + "\"" + ": {";
//                        	add attributes in movie object
                        	responseObject = setAttribute(responseObject, "title", resultTitle, true);
                        	responseObject = setAttribute(responseObject, "year", resultYear, false);
                        	responseObject = setAttribute(responseObject, "director", resultDirector, false);
                        	if (android.equals("true")) {
                        		responseObject = setAttribute(responseObject, "genre", grs.getString(1), false);
                        		grs.first();
                        		
                        		responseObject = setAttribute(responseObject, "star", srs.getString(1), false);
                        		srs.first();
                        		
                        		System.out.println("ANDROID VERSION: " + responseObject);
                        		responseObject += "}";
                        	}
                        	else {
                            	responseObject += ", \"genres\": {";
                            	responseObject = setAttribute(responseObject, att, gen, true);
                            	while(grs.next()) {
                            		genreNum++;
                            		responseObject = setAttribute(responseObject, "genre"+genreNum, grs.getString(1), false);
                            	}
                            	
//                            	close genres and open stars object
                            	responseObject += "}, \"stars\": {";
                            	responseObject = setAttribute(responseObject, "star" + starNum, srs.getString(1), true);
                            	while(srs.next()) {
                            		starNum++;
                            		responseObject = setAttribute(responseObject, "star"+starNum, srs.getString(1), false);
                            	}
                            	
//                            	close stars and entire Movie object in JSON
                            	responseObject += "} }";
                        	}

                        	if(i == (keywords.length - 1) && rs.isLast()) {
                        		System.out.println("LAST ONE");
                        		
                        	}
                        	else {
                           		responseObject += ", \n";
                        	}
                    	}

                } // end while
            } // end for
            
            
            

            if (ajax.equals("true")) {
            	// if more than 10 entries, limit the list with lesser entries up to 5
            	if (movieList.size() + starList.size() > 10) { 
            		if (movieList.size() > starList.size()) {
            			if (starList.size() > 5) {
            				starMax = 5;
            				movieMax = 5;
            			}
            			else {
            				starMax = starList.size();
            				movieMax = 10 - starList.size();
            			}
            		}
            		else {
            			if (movieList.size() > 5) {
            				starMax = 5;
            				movieMax = 5;
            			}
            			else {
            				movieMax = starList.size();
            				starMax = 10 - movieList.size();
            			}
            		}
            	}
            	
            	responseObject += "{\"value\":\"------MOVIES------\", \"data\":\"label\"}, ";
            	for (int j = 0; j < movieMax; j++) {
            		responseObject += movieList.get(j);
            		if (starMax > 0 || j != movieMax - 1) {
            			responseObject += ", \n";
            		}
            	}
            	
            	responseObject += "{\"value\":\"-------STARS-------\", \"data\":\"label\"}, ";
            	for (int k = 0; k < starMax; k++) {
            		responseObject += starList.get(k);
            		if(k != starMax - 1) {
            			responseObject += ", \n";
            		}
            	}
            	
            	responseObject += "]";
            }
            else {
                responseObject += "}";
            }
//            long serveEnd = System.nanoTime();
//            long elapsedServlet = serveEnd - serveStart;
//            long elapsedJDBC = endTime - jdbcStart;
//            
//            File servlet_log = new File("/home/ubuntu/tomcat/logs/servlet_log.txt");
//            File jdbc_log = new File("/home/ubuntu/tomcat/logs/jdbc_log.txt");
            
//            if (!servlet_log.exists()) {
//            	System.out.println("CREATING NEW FILE S");
//            	servlet_log.createNewFile();
//            }
//            if(!jdbc_log.exists()) {
//            	System.out.println("CREATING NEW FILE J");
//            	jdbc_log.createNewFile();
//            }
            
//            FileWriter fw = new FileWriter(servlet_log.getAbsolutePath(), true);
//            FileWriter fw2 = new FileWriter(jdbc_log.getAbsolutePath(), true);
//            
//            BufferedWriter out = new BufferedWriter(fw);
//            BufferedWriter out2 = new BufferedWriter(fw2);
//            
//            
//            out.write(String.valueOf(elapsedServlet));
//            out2.write(String.valueOf(elapsedJDBC));
//            
//            System.out.println(elapsedServlet);
//            System.out.println(elapsedJDBC);
//        	
//            out.flush();
//            out2.flush();
//            
//        	out.close();
//        	out2.close();
            System.out.println("RESPONSE OBJECT: " +responseObject);
        	connection.close();

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
            if (ex != null) {
            	System.out.println();
                System.out.println ("Title:  " + title);
                System.out.println("Ajax: " + ajax);
                System.out.println("Android: " + android);
                System.out.println(ex);
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
