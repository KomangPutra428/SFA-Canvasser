package com.tvip.canvasser.pojo;

public class data_history_bkb_pojo {
    private String szDocId;
    private String szName;
    private String szProductId;
    private String decPrice;
    private String decQty;
    private String decAmount;

    private String decDiscPrinciple;
    private String decDiscDistributor;
    private String decDiscInternal;

    public data_history_bkb_pojo(String szDocId, String szName, String szProductId, String decPrice, String decQty, String decAmount,
                                 String decDiscPrinciple, String decDiscDistributor, String decDiscInternal) {
        this.szDocId = szDocId;
        this.szName = szName;
        this.szProductId = szProductId;
        this.decPrice = decPrice;
        this.decQty = decQty;
        this.decAmount = decAmount;

        this.decDiscPrinciple = decDiscPrinciple;
        this.decDiscDistributor = decDiscDistributor;
        this.decDiscInternal = decDiscInternal;

    }

    public String getSzProductId() {
        return szProductId;
    }

    public String getDecAmount() {
        return decAmount;
    }

    public String getDecPrice() {
        return decPrice;
    }

    public String getDecQty() {
        return decQty;
    }

    public String getSzDocId() {
        return szDocId;
    }

    public String getSzName() {
        return szName;
    }

    public String getDecDiscDistributor() {
        return decDiscDistributor;
    }

    public String getDecDiscInternal() {
        return decDiscInternal;
    }

    public String getDecDiscPrinciple() {
        return decDiscPrinciple;
    }
}
