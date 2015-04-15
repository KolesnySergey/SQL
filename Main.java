import java.sql.SQLException;
import java.text.ParseException;

public class Main {

	public static void main(String[] args)  throws ClassNotFoundException, SQLException, ParseException {
		Conn.connect();
        Conn.reWriteDB();
        Conn.readUsersDB();
        Conn.getUsersWithoutBirthdate();
        Conn.readUsersDbLimit(5);
        Conn.readUsersDbLimitStart(5);
        Conn.readUsersBdByDate("01.01.1992", "01.01.2014");
        Conn.closeDB();
	}

}
