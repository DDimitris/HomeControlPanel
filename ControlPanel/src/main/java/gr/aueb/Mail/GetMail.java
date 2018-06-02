package gr.aueb.Mail;

import gr.aueb.Main.Communicator;
import gr.aueb.Main.GUI;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class GetMail extends Thread {
    public boolean stop = false;


    public void stop(boolean stop) {
        this.stop = stop;
        if (stop != true) {
            GUI.txtLog.append("Communication via e-mail started!! \n");
        } else {
            GUI.txtLog.append("Communication via e-mail stoped!! \n");
        }
    }


    public void run() {
        while (!stop) {

            String line;
            ArrayList getMail = new ArrayList();

            BufferedReader reader;
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");
            try {
                reader = new BufferedReader(new FileReader(
                        "D:\\HomeControlPanelMailInfo.txt"));

                while ((line = reader.readLine()) != null)
                    getMail.add(line);

                reader.close();
                String user = (String) getMail.get(1);
                String password = (String) getMail.get(2);
                Session session = Session.getDefaultInstance(props, null);
                Store store = session.getStore("imaps");
                store.connect("imap.gmail.com", user, password);
                System.out.println(store);

                Folder inbox = store.getFolder("Inbox");
                inbox.open(Folder.READ_WRITE);

                FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
                Message[] messages = inbox.search(ft);

                for (int i = 0; i < messages.length; i++) {
                    System.out.println("------------ Message " + (i + 1)
                            + " ------------");

                    String subject = messages[i].getSubject();

                    if (subject.toLowerCase().equals("on")) {
                        Communicator.writeData("o");
                        GUI.txtLog.append("Email was retrieved successfully!! \n");
                    }
                    if (subject.toLowerCase().equals("off")) {
                        Communicator.writeData("c");
                        GUI.txtLog.append("Email was retrieved successfully!! \n");

                    } else if (subject != null) {
                        System.out.println("subject:" + subject);
                    }

                    Date sent = messages[i].getSentDate();
                    if (sent != null) {
                        System.out.println("Sent: " + sent);
                    }
                    /*
                     * Body and date of message if needed
                     *
                     * System.out.println(); System.out.println("Message : "); if
                     * (messages[i].getContentType().toLowerCase()
                     * .contains("text/plain")) {
                     * System.out.println(messages[i].getContent().toString()); }
                     * else { Multipart multipart = (Multipart)
                     * messages[i].getContent();
                     *
                     * for (int x = 0; x < multipart.getCount(); x++) { BodyPart
                     * bodyPart = multipart.getBodyPart(x);
                     *
                     * String disposition = bodyPart.getDisposition();
                     *
                     * if (disposition != null &&
                     * (disposition.equals(BodyPart.ATTACHMENT))) {
                     * System.out.println("Mail have some attachment : ");
                     *
                     * DataHandler handler = bodyPart.getDataHandler();
                     * System.out.println("file name : " + handler.getName()); }
                     * else { System.out.println(bodyPart.getContent()); } }
                     * System.out.println(); }
                     */

                    messages[i].setFlag(Flags.Flag.DELETED, true);
                }

                inbox.close(true);
                store.close();
                Thread.sleep(5000);
                //Thread thread = new GetMail();

                //thread.start();

            } catch (NoSuchProviderException e) {
                e.printStackTrace();
                GUI.system_log.setForeground(Color.red);
                GUI.system_log.append("There was a problem with the provider!! \n");
            } catch (MessagingException e) {
                e.printStackTrace();
                GUI.system_log.setForeground(Color.red);
                GUI.system_log
                        .append("There was a problem with authentication!! \n");
            } catch (IOException e) {
                e.printStackTrace();
                GUI.system_log.setForeground(Color.red);
                GUI.system_log
                        .append("There was a problem with the file info!! \n");
            } catch (InterruptedException e) {
                GUI.system_log.setForeground(Color.red);
                GUI.system_log.append("The process was interrupted!! \n");
                e.printStackTrace();
            }

        }

    }
}




