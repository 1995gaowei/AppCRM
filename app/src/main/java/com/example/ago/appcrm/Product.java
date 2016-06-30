package com.example.ago.appcrm;

/**
 * Created by Wei on 2016/6/23.
 */
public class Product {
    
    private String productid;
    private String productname;
    private String productsn;
    private String standardprice;
    private String salesunit;
    private String unitcost;
    private String classification;
    private String introduction;
    private String productremarks;

    public Product() {
        this.classification = "";
        this.introduction = "";
        this.productid = "";
        this.productname = "";
        this.productremarks = "";
        this.productsn = "";
        this.salesunit = "";
        this.standardprice = "";
        this.unitcost = "";
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductremarks() {
        return productremarks;
    }

    public void setProductremarks(String productremarks) {
        this.productremarks = productremarks;
    }

    public String getProductsn() {
        return productsn;
    }

    public void setProductsn(String productsn) {
        this.productsn = productsn;
    }

    public String getSalesunit() {
        return salesunit;
    }

    public void setSalesunit(String salesunit) {
        this.salesunit = salesunit;
    }

    public String getStandardprice() {
        return standardprice;
    }

    public void setStandardprice(String standardprice) {
        this.standardprice = standardprice;
    }

    public String getUnitcost() {
        return unitcost;
    }

    public void setUnitcost(String unitcost) {
        this.unitcost = unitcost;
    }
}
