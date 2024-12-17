package com.tvip.canvasser.pojo;

public class so_penjualan_pojo {
    private String szDocId;
    private String dtmDoc;
    private String decAmount;

    public so_penjualan_pojo(String szDocId, String dtmDoc, String decAmount) {
        this.szDocId = szDocId;
        this.dtmDoc = dtmDoc;
        this.decAmount = decAmount;
    }

    public String getSzDocId() {return szDocId;}
    public String getDtmDoc() {return dtmDoc;}
    public String getDecAmount() {return decAmount;}
}
