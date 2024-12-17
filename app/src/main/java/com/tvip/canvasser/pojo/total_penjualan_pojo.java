package com.tvip.canvasser.pojo;

public class total_penjualan_pojo {
    private String szId;
    private String szName;
    private String decPrice;
    private String stock;
    private String display;
    private String jumlah_harga;
    private String jumlah_diskon;
    private String stockawal;
    private String expired;
    private String szUomId;
    private String disc_distributor;
    private String disc_internal;
    private String disc_principle;


    public total_penjualan_pojo(String szId, String szName,
                                String decPrice, String stock,
                                String display, String jumlah_harga,
                                String jumlah_diskon,
                                String stockawal, String expired,
                                String szUomId,
                                String disc_distributor, String disc_internal,
                                String disc_principle) {
        this.szId = szId;
        this.szName = szName;
        this.decPrice = decPrice;
        this.stock = stock;
        this.display = display;
        this.jumlah_harga = jumlah_harga;
        this.jumlah_diskon = jumlah_diskon;
        this.stockawal = stockawal;
        this.expired = expired;
        this.szUomId = szUomId;
        this.disc_distributor = disc_distributor;
        this.disc_internal = disc_internal;
        this.disc_principle = disc_principle;

    }

    public String getSzId() { return szId; }
    public String getSzName() { return szName; }
    public String getDecPrice() { return decPrice; }
    public String getStock() { return stock; }
    public String getDisplay() { return display; }
    public String getJumlah_harga() { return jumlah_harga; }
    public String getJumlah_diskon() { return jumlah_diskon; }

    public String getStockawal() {
        return stockawal;
    }

    public String getExpired() {
        return expired;
    }

    public String getSzUomId() {return szUomId;}

    public String getDisc_distributor() {return disc_distributor;}
    public String getDisc_internal() {return disc_internal;}
    public String getDisc_principle() {return disc_principle;}
}
