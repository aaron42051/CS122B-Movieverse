import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import java.util.*;  



import org.apache.juli.logging.*;

public class ParseXML {
	Document dom;
	Document stardom;
	Document castdom;
	static String query;
	static HashMap<String, HashMap<String, String>> movies = new HashMap<String, HashMap<String, String>>();
	static HashMap<String, String> genres = new HashMap<String, String>();
	static HashMap<String, String> fid_to_a = new HashMap<String, String>();
	static HashMap<String, String> stars = new HashMap<String, String>();
	public ParseXML() {
		genres.put("Epic", "Epic");
		genres.put("Comd", "Comedy");
		genres.put("Dram", "Drama");
		genres.put("Romt", "Romance");
		genres.put("BioP", "Biography");
		genres.put("Susp", "Suspense");
		genres.put("Romt Comd", "Comedy");
		genres.put("Porn", "Adult");
		genres.put("Horr", "Horror");
		genres.put("Docu", "Documentary");
		genres.put("Advt", "Adventure");
		genres.put("Fant", "Fantasy");
		genres.put("Musc", "Musical");
		genres.put("West", "Western");
		genres.put("Cart", "Animation");
		genres.put("CnRb",  "Crime");
		genres.put("Actn",  "Action");
		genres.put("Myst", "Mystery");
		genres.put("SciF",  "Sci-Fi");
		genres.put("Surl", "Surreal");
		genres.put("Surr", "Surreal");
		genres.put("Noir", "Noir");
	}
	
	
	public void run() {
		setupXMLFile();
		parseDocument();
	}
	
	private void setupXMLFile() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("stanford-movies/mains243.xml");
			stardom = db.parse("stanford-movies/actors63.xml");
			castdom = db.parse("stanford-movies/casts124.xml");
		}catch(ParserConfigurationException pce) {
		pce.printStackTrace();
		}catch(SAXException se) {
		se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private void addMovieContents(HashMap<String, String> movie, Element title, Element year, Element director, Element genre, Element id) {
		try {
			if (title.getFirstChild().getNodeValue().trim().length() > 0) {
				movie.put("title", title.getFirstChild().getNodeValue());
			}
			else {
				System.out.println("No title, not adding " + id.getFirstChild().getNodeValue());
				return;
			}
				
		}
		catch(Exception e) {
			System.out.println("No title, not adding " + id.getFirstChild().getNodeValue());
			movie.put("title", null);
		}
		
		try {
			Integer.parseInt(year.getFirstChild().getNodeValue());
			movie.put("year", year.getFirstChild().getNodeValue());
		}
		catch(Exception e) {
			try {
				System.out.println("Cannot add entry " + id.getFirstChild().getNodeValue() + " due to year: " + year.getFirstChild().getNodeValue());
				return;
			}
			catch (Exception f) {
				System.out.println("Year is null, not adding " + id.getFirstChild().getNodeValue());
				return;
			}
		}
		
		try {
			movie.put("director", director.getFirstChild().getNodeValue());
		}
		catch(Exception e) {
			movie.put("director", null);
		}
		
		try {
			NodeList cat = genre.getElementsByTagName("cat");
			Element catEL = (Element)cat.item(0);
			movie.put("genre", catEL.getFirstChild().getNodeValue());
		}
		catch(Exception e) {
			movie.put("genre", null);
		}
		try {
			movies.put(id.getFirstChild().getNodeValue(), movie);
		}
		catch(Exception e) {
			System.out.println("fid is null, not adding " + title.getFirstChild().getNodeValue());
		}
	}
	
	private void parseStars(NodeList actor) {
		int count = 0;
		if (actor != null && actor.getLength() > 0) {
			for (int i = 0; i < actor.getLength(); i++) {
				// get actors Element
				Element actorEL = (Element)actor.item(i);
				
				// get stagename NL/EL
				NodeList stagename = actorEL.getElementsByTagName("stagename");
				Element stagenameEL = (Element) stagename.item(0);
				
				// get dob NL/EL
				NodeList dob = actorEL.getElementsByTagName("dob");
				Element dobEL = (Element)dob.item(0);
				
				try {
					String name = stagenameEL.getFirstChild().getNodeValue();


					try {
						Integer.parseInt(dobEL.getFirstChild().getNodeValue());
						stars.put(name, dobEL.getFirstChild().getNodeValue());
					}
					catch (Exception e) {
						//System.out.println("Star dob not an integer, set to null");
						stars.put(name, null);
					}
					
				}
				catch (Exception e) {
					count += 1;
					System.out.println(count + ". star name is null, didn't add star");
				}
			}
		}
	}
	
	private void parseCasts(NodeList filmc) {
		if (filmc != null && filmc.getLength() > 0) {
			
			for (int i = 0; i < filmc.getLength(); i++) {
				// get filmc Element
				Element filmcEL = (Element)filmc.item(i);
				
				// get m NodeList and Element
				NodeList m = filmcEL.getElementsByTagName("m");
				for (int j = 0; j < m.getLength(); ++j) {
					Element mEL = (Element)m.item(j);
					
					// get f NodeList and Element
					NodeList f = mEL.getElementsByTagName("f");
					Element fEL = (Element)f.item(0);
					
					// get a NodeList and Element
					NodeList a = mEL.getElementsByTagName("a");
					Element aEL =(Element)a.item(0);

					try {
						String name = aEL.getFirstChild().getNodeValue();
						if (name.indexOf("\"") >= 0 || name.indexOf("`") >=0) {
							name = name.replaceAll("[\"'`]", "'");
						}
						fid_to_a.put(name, aEL.getFirstChild().getNodeValue());
					}
					catch (Exception e) {
						System.out.println("fid or star name is null, didn't add stars_in_movies relation");
					}
					
				}
			}
		}
	}
	
	private void parseMovies(NodeList directorfilms) {
		int total = 0;
		if(directorfilms != null && directorfilms.getLength() > 0) {
			for(int i = 0 ; i < directorfilms.getLength();i++) {
				// getting directorfilm Element
				Element el = (Element)directorfilms.item(i);
				

				
				// director NodeList and Element
				NodeList director = el.getElementsByTagName("director");
				Element directorEL = (Element)director.item(0);
				
				// dirname NodeList and Element
				NodeList dirname = directorEL.getElementsByTagName("dirname");
				Element dirnameEL = (Element)dirname.item(0);
				
				// all films NodeList
				NodeList films = el.getElementsByTagName("films");
				Element filmsEL = (Element)films.item(0);
				
				NodeList film = filmsEL.getElementsByTagName("film");
//				total += film.getLength();
//				System.out.println(total);
				for (int j = 0; j < film.getLength(); j++) {
					
					// current film Element
					Element filmEL = (Element)film.item(j);
					
					// id NodeList and Element
					NodeList id = filmEL.getElementsByTagName("fid");
					Element idEL = (Element) id.item(0);
					
					// current film title
					NodeList filmTitle = filmEL.getElementsByTagName("t");
					Element filmTitleEL = (Element) filmTitle.item(0);
					
					// current film year
					NodeList filmYear = filmEL.getElementsByTagName("year");
					Element filmYearEL = (Element) filmYear.item(0);
					
					// current film cat (genre)
					NodeList cats = filmEL.getElementsByTagName("cats");
					Element catsEL = (Element)cats.item(0);

					HashMap<String, String> movie = new HashMap<String, String>();
					
					addMovieContents(movie, filmTitleEL, filmYearEL, dirnameEL, catsEL, idEL);

					
				}
			}
		}
	}
	
	private void parseDocument() {
		//get the root elememt
		Element docEle = dom.getDocumentElement();
		System.out.println("First element: " + docEle.getTagName());
		NodeList nl = docEle.getElementsByTagName("directorfilms");
		parseMovies(nl);
		
		Element docEleC = castdom.getDocumentElement();
		NodeList nlC = docEleC.getElementsByTagName("filmc");
		parseCasts(nlC);
		
		Element docEleS = stardom.getDocumentElement();
		NodeList nlS = docEleS.getElementsByTagName("actor");
		parseStars(nlS);
		
	}
	
	public static void main(String[] args) {
		ParseXML px= new ParseXML();
		Date start = new Date();
		System.out.println("Starting at: " + start);
		px.run();
		int maxMovie = 1579521;
		int maxGenre = 24;
		int maxStar = 9571154;
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","ajching", "ajching");

            Statement statement = connection.createStatement();
            
            connection.setAutoCommit(false);
            
//            String add_movie = "INSERT INTO movies(id, title, year, director) " +
//            				   "VALUES(?, ?, ?, ?)";
//            PreparedStatement pstatement = connection.prepareStatement(add_movie);
//            
//    		String add_genre = "INSERT INTO genres(id, name)" +
//					"VALUES(?, ?)";
//    		PreparedStatement pgstatement = connection.prepareStatement(add_genre);
//            
//    		String add_genre_to_movie = "INSERT INTO genres_in_movies(genreId, movieId) " +
//    				"VALUES(?, ?)";
//    		PreparedStatement pgtmstatement = connection.prepareStatement(add_genre_to_movie);
//    		
//    		String add_star = "INSERT INTO stars(id, name, birthYear) " +
//    				"VALUES(?, ?, ?)";
//    		PreparedStatement psstatement = connection.prepareStatement(add_star);
//    		
//    		String add_star_to_movie = "INSERT INTO stars_in_movies(starId, movieId) " +
//    				"VALUES(?, ?)";
//    		PreparedStatement pstmstatement = connection.prepareStatement(add_star_to_movie);
            // AWS VERSION
//          String useDB = "use moviedb;";
          
            // LOCAL VERSION
            String useDB = "use cs122b;";
            
            statement.execute(useDB);
            
            for (Map.Entry<String, HashMap<String, String>> entry : movies.entrySet()) {
            	// System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
            	HashMap<String, String> mov = entry.getValue();
            	String genre = genres.get(mov.get("genre"));
            	String star = fid_to_a.get(entry.getKey());
            	String starYear = stars.get(star);
            	String check = "SELECT * FROM movies m WHERE m.title = \"" + mov.get("title") + "\";";
            	
            	ResultSet rs = statement.executeQuery(check);
            	if (!rs.next()) {
            		
            	
            	query = "CALL add_movie(\"" + mov.get("title") + "\", " + mov.get("year") + ", ";
//            	pstatement.setString(1, "tt" + (maxMovie + 1));
//            	maxMovie+=1;
//            	pstatement.setString(2, mov.get("title"));
//            	pstatement.setInt(3, Integer.parseInt(mov.get("year")));
            	try {
//            		pstatement.setString(4, mov.get("director"));
            		query += "\"" + mov.get("director") + "\", ";
            	}
            	catch(Exception e) {
            		System.out.println("no director");
//            		pstatement.setString(4, null);
            		query += "null, ";
            		System.out.println("director set to null");
            	}
            	try {
////            		pstatement.setString(4, genres.get(mov.get("genre")));
            		query += "\"" + genres.get(mov.get("genre")) + "\", ";
            	}
            	catch (Exception e) {
            		System.out.println("no genre");
//
////            		pstatement.setString(4, null);
            		query += "null, ";
            		System.out.println("genre set to null");
//
            	}
//            	if ( genres.get(mov.get("genre")) != null) {
//            		// add the genre if it doesn't exist
//                	String checkGenre = "SELECT * FROM genres g WHERE g.name=\"" + genres.get(mov.get("genre")) + "\";";
//                	ResultSet rs2 = statement.executeQuery(checkGenre);
//                	int genreID = 0;
//                	if (!rs2.next()) {
//
//                        pgstatement.setInt(1, maxGenre);
//                        genreID = maxGenre;
//                        pgstatement.setString(2,  genres.get(mov.get("genre")));
//  
//                        maxGenre += 1;
//                        pgstatement.addBatch();
//                	}
//                	else {
//                		genreID = rs2.getInt(1);
//                	}
//                	
//                	// add genres_in_movies relation
//                	pgtmstatement.setInt(1, genreID);
//                	pgtmstatement.setString(2, "tt" + (maxMovie + 1));
//                	pgtmstatement.addBatch();       	
//            	}
//            	else {
////            		System.out.println("genre is null, not adding");
//            	}
//            	try {
//            		fid_to_a.get(entry.getKey());
//            		// add the star if it doesn't exist
//            		String checkStar = "SELECT * FROM stars s WHERE s.name =\"" + fid_to_a.get(entry.getKey()) + "\";";
//            		ResultSet rs3 = statement.executeQuery(checkStar);
//            		if(!rs3.next()) {
//
//                		psstatement.setString(1, "nm" + maxStar);
//            			psstatement.setString(2, fid_to_a.get(entry.getKey()));
//            			try {
//                			psstatement.setInt(3, Integer.parseInt(stars.get(fid_to_a.get(entry.getKey()))));
//            			}
//            			catch (Exception e) {
//            				psstatement.setInt(3, 0);
//            			}
//                		psstatement.addBatch();
//
//            		}
//            	}
//            	catch (Exception e) {
//            		System.out.println(e);
//            		System.out.println("Not adding star, no name");
//            	}

            	
            	try {

//            		pstatement.setString(5, fid_to_a.get(entry.getKey()));
            		query += "\"" + fid_to_a.get(entry.getKey()) + "\", ";
            	}
            	catch(Exception e) {
            		System.out.println("no star");
//
//            		pstatement.setString(5, null);
            		query += "null, ";
            		System.out.println("star set to null");

            	}
//            	try {
//                	pstatement.setInt(6, Integer.parseInt(stars.get(star)));
//
//            	}
//            	catch(Exception e) {
//            		pstatement.setInt(6, 0);
//            	}
            	
            	query += stars.get(star) + ");";
//            	System.out.println(pstatement.toString());
//            	if (Integer.parseInt(mov.get("year")) != 1937) {
//            		//System.out.println("!!!!!!!!!!!!!!!!!!");
//                	pstatement.addBatch();
//
//            	}
//            	statement.addBatch(query);
//            	System.out.println(query);
            	statement.executeQuery(query);
            	}
            }
            Date end = new Date();
            System.out.println("Start: " + start);
            System.out.println("Finish: " + end);
            System.out.println(movies.size());
//            int[] count = pstatement.executeBatch();
//            int[] count2 = pgstatement.executeBatch();
//            int[] count3 = pgtmstatement.executeBatch();
//            int[] count4 = psstatement.executeBatch();
            connection.commit();
//            System.out.println(count);
		}
        catch (SQLException ex) {
            while (ex != null) {
                  System.out.println ("SQL Exception:  " + ex.getMessage ());
                  System.out.println(ex.getErrorCode());
                  //System.out.println(pstatement.)
                  ex = ex.getNextException ();
              }  
          }  
        catch(java.lang.Exception ex)
        {
                System.out.println ("Exception:  " + ex.getMessage ());
                
        }
	}
	
	
}