package KazukiDEV.WolkenNET.Content;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import KazukiDEV.WolkenNET.Config.Config;

public class SMTPAuthenticator extends Authenticator

{

private static final String SMTP_AUTH_USER = Config.getString("mail");

private static final String SMTP_AUTH_PWD = Config.getString("mailpw");

public PasswordAuthentication getPasswordAuthentication()

{

return new PasswordAuthentication(getUsername(),
getPassword());

}

private String getUsername(){

return SMTPAuthenticator.SMTP_AUTH_USER;

}

private String getPassword(){

return SMTPAuthenticator.SMTP_AUTH_PWD;

}

}


