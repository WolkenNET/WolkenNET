package KazukiDEV.WolkenNET.Sites.Get;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Main.App;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;

public class teamPage implements Route {
	public Map<String, Object> m = new HashMap<>();

	public teamPage() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request.cookie("session"), this.m, response);
		m.put("titlebar", "Unser Team");
		m.put("banner", "/img/banner/team.jpg");

		try {
			Template template = App.cfg.getTemplate("team.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
