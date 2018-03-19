

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

@WebServlet("/AddStarServlet")
public class AddStarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AddStarServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("star-name");
		
		System.out.println("name: " + name);
		
		int year = 0;
		try {
			year = Integer.parseInt(request.getParameter("star-year"));
		}
				
		catch(NumberFormatException ex) {
			System.out.println("Not a numerical year!");
			String res = "{\"status\":\"failed\", \"message\":\"Birth year is not an integer!\"}";
			response.getWriter().write(res);
			return;
		}	
		System.out.println("year: " + year);

		if (name == null || year == 0) {
			response.getWriter().write("{\"status\":\"failed\", \"message\":\"Please fill out both inputs!\"}");
			return;
		}
		
		
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
            
//            Statement statement = connection.createStatement();

//			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
//			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false","root", "Username42051");
			
            
            String useDB = "use cs122b;";

            String query = "SELECT s.name FROM stars s WHERE s.name = \"" + name + "\" and s.birthYear = \"" + year + "\";";
            String getID = "SELECT * FROM maxID WHERE id = 1;";

//            statement.execute(useDB);
            
            connection.setAutoCommit(false);
            PreparedStatement pstatement;
            pstatement = connection.prepareStatement(useDB);
            pstatement.execute();
            
            System.out.println("CHECK STAR: " + query);
            System.out.println("GET ID: " + getID);
            
//            ResultSet rs = statement.executeQuery(query);
            pstatement = connection.prepareStatement(query);
            ResultSet rs = pstatement.executeQuery();
            
            if (rs.next()) {
            	response.getWriter().write("{\"status\":\"failed\", \"message\":\"Already in database\"}");
            }
            else {
                pstatement = connection.prepareStatement(getID);
                ResultSet rs2 = pstatement.executeQuery();
//            	ResultSet rs2  = statement.executeQuery(getID);
            	rs2.next();
            	int id = rs2.getInt(2) + 1;
            	
                pstatement = connection.prepareStatement("INSERT INTO stars VALUES(\"" + "nm" + id + "\", \"" + name +"\", \"" + year +"\");");
                pstatement.execute();
                pstatement = connection.prepareStatement("UPDATE maxID SET maxID = maxID + 1 WHERE id = 1;");
                pstatement.execute();
//            	statement.execute("INSERT INTO stars VALUES(\"" + "nm" + id + "\", \"" + name +"\", \"" + year +"\");");
//            	statement.execute("UPDATE maxID SET maxID = maxID + 1 WHERE id = 1;");
            	
                connection.setAutoCommit(true);
                connection.commit();
            	connection.close();

            	response.getWriter().write("{\"status\":\"success\", \"message\": \"New star named " + name + " has been added with id nm" + id +"!\"}");
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

}
