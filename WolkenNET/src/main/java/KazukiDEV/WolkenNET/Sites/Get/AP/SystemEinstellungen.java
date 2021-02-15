package KazukiDEV.WolkenNET.Sites.Get.AP;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class SystemEinstellungen implements Route {
	public Map<String, Object> m = new HashMap<>();

	public SystemEinstellungen() {
	}

	public Object handle(Request request, Response response) {
		m.put("titlebar", "AP System Einstellungen");

		Permissions.hasPermissions(request.cookie("session"), this.m, response);
		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		String sql_settings = "SELECT * FROM `system_settings`";
		ResultSet sql_rs = mysql.Query(sql_settings);
		try {
			while (sql_rs.next()) {
				int i = sql_rs.getInt("id");
				if (i == 1) {
					m.put("recaptcha", sql_rs.getString("value_string"));
				} else if (i == 2) {
					m.put("registrations", sql_rs.getInt("value_int"));
				} else if (i == 3) {
					m.put("home_alert", sql_rs.getString("value_string"));
				} else if (i == 4) {
					m.put("home_bool", sql_rs.getInt("value_int"));
				}

			}
		} catch (Exception e) {
			new errorManager(e);
		}

		try {
			Template template = App.cfg.getTemplate("ap/systemsettings.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
