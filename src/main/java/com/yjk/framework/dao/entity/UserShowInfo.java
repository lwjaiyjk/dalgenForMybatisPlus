package com.yjk.framework.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jkyu1
 * @since 2018-10-22
 */
public class UserShowInfo extends Model<UserShowInfo> {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String targetId;
    private String userId;
    private String showId;
    private Object showAvator;
    private String showSex;
    private String creator;
    private String updater;
    private Date createTime;
    private Date updateTime;
    private String publishFlag;
    private String blackList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public Object getShowAvator() {
        return showAvator;
    }

    public void setShowAvator(Object showAvator) {
        this.showAvator = showAvator;
    }

    public String getShowSex() {
        return showSex;
    }

    public void setShowSex(String showSex) {
        this.showSex = showSex;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPublishFlag() {
        return publishFlag;
    }

    public void setPublishFlag(String publishFlag) {
        this.publishFlag = publishFlag;
    }

    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "UserShowInfo{" +
        ", id=" + id +
        ", targetId=" + targetId +
        ", userId=" + userId +
        ", showId=" + showId +
        ", showAvator=" + showAvator +
        ", showSex=" + showSex +
        ", creator=" + creator +
        ", updater=" + updater +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", publishFlag=" + publishFlag +
        ", blackList=" + blackList +
        "}";
    }
}
