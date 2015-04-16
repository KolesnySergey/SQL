import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Conn {
	
	public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;
    
    // --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
    public static void connect() throws ClassNotFoundException, SQLException 
    {
           conn = null;
           Class.forName("org.sqlite.JDBC");
           conn = DriverManager.getConnection("jdbc:sqlite:Authorization.s3db");
           System.out.println("Database connected!");
           statmt = conn.createStatement();
    }
    
    public static void reWriteDB() throws SQLException, ParseException
    {
    	statmt.execute("DELETE FROM 'roles'");
    	statmt.execute("DELETE FROM sqlite_sequence WHERE name='roles'");
    	statmt.execute("DELETE FROM 'users'");
    	statmt.execute("DELETE FROM sqlite_sequence WHERE name='users'");
    	//Dobavljaem "roli"
    	addRole("Administrator");
    	addRole("Moderator");
    	addRole("User");
    	addRole("Redaktor");
    	addRole("Gl.redaktor");
    	
    	//Dobavljaem pol'zovatelej
		addUser("Admin","12345","Administrator", "11.11.1992", 1);
		addUser("Ivan","123456","Ivan", "12.11.1992", 2);
		addUser("Gena","123451","Gennadij", "14.11.1990", 3);
		addUser("Seva","123450","Seva", "01.01.1994", 4);
		addUser("Onegin","123451","Evgenij", "01.01.1985", 5);
		addUser("Sniper","123452","Pushkin", "10.01.1990", 2);
		addUser("Obana","123453","Obama", "01.01.1989", 3);
		addUser("Putin","123454","Putin", "01.01.1988", 5);
		addUser("Ivanov","123455","Ivanov", "01.01.1987", 4);
		addUser("Petrov","123457","Petrov", "01.01.1986", 2);
		addUser("Sidorov","123458","Sidorov", "01.01.1991", 2);
		addUser("Vasya","1234580","Vasja", "01.01.1992", 3);
		addUser("Qwerty","1234581","Qwerty", "01.01.1941", 2);
		addUser("User","123458","User", "01.01.1971", 5);
		addUser("NotUser","123458","NotUser", "01.01.1961", 4);
		addUser("NotUser1","123458","NotUser1", "", 4);
		
		System.out.println("Tablica \"roles\" zapolnena");
		System.out.println("Tablica \"users\" zapolnena");
    }
    
    static void deleteUserById(int id) throws SQLException, ParseException
    {
    	System.out.println("After delete user #"+id+": ");
    	statmt.execute("DELETE FROM users WHERE id = "+id);
    	printAllUsers();
    }
    
    static void addUser(String login, String password, String username, String birthdate, Integer roleId) throws SQLException, ParseException
    {
    	SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
    	Date dt = new Date();
    	try { dt = fmt.parse(birthdate); }
    	catch(ParseException ex) { dt = null; }
    
    	try
    	{
	    	statmt.execute("INSERT INTO 'users' ('login', 'password','username','birthdate','role_id') "
	    			+ "VALUES ('"+login+"', '"+password+"', '"+username+"','"+dt+"', "+roleId+")");
    	}
    	catch(SQLException ex)
    	{
    		System.out.println("User already exists! Message error: " + ex.getLocalizedMessage());
    	}
    }
    
    static void addRole(String name)
    {
    	try
    	{
	    	statmt.execute("INSERT INTO 'roles' ('name') VALUES ('"+name+"')");
    	}
    	catch(SQLException ex)
    	{
    		System.out.println("Role already exists! Message error: " + ex.getLocalizedMessage());
    	}
    }
    
    public static void readUsersBdByDate(String start, String end) throws SQLException, ParseException
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    	Date startDate = dateFormat.parse(start);
    	Date endDate = dateFormat.parse(end);
    	
    	System.out.println("User list between "+start+" to "+end+": ");
    	resSet = statmt.executeQuery("SELECT * FROM users WHERE (birthdate BETWEEN '"+startDate+"' AND '"+endDate+"')");
    	printUsers();
    }
    
    public static void readUsersDbLimit(int limit) throws SQLException, ParseException
    {
    	System.out.println(limit + " users: ");
    	resSet = statmt.executeQuery("SELECT * FROM users LIMIT "+limit);
    	printUsers();
    }
    
    public static void readUsersDbLimitStart(int limit) throws SQLException, ParseException
    {
    	System.out.println(limit + " users from 3: ");
    	resSet = statmt.executeQuery("SELECT * FROM users LIMIT 3, "+limit);
    	printUsers();
    }
    
    public static void getUsersWithoutBirthdate() throws SQLException, ParseException 
    {
    	System.out.println("Users list without birth date: ");
    	resSet = statmt.executeQuery("SELECT * FROM users WHERE birthdate = '" + null + "'");
    	printUsers();
    }
    
    public static void readUsersDB() throws SQLException, ParseException
    {
    	System.out.println("All users: ");
    	resSet = statmt.executeQuery("SELECT * FROM users");
    	printUsers();
    }
    
    public static void printAllUsers() throws SQLException, ParseException
    {
    	resSet = statmt.executeQuery("SELECT * FROM users");
    	printUsers();
    }
    
    public static void printUsers() throws SQLException, ParseException
    {
    	System.out.println("----------------------------------------------------------------------------------");
		while(resSet.next())
		{
		    int id = resSet.getInt("id");
		    String login = resSet.getString("login");
		    String password = resSet.getString("password");
		    String username = resSet.getString("username");
		    Date birthdate = resSet.getDate("birthdate");
		    int roleId = resSet.getInt("role_id");
		
		    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		    
		    System.out.println("ID: "+id);
		    System.out.println("Login: "+login);
		    System.out.println("Pasword: "+password);
		    System.out.println("Username: "+username);
		    System.out.println("Birthdate: "+ dateFormat.format(birthdate).toString());
		    System.out.println("Role ID: "+roleId);
		    System.out.println("----------------------------------------------------------------------------------");
		}	
		System.out.println("Table showed!");
    }
    
     
	   public static void closeDB() throws ClassNotFoundException, SQLException
	   {
			conn.close();
			statmt.close();
			resSet.close();
			
			System.out.println("Connection closed!");
	   }


}
