package com.aliyun.ayland.data;

import java.util.List;

public class AllVillageDetailBean1 {
    /**
     * area : [{"immeuble":[{"unit":[{"floor":[{"room":[{"code":"100015","name":"3室"},{"code":"100078","name":"1"}],"code":"100014","name":"3层"}],"code":"100013","name":"3单元"}],"code":"100012","name":"3栋"}],"code":"100011","name":"3区"}]
     * code : 100010
     * name : 3期
     */

    private String code;
    private String name;
    private List<AreaBean> area;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AreaBean> getArea() {
        return area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }

    public static class AreaBean {
        /**
         * immeuble : [{"unit":[{"floor":[{"room":[{"code":"100015","name":"3室"},{"code":"100078","name":"1"}],"code":"100014","name":"3层"}],"code":"100013","name":"3单元"}],"code":"100012","name":"3栋"}]
         * code : 100011
         * name : 3区
         */

        private String code;
        private String name;
        private List<ImmeubleBean> immeuble;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ImmeubleBean> getImmeuble() {
            return immeuble;
        }

        public void setImmeuble(List<ImmeubleBean> immeuble) {
            this.immeuble = immeuble;
        }

        public static class ImmeubleBean {
            /**
             * unit : [{"floor":[{"room":[{"code":"100015","name":"3室"},{"code":"100078","name":"1"}],"code":"100014","name":"3层"}],"code":"100013","name":"3单元"}]
             * code : 100012
             * name : 3栋
             */

            private String code;
            private String name;
            private List<UnitBean> unit;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<UnitBean> getUnit() {
                return unit;
            }

            public void setUnit(List<UnitBean> unit) {
                this.unit = unit;
            }

            public static class UnitBean {
                /**
                 * floor : [{"room":[{"code":"100015","name":"3室"},{"code":"100078","name":"1"}],"code":"100014","name":"3层"}]
                 * code : 100013
                 * name : 3单元
                 */

                private String code;
                private String name;
                private List<FloorBean> floor;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<FloorBean> getFloor() {
                    return floor;
                }

                public void setFloor(List<FloorBean> floor) {
                    this.floor = floor;
                }

                public static class FloorBean {
                    /**
                     * room : [{"code":"100015","name":"3室"},{"code":"100078","name":"1"}]
                     * code : 100014
                     * name : 3层
                     */

                    private String code;
                    private String name;
                    private List<RoomBean> room;

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public List<RoomBean> getRoom() {
                        return room;
                    }

                    public void setRoom(List<RoomBean> room) {
                        this.room = room;
                    }

                    public static class RoomBean {
                        /**
                         * code : 100015
                         * name : 3室
                         */

                        private String code;
                        private String name;

                        public String getCode() {
                            return code;
                        }

                        public void setCode(String code) {
                            this.code = code;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }
                    }
                }
            }
        }
    }
}