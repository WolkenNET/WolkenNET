package KazukiDEV.WolkenNET.Sites.API;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Moderation;
import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import spark.Request;
import spark.Response;
import spark.Route;

public class lockContribution implements Route {
	public Map<String, Object> m = new HashMap<>();

	public lockContribution() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request.cookie("session"), this.m, response);

		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		// TODO: Check if id is id

		String cID = request.queryParams("id");
		try {
			Moderation.lockContribution(Integer.parseInt(cID));
		} catch (Exception e) {
			new errorManager(e);
		}
			

		// TODO: Weiterleiten
		return "Contribution wurde gelockt";

	}
}
