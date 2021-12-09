/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serialcom;
import javax.swing.JFrame;
import javax.swing.JSlider;
import com.fazecast.jSerialComm.*;
import java.io.InputStream;
import java.util.Scanner;
import java.io.PrintWriter;
import java.awt.BorderLayout;
import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;
/**
 *
 * @author User
 */
public class Serialcom {
    /**
     * @param args the command line arguments
     */
    static SerialPort chosenPort;
    public static void main(String[] args) {
        //project for displaying time and date on an lcd display
        JFrame window = new JFrame();
        window.setTitle("Date and time");
        window.setSize(400, 75);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create dropBox and connect button
        JComboBox<String> portList = new JComboBox<String>();
        JButton connectButton = new JButton("Connect");
        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectButton);
        window.add(topPanel, BorderLayout.NORTH);
        //poputate the dropBox
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (int i = 0; i < portNames.length; i ++)
            portList.addItem(portNames[i].getSystemPortName());
        
        connectButton.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent arg0){
                if (connectButton.getText().equals("Connect")){
                    //attempt to connect to serial port
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                   if (chosenPort.openPort()){
                       connectButton.setText("Disconnect");
                       portList.setEnabled(false);
                       //create new thread to send data to the arduino
                               // for sending values through the serial ports 
        Thread thread = new Thread(){
            @Override public void run(){
                try{Thread.sleep(100);}catch(Exception e){}
                
                PrintWriter output = new PrintWriter(chosenPort.getOutputStream());
                while(true){
                    output.print(new SimpleDateFormat("hh:mm:ss a MMMMMMMdd, yyyy").format(new Date()));
                    output.flush();
                    try{Thread.sleep(100);}catch(Exception e){}
                   
                }
            }
        };
        thread.start();
                   } 
                }else {
                   //disconnect from the serial port
                    chosenPort.closePort();
                    portList.setEnabled(true);
                    connectButton.setText("Connect");
                }
            }
            
        });
        
        window.setVisible(true);  
        
        
        
        /*JFrame window = new JFrame();
        JSlider slider = new JSlider();
        slider.setMaximum(1023);
        window.add(slider);
        window.pack();
        window.setVisible(true);
        
        //creates an array of availiable ports
        SerialPort ports[] = SerialPort.getCommPorts();
        System.out.println("Select a port");
        
        int i  = 1;
        for (SerialPort port : ports){
            System.out.println(i++ + ". " + port.getSystemPortName());
        }
        Scanner input = new Scanner(System.in);
        int choosenPort = input.nextInt();
        
        SerialPort port = ports[choosenPort - 1];
        //for opening ports 
        if (port.openPort()){
            System.out.println("port opened successfully");
        }
        else {
            System.out.println("unable to open port");
                    return;
        }
        //for receiving values through the serial ports
       /* port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING,  0  , 0);
        
        Scanner data = new Scanner(port.getInputStream());
        while(data.hasNextLine()){
            System.out.println(data.nextLine());
            int number = 0 ;
                try {number = Integer.parseInt(data.nextLine());}catch(Exception e){}
                slider.setValue(number);
                
        }
        
        // for sending values through the serial ports 
        Thread thread = new Thread(){
            @Override public void run(){
                try{Thread.sleep(100);}catch(Exception e){}
                
                PrintWriter output = new PrintWriter(port.getOutputStream());
                while(true){
                    output.print(1);
                    output.flush();
                    try{Thread.sleep(1000);}catch(Exception e){}
                    output.print(0);
                    output.flush();
                    try{Thread.sleep(1000);}catch(Exception e){}
                }
            }
        };
        thread.start();
        */
    }
}
