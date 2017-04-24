package com.dy.aikexue.ssolibrary.bean;


import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2017/3/20.
 */

public class EvaluateBean {


    /**
     * code : 0
     * data : {"apps":[{"contents":[{"cid":"575e22e3c3666e0e6c1bbc03","imgs":[],"target":"tg100001","text":"this is pub test","time":1465787107145,"uid":"u10"}],"id":"575e22e3c3666e0e6c1bbc02","scores":{"vote":2},"tags":null,"target":"tg100001","time":1465787107145,"type":"course","uid":"u10"}],"serveTime":1465787773842,"statistics":[{"count":1,"vote":2}],"total":1,"resumeInfo":{"u10":{"attrs":{"basic":{"avatar":"","desc":"这家伙很懒，什么也没留下","gender":2,"nickName":"客服温老师"}},"id":"u10"}}}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private long serveTime;
        private int total;
        private HashMap<String, ResumeInfo> resumeInfo;
        private List<AppsBean> apps;
        private List<StatisticsBean> statistics;
        private HashMap<String, Recruit> recruit;

        public void setRecruit(HashMap<String, Recruit> recruit) {
            this.recruit = recruit;
        }

        public HashMap<String, Recruit> getRecruit() {
            return recruit;
        }

        public long getServeTime() {
            return serveTime;
        }

        public void setServeTime(long serveTime) {
            this.serveTime = serveTime;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public HashMap<String,ResumeInfo> getResumeInfo() {
            return resumeInfo;
        }

        public void setResumeInfo(HashMap<String, ResumeInfo> resumeInfo) {
            this.resumeInfo = resumeInfo;
        }

        public List<AppsBean> getApps() {
            return apps;
        }

        public void setApps(List<AppsBean> apps) {
            this.apps = apps;
        }

        public List<StatisticsBean> getStatistics() {
            return statistics;
        }

        public void setStatistics(List<StatisticsBean> statistics) {
            this.statistics = statistics;
        }

        public static class Recruit {
            String _id;
            String title;

            public void set_id(String _id) {
                this._id = _id;
            }

            public String get_id() {
                return _id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }
        }

        public static class UserBean {
            /**
             * u10 : {"attrs":{"basic":{"avatar":"","desc":"这家伙很懒，什么也没留下","gender":2,"nickName":"客服温老师"}},"id":"u10"}
             */

            private U10Bean u10;

            public U10Bean getU10() {
                return u10;
            }

            public void setU10(U10Bean u10) {
                this.u10 = u10;
            }

            public static class U10Bean {
                /**
                 * attrs : {"basic":{"avatar":"","desc":"这家伙很懒，什么也没留下","gender":2,"nickName":"客服温老师"}}
                 * id : u10
                 */

                private AttrsBean attrs;
                private String id;

                public AttrsBean getAttrs() {
                    return attrs;
                }

                public void setAttrs(AttrsBean attrs) {
                    this.attrs = attrs;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public static class AttrsBean {
                    /**
                     * basic : {"avatar":"","desc":"这家伙很懒，什么也没留下","gender":2,"nickName":"客服温老师"}
                     */

                    private BasicBean basic;

                    public BasicBean getBasic() {
                        return basic;
                    }

                    public void setBasic(BasicBean basic) {
                        this.basic = basic;
                    }

                    public static class BasicBean {
                        /**
                         * avatar :
                         * desc : 这家伙很懒，什么也没留下
                         * gender : 2
                         * nickName : 客服温老师
                         */

                        private String avatar;
                        private String desc;
                        private int gender;
                        private String nickName;

                        public String getAvatar() {
                            return avatar;
                        }

                        public void setAvatar(String avatar) {
                            this.avatar = avatar;
                        }

                        public String getDesc() {
                            return desc;
                        }

                        public void setDesc(String desc) {
                            this.desc = desc;
                        }

                        public int getGender() {
                            return gender;
                        }

                        public void setGender(int gender) {
                            this.gender = gender;
                        }

                        public String getNickName() {
                            return nickName;
                        }

                        public void setNickName(String nickName) {
                            this.nickName = nickName;
                        }
                    }
                }
            }
        }

        public static class AppsBean {
            public AppsBean() {
            }

            public AppsBean(String id) {
                this.id = id;
            }

            /**
             * contents : [{"cid":"575e22e3c3666e0e6c1bbc03","imgs":[],"target":"tg100001","text":"this is pub test","time":1465787107145,"uid":"u10"}]
             * id : 575e22e3c3666e0e6c1bbc02
             * scores : {"vote":2}
             * tags : null
             * target : tg100001
             * time : 1465787107145
             * type : course
             * uid : u10
             */

            public String getFirstContent() {
                String content = "";
                if (contents != null && !contents.isEmpty()) {
                    ContentsBean c = contents.get(0);
                    content = c.getText();
                }

                return content;
            }

            private String id;
            private ScoresBean scores;
            private Object tags;
            private String target;
            private long time;
            private String type;
            private String uid;
            private List<ContentsBean> contents;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public ScoresBean getScores() {
                return scores;
            }

            public void setScores(ScoresBean scores) {
                this.scores = scores;
            }

            public Object getTags() {
                return tags;
            }

            public void setTags(Object tags) {
                this.tags = tags;
            }

            public String getTarget() {
                return target;
            }

            public void setTarget(String target) {
                this.target = target;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public List<ContentsBean> getContents() {
                return contents;
            }

            public void setContents(List<ContentsBean> contents) {
                this.contents = contents;
            }

            public static class ScoresBean {
                /**
                 * vote : 2
                 */

                private int vote;

                public int getVote() {
                    return vote;
                }

                public void setVote(int vote) {
                    this.vote = vote;
                }
            }

            public static class ContentsBean {
                /**
                 * cid : 575e22e3c3666e0e6c1bbc03
                 * imgs : []
                 * target : tg100001
                 * text : this is pub test
                 * time : 1465787107145
                 * uid : u10
                 */

                private String cid;
                private String target;
                private String text;
                private long time;
                private String uid;
                private List<?> imgs;

                public String getCid() {
                    return cid;
                }

                public void setCid(String cid) {
                    this.cid = cid;
                }

                public String getTarget() {
                    return target;
                }

                public void setTarget(String target) {
                    this.target = target;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public List<?> getImgs() {
                    return imgs;
                }

                public void setImgs(List<?> imgs) {
                    this.imgs = imgs;
                }
            }
        }

        public static class StatisticsBean {
            /**
             * count : 1
             * vote : 2
             */

            private int count;
            private int vote;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getVote() {
                return vote;
            }

            public void setVote(int vote) {
                this.vote = vote;
            }
        }
    }
}
