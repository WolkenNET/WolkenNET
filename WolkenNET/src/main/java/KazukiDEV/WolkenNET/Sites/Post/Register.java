package KazukiDEV.WolkenNET.Sites.Post;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.errorManager;
import KazukiDEV.WolkenNET.Content.mysql;
import KazukiDEV.WolkenNET.Content.reCaptcha;
import spark.Request;
import spark.Response;
import spark.Route;

public class Register implements Route {
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	public Register() {
	}

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	static SecureRandom rnd = new SecureRandom();

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public Object handle(Request request, Response response) {
		HashMap<String, Object> m = new HashMap<>();
		if(Permissions.hasPermissions(request, m, response)) {
			response.redirect("/");
			return null;
		}
		
		try {
			Boolean recaptcha = reCaptcha.handleCaptcha(request.queryParams("g-recaptcha-response"));
			if(recaptcha == false) {
				response.redirect("/?r=rcf&open=register");
				return null;
			}
			
		} catch (Exception e1) {
			new errorManager(e1);
			e1.printStackTrace();
		}
		
		if((int)m.get("registrations") != 1) {
			response.redirect("/?r=rd&open=register");
			return null;
		}
		
		try {
			String username = request.queryParams("username");
			String password = request.queryParams("password");
			String email = request.queryParams("email");
			String country = request.queryParams("country");
			
			if(request.cookie("session") != null) {
				response.redirect("/?r=re&open=register");
				return null;
			}
	
			if (!validate(email)) {
				response.redirect("/?r=rie&open=register");
				return null;
			}
			
			if(username.contains(" ")) {
				response.redirect("/?r=rus&open=register");
				return null;
			}
	
			String sql_us_ch = "SELECT * FROM `users` WHERE `username` = ?";
			ResultSet rs_us_ch = mysql.Query(sql_us_ch, username);
			
			if (rs_us_ch.next()) {
				response.redirect("/?r=rut&open=register");
				return null;
			}
				
			String sql_ma_ch = "SELECT * FROM `users` WHERE `email` = ?";
			ResultSet rs_ma_ch = mysql.Query(sql_ma_ch, email);
			if (rs_ma_ch.next()) {
				response.redirect("/?r=ret&open=register");
				return null;
			}
		
		    if(username.length() > 15) {
		    	response.redirect("/?r=rul&open=register");
				return null;
		    }
		    
		    if(email.length() > 50) {
		    	response.redirect("/?r=rel&open=register");
				return null;
		    }
		    
		    if(password.length() < 5) {
		    	response.redirect("/?r=rts&open=register");
				return null;
		    }
		    
		    long millis=System.currentTimeMillis();  
		    Date date = new Date(millis);  
		    
	
		    String extraSQL = "INSERT INTO `users_extra`(`bbcode_text`) VALUES (?)";
		    mysql.Exec(extraSQL, "");
	
			String sql = "INSERT INTO `users`(`username`, `email`, `password_md5`, `registered_on`, `last_login`, `country`, `permissions`, `avatar`) VALUES (?,?,?,?,?,?,?,?)";
			mysql.Exec(sql, username, email, MD5(password), date.toString(), "", country, "1", 1+"");
			response.redirect("/?open=login");
			return "";
		}catch(Exception e) {
			new errorManager(e);
			e.printStackTrace();
			response.redirect("/?r=re&open=register");
			return null;
		}
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
			new errorManager(noSuchAlgorithmException);
			return null;
		}
	}
}
