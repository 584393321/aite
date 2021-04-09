package com.aliyun.ayland.data;

import java.util.List;

public class ATGroupDetailBean {

    /**
     * peoples : [{"avatarUrl":"http://alisaas.atsmartlife.com/pic/userHeadImg/1140807701130522624/1568710751095.png","createTime":1568188437000,"nickName":"艺术与科技的结合体","openId":"1140807701130522624"},{"avatarUrl":"http://alisaas.atsmartlife.com/pic/userHeadImg/1132916889676607488/1568615755402.png","createTime":1568776526000,"nickName":"Ttyt","openId":"1132916889676607488"}]
     * communityIcon : http://alisaas.atsmartlife.com/pic/communityImg/7/1568252336160.png
     * createPerson : 1140807701130522624
     * updateTime : 1568188437000
     * createPersonVillageId : 100007
     * createPersonBuildingCode : 100033
     * huanxinGroupId : 93103099805699
     * personNum : 2
     * communitySynopsis : 看到我的车轮滚滚没？
     * createPersonPhone : 12345
     * createTime : 1568171867000
     * communityName : 大家好我是娱乐类社群板块6号
     * communityType : 广场
     * id : 7
     * createPersonName : 卡布达
     * updatePerson : 1140807701130522624
     * communityStatus : 2
     */

    private String communityIcon;
    private String createPerson;
    private long updateTime;
    private int createPersonVillageId;
    private int createPersonBuildingCode;
    private String huanxinGroupId;
    private int personNum;
    private String communitySynopsis;
    private String createPersonPhone;
    private long createTime;
    private String communityName;
    private String communityType;
    private int id;
    private String createPersonName;
    private String updatePerson;
    private int communityStatus;
    private List<PeoplesBean> peoples;

    public String getCommunityIcon() {
        return communityIcon;
    }

    public void setCommunityIcon(String communityIcon) {
        this.communityIcon = communityIcon;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getCreatePersonVillageId() {
        return createPersonVillageId;
    }

    public void setCreatePersonVillageId(int createPersonVillageId) {
        this.createPersonVillageId = createPersonVillageId;
    }

    public int getCreatePersonBuildingCode() {
        return createPersonBuildingCode;
    }

    public void setCreatePersonBuildingCode(int createPersonBuildingCode) {
        this.createPersonBuildingCode = createPersonBuildingCode;
    }

    public String getHuanxinGroupId() {
        return huanxinGroupId;
    }

    public void setHuanxinGroupId(String huanxinGroupId) {
        this.huanxinGroupId = huanxinGroupId;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public String getCommunitySynopsis() {
        return communitySynopsis;
    }

    public void setCommunitySynopsis(String communitySynopsis) {
        this.communitySynopsis = communitySynopsis;
    }

    public String getCreatePersonPhone() {
        return createPersonPhone;
    }

    public void setCreatePersonPhone(String createPersonPhone) {
        this.createPersonPhone = createPersonPhone;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityType() {
        return communityType;
    }

    public void setCommunityType(String communityType) {
        this.communityType = communityType;
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

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public int getCommunityStatus() {
        return communityStatus;
    }

    public void setCommunityStatus(int communityStatus) {
        this.communityStatus = communityStatus;
    }

    public List<PeoplesBean> getPeoples() {
        return peoples;
    }

    public void setPeoples(List<PeoplesBean> peoples) {
        this.peoples = peoples;
    }

    public static class PeoplesBean {
        /**
         * avatarUrl : http://alisaas.atsmartlife.com/pic/userHeadImg/1140807701130522624/1568710751095.png
         * createTime : 1568188437000
         * nickName : 艺术与科技的结合体
         * openId : 1140807701130522624
         */

        private String avatarUrl;
        private long createTime;
        private String nickName;
        private String openId;

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }
    }
}