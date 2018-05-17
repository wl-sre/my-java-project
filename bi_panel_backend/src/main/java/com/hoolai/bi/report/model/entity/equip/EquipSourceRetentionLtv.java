package com.hoolai.bi.report.model.entity.equip;

import java.util.Date;

public class EquipSourceRetentionLtv {
    private Long id;

    private String snid;

    private String gameid;

    private String ds;

    private String source;

    private Integer newEquip;

    private Integer installEquip;

    private String installDate;

    private Integer retentionEquip;

    private Double totalPayment;

    private Date createTime;

    public EquipSourceRetentionLtv(){}
    
    public EquipSourceRetentionLtv(String snid, String gameid) {
    	this.snid = snid;
    	this.gameid = gameid;
	}
    
    public EquipSourceRetentionLtv(String snid, String gameid,String source){
    	this.snid = snid;
    	this.gameid = gameid;
    	this.source = source;
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSnid() {
        return snid;
    }

    public void setSnid(String snid) {
        this.snid = snid;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getNewEquip() {
        return newEquip;
    }

    public void setNewEquip(Integer newEquip) {
        this.newEquip = newEquip;
    }

    public Integer getInstallEquip() {
        return installEquip;
    }

    public void setInstallEquip(Integer installEquip) {
        this.installEquip = installEquip;
    }


    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public Integer getRetentionEquip() {
        return retentionEquip;
    }

    public void setRetentionEquip(Integer retentionEquip) {
        this.retentionEquip = retentionEquip;
    }

    public Double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(Double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}