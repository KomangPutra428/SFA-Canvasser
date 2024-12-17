package com.tvip.canvasser.pojo;

public class data_pelanggan_luar_rute_pojo {
    private String szCustomerId;
    private String szName;
    private String szAddress;
    private String szLatitude;
    private String szLongitude;

    private String bStarted;
    private String bsuccess;
    private String bScanBarcode;
    private String bPostPone;

    private String szDocDO;

    public data_pelanggan_luar_rute_pojo(String szCustomerId, String szName, String szAddress, String szLatitude, String szLongitude, String bStarted, String bsuccess, String bScanBarcode, String bPostPone, String szDocDO) {
        this.szCustomerId = szCustomerId;
        this.szName = szName;
        this.szAddress = szAddress;
        this.szLatitude = szLatitude;
        this.szLongitude = szLongitude;
        this.bStarted = bStarted;
        this.bsuccess = bsuccess;
        this.bScanBarcode = bScanBarcode;
        this.bPostPone = bPostPone;
        this.szDocDO = szDocDO;
    }

    public String getSzCustomerId() {
        return szCustomerId;
    }

    public String getSzName() {
        return szName;
    }

    public String getSzAddress() { return szAddress; }

    public String getSzLatitude() { return szLatitude; }

    public String getSzLongitude() { return szLongitude; }

    public String getbStarted() { return bStarted; }

    public String getBsuccess() {
        return bsuccess;
    }

    public String getbScanBarcode() {
        return bScanBarcode;
    }

    public String getbPostPone() {
        return bPostPone;
    }

    public String getSzDocDO() {
        return szDocDO;
    }


}
