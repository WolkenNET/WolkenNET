package KazukiDEV.WolkenNET.Sites.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class saveBio implements Route {
	public saveBio() {
	}
	public Map<String, Object> m = new HashMap<>();

	public Object handle(Request request, Response response) {
		if(Permissions.hasPermissions(request, this.m, response) == false) {
			//TODO: Login Fenster öffnen
			response.redirect("/");
			return null;
		}
		
		String bbcodeText = request.queryParams("bbcode_text");
		String userID = (String)m.get("useridst");
		String userName = (String)m.get("username");
		
		
		
		try {
			String checkSQL = "SELECT * FROM `users_extra` WHERE `id` = ?";
			ResultSet checkRS = mysql.Query(checkSQL, userID);
			while(checkRS.next()) {
				String updateSQL = "UPDATE `users_extra` SET `bbcode_text`= ? WHERE `id` = ?";
				mysql.Exec(updateSQL, bbcodeText, userID);
				response.redirect("/" + userName + "/settings");
				return null;
			}
			String insertSQL = "INSERT INTO `users_extra`(`id`, `bbcode_text`) VALUES (?, ?)";
			mysql.Exec(insertSQL, userID, bbcodeText);
			response.redirect("/" + userName + "/settings");
			return null;
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		return null;

	}
}
