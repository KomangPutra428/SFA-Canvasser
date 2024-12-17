package com.tvip.canvasser.pojo;

public class data_pelanggan_pojo {
    private String szId;
    private String szName;
    private String szAddress;
    private String szLangitude;
    private String szLongitude;

    public data_pelanggan_pojo(String szId, String szName, String szAddress, String szLangitude, String szLongitude) {
        this.szId = szId;
        this.szName = szName;
        this.szAddress = szAddress;
        this.szLangitude = szLangitude;
        this.szLongitude = szLongitude;
    }

    public String getSzId() { return szId; }

    public String getSzName() { return szName; }
    public String getSzAddress() { return szAddress; }

    public String getSzLangitude() { return szLangitude; }
    public String getSzLongitude() { return szLongitude; }

    @Override
    public String toString() {
        return szName;
    }
}

