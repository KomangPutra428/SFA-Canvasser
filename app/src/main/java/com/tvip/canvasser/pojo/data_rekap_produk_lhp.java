package com.tvip.canvasser.pojo;

public class data_rekap_produk_lhp {
    private String idproduk;
    private String namaproduk;
    private String qtyawal;
    private String totalhargaqtyawal;
    private String qtyterjual;
    private String totalhargaqtyterjual;

    public data_rekap_produk_lhp(String idproduk, String namaproduk, String qtyawal, String totalhargaqtyawal,
                                 String qtyterjual, String totalhargaqtyterjual) {
        this.idproduk = idproduk;
        this.namaproduk = namaproduk;
        this.qtyawal = qtyawal;
        this.totalhargaqtyawal = totalhargaqtyawal;
        this.qtyterjual = qtyterjual;
        this.totalhargaqtyterjual = totalhargaqtyterjual;


    }

    public String getIdproduk() {
        return idproduk;
    }

    public void setIdproduk(String idproduk) {
        this.idproduk = idproduk;
    }

    public String getNamaproduk() {
        return namaproduk;
    }

    public void setNamaproduk(String namaproduk) {
        this.namaproduk = namaproduk;
    }

    public String getQtyawal() {
        return qtyawal;
    }

    public void setQtyawal(String qtyawal) {
        this.qtyawal = qtyawal;
    }

    public String getTotalhargaqtyawal() {
        return totalhargaqtyawal;
    }

    public void setTotalhargaqtyawal(String totalhargaqtyawal) {
        this.totalhargaqtyawal = totalhargaqtyawal;
    }

    public String getQtyterjual() {
        return qtyterjual;
    }

    public void setQtyterjual(String qtyterjual) {
        this.qtyterjual = qtyterjual;
    }

    public String getTotalhargaqtyterjual() {
        return totalhargaqtyterjual;
    }

    public void setTotalhargaqtyterjual(String totalhargaqtyterjual) {
        this.totalhargaqtyterjual = totalhargaqtyterjual;
    }
}
