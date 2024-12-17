package com.tvip.canvasser.pojo;

public class data_pelanggan_dalam_rute_pojo_driver {
    private String Id_std;
    private String szCustomerId;
    private String szName;
    private String szAddress;
    private String szLatitude;
    private String szLongitude;

    private String bStarted;
    private String bsuccess;
    private String bScanBarcode;
    private String bPostPone;
    private String szRefDocId;

    private String ritase;

    public data_pelanggan_dalam_rute_pojo_driver(String Id_std, String szCustomerId, String szName, String szAddress, String szLatitude, String szLongitude, String bStarted, String bsuccess, String bScanBarcode, String bPostPone, String szRefDocId, String ritase) {
        this.Id_std = Id_std;
        this.szCustomerId = szCustomerId;
        this.szName = szName;
        this.szAddress = szAddress;
        this.szLatitude = szLatitude;
        this.szLongitude = szLongitude;
        this.bStarted = bStarted;
        this.bsuccess = bsuccess;
        this.bScanBarcode = bScanBarcode;
        this.bPostPone = bPostPone;
        this.szRefDocId = szRefDocId;
        this.ritase = ritase;
    }

    public String getId_std() {
        return Id_std;
    }

    public String getSzCustomerId() {
        return szCustomerId;
    }

    public String getSzName() {
        return szName;
    }

    public String getSzAddress() {
        return szAddress;
    }

    public String getSzLatitude() {
        return szLatitude;
    }

    public String getSzLongitude() {
        return szLongitude;
    }

    public String getbStarted() {
        return bStarted;
    }

    public String getBsuccess() {
        return bsuccess;
    }

    public String getbScanBarcode() {
        return bScanBarcode;
    }

    public String getbPostPone() {
        return bPostPone;
    }

    public String getSzRefDocId() {
        return szRefDocId;
    }

    public String getritase() {
        return ritase;
    }

    @Override
    public String toString() {
        return szName;
    }
}

