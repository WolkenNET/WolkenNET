package KazukiDEV.WolkenNET.Sites.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import KazukiDEV.WolkenNET.Content.SMTPAuthenticator;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Content.reCaptcha;
import spark.Request;
import spark.Response;
import spark.Route;

public class pwReset implements Route {
	
	public pwReset() {
	}


	public Object handle(Request request, Response response) {
		
		try {
			Boolean recaptcha = reCaptcha.handleCaptcha(request.queryParams("g-recaptcha-response"));
			if(recaptcha == false) {
				response.redirect("/passwordreset");
				return null;
			}
			
		} catch (Exception captchaError) {
			new errorManager(captchaError);
			captchaError.printStackTrace();
		}
		
		String mailSQL = "SELECT `id` FROM `users` WHERE `email` = ?";
		ResultSet mailRS = mysql.Query(mailSQL, request.queryParams("email"));
		try {
			while(mailRS.next()) {
				String resetKey = KazukiDEV.WolkenNET.Content.sessionHandler.randomString(30);
				String insertResetKeySQL = "INSERT INTO `reset_keys`(`id`, `key`) VALUES (?,?)";
				mysql.Exec(insertResetKeySQL, mailRS.getInt("id") + "", resetKey);
				
				Properties properties = System.getProperties();
			    properties.setProperty( "mail.smtp.host", "cmail01.mailhost24.de" );
			    Authenticator auth = new SMTPAuthenticator();
			      Session session = Session.getDefaultInstance(properties, auth);
			      MimeMessage message = new MimeMessage( session );
			      try {
					message.setFrom( new InternetAddress( "mail@wolkennet.de" ) );
					 message.addRecipient( Message.RecipientType.TO, new InternetAddress( request.queryParams("email") ) );
				      message.setSubject( "Dein Passwort bei WolkenNET", "ISO-8859-1" );
				      message.setContent("Wir haben eine Anfrage erhalten das dein Passwort zurückgesetzt werden muss! <a href='https://marc-andre-herpers.tech/pwreset?token=" + resetKey + "'>HIER KLICKEN</a>", "text/html");
				     
				      Transport.send( message );
				} catch (Exception e) {
					new errorManager(e);
					e.printStackTrace();
				}
			     
			}
		} catch (SQLException e) {
			new errorManager(e);
			e.printStackTrace();
		}
		
		return null;
	}
}
