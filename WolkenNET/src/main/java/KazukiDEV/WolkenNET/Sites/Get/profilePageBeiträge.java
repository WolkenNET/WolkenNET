package KazukiDEV.WolkenNET.Sites.Get;

import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.BBCode;
import KazukiDEV.WolkenNET.Content.Contribution;
import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class profilePageBeiträge implements Route {
	public Map<String, Object> m = new HashMap<>();

	public profilePageBeiträge() {
	}

	public Object handle(Request request, Response response) {
		m.put("l", 3);
		try {
			Permissions.hasPermissions(request, this.m, response);
			m.put("banner", "/img/banner/wolken2.jpg");
			String userName = request.params("user").replaceAll("%20", " ");
			m.put("titlebar", "Beiträge von " + userName);
			String userSQL = "SELECT * FROM `users` WHERE username = ?";
			ResultSet userRS = mysql.Query(userSQL, userName);
			int userID = 0;
			String perm = null;
			String avatar = "1";
			while (userRS.next()) {
				avatar = userRS.getString("avatar");
				userID = userRS.getInt("id");
				perm = userRS.getString("permissions");
				m.put("perm", Integer.parseInt(perm));
				m.put("uname", userName);
				m.put("auth", userRS.getString("authority"));
				m.put("registered_on", userRS.getString("registered_on"));
				m.put("last_login", userRS.getString("last_login"));
				m.put("uavatar", userRS.getString("avatar"));
			}

			String lockedSQL = " AND `locked` = 0";
			if (((String) m.get("permissions")).contains("10")) {
				lockedSQL = " ";
			}

			String sql_count = "SELECT * FROM `contributions` WHERE `user_id` = ?" + lockedSQL
					+ " ORDER BY `contributions`.`views` DESC";
			ResultSet countset = mysql.Query(sql_count, new StringBuilder().append(userID).toString());
			ArrayList<Contribution> contarr = new ArrayList<Contribution>();
			while (countset.next()) {
				Contribution cont = new Contribution();
				if (countset.getString("bbcode_text").length() < 100) {
					cont.setBbcode_text(BBCode.bbcode_th(countset.getString("bbcode_text").replaceAll("\\<[^>]*>", ""))
							.replaceAll("\\[.*?\\]", " "));
				} else {
					cont.setBbcode_text(BBCode
							.bbcode_th(countset.getString("bbcode_text").substring(0, 100).replaceAll("\\<[^>]*>", ""))
							.replaceAll("\\[.*?\\]", " "));
				}
				cont.setLocked(countset.getInt("locked"));
				cont.setTimestamp(countset.getString("timestamp"));
				cont.setSublink(countset.getString("sublink"));
				cont.setViews(countset.getInt("views"));
				cont.setUsername(userName);
				cont.setUserid(userID + "");
				cont.setAvatar(avatar);
				cont.setPerm(perm);

				contarr.add(cont);
			}

			this.m.put("conts", contarr);

			Template template = App.cfg.getTemplate("profileList.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (Exception e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
