package com.dy.aikexue.ssolibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhong on 2017/3/28.
 */

public class PostProgressBean {

    /**
     * code : 0
     * data : {"delivery":{"history":[{"remark":"remark","status":"waiting","time":"1234543235"},{"remark":"remark","status":"waiting","time":"1234543235"}]}}
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
        /**
         * delivery : {"history":[{"remark":"remark","status":"waiting","time":"1234543235"},{"remark":"remark","status":"waiting","time":"1234543235"}]}
         */

        private DeliveryBean delivery;

        public DeliveryBean getDelivery() {
            return delivery;
        }

        public void setDelivery(DeliveryBean delivery) {
            this.delivery = delivery;
        }

        public static class DeliveryBean {
            private List<HistoryBean> history;

            public List<HistoryBean> getHistory() {
                return history;
            }

            public void setHistory(List<HistoryBean> history) {
                this.history = history;
            }

            public static class HistoryBean implements Serializable {
                private static final long serialVersionUID = 111111111111111111L;
                /**
                 * remark : remark
                 * status : waiting
                 * time : 1234543235
                 */

                private String remark;
                private String status;
                private long time;

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
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
            }
        }
    }
}
