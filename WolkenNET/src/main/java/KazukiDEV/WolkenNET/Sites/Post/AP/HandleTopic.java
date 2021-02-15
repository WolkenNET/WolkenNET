package KazukiDEV.WolkenNET.Sites.Post.AP;

import java.util.HashMap;
import java.util.Map;

import KazukiDEV.WolkenNET.Content.Permissions;
import KazukiDEV.WolkenNET.Content.mysql;
import spark.Request;
import spark.Response;
import spark.Route;

public class HandleTopic implements Route {
  
  public Object handle(Request request, Response response) {
    Map<String, Object> m = new HashMap<>();
    Permissions.hasPermissions(request, m, response);
    
    if (!((String) m.get("permissions")).equals("10")) {
        response.redirect("/");
        return null;
      } 
    
    String type = request.queryParams("type");
    
    if (type.equals("create")) {
      mysql.Exec("INSERT INTO `topics`(`icon`, `title`, `description`, `sublink`, `groupid`, `important`) VALUES (?,?,?,?,?,?)", request.queryParams("icon"), request.queryParams("title"), request.queryParams("description")
    		  , request.queryParams("sublink"), request.queryParams("groupid"), request.queryParams("important"));
      
    } else if (type.equals("edit")) {
      String id = request.queryParams("id");
      mysql.Exec("UPDATE `topics` SET `icon`=?,`title`=?,`description`=?,`sublink`=?,`groupid`=?,`important`=? WHERE `id`=?", request.queryParams("icon"), request.queryParams("title"), request.queryParams("description")
    		  , request.queryParams("sublink"), request.queryParams("groupid"), request.queryParams("important"),id);
    } 
    response.redirect("/ap/topic");
    return null;
  }
}
