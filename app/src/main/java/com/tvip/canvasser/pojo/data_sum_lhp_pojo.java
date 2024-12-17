package com.tvip.canvasser.pojo;

public class data_sum_lhp_pojo {private String sumamount;
    private String sumtotal;
    private String sumdiskon;
    private String sumtest;

    private String nomor_do;
    private String nomor_std;
    private String nomor_bkb;
    private String nomor_customer;

    public  data_sum_lhp_pojo (String sumamount, String sumtotal, String sumdiskon, String sumtest,
                               String nomor_do, String nomor_std, String nomor_bkb, String nomor_customer) {

        this.sumamount = sumamount;
        this.sumtotal = sumtotal;
        this.sumdiskon = sumdiskon;
        this.sumtest = sumtest;

        this.nomor_do = nomor_do;
        this.nomor_std = nomor_std;
        this.nomor_bkb = nomor_bkb;
        this.nomor_customer = nomor_customer;
    }

    public String getSumamount() {
        return sumamount;
    }

    public void setSumamount(String sumamount) {
        this.sumamount = sumamount;
    }

    public String getSumtotal() {
        return sumtotal;
    }

    public void setSumtotal(String sumtotal) {
        this.sumtotal = sumtotal;
    }

    public String getSumdiskon() {
        return sumdiskon;
    }

    public void setSumdiskon(String sumdiskon) {
        this.sumdiskon = sumdiskon;
    }

    public String getSumtes() {
        return sumtest;
    }

    public void setSumtest(String sumtest) {
        this.sumtest = sumtest;
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

    public String getNomor_bkb() {
        return nomor_bkb;
    }

    public void setNomor_bkb(String nomor_bkb) {
        this.nomor_bkb = nomor_bkb;
    }

    public String getNomor_customer() {
        return nomor_customer;
    }

    public void setNomor_customer(String nomor_customer) {
        this.nomor_customer = nomor_customer;
    }
}

