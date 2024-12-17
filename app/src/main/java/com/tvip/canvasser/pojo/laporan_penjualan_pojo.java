package com.tvip.canvasser.pojo;

public class laporan_penjualan_pojo {
    private String szDocId;
    private String szCustomerId;
    private String szName;

    public laporan_penjualan_pojo(String szDocId, String szCustomerId, String szName) {
        this.szDocId = szDocId;
        this.szCustomerId = szCustomerId;
        this.szName = szName;
    }

    public String getSzDocId() {return szDocId;}
    public String getSzCustomerId() {return szCustomerId;}
    public String getSzName() { return szName; }

}
