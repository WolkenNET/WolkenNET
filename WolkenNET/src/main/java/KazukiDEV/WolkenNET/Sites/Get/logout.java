package KazukiDEV.WolkenNET.Sites.Get;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class logout implements Route {
	public Map<String, Object> m = new HashMap<>();

	public logout() {
	}

	public Object handle(Request request, Response response) {
		if(Permissions.hasPermissions(request, this.m, response) == true) {
			String deleteSessionSQL = "DELETE FROM `sessions` WHERE `user_id` = ?";
			try {
				mysql.Exec(deleteSessionSQL, (String)m.get("useridst"));
			}catch(Exception e) {
				new errorManager(e);
				e.printStackTrace();
			}
			response.removeCookie("session");
			response.redirect("/");
		}
		return null;
	}
}
