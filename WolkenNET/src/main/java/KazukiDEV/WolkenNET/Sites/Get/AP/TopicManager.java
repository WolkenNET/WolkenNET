package KazukiDEV.WolkenNET.Sites.Get.AP;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.Topic;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class TopicManager implements Route {
	public Map<String, Object> m = new HashMap<>();

	public String type = "";

	public TopicManager(String type) {
		this.type = type;
	}

	public Object handle(Request request, Response response) {
		m.put("titlebar", "AP Themen Manager");

		Permissions.hasPermissions(request, this.m, response);
		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		if (this.type.equals("edit")) {
			String id = request.queryParams("id");

			m.put("tt", "edit");

			String tag_sql = "SELECT * FROM `topics` WHERE `id` = " + id;

			try {

				ResultSet up_next_rs = mysql.Query(tag_sql, new String[0]);
				while (up_next_rs.next()) {
					m.put("ttitle", up_next_rs.getString("title"));
					m.put("ticon", up_next_rs.getString("icon"));
					m.put("tdescription", up_next_rs.getString("description"));
					m.put("tsublink", up_next_rs.getString("sublink"));
					m.put("tgroupid", up_next_rs.getString("groupid"));
					m.put("timportant", up_next_rs.getString("important"));
					m.put("tid", id);
				}

			} catch (Exception ex) {
				new errorManager(ex);
				ex.printStackTrace();
			}

		} else if (this.type.equals("create")) {
			m.put("tt", "create");

			m.put("ttitle", "");
			m.put("ticon", "");
			m.put("tdescription", "");
			m.put("tsublink", "");
			m.put("tgroupid", "");
			m.put("timportant", "");
			m.put("tid", "");
		} else {
			if (type.equals("delete")) {
				String id = request.queryParams("id");
				if (id == null) {
					response.redirect("/ap/topic");
					return null;
				}
				try {
					mysql.Exec("DELETE FROM `topics` WHERE `id` = ?", id + "");
				} catch (Exception e) {
					new errorManager(e);
				}

				response.redirect("/ap/topic");
				return null;

			}
			if (this.type.equals("manager")) {
				try {
					ArrayList<Topic> tag_array = new ArrayList<Topic>();
					String tags_sql = "SELECT * FROM `topics`";
					ResultSet tags_rs = mysql.Query(tags_sql, new String[0]);
					while (tags_rs.next()) {

						Topic t = new Topic();
						t.setID(tags_rs.getInt("id"));
						t.setDescription(tags_rs.getString("description"));
						t.setTitle(tags_rs.getString("title"));
						t.setIcon(tags_rs.getString("icon"));
						t.setSublink(tags_rs.getString("sublink"));
						t.setGroupid(tags_rs.getString("groupid"));
						t.setImportant(tags_rs.getString("important"));

						tag_array.add(t);
					}
					this.m.put("tags", tag_array);
				} catch (Exception exception) {
					new errorManager(exception);
				}
				try {
					Template template = App.cfg.getTemplate("ap/topic.html");
					Writer out = new StringWriter();
					template.process(this.m, out);
					return out.toString();
				} catch (IOException | freemarker.template.TemplateException e) {
					new errorManager(e);
					throw new RuntimeException(e);
				}
			}
		}
		try {
			Template template = App.cfg.getTemplate("ap/topicc.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
