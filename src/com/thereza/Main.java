package com.thereza;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main extends JFrame {

    JTextArea   textArea;
    JScrollPane scrollPane;
    Container container;
    JPanel      panel;
    JLabel      label;
    JButton btnStart,btnStop;
    private static final String initWifiCommand = "netsh wlan set hostednetwork mode=allow ssid=kwikkoders key=123456789";
    private static final String startWifiCommand = "netsh wlan start hostednetwork";
    private static final String stopWifiCommand = "netsh wlan stop hostednetwork";
    private static final String showWifiCommand = "netsh wlan show hostednetwork";

    public Main() {
        initialize();
        addToPanel();
        processBuilder(showWifiCommand);
    }

    public static void main(String[] args) {
        Main cw = new Main();
        cw.setDefaultCloseOperation(3); // JFrame.EXIT_ON_CLOSE => 3
        cw.pack();
        cw.setTitle("Tithi Wifi Zone");
        cw.setSize(400,300);
        cw.setVisible(true);
    }


    public void initialize() {
        container = getContentPane();
        textArea = new JTextArea(10,20);
        scrollPane = new JScrollPane(textArea);

        btnStart = new JButton(" Start Wifi ");
        btnStop = new JButton(" Stop Wifi ");

        panel = new JPanel();

        label = new JLabel(" Developed by theReza ");
        label.setBackground(Color.yellow);
    }

    public void addToPanel() {

        panel.add(btnStart);
        panel.add(btnStop);

        container.add(scrollPane, BorderLayout.NORTH);
        container.add(panel, BorderLayout.CENTER);
        container.add(label, BorderLayout.SOUTH);

        btnStart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                //label.setText("No.of Words : "+ getTotalWords(textArea.getText()));
                processBuilder(initWifiCommand);
            }
        });

        btnStop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
                //label.setText("No.of Words : "+ getTotalWords(textArea.getText()));
                processBuilder(stopWifiCommand);
            }
        });
    }

    public int getTotalWords( String text ){
        String words[]= text.trim().split(" ");
        return words.length;
    }

    public void processBuilder(String command){
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Windows
        processBuilder.command("cmd.exe", "/c", command);

        try {

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Started")) {
                    //System.out.println("data : " + ssidArr[1]);
                    diableButton(btnStart);
                    enableButton(btnStop);
                }
                if (line.contains("Not started")) {
                    //System.out.println("data : " + ssidArr[1]);
                    enableButton(btnStart);
                    diableButton(btnStop);
                }
                if (line.contains("The hosted network stopped")) {
                    //System.out.println("data : " + ssidArr[1]);
                    enableButton(btnStart);
                    diableButton(btnStop);
                }
                if (line.contains("The hosted network started")) {
                    System.out.println("data : " + line);
                    diableButton(btnStart);
                    enableButton(btnStop);
                    textArea.setText(line);
                    //processBuilder(showWifiCommand);

                }
                if (line.contains("The user key passphrase of the hosted network has been successfully changed")){
                    processBuilder(startWifiCommand);
                }

                sb.append(line+"\n");

            }
            textArea.setText(sb.toString().trim());

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void diableButton(JButton btn){
        btn.setEnabled(false);
        btn.setBackground(Color.GRAY);
    }
    private void enableButton(JButton btn){
        btn.setEnabled(true);
        btn.setBackground(Color.GREEN);
    }

    /*public static ArrayList scanWiFi() {
        ArrayList<String> networkList = new ArrayList<>();
        try {
            // Execute command
            String command = "netsh wlan show hostednetwork";
            Process p = Runtime.getRuntime().exec(command);
            try {
                p.waitFor();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream())
            );
            String line;
            StringBuilder sb = new StringBuilder();
            String ssidArr[];

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                if (line.contains("SSID ") && !line.contains("BSSID ")) {
                    sb.append(line);
                    networkList.add(line.split(":")[1]);
                    System.out.println("data : " + ssidArr[1]);
                }
            }
            System.out.println(networkList);
        } catch (IOException e) {
        }
        return networkList;
    }*/
}
