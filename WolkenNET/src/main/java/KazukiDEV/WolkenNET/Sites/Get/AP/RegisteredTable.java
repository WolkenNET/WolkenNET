package KazukiDEV.WolkenNET.Sites.Get.AP;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.Stat;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisteredTable implements Route{
	
	public Map<String, Object> m = new HashMap<>();

	public RegisteredTable() {
	}

	public Object handle(Request request, Response response) {
		m.put("titlebar", "AP Registrierte Nutzer Statistiken");

		Permissions.hasPermissions(request, this.m, response);
		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}
		
		HashMap<String, Integer> statMap = new HashMap<>();
		
		String statsSQL = "SELECT * FROM `users` ORDER BY `users`.`id` DESC";
		ResultSet statsRS = mysql.Query(statsSQL);
		try {
			while(statsRS.next()) {
				if(statMap.get(statsRS.getString("registered_on")) != null) {
					int total = statMap.get(statsRS.getString("registered_on")) + 1;
					statMap.remove(statsRS.getString("registered_on"));
					statMap.put(statsRS.getString("registered_on"), total);
				}else {
					statMap.put(statsRS.getString("registered_on"), 1);
				}
			}
		} catch (SQLException e1) {
			new errorManager(e1);
			e1.printStackTrace();
		}
		
		ArrayList<Stat> stats = new ArrayList<>();
		
		for(Map.Entry<String, Integer> entry : statMap.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    Stat st = new Stat();
		    st.setDate(key);
		    st.setInt(value);
		    stats.add(st);
		}
		m.put("stm", stats);


		try {
			Template template = App.cfg.getTemplate("ap/rgstats.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}

}
