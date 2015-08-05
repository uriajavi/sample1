/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package useraccess.util;


import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class encapsulates methods for email sending.
 * @author javi
 */
@Dependent
public class MailSender implements Serializable {
    //Java Mail session for notifications
    @Resource(name="mail/registrationSession")
    private Session session;
        //Logger
    private static final Logger logger=
            Logger.getLogger("useraccess.util.MailSender");

    /**
     * Sends a mail to the email list accounts with a subject and a messageBody
     * @param emailList The space separated email adresses list.
     * @param subject   The subject of the email.
     * @param messageBody The message to be sent.
     */
    public void sendMail(String emailList, String subject, String messageBody){
        try {
            Message message = new MimeMessage(session);
            message.setFrom();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailList));
            message.setSubject(subject);
            message.setHeader("X-Mailer", "JavaMail");
            Date timeStamp = new Date();
            message.setText(messageBody);
            message.setSentDate(timeStamp);
            Transport.send(message);
            logger.log(Level.INFO, "Mail sent to {0}", emailList);
        } catch (MessagingException ex) {
            logger.severe("Error in sending message.");
            logger.severe(ex.getMessage());
        }
    }
    
}
