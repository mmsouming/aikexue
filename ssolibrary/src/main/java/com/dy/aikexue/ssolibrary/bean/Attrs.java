package com.dy.aikexue.ssolibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:Pxy
 * Date: 2016-03-21
 * Time: 18:52
 */

public class Attrs implements Serializable {

    private Basic basic;

    private Certification certification;

    private Extra extra;
    private OrgInfo orgInfo;

    public void setOrgInfo(OrgInfo orgInfo) {
        this.orgInfo = orgInfo;
    }

    public OrgInfo getOrgInfo() {
        return orgInfo;
    }

    //绑定手机号等私密信息
    private Private privated;
    private Role role;

    public Private getPrivated() {
        return privated;
    }

    public void setPrivated(Private privated) {
        this.privated = privated;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Basic getBasic() {
        return this.basic;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public Certification getCertification() {
        return this.certification;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }


    public static class Org implements Serializable {
        public int AKX_TEACHER;
        public int CERT_VERIFY_ADMIN;
        public int COURSE_EDITOR;
        public int COURSE_VERIFY_ADMIN;
        public String orgid;

        public int getAKX_TEACHER() {
            return AKX_TEACHER;
        }

        public void setAKX_TEACHER(int AKX_TEACHER) {
            this.AKX_TEACHER = AKX_TEACHER;
        }

        public int getCERT_VERIFY_ADMIN() {
            return CERT_VERIFY_ADMIN;
        }

        public void setCERT_VERIFY_ADMIN(int CERT_VERIFY_ADMIN) {
            this.CERT_VERIFY_ADMIN = CERT_VERIFY_ADMIN;
        }

        public int getCOURSE_EDITOR() {
            return COURSE_EDITOR;
        }

        public void setCOURSE_EDITOR(int COURSE_EDITOR) {
            this.COURSE_EDITOR = COURSE_EDITOR;
        }

        public int getCOURSE_VERIFY_ADMIN() {
            return COURSE_VERIFY_ADMIN;
        }

        public void setCOURSE_VERIFY_ADMIN(int COURSE_VERIFY_ADMIN) {
            this.COURSE_VERIFY_ADMIN = COURSE_VERIFY_ADMIN;
        }

        public String getOrgid() {
            return orgid;
        }

        public void setOrgid(String orgid) {
            this.orgid = orgid;
        }
    }

    public static class Basic implements Serializable {
        private String avatar;

        private String desc;

        private String email;

        private int gender;

        private String nickName;

        private String phone;

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatar() {
            return this.avatar;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return this.email;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getGender() {
            return this.gender;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getNickName() {
            return this.nickName;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return this.phone;
        }

        @Override
        public String toString() {
            return "Basic{" +
                    "avatar='" + avatar + '\'' +
                    ", desc='" + desc + '\'' +
                    ", email='" + email + '\'' +
                    ", gender=" + gender +
                    ", nickName='" + nickName + '\'' +
                    ", phone='" + phone + '\'' +
                    '}';
        }
    }

    public static class Private implements Serializable {
        private String phone;
        private Weixin weixin;
        private String no;

        public void setNo(String no) {
            this.no = no;
        }

        public String getNo() {
            return no;
        }

        public boolean isBindingWeixin() {
            if (weixin != null && weixin.getUnionId() != null) {
                return true;
            }
            return false;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Weixin getWeixin() {
            return weixin;
        }

        public void setWeixin(Weixin weixin) {
            this.weixin = weixin;
        }

        @Override
        public String toString() {
            return "Private{" +
                    "phone='" + phone + '\'' +
                    '}';
        }

        public class Weixin implements Serializable {

            /**
             * name : yuhy
             * openId : oarD4wvUkmem7t8S-X04Ox18bl6U
             * unionId : oZydIxDI7-tkK1K1Hm9MIE-sqxuk
             */

            private String name;
            private String openId;
            private String unionId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getOpenId() {
                return openId;
            }

            public void setOpenId(String openId) {
                this.openId = openId;
            }

            public String getUnionId() {
                return unionId;
            }

            public void setUnionId(String unionId) {
                this.unionId = unionId;
            }
        }
    }

    public class Role implements Serializable {
        private List<Org> org;
        private int UCS_ADMIN;

        private int COURSE_VERIFY_ADMIN;

        private int PAGE_EDIT_ADMIN;

        public void setUCS_ADMIN(int UCS_ADMIN) {
            this.UCS_ADMIN = UCS_ADMIN;
        }

        public int getUCS_ADMIN() {
            return this.UCS_ADMIN;
        }

        public void setCOURSE_VERIFY_ADMIN(int COURSE_VERIFY_ADMIN) {
            this.COURSE_VERIFY_ADMIN = COURSE_VERIFY_ADMIN;
        }

        public int getCOURSE_VERIFY_ADMIN() {
            return this.COURSE_VERIFY_ADMIN;
        }

        public void setPAGE_EDIT_ADMIN(int PAGE_EDIT_ADMIN) {
            this.PAGE_EDIT_ADMIN = PAGE_EDIT_ADMIN;
        }

        public int getPAGE_EDIT_ADMIN() {
            return this.PAGE_EDIT_ADMIN;
        }

        @Override
        public String toString() {
            return "Role{" +
                    "UCS_ADMIN=" + UCS_ADMIN +
                    ", COURSE_VERIFY_ADMIN=" + COURSE_VERIFY_ADMIN +
                    ", PAGE_EDIT_ADMIN=" + PAGE_EDIT_ADMIN +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "Attrs{" +
                "basic=" + basic +
                ", certification=" + certification +
                ", extra=" + extra +
                ", privated=" + privated +
                ", role=" + role +
                '}';
    }
}
