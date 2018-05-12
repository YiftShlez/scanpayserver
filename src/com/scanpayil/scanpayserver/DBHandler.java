package com.scanpayil.scanpayserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHandler
{
    private static final String jdbc_driver = "com.mysql.jdbc.Driver";
    private static final String db_url = "jdbc:mysql://localhost/scanpay"; //database url
    private static final String db_user = "dbuser";
    private static final String db_pass = "dbpassword"; //database login details

    private static final String storesTableName = "stores"; //stores table name

    private static final String storeIDColumn = "storeID"; //database column names
    private static final String storeNameColumn = "storeName";
    private static final String cityColumn = "city";
    private static final String phoneNoColumn = "phoneNo";
    private static final String emailAddrColumn = "emailAddr";
    private static final String wifiColumn = "wifi";

    private Connection conn;
    private Statement stmt;
    public DBHandler()
    {
        try
        {
            Class.forName(jdbc_driver); //connecting to the jdbc driver
        } catch (ClassNotFoundException cnfe)
        {
            System.err.println("Error connecting to jdbc driver");
            System.exit(1);
        }
        try
        {
            conn = DriverManager.getConnection(db_url, db_user, db_pass); //connecting to database
        } catch (SQLException sqle)
        {
            System.err.println("Error connecting to database");
            sqle.printStackTrace();
            System.exit(2);
        }
        try
        {
            stmt = conn.createStatement();
        } catch (SQLException sqle)
        {
            System.err.println("Error creating statement");
            sqle.printStackTrace();
            System.exit(3);
        }
    }

    /**
     *
     * A function for adding a store to the database
     * @param storeName the name of the store
     * @param storeCity the city where the store is in
     * @param phoneNo the phone number of the store
     * @param emailAddr the email address of the store
     * @param wifi the SSID of the store's wifi, used to identify the store by the user's device
     * @return true if the store was registered successfully, false otherwise
     */
    public boolean registerStore(String storeName, String storeCity,
                                 String phoneNo, String emailAddr, String wifi)
    {
        try
        {
            return stmt.execute("INSERT INTO stores (" + storeNameColumn + ", " +
                    cityColumn + ", " + phoneNoColumn + ", " + emailAddrColumn + ", " + wifiColumn + ")" +
                    " VALUES ('" + storeName + "' '" + storeCity + "' '" + phoneNo +
                    "' '" + emailAddr +  "', '" + wifi + "')");
            //sql script to add the store details to the database
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
            return false;
        }
    }

    /**
     * A function to check if a specific wifi SSID belongs to any store
     * @param SSID the wifi SSID, given by the user's device
     * @return an array of the stores whose wifi SSID is the given SSID
     */
    public Integer [] getStoresFromSSID (String SSID)
    {
        ResultSet result = null;
        List <Integer> stores = new ArrayList<>(); //a list of the store IDs
        try
        {
            result = stmt.executeQuery("SELECT " + storeIDColumn + " FROM " +
                    storesTableName + " WHERE "  + wifiColumn + "='" + SSID + "'");
            if (result != null)
            {
                //check all the results until reached the last line
                for (result.next();!result.isAfterLast(); result.next())
                {
                    stores.add(result.getInt(storeIDColumn));
                }
            }
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return stores.toArray(new Integer [stores.size()]); //convert the list to an integer array and return
    }

    /**
     * A function to get info about a store using its storeID
     * @param storeID the storeID of the store
     * @return a Store data object with the info about the store
     */
    public Store getStore (int storeID)
    {
        ResultSet result = null;
        try
        {
            //a sql query to get all of the information about the store using the storeID
            result = stmt.executeQuery("SELECT " + storeNameColumn + ", " + cityColumn + ", " +
                    phoneNoColumn + ", " + emailAddrColumn + ", " + wifiColumn + " FROM " +
                    storesTableName + " WHERE " + storeIDColumn + "=" + storeID);
            if (result != null)
            {
                result.next();
                if (!result.isAfterLast())
                {
                    return new Store(storeID,
                            result.getString(storeNameColumn),
                            result.getString(cityColumn),
                            result.getString(phoneNoColumn),
                            result.getString(emailAddrColumn),
                            result.getString(wifiColumn)); //returning the store object with the information
                }
            }
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null; //if a store wasn't found, or there was an exception, a null object is returned
    }

    /**
     * This method is used to get the name of a store with a given storeID
     * @param storeID the storeID of the store
     * @return the name of the store, or null if didn't find store
     */
    public String getStoreName (int storeID)
    {
        //a sql query to select the store name where the storeID is the given id
        String query = "SELECT " + storeNameColumn + " FROM " + storesTableName +
                " WHERE " + storeIDColumn + "=" + storeID;
        ResultSet result;
        try
        {
            result = stmt.executeQuery(query);
            result.next();
            if (!result.isAfterLast())
                return result.getString(storeNameColumn); //returns the store name
        }
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        return null;
    }

}



