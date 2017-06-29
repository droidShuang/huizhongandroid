package com.junova.huizhong.model;

import java.util.List;

/**
 * Created by rider on 2016/8/9 0009 14:08.
 * Description :
 */
public class HistoryRecord {

    /**
     * ISSUECODE : 1-20160808-002
     * DETAIL : [{"NAME":"saf","DETDESC":"sbf23"}]
     * STATUS : 02
     * CREATED : 2016-08-08 13:56:20
     * PATROLRECORD_ID : ca649d80440d4e218f60c53589375bca
     */

    private String ISSUECODE;
    private String STATUS;
    private String CREATED;
    private String PATROLRECORD_ID;
    /**
     * NAME : saf
     * DETDESC : sbf23
     */

    private List<DetailBean> DETAIL;

    public String getISSUECODE() {
        return ISSUECODE;
    }

    public void setISSUECODE(String ISSUECODE) {
        this.ISSUECODE = ISSUECODE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCREATED() {
        return CREATED;
    }

    public void setCREATED(String CREATED) {
        this.CREATED = CREATED;
    }

    public String getPATROLRECORD_ID() {
        return PATROLRECORD_ID;
    }

    public void setPATROLRECORD_ID(String PATROLRECORD_ID) {
        this.PATROLRECORD_ID = PATROLRECORD_ID;
    }

    public List<DetailBean> getDETAIL() {
        return DETAIL;
    }

    public void setDETAIL(List<DetailBean> DETAIL) {
        this.DETAIL = DETAIL;
    }


}
