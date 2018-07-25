package com.wms.business;

import com.plat.common.beans.BaseModel;

/**
 * 库存月报表
 *
 * @author zhouxianglh@gmail.com
 *
 *         2017年6月8日
 */
public class StockReport extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 6469823899654919169L;
    private Integer beforeStock;
    private String endTime;
    private String goodsAlias;
    private String whCode;
    private String goodsCode;
    private String goodsName;
    private String goodsType;
    private Integer inStock;
    private Integer nowStock;
    private String oraCode;
    private String oraName;
    private Integer outStock;
    private String reportDate;
    private String startTime;

    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
    }

    public Integer getBeforeStock() {
        return beforeStock;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getGoodsAlias() {
        return goodsAlias;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public Integer getInStock() {
        return inStock;
    }

    public Integer getNowStock() {
        return nowStock;
    }

    public String getOraCode() {
        return oraCode;
    }

    public String getOraName() {
        return oraName;
    }

    public Integer getOutStock() {
        return outStock;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setBeforeStock(Integer beforeStock) {
        this.beforeStock = beforeStock;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setGoodsAlias(String goodsAlias) {
        this.goodsAlias = goodsAlias;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public void setNowStock(Integer nowStock) {
        this.nowStock = nowStock;
    }

    public void setOraCode(String oraCode) {
        this.oraCode = oraCode;
    }

    public void setOraName(String oraName) {
        this.oraName = oraName;
    }

    public void setOutStock(Integer outStock) {
        this.outStock = outStock;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
