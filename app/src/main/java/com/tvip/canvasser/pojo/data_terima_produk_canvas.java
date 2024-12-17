package com.tvip.canvasser.pojo;

public class data_terima_produk_canvas {
    private String szProductId;
    private String parameter_ppn;
    private String szName;
    private String decPrice;
    private String szOrderItemtypeId;
    private String szUomId;
    private String id_customer;
    private String qty_awal;
    private String qty_terjual;

    public data_terima_produk_canvas(String szProductId, String parameter_ppn, String szName, String decPrice, String szOrderItemtypeId, String szUomId, String id_customer, String qty_awal, String qty_terjual) {
        this.szProductId = szProductId;
        this.parameter_ppn = parameter_ppn;
        this.szName = szName;
        this.decPrice = decPrice;
        this.szOrderItemtypeId = szOrderItemtypeId;
        this.szUomId = szUomId;
        this.id_customer = id_customer;
        this.qty_awal = qty_awal;
        this.qty_terjual = qty_terjual;
    }

    public String getSzProductId() {
        return szProductId;
    }

    public void setSzProductId(String szProductId) {
        this.szProductId = szProductId;
    }

    public String getParameter_ppn() {
        return parameter_ppn;
    }

    public void setParameter_ppn(String parameter_ppn) {
        this.parameter_ppn = parameter_ppn;
    }

    public String getSzName() {
        return szName;
    }

    public void setSzName(String szName) {
        this.szName = szName;
    }

    public String getDecPrice() {
        return decPrice;
    }

    public void setDecPrice(String decPrice) {
        this.decPrice = decPrice;
    }

    public String getSzOrderItemtypeId() {
        return szOrderItemtypeId;
    }

    public void setSzOrderItemtypeId(String szOrderItemtypeId) {
        this.szOrderItemtypeId = szOrderItemtypeId;
    }

    public String getSzUomId() {
        return szUomId;
    }

    public void setSzUomId(String szUomId) {
        this.szUomId = szUomId;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getQty_awal() {
        return qty_awal;
    }

    public void setQty_awal(String qty_awal) {
        this.qty_awal = qty_awal;
    }

    public String getQty_terjual() {
        return qty_terjual;
    }

    public void setQty_terjual(String qty_terjual) {
        this.qty_terjual = qty_terjual;
    }
}
