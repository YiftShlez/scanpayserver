package com.scanpayil.scanpayserver;

public class Store
{
    private int id;
    private String name;
    private String city;
    private String phoneNumber;
    private String emailAddr;
    private String wifiSSID;
    public Store(int id, String name, String city, String phoneNumber, String emailAddr, String wifiSSID)
    {
        this.id = id;
        this.name = name;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.emailAddr = emailAddr;
        this.wifiSSID = wifiSSID;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddr()
    {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr)
    {
        this.emailAddr = emailAddr;
    }

    public String getWifiSSID()
    {
        return wifiSSID;
    }

    public void setWifiSSID(String wifiSSID)
    {
        this.wifiSSID = wifiSSID;
    }

    @Override
    public String toString()
    {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
