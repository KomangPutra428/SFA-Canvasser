package com.tvip.canvasser.pojo;

public class summary_SKU_pojo {
    private String szName;
    private String decQty;

    public summary_SKU_pojo(String szName, String decQty) {
        this.szName = szName;
        this.decQty = decQty;
    }

    public String getSzName() {return szName;}
    public String getDecQty() {return decQty;}
}
