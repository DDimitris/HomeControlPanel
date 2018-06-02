package gr.aueb.Main;

import gnu.io.*;
import gr.aueb.Mail.GetMail;
import gr.aueb.Mail.Mail;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

public class Communicator implements SerialPortEventListener {
    // the timeout value for connecting with the port
    final static int TIMEOUT = 2000;
    final static int NEW_LINE_ASCII = 10;
    static String logText = null;
    private static OutputStream output = null;
    public gr.aueb.Mail.GetMail GetMail;
    GUI window = null;
    private int baud1 = 4800;
    private int baud2 = 9600;
    private int baud3 = 14400;
    private int baud4 = 19200;
    private int baud5 = 28800;
    private int baud6 = 38400;
    private int baud7 = 57600;
    private int baud8 = 115200;
    // for containing the ports that will be found
    private Enumeration ports = null;
    // map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();
    // this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;
    // input and output streams for sending and receiving data
    private InputStream input = null;
    // just a boolean flag that i use for enabling
    // and disabling buttons depending on whether the program
    // is connected to a serial port or not
    private boolean bConnected = false;
    private String from;
    private String Fire = "F";

    public Communicator(GUI window) {
        this.window = window;
    }

    public Communicator(GetMail GetMail) {
        this.GetMail = GetMail;
    }

    public static void writeData(String write) {
        try {

            output.write(write.getBytes());
            output.flush();

        } catch (Exception e) {
            logText = "Failed to write data. (" + e.toString() + ")";
            GUI.system_log.setForeground(Color.red);
            GUI.system_log.append(logText + "\n");
        }
    }

    public void searchForPorts() {
        window.baud_rate.addItem(baud1);
        window.baud_rate.addItem(baud2);
        window.baud_rate.addItem(baud3);
        window.baud_rate.addItem(baud4);
        window.baud_rate.addItem(baud5);
        window.baud_rate.addItem(baud6);
        window.baud_rate.addItem(baud7);
        window.baud_rate.addItem(baud8);
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports
                    .nextElement();

            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                window.cboxPorts.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
    }

    // connect to the selected port in the combo box
    // pre: ports are already found by using the searchForPorts method
    // post: the connected comm port is stored in commPort, otherwise,
    // an exception is generated
    public void connect() {
        String selectedPort = (String) window.cboxPorts.getSelectedItem();
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);

        CommPort commPort = null;

        try {

            // the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("HomeControlPanel", TIMEOUT);

            // the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams((Integer) window.baud_rate.getSelectedItem(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            // for controlling GUI elements
            setConnected(true);

            // logging
            logText = selectedPort + " opened successfully.";
            window.txtLog.setForeground(Color.black);
            window.txtLog.append(logText + "\n");

            // CODE ON SETTING BAUD RATE ETC OMITTED
            // XBEE PAIR ASSUMED TO HAVE SAME SETTINGS ALREADY

            // enables the controls on the GUI if a successful connection is
            // made
            window.communicator.toggleControls();
        } catch (PortInUseException e) {
            logText = selectedPort + " is in use. (" + e.toString() + ")";

            window.system_log.setForeground(Color.RED);
            window.system_log.append(logText + "\n");
        } catch (Exception e) {
            logText = "Failed to open " + selectedPort + "(" + e.toString()
                    + ")";
            window.system_log.append(logText + "\n");
            window.system_log.setForeground(Color.RED);
        }
    }

    // open the input and output streams
    // pre: an open port
    // post: initialized intput and output streams for use to communicate data
    public boolean initIOStream() {
        // return value for whather opening the streams is successful or not
        boolean successful = false;

        try {
            //
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();

            successful = true;
            return successful;
        } catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            window.system_log.setForeground(Color.red);
            window.system_log.append(logText + "\n");
            return successful;
        }
    }

    // starts the event listener that knows whenever data is available to be
    // read
    // pre: an open serial port
    // post: an event listener for the serial port that knows when data is
    // recieved
    public void initListener() {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            logText = "Too many listeners. (" + e.toString() + ")";
            window.system_log.setForeground(Color.red);
            window.system_log.append(logText + "\n");
        }
    }

    // disconnect the serial port
    // pre: an open serial port
    // post: clsoed serial port
    public void disconnect() {
        // close the serial port
        try {

            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            output.close();
            setConnected(false);
            window.communicator.toggleControls();
            window.thread.stop(true);
            logText = "Disconnected.";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
            window.text_alert.setForeground(Color.red);
            window.text_alert.append(logText + "\n");
        } catch (Exception e) {
            logText = "Failed to close " + serialPort.getName() + "("
                    + e.toString() + ")";
            window.system_log.setForeground(Color.red);
            window.system_log.append(logText + "\n");
        }
    }

    final public boolean getConnected() {
        return bConnected;
    }

    public void setConnected(boolean bConnected) {
        this.bConnected = bConnected;
    }

    // what happens when data is received
    // pre: serial event is triggered
    // post: processing on the data it reads
    public void serialEvent(SerialPortEvent evt) {
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

            try {
                serialPort.setSerialPortParams((Integer) window.baud_rate.getSelectedItem(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                byte singleData = (byte) input.read();

                if (singleData != NEW_LINE_ASCII) {
                    logText = new String(new byte[]{singleData});

                    if (logText.equals(Fire) == true) {
                        window.text_alert
                                .append("There is a chance of fire!! High temprature detected!!! \n");
                        String line;
                        ArrayList mail = new ArrayList();

                        BufferedReader reader = new BufferedReader(
                                new FileReader(
                                        "D:\\HomeControlPanelMailInfo.txt"));

                        while ((line = reader.readLine()) != null)
                            mail.add(line);

                        reader.close();

                        String from = (String) mail.get(1);
                        String password = (String) mail.get(2);
                        String to = (String) mail.get(3);
                        String cc = (String) mail.get(4);
                        String alert = (String) mail.get(5);
                        Mail message = new Mail();
                        message.sendMail(from, password, "smtp.gmail.com",
                                "465", "true", "true", true,
                                "javax.net.ssl.SSLSocketFactory", "false", to,
                                cc, alert,
                                "There is a chance of fire!! High temprature detected!!!");

                    } else {
                        window.txtLog.append(logText);
                    }

                } else {
                    window.txtLog.append("\n");
                }

            } catch (IOException e) {
                window.system_log.setForeground(Color.red);
                window.system_log
                        .append("Mail configuration can not be found!! \n ("
                                + e.toString() + ")");

            } catch (Exception e) {
                logText = "Failed to read data from arduino. (" + e.toString()
                        + ")";
                window.system_log.setForeground(Color.red);
                window.system_log.append(logText + "\n");
            }
        }

    }

    public void toggleControls() {
        if (window.communicator.getConnected() == true) {
            window.submit.setEnabled(true);
            window.light_off.setEnabled(true);
            window.light_on.setEnabled(true);
            window.send_command.setEnabled(true);
            window.btnDisconnect.setEnabled(true);
            window.btnConnect.setEnabled(false);
            window.cboxPorts.setEnabled(false);
            window.mail_from.setEditable(true);
            window.mail_cc.setEditable(true);
            window.mail_password.setEditable(true);
            window.mail_to.setEditable(true);
            window.text_command.setEditable(true);
            window.clear_log.setEnabled(true);
            window.test_mail.setEnabled(true);
            window.start_communication.setEnabled(true);
            window.stop_communication.setEnabled(true);
//			window.play.setEnabled(true);

        } else {
//			window.play.setEnabled(false);
            window.stop_communication.setEnabled(false);
            window.start_communication.setEnabled(false);
            window.test_mail.setEnabled(false);
            window.clear_log.setEnabled(false);
            window.text_command.setEditable(false);
            window.mail_from.setEditable(false);
            window.mail_cc.setEditable(false);
            window.mail_password.setEditable(false);
            window.mail_to.setEditable(false);
            window.submit.setEnabled(false);
            window.light_off.setEnabled(false);
            window.light_on.setEnabled(false);
            window.send_command.setEnabled(false);
            window.btnDisconnect.setEnabled(false);
            window.btnConnect.setEnabled(true);
            window.cboxPorts.setEnabled(true);
        }
    }

}
