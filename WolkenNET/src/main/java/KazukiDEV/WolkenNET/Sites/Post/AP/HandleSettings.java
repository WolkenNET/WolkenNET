package KazukiDEV.WolkenNET.Sites.Post.AP;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class HandleSettings implements Route {

	public Object handle(Request request, Response response) {
		Map<String, Object> m = new HashMap<>();
		Permissions.hasPermissions(request, m, response);

		if (!((String) m.get("permissions")).equals("10")) {
			response.redirect("/");
			return null;
		}

		mysql.Exec("UPDATE `system_settings` SET `value_string`=? WHERE `id` = 1", request.queryParams("recaptcha"));
		mysql.Exec("UPDATE `system_settings` SET `value_int`=? WHERE `id` = 2", request.queryParams("registrations"));
		mysql.Exec("UPDATE `system_settings` SET `value_string`=? WHERE `id` = 3", request.queryParams("home_alert"));
		mysql.Exec("UPDATE `system_settings` SET `value_int`=? WHERE `id` = 4", request.queryParams("home_bool"));
		response.redirect("/ap/settings");
		return null;
	}
}
