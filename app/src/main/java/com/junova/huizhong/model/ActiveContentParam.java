/**
 * @Title: ActiveContentParam.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * @author hao_mo@loongjoy.com
 * @date 2015-9-9 下午3:17:12
 * @version V1.0
 */

package com.junova.huizhong.model;

import java.util.List;

/**
 * @author hao_mo@loongjoy.com
 * @ClassName: ActiveContentParam
 * @Description: TODO
 * @date 2015-9-9 下午3:17:12
 */

public class ActiveContentParam {
    String id;
    String title;
    boolean completed;
    String describtion;
    List<String> imagePaths;

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ActiveContentParam(String id, String title, boolean completed) {
        super();
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public ActiveContentParam(String id, String title, boolean completed, String describtion, List<String> imagePaths) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.describtion = describtion;
        this.imagePaths = imagePaths;
    }
}
