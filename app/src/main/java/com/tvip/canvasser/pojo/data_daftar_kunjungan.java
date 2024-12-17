package com.tvip.canvasser.pojo;

public class data_daftar_kunjungan {
    private String IdStd;
    private String tanggaldokumen;
    private String ritase;

    public data_daftar_kunjungan(String IdStd, String tanggaldokumen, String ritase) {
        this.IdStd = IdStd;
        this.tanggaldokumen = tanggaldokumen;
        this.ritase = ritase;
    }

    public String getIdStd() { return IdStd; }
    public String getTanggaldokumen() { return tanggaldokumen; }
    public String getRitase() { return ritase; }

}
