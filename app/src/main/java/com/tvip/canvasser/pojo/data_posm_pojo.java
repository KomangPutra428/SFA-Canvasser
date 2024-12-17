package com.tvip.canvasser.pojo;

public class data_posm_pojo {
    private String szId;
    private String szName;
    private String szUomId;
    private String qty;
    private boolean isSelected;

    public data_posm_pojo(String szId, String szName, String szUomId) {
        this.szId = szId;
        this.szName = szName;
        this.szUomId = szUomId;
    }

    public String getSzId() { return szId; }
    public String getSzName() { return szName; }

    public String getSzUomId() {
        return szUomId;
    }

    public String getQty() { return qty; }
    public void setQty(String qty) { this.qty = qty; }

    public boolean getSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) { isSelected = selected; }
}
