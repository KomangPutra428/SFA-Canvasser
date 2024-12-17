package com.tvip.canvasser.pojo;

public class data_ritaselhp_pojo {

    private String ritase;
    private String nomor_do;
    private String nomor_std;
    private String nomor_bkb;
    private String mix_ref_std;

    public data_ritaselhp_pojo(String ritase, String nomor_do, String nomor_std, String nomor_bkb, String mix_ref_std) {
        this.ritase = ritase;
        this.nomor_do = nomor_do;
        this.nomor_std = nomor_std;
        this.nomor_bkb = nomor_bkb;
        this.mix_ref_std = mix_ref_std;
    }

    public String getRitase() {
        return ritase;
    }

    public void setRitase(String ritase) {
        this.ritase = ritase;
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

    public void setBkb(String nomor_bkb) { this.nomor_bkb = nomor_bkb; };

    public String getMix_ref_std() {
        return mix_ref_std;
    }
}

