package com.aliyun.ayland.data;


import java.util.List;

/**
 * @author guikong on 18/4/12.
 */
public class ATCommunityDynamicBean {
    /**
     * agreeNum : 0
     * avatarUrl : http://alisaas.atsmartlife.com/pic/userHeadImg/1132916889676607488/1567050549933.png
     * fromCommunityName : 体育板块
     * createPerson : 1132916889676607488
     * fromCommunity : 1
     * userCommunityStatus : 1
     * content : 陈
     * agreeStatus : 0
     * createTime : 1567059703000
     * id : 5
     * createPersonName : Ttyt
     * createTimeStr : 2019-08-29 14:21
     * image_list : http://alisaas.atsmartlife.com/pic/userCommunityImg/1132916889676607488/1567059697385.png,http://alisaas.atsmartlife.com/pic/userCommunityImg/1132916889676607488/1567059697556.png
     * commentList : [{"beCommentPerson":"1132916889676607488","beCommentPersonName":"Ttyt","commentPerson":"1132916889676607488","commentPersonName":"Ttyt","communityId":1,"content":"虽然不知道发生了什么但我先放一盒卫生纸在这","id":2},{"beCommentPerson":"1140430804614561792","beCommentPersonName":"13128839389","commentPerson":"1140430804614561792","commentPersonName":"13128839389","communityId":1,"content":"我是评论","id":1}]
     * sendCommunity : 1
     */
    private int agreeNum;
    private String avatarUrl;
    private String fromCommunityName;
    private String createPerson;
    private int fromCommunity;
    private String content;
    private int agreeStatus;
    private long createTime;
    private int id;
    private String createPersonName;
    private String createTimeStr;
    private String image_list;
    private String huanxinGroupId;
    private int sendCommunity;
    private int userCommunityStatus;
    private List<CommentListBean> commentList;

    public String getHuanxinGroupId() {
        return huanxinGroupId;
    }

    public void setHuanxinGroupId(String huanxinGroupId) {
        this.huanxinGroupId = huanxinGroupId;
    }

    public int getUserCommunityStatus() {
        return userCommunityStatus;
    }

    public void setUserCommunityStatus(int userCommunityStatus) {
        this.userCommunityStatus = userCommunityStatus;
    }

    public int getAgreeNum() {
        return agreeNum;
    }

    public void setAgreeNum(int agreeNum) {
        this.agreeNum = agreeNum;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFromCommunityName() {
        return fromCommunityName;
    }

    public void setFromCommunityName(String fromCommunityName) {
        this.fromCommunityName = fromCommunityName;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public int getFromCommunity() {
        return fromCommunity;
    }

    public void setFromCommunity(int fromCommunity) {
        this.fromCommunity = fromCommunity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAgreeStatus() {
        return agreeStatus;
    }

    public void setAgreeStatus(int agreeStatus) {
        this.agreeStatus = agreeStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getImage_list() {
        return image_list;
    }

    public void setImage_list(String image_list) {
        this.image_list = image_list;
    }

    public int getSendCommunity() {
        return sendCommunity;
    }

    public void setSendCommunity(int sendCommunity) {
        this.sendCommunity = sendCommunity;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class CommentListBean {
        /**
         * beCommentPerson : 1132916889676607488
         * beCommentPersonName : Ttyt
         * commentPerson : 1132916889676607488
         * commentPersonName : Ttyt
         * communityId : 1
         * content : 虽然不知道发生了什么但我先放一盒卫生纸在这
         * id : 2
         */

        private String beCommentPerson;
        private String beCommentPersonName;
        private String commentPerson;
        private String commentPersonName;
        private int communityId;
        private String content;
        private int id;

        public String getBeCommentPerson() {
            return beCommentPerson;
        }

        public void setBeCommentPerson(String beCommentPerson) {
            this.beCommentPerson = beCommentPerson;
        }

        public String getBeCommentPersonName() {
            return beCommentPersonName;
        }

        public void setBeCommentPersonName(String beCommentPersonName) {
            this.beCommentPersonName = beCommentPersonName;
        }

        public String getCommentPerson() {
            return commentPerson;
        }

        public void setCommentPerson(String commentPerson) {
            this.commentPerson = commentPerson;
        }

        public String getCommentPersonName() {
            return commentPersonName;
        }

        public void setCommentPersonName(String commentPersonName) {
            this.commentPersonName = commentPersonName;
        }

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}