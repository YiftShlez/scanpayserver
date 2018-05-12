package com.scanpayil.scanpayserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
/**
 * This class is responsible for handling server-client connections using sockets
 * @author Yiftah Schlesinger
 * @version 1.0
 */
public class Server extends Thread
{
    private ServerSocket serverSocket;
    private final static int port = 54789;
    private HashMap<Integer, ClientThread> clients = new HashMap<>();
    DBHandler handler = new DBHandler(); //A class for handling the connection with the database

    int count = 1; //The number of the clients

    public boolean isRunning = false; //While isRunning is true, the server is running
    @Override
    public void run() //the main function of the server
    {
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioe)
        {
            System.err.println("Error connecting to server");
            ioe.printStackTrace();
            System.exit(4);
        }
        try
        {
            System.out.println("Server started in address " + InetAddress.getLocalHost().getHostAddress()
                    + " in port " + port);
        }
        catch (UnknownHostException uhe)
        {
            uhe.printStackTrace();
        }
        while (isRunning)
        {
            ClientThread currentClient = null;
            try
            {
                //Every time a new client is received, a new thread is made for connecting with him
                currentClient = new ClientThread(serverSocket.accept(),
                        count++);
                System.out.println("Received connection");
                clients.put(currentClient.getClientID(), currentClient); //adding the client to the clients hashmap
                currentClient.start(); //starting the client's thread and beginning communication
            }
            catch (IOException ioe)
            {
                System.err.println("Error connecting to client #"
                        + (currentClient!=null?"" + currentClient.getClientID():"unknown"));
            }
        }
    }

    class ClientThread extends Thread //A class for handling the communication with a single client
    {
        private Socket clientSocket;
        private BufferedReader clientIn;
        private PrintWriter clientOut; //socket and streams for the connection
        private int clientID; //an id used to identify the client

        ClientThread(Socket clientSocket, int clientID)
        {
            this.clientSocket = clientSocket;
            this.clientID = clientID;
        }

        int getClientID()
        {
            return clientID;
        }

        public void run()
        {
            try
            {
                clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientOut = new PrintWriter(clientSocket.getOutputStream());
            } catch (IOException ioe)
            {
                System.err.println("Error connecting to client #" + clientID);
                ioe.printStackTrace();
                try
                {
                    clientSocket.close();
                } catch (IOException ioe2)
                {
                    ioe2.printStackTrace();
                }
                clients.remove(clientID);
                stop();
            }
            System.out.println("Connected to client #" + clientID);
            ClientListener clientListener = new ClientListener(clientIn, clientID, (String line) ->
            {
                System.out.println(line);
                if (line.startsWith("checkWifi:"))
                {
                    String WifiSSID = line.substring(10);

                }
                if (line.startsWith("store#"))
                {
                    int storeID = Integer.parseInt(line.substring(6));
                    Store store = handler.getStore(storeID);
                    if (store != null)
                    {
                        clientOut.println("storedetails#" + store.getName() + ";" + store.getCity() + ";"
                                + store.getPhoneNumber() +  ";" + store.getEmailAddr());
                    }
                }
            });
            clientListener.start();
        }
        class ClientListener extends Thread
        {
            private BufferedReader in;
            private int clientID;
            private LineEvent le;

            public ClientListener(BufferedReader in, int clientID, LineEvent le)
            {
                ClientListener.this.in = in;
                ClientListener.this.clientID = clientID;
                ClientListener.this.le = le;
            }

            public void run()
            {
                while (in != null)
                {
                    try
                    {
                        String line = in.readLine();
                        if (line == null)
                            return;
                        le.onLineReceived(line);
                    } catch (IOException ioe)
                    {
                        System.err.println("Error receiving line from client #" + clientID);
                    }
                }
            }
        }
    }

    interface LineEvent
    {
        void onLineReceived(String line);
    }
}