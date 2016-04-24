package com.mapchat.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A singleton class for sending mail messages by using JavaMail API
 * and using gmail's smtp host service.
 * @author Corey McQuay
 */
public class MailService {

     private static MailService theService = null;

    private static Session mailSession;

    private static final String HOST = "smtp.gmail.com"; //The host email server from which will be supporting emails
    private static final int PORT = 465; //Port number of the server
    private static final String USER = "mapchatservice@gmail.com";     // Username from which "send messages" Just use ourself
    private static final String PASSWORD = "csd@VT(S16)"; // password to the user account
    private static final String FROM = "mapchatservice@gmail.com"; //Same as user but establishes where the email is coming from
    
    
    /**
     * Sends a subject and message to a recipient
     * @param recipient Internet address of the recipient
     * @param subject the subject of the message
     * @param message the message
     * @throws MessagingException
     */
    public static void sendMessage(String recipient, String subject, String message) throws MessagingException {

        if ( theService == null ) {
            theService = new MailService(); //Initialize a new mail service.
        }

        //Make a mime message
        MimeMessage mimeMessage = new MimeMessage(mailSession);

        //Set the contents of the message to get ready to send
	mimeMessage.setFrom(new InternetAddress(FROM));//Use from a valid internet address 
	mimeMessage.setSender(new InternetAddress(FROM));
	mimeMessage.setSubject(subject); //Subject that was gathered from the bean.
        mimeMessage.setContent(message, "text/plain");

        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        Transport transport = mailSession.getTransport("smtps"); //Prepare transportation
        transport.connect(HOST, PORT, USER, PASSWORD); //Attempt connection with the attributes needed to send a message

        transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
        transport.close();

    }

    private MailService() {
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", HOST);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtp.from", FROM);
        props.put("mail.smtps.quitwait", "false");

        mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
    }

   
}