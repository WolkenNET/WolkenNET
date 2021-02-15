package KazukiDEV.WolkenNET.Content;

import java.security.SecureRandom;
import java.sql.ResultSet;

public class sessionHandler {
	
	private String token = "";
	private String agent = null;
	public int userid = 0;
	
	public sessionHandler(String token, String agent) {
		this.token = token;
		this.agent = agent;
	}
	
	public sessionHandler(int userid, String agent) {
		token = randomString(30);
		this.agent = agent;
		
		String insertSessionSQL = "INSERT INTO `sessions`(`user_id`, `token`, `user_agent`) VALUES (?,?,?)";
		mysql.Exec(insertSessionSQL, userid+"", token, agent);
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public Boolean checkSession() throws Exception {
		String checkSQL = "SELECT `user_id` FROM `sessions` WHERE `token` = ? AND user_agent = ?";
		ResultSet checkRS = mysql.Query(checkSQL, token, agent);
		while(checkRS.next()) {
			userid = checkRS.getInt("user_id");
			return true;
		}
		return false;
	}
	
	
	
	static SecureRandom rnd = new SecureRandom();
	
	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
					.charAt(rnd.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
		return sb.toString();
	}
	
	

}
