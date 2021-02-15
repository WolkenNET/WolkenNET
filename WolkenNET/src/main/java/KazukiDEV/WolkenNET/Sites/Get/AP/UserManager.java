package KazukiDEV.WolkenNET.Sites.Get.AP;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.User;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserManager implements Route {
	public Map<String, Object> m = new HashMap<>();

	public String type = "";

	public UserManager(String type) {
		this.type = type;
	}

	public Object handle(Request request, Response response) {
		m.put("titlebar", "AP User Manager");

		Permissions.hasPermissions(request, this.m, response);
		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		try {
			ArrayList<User> tag_array = new ArrayList<User>();
			String tags_sql = "SELECT * FROM `users`";
			ResultSet tags_rs = mysql.Query(tags_sql, new String[0]);
			while (tags_rs.next()) {

				User t = new User();
				t.setID(tags_rs.getInt("id"));
				t.setAuthority(tags_rs.getString("authority"));
				t.setCountry(tags_rs.getString("country"));
				t.setUsername(tags_rs.getString("username"));
				t.setMail(tags_rs.getString("email"));

				tag_array.add(t);
			}
			this.m.put("tags", tag_array);
		} catch (Exception exception) {
			new errorManager(exception);
		}
		try {
			Template template = App.cfg.getTemplate("ap/user.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
