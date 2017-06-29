package com.junova.huizhong.model;

/**
 * Created by junova on 2016/10/27 0027.
 */

public class ActiveHistory {

    /**
     * TIME : 2016-10-01 10:25:45
     * TITLE1 : 落实安全生产最重要
     * DESCRIPTION : ceshi11
     * TITLE2 : 1落实安全生产
     * IMAGES : 3634d9715de94f47b3c09e1208e99ce9.jpg:
     */

    private String TIME;
    private String TITLE1;
    private String DESCRIPTION;
    private String TITLE2;
    private String IMAGES;

    public ActiveHistory(String TIME, String TITLE1, String DESCRIPTION, String TITLE2, String IMAGES) {
        this.TIME = TIME;
        this.TITLE1 = TITLE1;
        this.DESCRIPTION = DESCRIPTION;
        this.TITLE2 = TITLE2;
        this.IMAGES = IMAGES;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getTITLE1() {
        return TITLE1;
    }

    public void setTITLE1(String TITLE1) {
        this.TITLE1 = TITLE1;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getTITLE2() {
        return TITLE2;
    }

    public void setTITLE2(String TITLE2) {
        this.TITLE2 = TITLE2;
    }

    public String getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(String IMAGES) {
        this.IMAGES = IMAGES;
    }
}
