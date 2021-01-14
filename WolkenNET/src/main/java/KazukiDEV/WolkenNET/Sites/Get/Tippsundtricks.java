package KazukiDEV.WolkenNET.Sites.Get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.Topic;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class Tippsundtricks implements Route {
	public Map<String, Object> m = new HashMap<>();

	public Tippsundtricks() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request.cookie("session"), this.m, response);
		m.put("titlebar", "Tipps und Tricks");
		m.put("banner", "");
		
		try {
	          ArrayList<Topic> tag_array = new ArrayList<Topic>();
	          String tags_sql = "SELECT * FROM `topics` WHERE `groupid` = ?";
	          ResultSet tags_rs = mysql.Query(tags_sql, 1+"");
	          while (tags_rs.next()) {
	        	  
	            Topic t = new Topic();
	            t.setID(tags_rs.getInt("id"));
	            t.setDescription(tags_rs.getString("description"));
	            t.setTitle(tags_rs.getString("title"));
	            t.setIcon(tags_rs.getString("icon"));
	            t.setSublink(tags_rs.getString("sublink"));
	            t.setGroupid(1 + "");
	            t.setImportant(tags_rs.getString("important"));
	            t.setViews(tags_rs.getInt("views"));
	            int postcount = 0;
	            String sql_count = "SELECT * FROM `contributions` WHERE `topic_id` = ?";
				ResultSet countset = mysql.Query(sql_count, new StringBuilder().append(tags_rs.getInt("id")).toString());
				while(countset.next())postcount++;
				t.setPosts(new StringBuilder().append(postcount).toString());
	            tag_array.add(t);
	          } 
	          this.m.put("tags", tag_array);
	        } catch (Exception exception) {}
		
		
		
		try {
			Template template = App.cfg.getTemplate("tippsundtricks.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			throw new RuntimeException(e);
		}
	}
}
