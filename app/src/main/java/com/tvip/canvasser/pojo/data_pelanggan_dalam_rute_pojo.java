package com.tvip.canvasser.pojo;

public class data_pelanggan_dalam_rute_pojo {
    private String szCustomerId;
    private String szName;
    private String intItemNumber;
    private String szAddress;
    private String szLatitude;
    private String szLongitude;

    private String bStarted;
    private String bsuccess;
    private String bScanBarcode;
    private String bPostPone;
    private String szRefDocId;

    private String szFailReason;
    private String szDocDO;

    private String szCustomerId_After;
    private String nama_tokoalih;
    private String alamatalih;
    private String szStatusPending;

    public data_pelanggan_dalam_rute_pojo(String szCustomerId, String szName,
                                          String intItemNumber, String szAddress,
                                          String szLatitude, String szLongitude, String bStarted,
                                          String bsuccess, String bScanBarcode, String bPostPone,
                                          String szRefDocId, String szFailReason, String szDocDO,
                                          String szCustomerId_After, String nama_tokoalih, String alamatalih,
                                          String szStatusPending) {
        this.szCustomerId = szCustomerId;
        this.szName = szName;
        this.intItemNumber = intItemNumber;
        this.szAddress = szAddress;
        this.szLatitude = szLatitude;
        this.szLongitude = szLongitude;
        this.bStarted = bStarted;
        this.bsuccess = bsuccess;
        this.bScanBarcode = bScanBarcode;
        this.bPostPone = bPostPone;
        this.szRefDocId = szRefDocId;
        this.szFailReason = szFailReason;
        this.szDocDO = szDocDO;
        this.szCustomerId_After = szCustomerId_After;
        this.nama_tokoalih = nama_tokoalih;
        this.alamatalih = alamatalih;
        this.szStatusPending = szStatusPending;
    }

    public String getSzCustomerId() {
        return szCustomerId;
    }

    public String getSzName() {
        return szName;
    }

    public String getIntItemNumber() {
        return intItemNumber;
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

    public String getSzRefDocId() {return szRefDocId;}

    public String getSzFailReason() {
        return szFailReason;
    }

    public String getSzDocDO() {return szDocDO;}

    public String getSzCustomerId_After() {
        return szCustomerId_After;
    }

    public String getNama_tokoalih() {
        return nama_tokoalih;
    }

    public String getAlamatalih() {
        return alamatalih;
    }

    public String getSzStatusPending() {
        return szStatusPending;
    }
}
