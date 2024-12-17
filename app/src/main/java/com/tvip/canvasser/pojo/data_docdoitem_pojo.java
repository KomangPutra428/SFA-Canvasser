package com.tvip.canvasser.pojo;

public class data_docdoitem_pojo {
    String szDocId;
    String szDointItemNumbercId;
    String szProductId;
    String szOrderItemTypeId;
    String szTrnType;
    String decQty;
    String szUomId;

    public data_docdoitem_pojo(String szDocId, String szDointItemNumbercId, String szProductId, String szOrderItemTypeId, String szTrnType, String decQty, String szUomId) {
        this.szDocId = szDocId;
        this.szDointItemNumbercId = szDointItemNumbercId;
        this.szProductId = szProductId;
        this.szOrderItemTypeId = szOrderItemTypeId;
        this.szTrnType = szTrnType;
        this.decQty = decQty;
        this.szUomId = szUomId;
    }

    public String getSzDocId() {
        return szDocId;
    }

    public void setSzDocId(String szDocId) {
        this.szDocId = szDocId;
    }

    public String getSzDointItemNumbercId() {
        return szDointItemNumbercId;
    }

    public void setSzDointItemNumbercId(String szDointItemNumbercId) {
        this.szDointItemNumbercId = szDointItemNumbercId;
    }

    public String getSzProductId() {
        return szProductId;
    }

    public void setSzProductId(String szProductId) {
        this.szProductId = szProductId;
    }

    public String getSzOrderItemTypeId() {
        return szOrderItemTypeId;
    }

    public void setSzOrderItemTypeId(String szOrderItemTypeId) {
        this.szOrderItemTypeId = szOrderItemTypeId;
    }

    public String getSzTrnType() {
        return szTrnType;
    }

    public void setSzTrnType(String szTrnType) {
        this.szTrnType = szTrnType;
    }

    public String getDecQty() {
        return decQty;
    }

    public void setDecQty(String decQty) {
        this.decQty = decQty;
    }

    public String getSzUomId() {
        return szUomId;
    }

    public void setSzUomId(String szUomId) {
        this.szUomId = szUomId;
    }
}
