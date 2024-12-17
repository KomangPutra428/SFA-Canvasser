package com.tvip.canvasser.pojo;

public class data_docdoitemprice_pojo {
    String szDocId;
    String intItemNumber;
    String intItemDetailNumber;
    String szPriceId;
    String decPrice;
    String decDiscount;
    String bTaxable;
    String decAmount;
    String decTax;
    String decDpp;
    String szTaxId;
    String decTaxRate;
    String decDiscPrinciple;
    String decDiscDistributor;
    String decDiscInternal;

    public data_docdoitemprice_pojo(String szDocId, String intItemNumber, String intItemDetailNumber, String szPriceId, String decPrice, String decDiscount, String bTaxable, String decAmount, String decTax, String decDpp, String szTaxId, String decTaxRate, String decDiscPrinciple, String decDiscDistributor, String decDiscInternal) {
        this.szDocId = szDocId;
        this.intItemNumber = intItemNumber;
        this.intItemDetailNumber = intItemDetailNumber;
        this.szPriceId = szPriceId;
        this.decPrice = decPrice;
        this.decDiscount = decDiscount;
        this.bTaxable = bTaxable;
        this.decAmount = decAmount;
        this.decTax = decTax;
        this.decDpp = decDpp;
        this.szTaxId = szTaxId;
        this.decTaxRate = decTaxRate;
        this.decDiscPrinciple = decDiscPrinciple;
        this.decDiscDistributor = decDiscDistributor;
        this.decDiscInternal = decDiscInternal;
    }

    public String getSzDocId() {
        return szDocId;
    }

    public void setSzDocId(String szDocId) {
        this.szDocId = szDocId;
    }

    public String getIntItemNumber() {
        return intItemNumber;
    }

    public void setIntItemNumber(String intItemNumber) {
        this.intItemNumber = intItemNumber;
    }

    public String getIntItemDetailNumber() {
        return intItemDetailNumber;
    }

    public void setIntItemDetailNumber(String intItemDetailNumber) {
        this.intItemDetailNumber = intItemDetailNumber;
    }

    public String getSzPriceId() {
        return szPriceId;
    }

    public void setSzPriceId(String szPriceId) {
        this.szPriceId = szPriceId;
    }

    public String getDecPrice() {
        return decPrice;
    }

    public void setDecPrice(String decPrice) {
        this.decPrice = decPrice;
    }

    public String getDecDiscount() {
        return decDiscount;
    }

    public void setDecDiscount(String decDiscount) {
        this.decDiscount = decDiscount;
    }

    public String getbTaxable() {
        return bTaxable;
    }

    public void setbTaxable(String bTaxable) {
        this.bTaxable = bTaxable;
    }

    public String getDecAmount() {
        return decAmount;
    }

    public void setDecAmount(String decAmount) {
        this.decAmount = decAmount;
    }

    public String getDecTax() {
        return decTax;
    }

    public void setDecTax(String decTax) {
        this.decTax = decTax;
    }

    public String getDecDpp() {
        return decDpp;
    }

    public void setDecDpp(String decDpp) {
        this.decDpp = decDpp;
    }

    public String getSzTaxId() {
        return szTaxId;
    }

    public void setSzTaxId(String szTaxId) {
        this.szTaxId = szTaxId;
    }

    public String getDecTaxRate() {
        return decTaxRate;
    }

    public void setDecTaxRate(String decTaxRate) {
        this.decTaxRate = decTaxRate;
    }

    public String getDecDiscPrinciple() {
        return decDiscPrinciple;
    }

    public void setDecDiscPrinciple(String decDiscPrinciple) {
        this.decDiscPrinciple = decDiscPrinciple;
    }

    public String getDecDiscDistributor() {
        return decDiscDistributor;
    }

    public void setDecDiscDistributor(String decDiscDistributor) {
        this.decDiscDistributor = decDiscDistributor;
    }

    public String getDecDiscInternal() {
        return decDiscInternal;
    }

    public void setDecDiscInternal(String decDiscInternal) {
        this.decDiscInternal = decDiscInternal;
    }
}
