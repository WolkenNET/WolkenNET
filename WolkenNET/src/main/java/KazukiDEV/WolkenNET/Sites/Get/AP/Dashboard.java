package KazukiDEV.WolkenNET.Sites.Get.AP;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Dashboard implements Route {
	public Map<String, Object> m = new HashMap<>();

	public Dashboard() {
	}

	public Object handle(Request request, Response response) throws Exception {
		Permissions.hasPermissions(request.cookie("session"), this.m, response);
		m.put("titlebar", "AP Dashboard");
		m.put("sessionviews", App.sessionViews + "");
		m.put("sessionsql", App.sessionSQL + "");
		
		String registeredUsersSQL = "SELECT * FROM `users`";
		int users = 0;
		try {
			ResultSet up_next_rs = mysql.Query(registeredUsersSQL);
			while (up_next_rs.next()) {
				users++;
			}
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		m.put("registered", users +"");
		
		String topicSQL = "SELECT * FROM `topics`";
		int topics = 0;
		try {
			ResultSet up_next_rs = mysql.Query(topicSQL);
			while (up_next_rs.next()) {
				topics++;
			}
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		m.put("topics", topics +"");
		
		String contSQL = "SELECT * FROM `contributions`";
		int conts = 0;
		try {
			ResultSet up_next_rs = mysql.Query(contSQL);
			while (up_next_rs.next()) {
				conts++;
			}
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		m.put("conts", conts +"");
		
		String commentSQL = "SELECT * FROM `comments`";
		int comments = 0;
		try {
			ResultSet up_next_rs = mysql.Query(commentSQL);
			while (up_next_rs.next()) {
				comments++;
			}
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		m.put("comments", comments +"");
		
		
		

		try {
			Template template = App.cfg.getTemplate("/ap/dashboard.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
