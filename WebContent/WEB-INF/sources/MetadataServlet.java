

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@WebServlet("/MetadataServlet")
public class MetadataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public MetadataServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			

            // write query
            
            
            // AWS VERSION
//            String useDB = "use moviedb;";
            
            // LOCAL VERSION
            String useDB = "use cs122b;";
            
            String query = "show tables;";
            
            statement.execute(useDB);
            
            String responseObject = "{";
            
            System.out.println("fetching tables");
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
            	String table = rs.getString(1);
            	Statement s = connection.createStatement();
            	ResultSet rsTemp = s.executeQuery("describe " + table + ";");
            	responseObject += "\"" + table + "\": {";
            	while(rsTemp.next()) {
            		responseObject += "\"" + rsTemp.getString(1) + "\": {\"type\":\"" + rsTemp.getString(2) + "\", \"Null\": \"" + rsTemp.getString(3) + "\", \"Primary\":\"" + rsTemp.getString(4) + "\"}";
            		if (!rsTemp.isLast()) {
            			responseObject += ", \n";
            		}
            	}
            	responseObject += "}";
            	if (!rs.isLast()) {
            		responseObject += ", ";
            	}
            }
        	connection.close();

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
