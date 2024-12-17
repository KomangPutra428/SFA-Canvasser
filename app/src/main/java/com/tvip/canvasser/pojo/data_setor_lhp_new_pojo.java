package com.tvip.canvasser.pojo;

public class data_setor_lhp_new_pojo {private String id_driver;
    private String nomor_do;
    private String nomor_std;
    private String nomor_bkb;
    private String tanggal_do;
    private String driver;
    private String idtoko;
    private String toko;
    private String rit;
    private String idproduk;
    private String produk;
    private String nopol;

    private String stock_awal_docsoitem;
    private String terjual;
    private String stock_akhir;
    private String harga;
    private String sub_total;
    private String diskon;
    private String total;
    private String pay;
    private String totalhargado;

    private String sumtotaldiskon;
    private String sumtotalharga;
    private String sumtotalpembayaran;


    public data_setor_lhp_new_pojo(String id_driver, String nomor_do, String nomor_std, String nomor_bkb,
                                   String tanggal_do, String driver, String idtoko,  String toko,
                                   String rit, String idproduk, String produk, String nopol,
                                   String stock_awal_docsoitem,String terjual, String stock_akhir,
                                   String harga, String sub_total, String diskon, String total, String pay,
                                   String totalhargado, String sumtotaldiskon, String sumtotalharga, String sumtotalpembayaran) {
        this.id_driver = id_driver;
        this.nomor_do = nomor_do;
        this.nomor_std = nomor_std;
        this.nomor_bkb = nomor_bkb;
        this.tanggal_do = tanggal_do;
        this.driver = driver;
        this.toko = toko;
        this.idtoko = idtoko;
        this.rit = rit;
        this.idproduk = idproduk;
        this.produk = produk;
        this.nopol = nopol;
        this.stock_awal_docsoitem = stock_awal_docsoitem;
        this.terjual = terjual;
        this.stock_akhir = stock_akhir;
        this.harga = harga;
        this.sub_total = sub_total;
        this.diskon = diskon;
        this.total = total;
        this.pay = pay;
        this.totalhargado = totalhargado;
        this.sumtotaldiskon = sumtotaldiskon;
        this.sumtotalharga = sumtotalharga;
        this.sumtotalpembayaran = sumtotalpembayaran;

    }


    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getNomor_do() {
        return nomor_do;
    }

    public void setNomor_do(String nomor_do) {
        this.nomor_do = nomor_do;
    }

    public String getNomor_std() {
        return nomor_std;
    }

    public void setNomor_std(String nomor_std) {
        this.nomor_std = nomor_std;
    }

    public String getBkb() { return nomor_bkb; }
    public void setBkb(String nomor_bkb) { this.nomor_bkb = nomor_bkb; }

    public String getTanggaldo() {
        return tanggal_do;
    }

    public void setTanggal_do(String tanggal_do) {
        this.tanggal_do = tanggal_do;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String name_driver) {
        this.driver = name_driver;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getIdtoko() {
        return idtoko;
    }

    public void setIdtoko(String idtoko) {
        this.idtoko = idtoko;
    }

    public String getRit() {
        return rit;
    }

    public void setRit(String rit) {
        this.rit = rit;
    }

    public String getIdproduk() {
        return idproduk;
    }

    public void setIdproduk(String idproduk) {
        this.idproduk = idproduk;
    }

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

//    public String getStockawal() {
//        return stock_awal;
//    }
//
//    public void setStockawal(String stock_awal) {
//        this.stock_awal = stock_awal;
//    }

    public String getStockawaldocso() {
        return stock_awal_docsoitem;
    }

    public void setStockawaldocso(String stock_awal_docsoitem) {
        this.stock_awal_docsoitem = stock_awal_docsoitem;
    }

    public String getTerjual() {
        return terjual;
    }

    public void setTerjual(String terjual) {
        this.terjual = terjual;
    }

    public String getStockakhir() {
        return stock_akhir;
    }

    public void setStockakhir(String stock_akhir) {
        this.stock_akhir = stock_akhir;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getSubtotal() {
        return sub_total;
    }

    public void setSubtotal(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }


    public String getTotalhargado() {
        return totalhargado;
    }

    public void setTotalhargado(String totalhargado) {
        this.totalhargado = totalhargado;
    }

    public String getSumtotaldiskon() {
        return sumtotaldiskon;
    }

    public void setSumtotaldiskon(String sumtotaldiskon) {
        this.sumtotaldiskon = sumtotaldiskon;
    }

    public String getSumtotalharga() {
        return sumtotalharga;
    }

    public void setSumtotalharga(String sumtotalharga) {
        this.sumtotalharga = sumtotalharga;
    }

    public String getSumtotalpembayaran() {
        return sumtotalpembayaran;
    }

    public void setSumtotalpembayaran(String sumtotalpembayaran) {
        this.sumtotalpembayaran = sumtotalpembayaran;
    }
}

