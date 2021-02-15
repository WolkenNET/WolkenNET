package KazukiDEV.WolkenNET.Content;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import KazukiDEV.WolkenNET.Main.App;
import spark.Response;

public class Permissions {

	public static boolean hasPermissions(String cookie, Map<String, Object> map, Response r) {
		r.type("text/html");
		App.sessionViews++;
		try {
			String sql_settings = "SELECT * FROM `system_settings`";
			ResultSet sql_rs = mysql.Query(sql_settings);

			while (sql_rs.next()) {
				int i = sql_rs.getInt("id");
				if (i == 1) {
					map.put("recaptcha", sql_rs.getString("value_string"));
				} else if (i == 2) {
					map.put("registrations", sql_rs.getInt("value_int"));
				} else if (i == 3) {
					map.put("home_alert", sql_rs.getString("value_string"));
				} else if (i == 4) {
					map.put("home_bool", sql_rs.getInt("value_int"));
				}
			}

			String sql = "SELECT * FROM `users` WHERE `session` = ? AND `banned` = 0";
			ResultSet rs = mysql.Query(sql, cookie);
			if (rs.next()) {
				map.put("username", rs.getString("username"));
				map.put("avatar", rs.getString("avatar"));
				map.put("userid", Integer.valueOf(rs.getInt("id")));
				map.put("useridst", new StringBuilder().append(rs.getInt("id")).toString());
				map.put("permissions", rs.getString("permissions"));
				map.put("loggedin", "true");
				TimeZone tz = TimeZone.getTimeZone("UTC");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
				df.setTimeZone(tz);
				String nowAsISO = df.format(new Date());
				//TODO: Last login update
				mysql.Exec("UPDATE `users` SET `last_login`=? WHERE `id` = ?", nowAsISO, rs.getInt("id")+"");
				return true;
			}

		} catch (Exception e) {
			new errorManager(e);
			e.printStackTrace();
		}
		map.put("loggedin", "false");
		map.put("permissions", "0");
		map.put("userid", Integer.valueOf(0));
		map.put("useridst", "");

		return false;
	}

}
