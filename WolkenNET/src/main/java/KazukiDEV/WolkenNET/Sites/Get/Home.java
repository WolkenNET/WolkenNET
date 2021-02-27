package KazukiDEV.WolkenNET.Sites.Get;

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
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;

public class Home implements Route {
	public Map<String, Object> m = new HashMap<>();

	public Home() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request, this.m, response);
		m.put("titlebar", "Home");
		m.put("banner", "/img/banner/home.jpg");

		String registeredCountSQL = "SELECT COUNT(*) AS total FROM `users`";
		ResultSet registeredCountRS = mysql.Query(registeredCountSQL);
		int users = 0;
		try {
			registeredCountRS.next();
			users = registeredCountRS.getInt("total");
		}catch(Exception e) { new errorManager(e); }
			m.put("registered", users + "");

		try {
			Template template = App.cfg.getTemplate("home.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
