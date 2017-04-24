package com.dy.aikexue.ssolibrary.bean;

/**
 * Created by zhong on 2017/4/11.
 */

public class ResumeInfo {


    /**
     * _id : RE5837bc91d624d32520d02cc7
     * ownerId : u20
     * ownerInfo : {"avatar":"https://fs.dev.gdy.io/z64CCx==.png"}
     */

    private String _id;
    private String ownerId;
    private OwnerInfoBean ownerInfo;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public OwnerInfoBean getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(OwnerInfoBean ownerInfo) {
        this.ownerInfo = ownerInfo;
    }

    public static class OwnerInfoBean {
        /**
         * avatar : https://fs.dev.gdy.io/z64CCx==.png
         */

        private String avatar;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
