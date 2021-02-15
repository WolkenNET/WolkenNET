package KazukiDEV.WolkenNET.Sites.API;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Moderation;
import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import spark.Request;
import spark.Response;
import spark.Route;

public class banUser implements Route {
	public Map<String, Object> m = new HashMap<>();

	public banUser() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request.cookie("session"), this.m, response);

		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		String uID = request.queryParams("uname");
		try {
			Moderation.BanUser(uID);
		} catch (NumberFormatException e) {
			new errorManager(e);
			e.printStackTrace();
		}

		response.redirect("/ap/user");
		return null;

	}
}
