package com.aliyun.ayland.data;

import java.util.List;

public class ATRankingListBean {
    /**
     * rankList : [{"agree":0,"agreeStatus":0,"avatarUrl":"https://smarthome.cifi.com.cn/pic/userHeadImg/1242386385666252800/1591335239508.png","calorieNum":100,"nickname":"我","id":"674"}]
     * userRankInfo : {"agree":0,"agreeStatus":0,"avatarUrl":"https://smarthome.cifi.com.cn/pic/userHeadImg/1242386385666252800/1591335239508.png","calorieNum":100,"nickname":"我","openId":"1242386385666252800","rankNum":2}
     */

    private UserRankInfoBean userRankInfo;
    private List<RankListBean> rankList;

    public UserRankInfoBean getUserRankInfo() {
        return userRankInfo;
    }

    public void setUserRankInfo(UserRankInfoBean userRankInfo) {
        this.userRankInfo = userRankInfo;
    }

    public List<RankListBean> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankListBean> rankList) {
        this.rankList = rankList;
    }

    public static class UserRankInfoBean {
        /**
         * agree : 0
         * agreeStatus : 0
         * avatarUrl : https://smarthome.cifi.com.cn/pic/userHeadImg/1242386385666252800/1591335239508.png
         * calorieNum : 100
         * nickname : 我
         * openId : 1242386385666252800
         * rankNum : 2
         */

        private int agree;
        private int agreeStatus;
        private String avatarUrl;
        private int calorieNum;
        private String nickname;
        private String openId;
        private int rankNum;
        private int id;

        public int getAgree() {
            return agree;
        }

        public void setAgree(int agree) {
            this.agree = agree;
        }

        public int getAgreeStatus() {
            return agreeStatus;
        }

        public void setAgreeStatus(int agreeStatus) {
            this.agreeStatus = agreeStatus;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getCalorieNum() {
            return calorieNum;
        }

        public void setCalorieNum(int calorieNum) {
            this.calorieNum = calorieNum;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOpenId() {
            return openId;
        }

        public void setOpenId(String openId) {
            this.openId = openId;
        }

        public int getRankNum() {
            return rankNum;
        }

        public void setRankNum(int rankNum) {
            this.rankNum = rankNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class RankListBean {
        /**
         * agree : 0
         * agreeStatus : 0
         * avatarUrl : https://smarthome.cifi.com.cn/pic/userHeadImg/1242386385666252800/1591335239508.png
         * calorieNum : 100
         * nickname : 我
         * id : 674
         */

        private int agree;
        private int agreeStatus;
        private String avatarUrl;
        private int calorieNum;
        private String nickname;
        private int id;
        private int rankNum;

        public int getAgree() {
            return agree;
        }

        public void setAgree(int agree) {
            this.agree = agree;
        }

        public int getAgreeStatus() {
            return agreeStatus;
        }

        public void setAgreeStatus(int agreeStatus) {
            this.agreeStatus = agreeStatus;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public int getCalorieNum() {
            return calorieNum;
        }

        public void setCalorieNum(int calorieNum) {
            this.calorieNum = calorieNum;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRankNum() {
            return rankNum;
        }

        public void setRankNum(int rankNum) {
            this.rankNum = rankNum;
        }
    }
}