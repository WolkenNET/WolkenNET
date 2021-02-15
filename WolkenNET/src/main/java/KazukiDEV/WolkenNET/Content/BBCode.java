package KazukiDEV.WolkenNET.Content;

import java.util.HashMap;
import java.util.Map;

public class BBCode {
  public static String bbcode(String text) {
    String html = text;
    Map<String, String> bbMap = new HashMap<>();
    bbMap.put("(\r\n|\r|\n|\n\r)", "<br/>");
    bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
    bbMap.put("\\[i\\](.+?)\\[/i\\]", "<span style='font-style:italic;'>$1</span>");
    bbMap.put("\\[u\\](.+?)\\[/u\\]", "<span style='text-decoration:underline;'>$1</span>");
    bbMap.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
    bbMap.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
    bbMap.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
    bbMap.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
    bbMap.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
    bbMap.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
    
    
    
    bbMap.put("\\[list\\]", "<ol>");
    bbMap.put("\\[/list\\]", "</ol>");
    bbMap.put("\\[li\\](.+?)\\[/li\\]", "<li class=\"li-p\">$1</li>");
    
    bbMap.put("\\[indent\\](.+?)\\[/indent\\]", "<div style=\"text-indent: 50px;\">$1</div>");
    bbMap.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
    bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
    bbMap.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]", "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
    bbMap.put("\\[center\\](.+?)\\[/center\\]", "<div align='center'>$1</div>");
    bbMap.put("\\[left\\](.+?)\\[/left\\]", "<div align='left'>$1</div>");
    bbMap.put("\\[right\\](.+?)\\[/right\\]", "<div align='right'>$1</div>");
    bbMap.put("\\[align=(.+?)\\](.+?)\\[/align\\]", "<div align='$1'>$2");
    bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style='color:$1;'>$2</span>");
    bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style='font-size:$1;'>$2</span>");
    bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img src='$1' />");
    bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img width='$1' height='$2' src='$3' />");
    bbMap.put("\\[email\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$1</a>");
    bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$2</a>");
    bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a target='_blank' href='$1'>$1</a>");
    bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a target='_blank' href='$1'>$2</a>");
    bbMap.put("\\[youtube\\](.+?)\\[/youtube\\]", "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
    bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");
    bbMap.put("\\[B\\](.+?)\\[/B\\]", "<strong>$1</strong>");
    bbMap.put("\\[I\\](.+?)\\[/I\\]", "<span style='font-style:italic;'>$1</span>");
    bbMap.put("\\[U\\](.+?)\\[/U\\]", "<span style='text-decoration:underline;'>$1</span>");
    bbMap.put("\\[H1\\](.+?)\\[/H1\\]", "<h1>$1</h1>");
    bbMap.put("\\[H2\\](.+?)\\[/H2\\]", "<h2>$1</h2>");
    bbMap.put("\\[H3\\](.+?)\\[/H3\\]", "<h3>$1</h3>");
    bbMap.put("\\[H4\\](.+?)\\[/H4\\]", "<h4>$1</h4>");
    bbMap.put("\\[H5\\](.+?)\\[/H5\\]", "<h5>$1</h5>");
    bbMap.put("\\[H6\\](.+?)\\[/H6\\]", "<h6>$1</h6>");
    bbMap.put("\\[INDENT\\](.+?)\\[/INDENT\\]", "<div style=\"text-indent: 50px;\">$1</div>");
    bbMap.put("\\[QUOTE\\](.+?)\\[/QUOTE\\]", "<blockquote>$1</blockquote>");
    bbMap.put("\\[P\\](.+?)\\[/P\\]", "<p>$1</p>");
    bbMap.put("\\[P=(.+?),(.+?)\\](.+?)\\[/P\\]", "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
    bbMap.put("\\[CENTER\\](.+?)\\[/CENTER\\]", "<div align='center'>$1</div>");
    bbMap.put("\\[LEFT\\](.+?)\\[/LEFT\\]", "<div align='left'>$1</div>");
    bbMap.put("\\[RIGHT\\](.+?)\\[/RIGHT\\]", "<div align='center'>$1</div>");
    bbMap.put("\\[ALIGN=(.+?)\\](.+?)\\[/ALIGN\\]", "<div align='$1'>$2");
    bbMap.put("\\[COLOR=(.+?)\\](.+?)\\[/COLOR\\]", "<span style='color:$1;'>$2</span>");
    bbMap.put("\\[SIZE=(.+?)\\](.+?)\\[/SIZE\\]", "<span style='font-size:$1;'>$2</span>");
    bbMap.put("\\[IMG\\](.+?)\\[/IMG\\]", "<img src='$1' />");
    bbMap.put("\\[IMG=(.+?),(.+?)\\](.+?)\\[/IMG\\]", "<img width='$1' height='$2' src='$3' />");
    bbMap.put("\\[EMAIL\\](.+?)\\[/EMAIL\\]", "<a href='mailto:$1'>$1</a>");
    bbMap.put("\\[EMAIL=(.+?)\\](.+?)\\[/EMAIL\\]", "<a href='mailto:$1'>$2</a>");
    bbMap.put("\\[URL\\](.+?)\\[/URL\\]", "<a target='_blank' href='$1'>$1</a>");
    bbMap.put("\\[URL=(.+?)\\](.+?)\\[/URL\\]", "<a target='_blank' href='$1'>$2</a>");
    bbMap.put("\\[YOUTUBE\\](.+?)\\[/YOUTUBE\\]", "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
    bbMap.put("\\[YOUTUBE\\](.+?)\\[/YOUTUBE\\]", "<video src='$1' />");
    for (Map.Entry<String, String> entry : bbMap.entrySet())
      html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString()); 
    return html;
  }
  
  public static String bbcode_th(String text) {
	    String html = text;
	    Map<String, String> bbMap = new HashMap<>();
	    bbMap.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
	    bbMap.put("\\[i\\](.+?)\\[/i\\]", "<span style='font-style:italic;'>$1</span>");
	    bbMap.put("\\[u\\](.+?)\\[/u\\]", "<span style='text-decoration:underline;'>$1</span>");
	    bbMap.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
	    bbMap.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
	    bbMap.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style='color:$1;'>$2</span>");
	    bbMap.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style='font-size:$1;'>$2</span>");
	    bbMap.put("\\[img\\](.+?)\\[/img\\]", "<img class='img-thumbnail' src='$1' />");
	    bbMap.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img class='img-thumbnail' width='$1' height='$2' src='$3' />");
	    bbMap.put("\\[email\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$1</a>");
	    bbMap.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$2</a>");
	    bbMap.put("\\[url\\](.+?)\\[/url\\]", "<a href='$1'>$1</a>");
	    bbMap.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href='$1'>$2</a>");
	    bbMap.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");
	    bbMap.put("\\[B\\](.+?)\\[/B\\]", "<strong>$1</strong>");
	    bbMap.put("\\[I\\](.+?)\\[/I\\]", "<span style='font-style:italic;'>$1</span>");
	    bbMap.put("\\[U\\](.+?)\\[/U\\]", "<span style='text-decoration:underline;'>$1</span>");
	    bbMap.put("\\[QUOTE\\](.+?)\\[/QUOTE\\]", "<blockquote>$1</blockquote>");
	    bbMap.put("\\[CENTER\\](.+?)\\[/CENTER\\]", "<div align='center'>$1");
	    bbMap.put("\\[ALIGN=(.+?)\\](.+?)\\[/ALIGN\\]", "<div align='$1'>$2");
	    bbMap.put("\\[COLOR=(.+?)\\](.+?)\\[/COLOR\\]", "<span style='color:$1;'>$2</span>");
	    bbMap.put("\\[SIZE=(.+?)\\](.+?)\\[/SIZE\\]", "<span style='font-size:$1;'>$2</span>");
	    bbMap.put("\\[IMG=(.+?),(.+?)\\](.+?)\\[/IMG\\]", "<img class='img-thumbnail' width='$1' height='$2' src='$3' />");
	    bbMap.put("\\[EMAIL\\](.+?)\\[/EMAIL\\]", "<a href='mailto:$1'>$1</a>");
	    bbMap.put("\\[EMAIL=(.+?)\\](.+?)\\[/EMAIL\\]", "<a href='mailto:$1'>$2</a>");
	    bbMap.put("\\[URL\\](.+?)\\[/URL\\]", "<a href='$1'>$1</a>");
	    bbMap.put("\\[URL=(.+?)\\](.+?)\\[/URL\\]", "<a href='$1'>$2</a>");
	    for (Map.Entry<String, String> entry : bbMap.entrySet())
	      html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString()); 
	    return html;
	  }
}
