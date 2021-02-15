package KazukiDEV.WolkenNET.Content;

import java.sql.ResultSet;
import java.sql.SQLException;

import KazukiDEV.WolkenNET.Config.u;

public class Moderation {

	public static void BanUser(String username) {
		u.s.println(u.warning + "The User " + Color.RED + username + Color.RESET + " was banned");
		String ban_sql = "UPDATE `users` SET `banned`= true, `session` = ? WHERE `username` = ?";
		mysql.Exec(ban_sql, sessionHandler.randomString(30), username);
	}
	
	public static void deleteContribution(int id) {
		u.s.println(u.warning + "The Contribution with the ID: " + Color.RED + new StringBuilder().append(id).toString() + Color.RESET + " was deleted");
		String sql = "DELETE FROM `contributions` WHERE `id` = ?";
		mysql.Exec(sql, id+"");
	}
	
	public static void lockContribution(int id) throws SQLException {
		String sql = "SELECT `locked` FROM `contributions` WHERE `id` = ?";
		ResultSet sql_rs = mysql.Query(sql, id+"");
		while(sql_rs.next()) {
			Boolean locked = sql_rs.getBoolean("locked");
			String updatesql = "UPDATE `contributions` SET `locked`=? WHERE `id` = ?";
			if(locked == true) {
				mysql.Exec(updatesql, "0", id+"");
				u.s.println(u.warning + "The Contribution with the ID: " + Color.RED + new StringBuilder().append(id).toString() + Color.RESET + " was unlocked");
				
			}else {
				mysql.Exec(updatesql, "1", id+"");
				u.s.println(u.warning + "The Contribution with the ID: " + Color.RED + new StringBuilder().append(id).toString() + Color.RESET + " was locked");
			}
		}
	}
}
