package by.htp.library.dao.imple;

import static by.htp.library.dao.util.MainSqlPropertyManager.getLogin;
import static by.htp.library.dao.util.MainSqlPropertyManager.getPass;
import static by.htp.library.dao.util.MainSqlPropertyManager.getUrl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionBD {
	private static PreparedStatement st;

	static PreparedStatement conectionWithDB(String url) throws SQLException {
//		Properties prop = new Properties();
//		prop.put("user", getLogin());
//		prop.put("password", getPass());
//		prop.put("characterEncoding", "Cp1251");
		Connection conn = DriverManager.getConnection(getUrl(),getLogin(),getPass());

		st = conn.prepareStatement(url);
		return st;
	}
}
