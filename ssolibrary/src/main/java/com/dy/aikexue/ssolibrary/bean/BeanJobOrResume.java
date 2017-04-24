package com.dy.aikexue.ssolibrary.bean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by fuq on 2017/2/22.
 */

public class BeanJobOrResume {
    private int code;
    private BeanJobOrResumeData data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BeanJobOrResumeData getData() {
        return data;
    }

    public void setData(BeanJobOrResumeData data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class BeanJobOrResumeData{
        private HashMap<String, NewUserData.Data.Usr> org;
        private Object resume;      //简历数据
        private List<DropBoxBean.DataBean.RecruitBean> recruit;     //招聘信息数据
        private List<DropBoxBean.DataBean.RecruitBean> company;     //通过搜索公司获取的职位数据
        private int total;     //职位和公司总和
        private int rTotal;     //搜索到的职位数
        private int cTotal;     //搜索到的公司数

        public void setOrg(HashMap<String, NewUserData.Data.Usr> usr) {
            this.org = usr;
        }

        public HashMap<String, NewUserData.Data.Usr> getOrg() {
            return org;
        }

        public List<DropBoxBean.DataBean.RecruitBean> getRecruit() {
            return recruit;
        }

        public void setRecruit(List<DropBoxBean.DataBean.RecruitBean> recruit) {
            this.recruit = recruit;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DropBoxBean.DataBean.RecruitBean> getCompany() {
            return company;
        }

        public void setCompany(List<DropBoxBean.DataBean.RecruitBean> company) {
            this.company = company;
        }

        public int getrTotal() {
            return rTotal;
        }

        public int getcTotal() {
            return cTotal;
        }
    }

}
