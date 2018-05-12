package com.scanpayil.scanpayserver;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        new Scanner (System.in).nextLine();
        server.isRunning = false;
    }
}
