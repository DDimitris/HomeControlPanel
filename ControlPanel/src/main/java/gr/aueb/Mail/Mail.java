package gr.aueb.Mail;

import gr.aueb.Main.GUI;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.util.Properties;

public class Mail extends Thread {


    public synchronized void sendMail(String userName,
                                      String passWord, String host, String port, String starttls,
                                      String auth, boolean debug, String socketFactoryClass,
                                      String fallback, String to, String cc, String subject, String text) {
        Properties props = new Properties();

        props.put("mail.smtp.user", userName);
        props.put("mail.smtp.host", host);
        if (!"".equals(port))
            props.put("mail.smtp.port", port);
        if (!"".equals(starttls))
            props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.auth", auth);
        if (debug) {
            props.put("mail.smtp.debug", "true");
        } else {
            props.put("mail.smtp.debug", "false");
        }
        if (!"".equals(port))
            props.put("mail.smtp.socketFactory.port", port);
        if (!"".equals(socketFactoryClass))
            props.put("mail.smtp.socketFactory.class", socketFactoryClass);
        if (!"".equals(fallback))
            props.put("mail.smtp.socketFactory.fallback", fallback);

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress("Arduino_Alert!!"));

            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));

            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(host, userName, passWord);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            GUI window = new GUI();
            window.txtLog.append("An email was send to your personal email address!!");

        } catch (Exception mex) {
            GUI.system_log.setForeground(Color.red);
            GUI.system_log.append("Mail could not be send:" + mex.getStackTrace() + "\n");


        }
    }

}
