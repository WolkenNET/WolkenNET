package KazukiDEV.WolkenNET.Sites.API;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Moderation;
import KazukiDEV.WolkenNET.Content.Permissions;
import spark.Request;
import spark.Response;
import spark.Route;

public class deleteContribution implements Route {
	public Map<String, Object> m = new HashMap<>();

	public deleteContribution() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request, this.m, response);

		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		// TODO: Check if id is id

		String cID = request.queryParams("id");
		Moderation.deleteContribution(Integer.parseInt(cID));

		// TODO: Weiterleiten
		return "Contribution wurde gelöscht";

	}
}
