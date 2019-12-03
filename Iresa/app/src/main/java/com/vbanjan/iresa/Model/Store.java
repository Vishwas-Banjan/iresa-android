package com.vbanjan.iresa.Model;

public class Store {


    String storeName, storeStreet, storeState, storeZip, storeCity;
    String storeDocumentID;
    Double storeLat, storeLng;

    public Store(String storeName, String storeStreet, String storeState, String storeZip, String storeCity, String storeDocumentID, Double storeLat, Double storeLng) {
        this.storeName = storeName;
        this.storeStreet = storeStreet;
        this.storeState = storeState;
        this.storeZip = storeZip;
        this.storeCity = storeCity;
        this.storeDocumentID = storeDocumentID;
        this.storeLat = storeLat;
        this.storeLng = storeLng;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", storeStreet='" + storeStreet + '\'' +
                ", storeState='" + storeState + '\'' +
                ", storeZip='" + storeZip + '\'' +
                ", storeCity='" + storeCity + '\'' +
                ", storeDocumentID='" + storeDocumentID + '\'' +
                ", storeLat=" + storeLat +
                ", storeLng=" + storeLng +
                '}';
    }

    public Double getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(Double storeLat) {
        this.storeLat = storeLat;
    }

    public Double getStoreLng() {
        return storeLng;
    }

    public void setStoreLng(Double storeLng) {
        this.storeLng = storeLng;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public String getStoreZip() {
        return storeZip;
    }

    public void setStoreZip(String storeZip) {
        this.storeZip = storeZip;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreDocumentID() {
        return storeDocumentID;
    }

    public void setStoreDocumentID(String storeDocumentID) {
        this.storeDocumentID = storeDocumentID;
    }
}
