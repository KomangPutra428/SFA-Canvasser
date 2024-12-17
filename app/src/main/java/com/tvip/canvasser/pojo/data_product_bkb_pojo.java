package com.tvip.canvasser.pojo;

public class data_product_bkb_pojo {
    private String decQty;
    private String szProductId;
    private String szName;
    private String decPrice;
    private String szUomId;
    private String decQtyDeliveredDO;
    private String qtyEdit;

    private String TotalHarga;

    private String diskon_tiv;
    private String diskon_distributor;
    private String diskon_internal;

    public data_product_bkb_pojo(String decQty, String szProductId, String szName, String decPrice, String szUomId, String decQtyDeliveredDO, String qtyEdit) {
        this.decQty = decQty;
        this.szProductId = szProductId;
        this.szName = szName;
        this.decPrice = decPrice;
        this.szUomId = szUomId;
        this.decQtyDeliveredDO = decQtyDeliveredDO;
        this.qtyEdit = qtyEdit;

    }

    public String getDecQty() {return decQty;}

    public String getSzProductId() {return szProductId;}
    public String getSzName() { return szName; }
    public String getDecPrice() {return decPrice;}
    public String getSzUomId() {return szUomId;}

    public String getDecQtyDeliveredDO() {
        return decQtyDeliveredDO;
    }

    public String getTotalHarga() {return TotalHarga;}
    public void setTotalHarga(String totalHarga) {TotalHarga = totalHarga;}

    public String getQtyEdit() {return qtyEdit;}
    public void setQtyEdit(String qtyEdit) {this.qtyEdit = qtyEdit;}

    public String getDiskon_tiv() {return diskon_tiv;}
    public void setDiskon_tiv(String diskon_tiv) {this.diskon_tiv = diskon_tiv;}

    public void setDiskon_distributor(String diskon_distributor) {this.diskon_distributor = diskon_distributor;}
    public String getDiskon_distributor() {return diskon_distributor;}

    public void setDiskon_internal(String diskon_internal) {this.diskon_internal = diskon_internal;}
    public String getDiskon_internal() {return diskon_internal;}
}
