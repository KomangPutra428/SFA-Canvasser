package com.tvip.canvasser.pojo;

public class laporan_penjualan_product_pojo {
    private String szName;
    private String decQty;
    private String decPrice;
    private String decAmount;

    public laporan_penjualan_product_pojo(String szName, String decQty, String decPrice, String decAmount) {
        this.szName = szName;
        this.decQty = decQty;
        this.decPrice = decPrice;
        this.decAmount = decAmount;
    }
    public String getSzName() { return szName; }
    public String getDecQty() {return decQty;}
    public String getDecPrice() {return decPrice;}
    public String getDecAmount() {return decAmount;}
}
