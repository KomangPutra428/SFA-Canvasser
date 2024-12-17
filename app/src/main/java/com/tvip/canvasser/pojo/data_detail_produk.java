package com.tvip.canvasser.pojo;

public class data_detail_produk {
    private final String nosot;
    private final String idstd;
    private final String idproduk;
    private final String qty;
    private final String nama;
    private final String satuan;

    public data_detail_produk(String nosot, String idstd, String idproduk, String qty, String nama, String satuan) {
        this.nosot = nosot;
        this.idstd = idstd;
        this.idproduk = idproduk;
        this.qty = qty;
        this.nama = nama;
        this.satuan = satuan;
    }

    public String getNosot() {
        return nosot;
    }

    public String getIdstd() {
        return idstd;
    }

    public String getIdproduk() {
        return idproduk;
    }

    public String getQty() {
        return qty;
    }

    public String getNama() {
        return nama;
    }

    public String getSatuan() {
        return satuan;
    }


}
