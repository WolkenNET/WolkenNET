package KazukiDEV.WolkenNET.Content;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import KazukiDEV.WolkenNET.Main.App;
import spark.Request;
import spark.Response;

public class Permissions {

	public static boolean hasPermissions(Request req, Map<String, Object> map, Response res) {
		res.type("text/html");
		App.sessionViews++;
		String cookie = req.cookie("session");
		String agent = req.headers("user-agent");
		try {
			String settingsSQL = "SELECT * FROM `system_settings`";
			ResultSet settingsRS = mysql.Query(settingsSQL);
			while (settingsRS.next()) {
				int i = settingsRS.getInt("id");
				if (i == 1) {
					map.put("recaptcha", settingsRS.getString("value_string"));
				} else if (i == 2) {
					map.put("registrations", settingsRS.getInt("value_int"));
				} else if (i == 3) {
					map.put("home_alert", settingsRS.getString("value_string"));
				} else if (i == 4) {
					map.put("home_bool", settingsRS.getInt("value_int"));
				}
			}
			if(!(cookie == null)) {
				if(!(cookie == "")) {
					sessionHandler sess = new sessionHandler(cookie, agent);
					if(sess.checkSession() == true)  {
						int uid = sess.userid;
						String userSQL = "SELECT `username`,`permissions`,`banned`,`avatar` FROM `users` WHERE `id` = ?";
						ResultSet userRS = mysql.Query(userSQL, uid+"");
						while(userRS.next()) {
							if(userRS.getBoolean("banned") == false) {
								map.put("username", userRS.getString("username"));
								map.put("avatar", userRS.getString("avatar"));
								map.put("userid", uid);
								map.put("useridst", uid+"");
								map.put("permissions", userRS.getString("permissions"));
								map.put("loggedin", "true");
								TimeZone tz = TimeZone.getTimeZone("UTC");
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");df.setTimeZone(tz);String lastLogin = df.format(new Date());
								mysql.Exec("UPDATE `users` SET `last_login`=? WHERE `id` = ?", lastLogin, uid+"");
								return true;
							}
						}
					}
				}
			}
		}catch(Exception e) {
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
