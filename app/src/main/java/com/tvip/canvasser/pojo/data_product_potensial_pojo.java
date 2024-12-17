package com.tvip.canvasser.pojo;

public class data_product_potensial_pojo {
    private String szId;
    private String szName;
    private String qty;

    public data_product_potensial_pojo(String szId, String szName, String qty) {
        this.szId = szId;
        this.szName = szName;
        this.qty = qty;
    }

    public String getSzId() {return szId;}

    public String getSzName() { return szName; }

    public String getQty() {return qty;}
    public void setQty(String qty) {this.qty = qty;}
}
