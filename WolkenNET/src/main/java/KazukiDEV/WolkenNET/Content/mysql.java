package KazukiDEV.WolkenNET.Content;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import KazukiDEV.WolkenNET.Config.Config;
import KazukiDEV.WolkenNET.Config.u;
import KazukiDEV.WolkenNET.Main.App;

public final class mysql {
	private static Connection con = null;

	public mysql(String Username, String Password, String Database, String Hostname, int Port) {
		connect(Username, Password, Database, Hostname, Port);
	}

	public static Connection getCon() {
		return con;
	}

	private void connect(String Username, String Password, String Database, String Hostname, int Port) {
		String conStr = "jdbc:mysql://" + Hostname + ":" + Port + "/" + Database + "?user=" + Username + "&password="
				+ Password + "&serverTimezone=UTC";
		try {
			if (con != null && !con.isClosed())
				throw new Exception("Connection already established!");
			con = DriverManager.getConnection(conStr);
			ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();
			thread.scheduleAtFixedRate(new Runnable() {
				public void run() {
					try {
						mysql.this.antiTimeoutLoop();
					} catch (SQLException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, 0L, 1L, TimeUnit.SECONDS);
		} catch (Exception ex) {
			new errorManager(ex);
			System.out.println(u.mysql + ex.getMessage());
		}
	}

	public static ResultSet Query(String sql, String... args) {
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++)
				stmt.setString(i + 1, args[i]);
			if (Config.getString("debug").contains("true"))
				System.out.println(u.mysql + stmt.toString());
			App.sessionSQL++;
			return stmt.executeQuery();
		} catch (Exception e) {
			new errorManager(e);
			e.printStackTrace();
			return null;
		}
	}

	public static boolean Exec(String sql, String... args) {
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++)
				stmt.setString(i + 1, args[i]);
			if (Config.getString("debug").contains("true"))
				System.out.println(u.mysql + stmt.toString());
			App.sessionSQL++;
			return stmt.execute();
		} catch (Exception e) {
			new errorManager(e);
			e.printStackTrace();
			return false;
		}
	}

	private void antiTimeoutLoop() throws SQLException, InterruptedException {
		while (!con.isClosed()) {
			PreparedStatement stmt = con.prepareStatement("SELECT 1+1");
			stmt.execute();
			Thread.sleep(3000L);
		}
	}
}
