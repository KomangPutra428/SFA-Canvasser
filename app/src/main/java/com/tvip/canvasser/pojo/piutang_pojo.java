package com.tvip.canvasser.pojo;

public class piutang_pojo {
    private String szCustomerId;
    private String szDocStatus;
    private String szDescription;
    private String decAmount;

    public piutang_pojo(String szCustomerId, String szDocStatus, String szDescription, String decAmount) {
        this.szCustomerId = szCustomerId;
        this.szDocStatus = szDocStatus;
        this.szDescription = szDescription;
        this.decAmount = decAmount;

    }

    public String getSzCustomerId() {
        return szCustomerId;
    }

    public String getSzDocStatus() {
        return szDocStatus;
    }

    public String getSzDescription() {
        return szDescription;
    }

    public String getDecAmount() {
        return decAmount;
    }
}
