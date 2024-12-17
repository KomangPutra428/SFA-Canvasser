package com.tvip.canvasser.pojo;

public class data_lhp_do_pojo {
    private String nomor_do;
    private String id_toko;
    private String toko;

    private String nomorstd;

    private String nomorbkb;
    private String ritase;
    private String branchId;
    private String statuslhp;


    public data_lhp_do_pojo (String nomor_do, String id_toko, String toko, String nomorstd, String nomorbkb, String ritase, String branchId) {
        this.nomor_do = nomor_do;
        this.id_toko = id_toko;
        this.toko = toko;

        this.nomorstd = nomorstd;
        this.nomorbkb = nomorbkb;
        this.ritase = ritase;
        this.branchId = branchId;
    }

    public String getNomor_do() {
        return nomor_do;
    }

    public void setNomor_do(String nomor_do) {
        this.nomor_do = nomor_do;
    }

    public String getId_toko() {
        return id_toko;
    }

    public void setId_toko(String id_toko) {
        this.id_toko = id_toko;
    }

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getNomorstd() {
        return nomorstd;
    }

    public void setNomorstd(String nomorstd) {
        this.nomorstd = nomorstd;
    }

    public String getNomorbkb() {
        return nomorbkb;
    }

    public void setNomorbkb(String nomorbkb) {
        this.nomorbkb = nomorbkb;
    }

    public String getRitase() {
        return ritase;
    }

    public void setRitase(String ritase) {
        this.ritase = ritase;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getStatuslhp() {
        return statuslhp;
    }

    public void setStatuslhp(String statuslhp) {
        this.statuslhp = statuslhp;
    }
}

