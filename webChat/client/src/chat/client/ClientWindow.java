package chat.client;

import network.TCPConnection;
import network.TCPListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public class ClientWindow extends JFrame implements ActionListener, TCPListener{
    //185.31.164.41
    private static final String IP_ADR = "192.168.1.2";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextArea log = new JTextArea();
    private  final JTextField fieldNick = new JTextField("dimas");
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        log.setEditable(false);
        log.setLineWrap(true);
        add(log, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNick, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADR, PORT);
        } catch (Exception e) {
            printMsg("Connection exception" + e);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if (msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNick.getText() + ": " + msg);
    }


    @Override
    public void onConnectionReady(TCPConnection TCPConnection) {
        printMsg("Conn ready");
    }

    @Override
    public void onReceiveString(TCPConnection TCPConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection TCPConnection) {
        printMsg("Conn close");
    }

    @Override
    public void onException(TCPConnection TCPConnection, Exception e) {
        printMsg("Connection exception" + e);
    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
