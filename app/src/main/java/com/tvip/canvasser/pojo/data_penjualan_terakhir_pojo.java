package com.tvip.canvasser.pojo;

public class data_penjualan_terakhir_pojo {
    private String szDocId;
    private String dtmDoc;
    private String szCustomerId;
    private String szEmployeeId;
    private String decAmount;
    private String szDocCallId;

    public data_penjualan_terakhir_pojo(String szDocId, String dtmDoc, String szCustomerId,
                                        String szEmployeeId, String decAmount, String szDocCallId) {
        this.szDocId = szDocId;
        this.dtmDoc = dtmDoc;
        this.szCustomerId = szCustomerId;
        this.szEmployeeId = szEmployeeId;
        this.decAmount = decAmount;
        this.szDocCallId = szDocCallId;
    }

    public String getSzDocId() {return szDocId;}
    public String getDtmDoc() {return dtmDoc;}
    public String getSzCustomerId() {return szCustomerId;}
    public String getSzEmployeeId() {return szEmployeeId;}
    public String getDecAmount() {return decAmount;}
    public String getSzDocCallId() {return szDocCallId;}
}
