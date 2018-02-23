

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
//import java.util.List;
//import java.io.FileWriter;


//
//import org.jdom2.input.SAXBuilder;
//
//import org.jdom2.output.XMLOutputter;
//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.JDOMException;

import java.io.PrintWriter;


@WebServlet("/MovieList")
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private static List<Element> allChildren;
//	private static Element body;
//	private static Document doc;
//	private static SAXBuilder builder = new SAXBuilder();
    public MovieList() throws IOException {
        super();


    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("Hello!");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected static void setupJDOM(String path) throws IOException {

        //--------------JDOM HTML EDITING----------------------------------------------------
//        builder = new SAXBuilder();
//        doc = builder.build(path);


       
//        Element webapp = doc.getRootElement();
//        allChildren = webapp.getChildren();
//        System.out.println("First kid: " + ((Element)allChildren.get(0)).getName());
//        body = ((Element)allChildren.get(1));
       // body.setContent(new Element(null));
	}
	
	protected static void addMovie(String title, int year, String director, String genres, String stars, double rating, int rank) {
//		Element div = new Element("div").setAttribute("class", "movie-div");
//		Element _title = new Element("h2").setAttribute("class", "movie-title").setText(Integer.toString(rank) + ". " + title);
//		Element _year = new Element("h3").setAttribute("class", "movie-info").setText(" " + Integer.toString(year));
//		Element _director = new Element("h3").setAttribute("class", "movie-info").setText("Directed by " + director);
//		Element _genres = new Element ("h3").setAttribute("class", "movie-genres").setText(genres);
//		Element _stars = new Element("h3").setAttribute("class", "movie-stars").setText("Starring: " + stars);
//		Element _rating = new Element("h3").setAttribute("class", "movie-rating").setText("Rating: " + Double.toString(rating));
		
//
//		div.addContent(_title).addContent(_rating).addContent(_director).addContent(_year).addContent(_genres).addContent(_stars);
//		body.addContent(div);

	}
	public static void main(String[] args) throws Exception{


        // Incorporate mySQL driver
        Class.forName("com.mysql.jdbc.Driver").newInstance();

         // Connect to the test database
        Connection connection = DriverManager.getConnection("jdbc:mysql:///cs122b?autoReconnect=true&useSSL=false","root", "Username42051");

        // setup html file
        setupJDOM("WebContent/index.html");

        
        // FIRST QUERY -----------------------------------------------------------------------
        Statement select = connection.createStatement();
        ResultSet result = select.executeQuery("SELECT m2.id, m2.title, m2.year, m2.director, r.rating\r\n" + 
        		"FROM movies m2, ratings r\r\n" + 
        		"WHERE r.moviesId = m2.id\r\n" + 
        		"ORDER BY r.rating DESC\r\n" + 
        		"LIMIT 20;");

        // Get metatdata from stars; print # of attributes in table
        System.out.println("The results of the query");
        ResultSetMetaData metadata = result.getMetaData();
        System.out.println("There are " + metadata.getColumnCount() + " columns");

        // Print type of each attribute
        for (int i = 1; i <= metadata.getColumnCount(); i++)
                System.out.println("Type of column "+ i + " is " + metadata.getColumnTypeName(i));

        
        
        int rank = 1;
        while (result.next())
        {
        		// GET THE GENRES OF THIS MOVIE ---------------------------------------------
 
        		Statement genreStatement = connection.createStatement();
              	ResultSet genreSet = genreStatement.executeQuery("SELECT g.name \r\n" + 
              			"FROM genres g, movies m1, genres_in_movies gm \r\n" + 
              			"WHERE m1.id = gm.movieId AND g.id = gm.genreId AND m1.id = \"" + 
              			result.getString(1) + "\";");
              	String genres = "";
              	while (genreSet.next()) {
              		genres += genreSet.getString(1);
              		if (!genreSet.isLast()) {
              			genres += ", ";
              		}
              	}
              	
              	// GET THE STARS OF THIS MOVIE -----------------------------------------------
        		Statement starStatement = connection.createStatement();
              	ResultSet starSet = starStatement.executeQuery("SELECT s.name\r\n" + 
              			"FROM stars s, movies m, stars_in_movies sm\r\n" + 
              			"WHERE m.id = sm.movieId AND s.id = sm.starId AND m.id = \""+ 
              			result.getString(1) +"\";");
              	String stars = "";
              	while (starSet.next()) {
              		stars += starSet.getString(1);
              		if (!starSet.isLast()) {
              			stars += ", ";
              		}
              	}
              	
              	addMovie(result.getString(2), result.getInt(3), result.getString(4), genres, stars, result.getDouble(5), rank);
              	rank += 1;
        }
//        XMLOutputter fmt = new XMLOutputter();
//        fmt.output(doc, new FileWriter("WebContent/index.html"));
	}


}
