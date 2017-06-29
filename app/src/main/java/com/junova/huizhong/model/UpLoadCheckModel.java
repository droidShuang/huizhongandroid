/**
 * @Title: UpLoadCheckModel.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-14 下午4:59:35
 * @version V1.0
 */

package com.junova.huizhong.model;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: UpLoadCheckModel
 * @Description: TODO
 * @date 2015-9-14 下午4:59:35
 */

public class UpLoadCheckModel {

    String deviceId;
    String checkId;
    int status;
    String errorId;
    String pics;
    int type;
    String recordsId;
    String partRecordId;
    String erroDesc;
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErroDesc() {
        return erroDesc;
    }

    public void setErroDesc(String erroDesc) {
        this.erroDesc = erroDesc;
    }

    public String getPartRecordId() {
        return partRecordId;
    }

    public void setPartRecordId(String partRecordId) {
        this.partRecordId = partRecordId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public UpLoadCheckModel(String deviceId, String checkId, int status, String errorId,
                            String pics, int type, String recordsId, String partRecordId, String erroDesc, String name) {
        super();
        this.deviceId = deviceId;
        this.checkId = checkId;
        this.status = status;
        this.errorId = errorId;
        this.pics = pics;
        this.type = type;
        this.recordsId = recordsId;
        this.partRecordId = partRecordId;
        this.erroDesc = erroDesc;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRecordsId() {
        return recordsId;
    }

    public void setRecordsId(String recordsId) {
        this.recordsId = recordsId;
    }


}
