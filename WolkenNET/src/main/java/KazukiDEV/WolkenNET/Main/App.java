package KazukiDEV.WolkenNET.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import KazukiDEV.WolkenNET.Config.Config;
import KazukiDEV.WolkenNET.Config.u;
import KazukiDEV.WolkenNET.Content.Color;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Content.sitemapGenerator;
import KazukiDEV.WolkenNET.Sites.API.banUser;
import KazukiDEV.WolkenNET.Sites.API.deleteContribution;
import KazukiDEV.WolkenNET.Sites.API.lockContribution;
import KazukiDEV.WolkenNET.Sites.API.setProfilePicture;
import KazukiDEV.WolkenNET.Sites.Forum.KlinikUndÄrzte;
import KazukiDEV.WolkenNET.Sites.Forum.Medikamente;
import KazukiDEV.WolkenNET.Sites.Forum.Therapien;
import KazukiDEV.WolkenNET.Sites.Forum.Tippsundtricks;
import KazukiDEV.WolkenNET.Sites.Forum.krankheitenUndSymptome;
import KazukiDEV.WolkenNET.Sites.Get.Beiträge;
import KazukiDEV.WolkenNET.Sites.Get.Datenschutz;
import KazukiDEV.WolkenNET.Sites.Get.Forenregeln;
import KazukiDEV.WolkenNET.Sites.Get.Home;
import KazukiDEV.WolkenNET.Sites.Get.Impressum;
import KazukiDEV.WolkenNET.Sites.Get.Telefonseelsorge;
import KazukiDEV.WolkenNET.Sites.Get.Themen;
import KazukiDEV.WolkenNET.Sites.Get.donationPage;
import KazukiDEV.WolkenNET.Sites.Get.logout;
import KazukiDEV.WolkenNET.Sites.Get.pageNotFound;
import KazukiDEV.WolkenNET.Sites.Get.passwordReset;
import KazukiDEV.WolkenNET.Sites.Get.profilePage;
import KazukiDEV.WolkenNET.Sites.Get.profilePageBeiträge;
import KazukiDEV.WolkenNET.Sites.Get.profilePageComments;
import KazukiDEV.WolkenNET.Sites.Get.profileSettings;
import KazukiDEV.WolkenNET.Sites.Get.teamPage;
import KazukiDEV.WolkenNET.Sites.Get.verweise;
import KazukiDEV.WolkenNET.Sites.Get.AP.Dashboard;
import KazukiDEV.WolkenNET.Sites.Get.AP.SystemEinstellungen;
import KazukiDEV.WolkenNET.Sites.Get.AP.TopicManager;
import KazukiDEV.WolkenNET.Sites.Get.AP.UserManager;
import KazukiDEV.WolkenNET.Sites.Post.Login;
import KazukiDEV.WolkenNET.Sites.Post.Register;
import KazukiDEV.WolkenNET.Sites.Post.postComment;
import KazukiDEV.WolkenNET.Sites.Post.postContribution;
import KazukiDEV.WolkenNET.Sites.Post.pwReset;
import KazukiDEV.WolkenNET.Sites.Post.saveBio;
import KazukiDEV.WolkenNET.Sites.Post.AP.HandleSettings;
import KazukiDEV.WolkenNET.Sites.Post.AP.HandleTopic;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class App {
	public static Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);

	public static mysql msql = null;

	public static HashMap<String, Route> postroutes = new HashMap<>();
	public static HashMap<String, Route> getroutes = new HashMap<>();

	public static int sessionViews = 0;
	public static int sessionSQL = 0;

	public static void main(String[] args) throws Exception {
		
		System.out.println(Color.CYAN
				+ "           __    __  ________  ________                                                   \r\n"
				+ "          /  \\  /  |/        |/        |                                                  \r\n"
				+ "  ______  $$  \\ $$ |$$$$$$$$/ $$$$$$$$/        __   __   __   ______    ______    ______  \r\n"
				+ " /      \\ $$$  \\$$ |$$ |__       $$ |         /  | /  | /  | /      \\  /      \\  /      \\ \r\n"
				+ "/$$$$$$  |$$$$  $$ |$$    |      $$ |         $$ | $$ | $$ | $$$$$$  |/$$$$$$  |/$$$$$$  |\r\n"
				+ "$$ |  $$ |$$ $$ $$ |$$$$$/       $$ |         $$ | $$ | $$ | /    $$ |$$ |  $$/ $$    $$ |\r\n"
				+ "$$ |__$$ |$$ |$$$$ |$$ |_____    $$ |         $$ \\_$$ \\_$$ |/$$$$$$$ |$$ |      $$$$$$$$/ \r\n"
				+ "$$    $$/ $$ | $$$ |$$       |   $$ |         $$   $$   $$/ $$    $$ |$$ |      $$       |\r\n"
				+ "$$$$$$$/  $$/   $$/ $$$$$$$$/    $$/           $$$$$/$$$$/   $$$$$$$/ $$/        $$$$$$$/ \r\n"
				+ "$$ |                                                                                      \r\n"
				+ "$$ |                                                                                      \r\n"
				+ "$$/");
		System.out.println("\n" + u.warning + "Software by Marc Andre Herpers - Not for public use");
		Config.createConfig();
		Config.loadConfig();
		System.out.println(u.info + "Loading SQL...");
		msql = new mysql(Config.getString("mysqlusername"), Config.getString("mysqlpassword"),
				Config.getString("mysqldatabase"), Config.getString("mysqlip"),
				Integer.parseInt((new StringBuilder(String.valueOf(Config.getString("mysqlport")))).toString()));
		Spark.port(Integer.parseInt(Config.getString("sparkport")));
		Spark.ipAddress(Config.getString("ip"));
		
		new sitemapGenerator();

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);

		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		cfg.setWrapUncheckedExceptions(true);
		Spark.externalStaticFileLocation("static/");
		File templates = new File("templates/");
		File staticfolder = new File("static/");
		final File[] staticfiles = staticfolder.listFiles();
		if (!templates.exists())
			templates.mkdirs();
		if (!staticfolder.exists())
			staticfolder.mkdirs();
		cfg.setDirectoryForTemplateLoading(templates);
		// AP
		getroutes.put("/ap/dashboard", new Dashboard());
		getroutes.put("/ap/settings", new SystemEinstellungen());
		getroutes.put("/ap/topic", new TopicManager("manager"));
		getroutes.put("/ap/topic/delete", new TopicManager("delete"));
		getroutes.put("/ap/topic/create", new TopicManager("create"));
		getroutes.put("/ap/topic/edit", new TopicManager("edit"));
		getroutes.put("/ap/user", new UserManager(""));

		// API
		getroutes.put("/ap/api/delete/contribution", new deleteContribution());
		getroutes.put("/ap/api/lock/contributions", new lockContribution());
		getroutes.put("/ap/api/ban/user", new banUser());
		getroutes.put("/api/pb/user", new setProfilePicture());
		
		getroutes.put("/telefon-seelsorge", new Telefonseelsorge());
		getroutes.put("/", new Home());
		getroutes.put("/impressum", new Impressum());
		getroutes.put("/datenschutz", new Datenschutz());
		
		getroutes.put("/tippsundtricks", new Tippsundtricks());
		getroutes.put("/therapien", new Therapien());
		getroutes.put("/klinikundaerzte", new KlinikUndÄrzte());
		getroutes.put("/medikamente", new Medikamente());
		getroutes.put("/krankheiten_symptome", new krankheitenUndSymptome());
		
		getroutes.put("/forenregeln", new Forenregeln());
		getroutes.put("/404", new pageNotFound());
		getroutes.put("/team", new teamPage());
		getroutes.put("/passwordreset", new passwordReset());
		getroutes.put("/verweise", new verweise());
		getroutes.put("/support", new donationPage());
		getroutes.put("/logout", new logout());
		getroutes.put("/thema/:topic", new Themen());
		getroutes.put("/beitrag/:cont/:user", new Beiträge());
		getroutes.put("/profil/:user", new profilePage());
		getroutes.put("/profil/:user/list", new profilePageBeiträge());
		getroutes.put("/profil/:user/comments", new profilePageComments());
		getroutes.put("/:user/settings", new profileSettings());
		
		postroutes.put("/register", new Register());
		postroutes.put("/login", new Login());
		postroutes.put("/add/contribution", new postContribution());
		postroutes.put("/add/comment", new postComment());
		postroutes.put("/update/bio", new saveBio());
		postroutes.put("/pwreset", new pwReset());

		postroutes.put("/ap/topic", new HandleTopic());
		postroutes.put("/ap/settings", new HandleSettings());
		
		

		for (Map.Entry<String, Route> entry : getroutes.entrySet())
			Spark.get(entry.getKey(), entry.getValue());
		for (Map.Entry<String, Route> entry : postroutes.entrySet())
			Spark.post(entry.getKey(), entry.getValue());

		Spark.get("*", new pageNotFound());
		
		Spark.get("*", new Route() {
			public Object handle(Request request, Response response) {
				Boolean s = Boolean.valueOf(false);
				byte b;
				int i;
				File[] arrayOfFile;
				for (i = (arrayOfFile = staticfiles).length, b = 0; b < i;) {
					File f = arrayOfFile[b];
					if (request.pathInfo().startsWith("/" + f.getName()))
						s = Boolean.valueOf(true);
					b++;
				}
				if (s.booleanValue())
					return null;
				return null;
			}
		});
		
		
		
	}
}
