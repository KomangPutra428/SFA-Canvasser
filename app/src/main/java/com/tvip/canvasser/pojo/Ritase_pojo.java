package com.tvip.canvasser.pojo;

public class Ritase_pojo {
    private String id_bkb;
    private String ritase;
    private String jenis_type;

    public Ritase_pojo(String id_bkb, String ritase, String jenis_type) {
        this.id_bkb = id_bkb;
        this.ritase = ritase;
        this.jenis_type = jenis_type;
    }

    public String getId_bkb() {
        return id_bkb;
    }

    public String getRitase() {
        return ritase;
    }

    public String getJenis_type() {
        return jenis_type;
    }
}
