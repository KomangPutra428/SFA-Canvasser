package com.tvip.canvasser.pojo;

public class data_list_pelanggan {
    private final String szId;
    private final String szName;
    private final String szAddress;
    private final String szLangitude;
    private final String szLongitude;
    private final String nosot;
    private final String idstd;
    private final String idcustomer;

    public data_list_pelanggan(String szId, String szName, String szAddress, String szLangitude, String szLongitude, String nosot, String idstd, String idcustomer) {
        this.szId = szId;
        this.szName = szName;
        this.szAddress = szAddress;
        this.szLangitude = szLangitude;
        this.szLongitude = szLongitude;
        this.nosot = nosot;
        this.idstd = idstd;
        this.idcustomer = idcustomer;

    }

    public String getSzId() {
        return szId;
    }

    public String getSzName() {
        return szName;
    }

    public String getSzAddress() {
        return szAddress;
    }

    public String getSzLangitude() {
        return szLangitude;
    }

    public String getSzLongitude() {
        return szLongitude;
    }

    public String getNosot() {
        return nosot;
    }

    public String getIdstd() {
        return idstd;
    }

    public String getIdcustomer() {
        return idcustomer;
    }

    @Override
    public String toString() {
        return szName;
    }
}


