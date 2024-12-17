package com.tvip.canvasser.pojo;

public class data_ritase_pojo {
    private String szCustomerId;
    private String szName;
    private String szAddress;
    private String total_harga;
    private String bOutOfRoute;
    private String ritase;

    public data_ritase_pojo(String szCustomerId, String szName, String szAddress, String total_harga, String bOutOfRoute, String ritase) {
        this.szCustomerId = szCustomerId;
        this.szName = szName;
        this.szAddress = szAddress;
        this.total_harga = total_harga;
        this.bOutOfRoute = bOutOfRoute;
        this.ritase = ritase;
    }

    public String getSzCustomerId() {
        return szCustomerId;
    }

    public String getSzName() {
        return szName;
    }

    public String getSzAddress() {
        return szAddress;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public String getbOutOfRoute() {
        return bOutOfRoute;
    }

    public String getRitase() {
        return ritase;
    }
}
