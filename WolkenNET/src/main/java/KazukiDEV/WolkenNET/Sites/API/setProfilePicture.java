package KazukiDEV.WolkenNET.Sites.API;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class setProfilePicture implements Route {
	public Map<String, Object> m = new HashMap<>();

	public setProfilePicture() {
	}

	public Object handle(Request request, Response response) {
		if(Permissions.hasPermissions(request, this.m, response)) {
			int pb = Integer.parseInt(request.queryParams("pb"));
			if(Math.max(1, pb) == Math.min(pb, 16)) {
				String updateAvatarSQL = "UPDATE `users` SET `avatar`=? WHERE `id` = ?";
				mysql.Exec(updateAvatarSQL, pb+"", m.get("useridst").toString());
				response.redirect("/" + m.get("username") + "/settings");
			}
		}
		return null;
		

	}
}
