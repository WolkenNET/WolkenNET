package KazukiDEV.WolkenNET.Sites.Get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Themen implements Route {
	public Map<String, Object> m = new HashMap<>();

	public Themen() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request, this.m, response);
		
		String afterSQL = "";
		int page = 0;
		if (request.queryParams("page") != null) {
			int offset = Integer.parseInt(request.queryParams("page")) * 10;
			page = Integer.parseInt(request.queryParams("page"));
			offset = offset - 10;
			afterSQL = "LIMIT 10 OFFSET " + new StringBuilder().append(offset);
		} else {
			page = 1;
			afterSQL = "LIMIT 10";
		}
		m.put("page", page);

		String sql = "SELECT * FROM `topics` WHERE `sublink` = ?";
		ResultSet psql = mysql.Query(sql, request.params(":topic"));
		String groupid = "";
		int topicid = 0;
		try {

			while (psql.next()) {
				m.put("id", psql.getInt("id"));
				m.put("titlebar", psql.getString("title"));
				groupid = psql.getString("groupid");
				m.put("groupid", groupid);
				m.put("icon", psql.getString("icon"));
				m.put("description", psql.getString("description"));
				m.put("important", psql.getString("important"));
				m.put("banner", "/img/banner/sonnenuntergang.jpg");

				String lockedSQL = " AND `locked` = 0";
				if (((String) m.get("permissions")).contains("10")) {
					lockedSQL = " ";
				}

				String sql_count = "SELECT * FROM `contributions` WHERE `topic_id` = ?" + lockedSQL
						+ " ORDER BY `contributions`.`id` DESC " +afterSQL;
				ResultSet countset = mysql.Query(sql_count, new StringBuilder().append(psql.getInt("id")).toString());
				ArrayList<Contribution> contarr = new ArrayList<Contribution>();
				while (countset.next()) {
					Contribution cont = new Contribution();
					if (countset.getString("bbcode_text").length() < 100) {
						cont.setBbcode_text(
								BBCode.bbcode_th(countset.getString("bbcode_text").replaceAll("\\<[^>]*>", ""))
										.replaceAll("\\[.*?\\]", " "));
					} else {
						cont.setBbcode_text(BBCode
								.bbcode_th(
										countset.getString("bbcode_text").substring(0, 100).replaceAll("\\<[^>]*>", ""))
								.replaceAll("\\[.*?\\]", " "));
					}
					topicid = countset.getInt("topic_id");
					cont.setLocked(countset.getInt("locked"));
					cont.setTimestamp(countset.getString("timestamp"));
					cont.setSublink(countset.getString("sublink"));

					String sql_author = "SELECT * FROM `users` WHERE `id` = ?";
					ResultSet authorset = mysql.Query(sql_author, countset.getString("user_id"));
					while (authorset.next()) {
						cont.setAvatar(authorset.getString("avatar"));
						cont.setUsername(authorset.getString("username"));
						cont.setUserid(countset.getString("user_id"));
						cont.setPerm(authorset.getString("permissions"));
					}
					contarr.add(cont);
				}

				this.m.put("conts", contarr);

				String addview_sql = "UPDATE `topics` SET `views` = `views` + 1 where `id` = ?";
				mysql.Exec(addview_sql, new StringBuilder().append(psql.getInt("id")).toString());

				String sql_sites = "SELECT * FROM `sites` WHERE `id` = ?";
				ResultSet sql_sites_rs = mysql.Query(sql_sites, groupid);
				sql_sites_rs.next();
				m.put("bcsite", sql_sites_rs.getString("text"));
				m.put("bclink", sql_sites_rs.getString("link"));

			}

			// Pagination
			

			int pagesInt = 0;
			String pages = "SELECT * FROM `contributions` WHERE `topic_id` = ?";
			ResultSet pages_rs = mysql.Query(pages, topicid+"");
			while (pages_rs.next()) {
				pagesInt++;
			}
			int a = (((pagesInt + 9) / 10) * 10) / 10;
			m.put("allpages", a);
			m.put("pages", pagesInt);

		} catch (SQLException e1) {
			new errorManager(e1);
			e1.printStackTrace();
		}

		try {
			Template template = App.cfg.getTemplate("subforum.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
