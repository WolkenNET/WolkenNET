package KazukiDEV.WolkenNET.Sites.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class postComment implements Route {
	public postComment() {
	}
	public Map<String, Object> m = new HashMap<>();

	public Object handle(Request request, Response response) {
		if(Permissions.hasPermissions(request, this.m, response) == false) {
			//TODO: Login Fenster öffnen
			response.redirect("/");
			return null;
		}
		
		String bbcode_text = request.queryParams("bbcode_text");
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(new Date());
		String uID = (String) m.get("useridst");
		String cID = request.queryParams("cont_id");
		
		
		try {
			String insertSQL = "INSERT INTO `comments`(`user_id`, `cont_id`, `timestamp`, `bbcode_text`, `locked`) VALUES (?,?,?,?,?)";
			mysql.Exec(insertSQL, uID, cID, nowAsISO, bbcode_text, "0");
			
			String forwardSQL = "SELECT * FROM `contributions` WHERE `id` = ?";
			ResultSet forwardRS = mysql.Query(forwardSQL, request.queryParams("cont_id"));
			while(forwardRS.next()) {
				String link = forwardRS.getString("sublink").replaceAll(" ", "%20");
				response.redirect("/beitrag/" + link + "/" + forwardRS.getString("user_id"));
				return null;
			}
			
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		
		
		
		return null;

	}
}
