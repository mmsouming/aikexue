package com.dy.aikexue.ssolibrary.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2017/2/22.
 */

public class CompanyDetailBean {
    private int code;
    private DetailData data;

    public void setData(DetailData data) {
        this.data = data;
    }

    public DetailData getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static class DetailData {
        private long lastVisit;
        private int total;
        private List<DropBoxBean.DataBean.RecruitBean> recruits;
        private HashMap<String, NewUserData.Data.Usr> usr;

        public long getLastVisit() {
            return lastVisit;
        }

        public void setLastVisit(long lastVisit) {
            this.lastVisit = lastVisit;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DropBoxBean.DataBean.RecruitBean> getRecruits() {
            return recruits;
        }

        public void setRecruits(List<DropBoxBean.DataBean.RecruitBean> recruits) {
            this.recruits = recruits;
        }

        public HashMap<String, NewUserData.Data.Usr> getUsr() {
            return usr;
        }

        public void setUsr(HashMap<String, NewUserData.Data.Usr> usr) {
            this.usr = usr;
        }

        public DetailData() {
            super();
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
