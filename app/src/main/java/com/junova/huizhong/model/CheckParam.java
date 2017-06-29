package com.junova.huizhong.model;

import java.io.Serializable;
import java.util.List;

/**
 * 检查项
 *
 * @author Administrator
 */
public class CheckParam implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private int status;
    private List<ExceptionParam> exceptions;
    private String PARTRECORDID;

    public String getPARTRECORDID() {
        return PARTRECORDID;
    }

    public void setPARTRECORDID(String PARTRECORDID) {
        this.PARTRECORDID = PARTRECORDID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ExceptionParam> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ExceptionParam> exceptions) {
        this.exceptions = exceptions;
    }

    public CheckParam(String id, String name, int status,
                      List<ExceptionParam> exceptions,String PARTRECORDID) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
        this.exceptions = exceptions;
        this.PARTRECORDID=PARTRECORDID;
    }

    public CheckParam(String id, String name, int status,
                      List<ExceptionParam> exceptions) {
        super();
        this.id = id;
        this.name = name;
        this.status = status;
        this.exceptions = exceptions;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
