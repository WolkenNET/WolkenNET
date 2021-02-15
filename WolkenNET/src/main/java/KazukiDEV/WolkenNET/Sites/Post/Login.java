package KazukiDEV.WolkenNET.Sites.Post;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Content.reCaptcha;
import spark.Request;
import spark.Response;
import spark.Route;

public class Login implements Route {
	public Login() {
	}

	public Object handle(Request request, Response response) {
		String email = request.queryParams("email");
		String password = request.queryParams("password");
		
		try {
			Boolean recaptcha = reCaptcha.handleCaptcha(request.queryParams("g-recaptcha-response"));
			if(recaptcha == false) {
				response.redirect("/?l=lcf&open=login");
				return null;
			}
			
		} catch (Exception captchaError) {
			new errorManager(captchaError);
			captchaError.printStackTrace();
		}

		try {
			String loginSQL = "SELECT * FROM `users` WHERE `email` = ? AND `password_md5` = ?";
			ResultSet rs = mysql.Query(loginSQL, email, MD5(password));
			int i = 0;
			while (rs.next()) {
				if (rs.getBoolean("banned") == true) {
					response.redirect("/?l=lub&open=login");
					return null;
				}
				i++;
				response.cookie("session", rs.getString("session"));
				String lastLoginSQL = "UPDATE `users` SET `last_login`= ? WHERE `id` = ?";
				TimeZone tz = TimeZone.getTimeZone("UTC");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
				df.setTimeZone(tz);
				String nowAsISO = df.format(new Date());
				mysql.Exec(lastLoginSQL, nowAsISO, rs.getInt("id") +"");
				response.redirect("/");
				return null;
			}
			if (i == 0) {
				response.redirect("/?l=li&open=login");
				return null;
			}
		} catch (Exception e) {
			new errorManager(e);
			e.printStackTrace();
			response.redirect("/?l=lie&open=login");
			return null;
		}
		return null;

	}

	public String MD5(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; i++)
				sb.append(Integer.toHexString(array[i] & 0xFF | 0x100).substring(1, 3));
			return sb.toString();
		} catch (NoSuchAlgorithmException noSuchAlgorithmException) {
			// TODO: Error listen
			new errorManager(noSuchAlgorithmException);
			return null;
		}
	}
}
