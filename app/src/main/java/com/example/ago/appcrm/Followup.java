package com.example.ago.appcrm;

/**
 * Created by Wei on 2016/6/28.
 */
public class Followup {

    private String followupid;
    private String sourceid;
    private String sourcetype;
    private String followuptype;
    private String createtime;
    private String creatorid;
    private String content;
    private String followupremarks;
    private String name;

    public Followup() {
        this.content = "";
        this.createtime = "";
        this.creatorid = "";
        this.followupid = "";
        this.followupremarks = "";
        this.followuptype = "";
        this.name = "";
        this.sourceid = "";
        this.sourcetype = "";
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public String getFollowupid() {
        return followupid;
    }

    public void setFollowupid(String followupid) {
        this.followupid = followupid;
    }

    public String getFollowupremarks() {
        return followupremarks;
    }

    public void setFollowupremarks(String followupremarks) {
        this.followupremarks = followupremarks;
    }

    public String getFollowuptype() {
        return followuptype;
    }

    public void setFollowuptype(String followuptype) {
        this.followuptype = followuptype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceid() {
        return sourceid;
    }

    public void setSourceid(String sourceid) {
        this.sourceid = sourceid;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(String sourcetype) {
        this.sourcetype = sourcetype;
    }
}
