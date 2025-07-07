package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection connect;
    
    public DBContext(){
         try{
             String url = "jdbc:sqlserver://"+serverName+":"+portNumber +
                ";databaseName="+dbName;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
            connect = DriverManager.getConnection(url, userID, password);
         }catch (ClassNotFoundException | SQLException ex){
             //
         }
     }

    private final String serverName = "localhost";
    private final String dbName = "HotelManagementDBQuantity";
    private final String portNumber = "1433";
    private final String userID = "sa";
    private final String password = "123";
}
