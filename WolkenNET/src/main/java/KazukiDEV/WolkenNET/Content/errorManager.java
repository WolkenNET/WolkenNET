package KazukiDEV.WolkenNET.Content;

import KazukiDEV.WolkenNET.Config.u;

public class errorManager {
	
	public errorManager(Exception ex) {
		String exceptionType = ex.getClass().getSimpleName();
		String exceptionMessage = ex.getMessage();
		
		String exceptionSQL = "INSERT INTO `exceptions`(`name`, `message`) VALUES (?,?)";
		mysql.Exec(exceptionSQL, exceptionType, exceptionMessage);
		u.s.println(u.error + "Ein Fehler ist aufgereten, aber er wurde an den Error Manager weitergegeben");
	}

}
