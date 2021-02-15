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

public class Forenregeln implements Route {
	public Map<String, Object> m = new HashMap<>();

	public Forenregeln() {
	}

	public Object handle(Request request, Response response) {
		Permissions.hasPermissions(request, this.m, response);
		m.put("titlebar", "Forenregeln");
		m.put("banner", "/img/banner/kreide.jpg");
		try {
			Template template = App.cfg.getTemplate("forenregeln.html");
			Writer out = new StringWriter();
			template.process(this.m, out);
			return out.toString();
		} catch (IOException | freemarker.template.TemplateException e) {
			new errorManager(e);
			throw new RuntimeException(e);
		}
	}
}
