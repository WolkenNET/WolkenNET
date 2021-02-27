package KazukiDEV.WolkenNET.Content;

import java.io.File;
import java.sql.ResultSet;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

import KazukiDEV.WolkenNET.Config.u;

public class sitemapGenerator {
	
	public sitemapGenerator() throws Exception {
		u.s.println(u.info + "Sitemap is updating");
		WebSitemapGenerator wsg = new WebSitemapGenerator("https://wolkennet.de", new File("static/"));
		wsg.addUrl("https://wolkennet.de/telefon-seelsorge");
		wsg.addUrl("https://wolkennet.de/");
		wsg.addUrl("https://wolkennet.de/impressum");
		wsg.addUrl("https://wolkennet.de/datenschutz");
		wsg.addUrl("https://wolkennet.de/tippsundtricks");
		wsg.addUrl("https://wolkennet.de/therapien");
		wsg.addUrl("https://wolkennet.de/klinikundaerzte");
		wsg.addUrl("https://wolkennet.de/forenregeln");
		wsg.addUrl("https://wolkennet.de/support");
		wsg.addUrl("https://wolkennet.de/medikamente");
		wsg.addUrl("https://wolkennet.de/krankheiten_symptome");
		wsg.addUrl("https://wolkennet.de/team");
		wsg.addUrl("https://wolkennet.de/verweise");
		
		String topicSQL ="SELECT `sublink` FROM `topics`";
		ResultSet topicRS = mysql.Query(topicSQL);
		while(topicRS.next()) {
			wsg.addUrl("https://wolkennet.de/thema/" + topicRS.getString("sublink").replaceAll(" ", "%20").toLowerCase());
		}
		
		String contSQL = "SELECT `user_id`, `sublink` FROM `contributions`";
		ResultSet contRS = mysql.Query(contSQL);
		while(contRS.next()) {
			wsg.addUrl("https://wolkennet.de/beitrag/" + contRS.getString("sublink").replaceAll(" ", "%20").toLowerCase() + "/" + contRS.getInt("user_id"));
		}
		
		String userSQL = "SELECT `username` FROM `users`";
		ResultSet userRS = mysql.Query(userSQL);
		while(userRS.next()) {
			String username = userRS.getString("username");
			wsg.addUrl("https://wolkennet.de/profil/" +username);
		}
		
		u.s.println(u.info + "Sitemap wurde überschrieben");
		
		wsg.write();
	}

}
