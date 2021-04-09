package com.aliyun.ayland.data;

import java.util.List;

public class ATKitchenProjectBean {
    /**
     * checkedIcon : https://smarthome.cifi.com.cn/pic/childrenRoom/hsh_bg_qzyy_a2x.png
     * unCheckedIcon : https://smarthome.cifi.com.cn/pic/childrenRoom/hsh_bg_qzyy_a2x.png
     * time : [{"itemName":"午餐","checkedIcon":"/pic/childrenRoom/hsh_bg_qzyy_a2x.png","unCheckedIcon":"/pic/childrenRoom/hsh_bg_qzyy_a2x.png","createTime":1562382156000,"itemEndTime":14,"itemPrice":"300.00","id":1,"itemBeginTime":9,"itemTime":"09:00~14:00","useStatus":-1},{"itemName":"晚餐","itemEndTime":22,"itemPrice":"500.00","id":10,"itemBeginTime":15,"itemTime":"15:00~22:00","useStatus":1},{"itemName":"全天","itemEndTime":22,"itemPrice":"700.00","id":11,"itemBeginTime":9,"itemTime":"09:00~22:00","useStatus":-1}]
     * projectName : 厨房项目
     * projectId : 1
     */

    private String checkedIcon;
    private String unCheckedIcon;
    private String projectName;
    private int projectId;
    private List<TimeBean> time;

    public String getCheckedIcon() {
        return checkedIcon;
    }

    public void setCheckedIcon(String checkedIcon) {
        this.checkedIcon = checkedIcon;
    }

    public String getUnCheckedIcon() {
        return unCheckedIcon;
    }

    public void setUnCheckedIcon(String unCheckedIcon) {
        this.unCheckedIcon = unCheckedIcon;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public static class TimeBean {
        /**
         * itemName : 午餐
         * checkedIcon : /pic/childrenRoom/hsh_bg_qzyy_a2x.png
         * unCheckedIcon : /pic/childrenRoom/hsh_bg_qzyy_a2x.png
         * createTime : 1562382156000
         * itemEndTime : 14
         * itemPrice : 300.00
         * id : 1
         * itemBeginTime : 9
         * itemTime : 09:00~14:00
         * useStatus : -1
         */

        private String itemName;
        private String checkedIcon;
        private String unCheckedIcon;
        private long createTime;
        private int itemEndTime;
        private String itemPrice;
        private int id;
        private int itemBeginTime;
        private String itemTime;
        private int useStatus;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getCheckedIcon() {
            return checkedIcon;
        }

        public void setCheckedIcon(String checkedIcon) {
            this.checkedIcon = checkedIcon;
        }

        public String getUnCheckedIcon() {
            return unCheckedIcon;
        }

        public void setUnCheckedIcon(String unCheckedIcon) {
            this.unCheckedIcon = unCheckedIcon;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getItemEndTime() {
            return itemEndTime;
        }

        public void setItemEndTime(int itemEndTime) {
            this.itemEndTime = itemEndTime;
        }

        public String getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(String itemPrice) {
            this.itemPrice = itemPrice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getItemBeginTime() {
            return itemBeginTime;
        }

        public void setItemBeginTime(int itemBeginTime) {
            this.itemBeginTime = itemBeginTime;
        }

        public String getItemTime() {
            return itemTime;
        }

        public void setItemTime(String itemTime) {
            this.itemTime = itemTime;
        }

        public int getUseStatus() {
            return useStatus;
        }

        public void setUseStatus(int useStatus) {
            this.useStatus = useStatus;
        }
    }
}