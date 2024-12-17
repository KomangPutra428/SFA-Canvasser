package com.tvip.canvasser.pojo;

public class sales_performance_pojo {
    private String decQty;
    private String szName;
    private String decAmount;

    public sales_performance_pojo(String decQty, String szName, String decAmount) {
        this.decQty = decQty;
        this.szName = szName;
        this.decAmount = decAmount;
    }

    public String getDecQty() {
        return decQty;
    }

    public String getSzName() {
        return szName;
    }

    public String getDecAmount() {
        return decAmount;
    }
}
