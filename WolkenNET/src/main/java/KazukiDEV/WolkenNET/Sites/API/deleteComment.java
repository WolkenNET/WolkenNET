package KazukiDEV.WolkenNET.Sites.API;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Moderation;
import KazukiDEV.WolkenNET.Content.Permissions;
import spark.Request;
import spark.Response;
import spark.Route;

public class deleteComment implements Route {
	public Map<String, Object> m = new HashMap<>();

	public deleteComment() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request, this.m, response);

		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/?l=lnp&open=login");
			return null;
		}

		// TODO: Check if id is id
		try {
			Integer.parseInt(request.queryParams("id"));
		}catch(Exception e) {
			return "Keine ID vorhanden";
		}
		
		String cID = request.queryParams("id");
		Moderation.deleteContribution(Integer.parseInt(cID));

		// TODO: Weiterleiten
		return "Kommentar wurde gelöscht";

	}
}
