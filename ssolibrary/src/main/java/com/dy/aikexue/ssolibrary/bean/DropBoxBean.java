package com.dy.aikexue.ssolibrary.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2017/2/17.
 */

public class DropBoxBean {


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

    public static class Resume {
        private String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }
    }

    public static class Invitations {

        /**
         * _id : 58aab534cfbf6a36d44b7ce7
         * last : 0
         * oid : u76911
         * rctId : RT58aa8fa0cfbf6a3444528175
         * reason :
         * remark : good
         * rid : RE58aab534cfbf6a36d44b7cc6
         * status : waiting
         * time : 1487582516482
         * uid : u21
         */

        public Invitations() {
        }

        public Invitations(String _id) {
            this._id = _id;
        }

        private String _id;
        private long last;
        private String oid;
        private String rctId;
        private String reason;
        private String remark;
        private String rid;
        private String status;
        private long time;
        private String uid;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public long getLast() {
            return last;
        }

        public void setLast(long last) {
            this.last = last;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getRctId() {
            return rctId;
        }

        public void setRctId(String rctId) {
            this.rctId = rctId;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
    }

    public static class DataBean {

        private HashMap<String, RecruitBean> recruit;
        private int total;
        private HashMap<String, NewUserData.Data.Usr> usr;
        private List<DeliverysBean> deliverys;
        private HashMap<String, Resume> resume;
        private List<Invitations> invitations;

        public List<Invitations> getInvitations() {
            return invitations;
        }

        public void setInvitations(List<Invitations> invitations) {
            this.invitations = invitations;
        }

        public HashMap<String, Resume> getResume() {
            return resume;
        }

        public void setResume(HashMap<String, Resume> resume) {
            this.resume = resume;
        }

        public HashMap<String, RecruitBean> getRecruit() {
            return recruit;
        }

        public void setRecruit(HashMap<String, RecruitBean> recruit) {
            this.recruit = recruit;
        }


        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public HashMap<String, NewUserData.Data.Usr> getUsr() {
            return usr;
        }

        public void setUsr(HashMap<String, NewUserData.Data.Usr> usr) {
            this.usr = usr;
        }

        public List<DeliverysBean> getDeliverys() {
            return deliverys;
        }

        public void setDeliverys(List<DeliverysBean> deliverys) {
            this.deliverys = deliverys;
        }

        public static class RecruitBean {
            public RecruitBean() {
            }

            public RecruitBean(String _id) {
                this._id = _id;
            }

            private String _id;
            private String province;
            private String city;
            private String requireEdu;
            private String expe;
            private float minPay;
            private float maxPay;
            private String title;
            private String desc;
            private String content;
            private int postNum;
            private String status;
            private long createTime;
            private List<String> property;
            private List<String> types;
            private List<String> require;
            private Object weal;
//            private List<Object> weal;
            private String ownerId;

            //客户端使用，非接口返回
            private String resultType;
            public static String RESULT_NO_RECRUIT = "OnlyCompany";
            public static String RESULT_ADD_COMPANY = "bothCompany";

            public String getOwnerId() {
                return ownerId;
            }

            public void setOwnerId(String ownerId) {
                this.ownerId = ownerId;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String get_id() {
                return _id;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getRequireEdu() {
                return requireEdu;
            }

            public void setRequireEdu(String requireEdu) {
                this.requireEdu = requireEdu;
            }

            public String getExpe() {
                return expe;
            }

            public void setExpe(String expe) {
                this.expe = expe;
            }

            public float getMinPay() {
                return minPay;
            }

            public void setMinPay(float minPay) {
                this.minPay = minPay;
            }

            public float getMaxPay() {
                return maxPay;
            }

            public void setMaxPay(float maxPay) {
                this.maxPay = maxPay;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getPostNum() {
                return postNum;
            }

            public void setPostNum(int postNum) {
                this.postNum = postNum;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public List<String> getProperty() {
                return property;
            }

            public void setProperty(List<String> property) {
                this.property = property;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }

            public List<String> getRequire() {
                return require;
            }

            public void setRequire(List<String> require) {
                this.require = require;
            }

            public Object getWeal() {
                return weal;
            }

            public void setWeal(Object weal) {
                this.weal = weal;
            }

            public String getResultType() {
                return resultType;
            }

            public void setResultType(String resultType) {
                this.resultType = resultType;
            }
        }


        public static class DeliverysBean {

            /**
             * _id : DY58aab534cfbf6a36d44b7cdc
             * createTime : 1487582516433
             * rctId : RT58aaadf4d624d354d577a8cc
             * rctOwnerId : u41009
             * rsmId : RE58aab534cfbf6a36d44b7cc6
             * rsmOwnerId : u21
             * status : waiting
             */
            public DeliverysBean() {

            }

            public DeliverysBean(String id) {
                this._id = id;
            }

            private String _id;

            private long createTime;
            private String rctId;
            private String rctOwnerId;
            private String rsmId;
            private String rsmOwnerId;
            private String status;
            private Attrs attrs;

            public void setAttrs(Attrs attrs) {
                this.attrs = attrs;
            }

            public Attrs getAttrs() {
                return attrs;
            }

            public static class Attrs {
                private Change change;

                public Change getChange() {
                    return change;
                }

                public void setChange(Change change) {
                    this.change = change;
                }
            }

            public static class Change {
                private String reason;
                private long time;

                public String getReason() {
                    return reason;
                }

                public void setReason(String reason) {
                    this.reason = reason;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public long getTime() {
                    return time;
                }
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getRctId() {
                return rctId;
            }

            public void setRctId(String rctId) {
                this.rctId = rctId;
            }

            public String getRctOwnerId() {
                return rctOwnerId;
            }

            public void setRctOwnerId(String rctOwnerId) {
                this.rctOwnerId = rctOwnerId;
            }

            public String getRsmId() {
                return rsmId;
            }

            public void setRsmId(String rsmId) {
                this.rsmId = rsmId;
            }

            public String getRsmOwnerId() {
                return rsmOwnerId;
            }

            public void setRsmOwnerId(String rsmOwnerId) {
                this.rsmOwnerId = rsmOwnerId;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
