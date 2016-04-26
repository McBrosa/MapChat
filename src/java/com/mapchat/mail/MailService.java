package com.mapchat.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A class for sending mail messages by using JavaMail API
 * and using gmail's smtp host service, this gets the data from the EmailClientBean
 * and prepares a message for the recipient (mapchatservice@gmail.com).
 * @author Corey McQuay
 */
public class MailService {

    //Global Variables 
    private static MailService theService = null; //Service that has not started yet

    
    private static Session mailSession; //Mail Session to be instatiated

    private static final String HOST = "smtp.gmail.com"; //The host email server from which will be supporting emails
    private static final int PORT = 465; //Port number of the server
    private static final String USER = "mapchatservice@gmail.com";     // Username from which "send messages" Just use ourself
    private static final String PASSWORD = "csd@VT(S16)"; // Password to the user account
    private static final String FROM = "mapchatservice@gmail.com"; //Same as user but establishes where the email is coming from
    
    /**
     * Constructor that sets the properties of the Mail Service into the session.
     * This way glassfish can send the email smoothly
     */
    private MailService() {
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps"); //Type of protocol for transportation
        props.put("mail.smtps.host", HOST); //Who is hosting the mail session
        props.put("mail.smtps.auth", "true"); //Valid authorization for allowing the user to send emais
        props.put("mail.smtp.from", FROM); //Who the email is coming from
        props.put("mail.smtps.quitwait", "false");

        //Throw into global variable so it has properties
        mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
    }
    
    /**
     * Sends a subject and message to a recipient using a mime message service.
     * This sets all the fields of a regular email and then prepares a transport,
     * send message, and close transport.
     * 
     * @param recipient Internet address of the recipient
     * @param subject the subject of the message
     * @param message the message
     * @throws MessagingException
     */
    public static void sendMessage(String recipient, String subject, String message) throws MessagingException {

        if ( theService == null ) {
            theService = new MailService(); //Initialize a new mail service.
        }

        //Make a mime message from the mail session
        MimeMessage mimeMessage = new MimeMessage(mailSession);

        //Set the contents of the message to get ready to send
	mimeMessage.setFrom(new InternetAddress(FROM));//Use from a valid internet address 
	mimeMessage.setSender(new InternetAddress(FROM));
	mimeMessage.setSubject(subject); //Subject that was gathered from the bean.
        mimeMessage.setContent(message, "text/plain"); //Makes it so that the text is plain.

        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        Transport transport = mailSession.getTransport("smtps"); //Prepare transportation through a smtps server connection
        transport.connect(HOST, PORT, USER, PASSWORD); //Attempt connection with the attributes needed to send a message

        transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
        transport.close();

    }  
}