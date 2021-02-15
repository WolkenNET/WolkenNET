package KazukiDEV.WolkenNET.Sites.Get;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.BBCode;
import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class profileSettings implements Route {
	public Map<String, Object> m = new HashMap<>();

	public profileSettings() {
	}

	public Object handle(Request request, Response response) {
		m.put("l", 4);
		try {
			Permissions.hasPermissions(request, this.m, response);
			m.put("banner", "/img/banner/wolken2.jpg");
			String userName = request.params("user").replaceAll("%20", " ");
			m.put("titlebar", "Profil von " + userName);
			String userSQL = "SELECT * FROM `users` WHERE username = ?";
			ResultSet userRS = mysql.Query(userSQL, userName);
			int userID = 0;
			@SuppressWarnings("unused")
			String perm = "";
			while (userRS.next()) {
				userID = userRS.getInt("id");
				perm = userRS.getInt("permissions") + "";
				m.put("perm", userRS.getInt("permissions"));
				m.put("auth", userRS.getString("authority"));
				m.put("registered_on", userRS.getString("registered_on"));
				m.put("last_login", userRS.getString("last_login"));
				m.put("uname", userName);
				m.put("uavatar", userRS.getString("avatar"));
			}
			
			String userExtraSQL = "SELECT * FROM `users_extra` WHERE `id` = ?";
			ResultSet userExtraRS = mysql.Query(userExtraSQL, userID + "");
			while (userExtraRS.next()) {
				m.put("bbinfo", BBCode.bbcode(userExtraRS.getString("bbcode_text")));
			}
			
			

			Template template = App.cfg.getTemplate("settings.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (Exception e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
