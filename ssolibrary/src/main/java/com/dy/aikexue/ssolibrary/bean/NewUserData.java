package com.dy.aikexue.ssolibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by:Pxy
 * Date: 2016-03-22
 * Time: 10:30
 */
public class NewUserData implements Serializable {
    private int code;

    private Data data;

    private String msg;


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "NewUserData{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }


    public static class Data implements Serializable {
        private String token;

        private Usr usr;

        public void setToken(String token) {
            this.token = token;
        }

        public String getToken() {
            return this.token;
        }

        public void setUsr(Usr usr) {
            this.usr = usr;
        }

        public Usr getUsr() {
            return this.usr;
        }


        @Override
        public String toString() {
            return "Data{" +
                    "token='" + token + '\'' +
                    ", usr=" + usr.toString() +
                    '}';
        }

        public static class Usr implements Serializable {
            private String id;
            //系统账号
            private String account;
            //用户登录名列表
            private List<String> usr;

            private Attrs attrs;

            public String getName() {
                String name = "";
                if (getAttrs() != null && getAttrs().getBasic() != null) {
                    name = getAttrs().getBasic().getNickName();
                }
                return name;
            }

            public String getPhotoUrl() {
                String url = "";
                if (getAttrs() != null && getAttrs().getBasic() != null) {
                    url = getAttrs().getBasic().getAvatar();
                }
                return url;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public void setUsr(List<String> usr) {
                this.usr = usr;
            }

            public List<String> getUsr() {
                return this.usr;
            }

            public void setAttrs(Attrs attrs) {
                this.attrs = attrs;
            }

            public Attrs getAttrs() {
                return this.attrs;
            }

            @Override
            public String toString() {
                return "Usr{" +
                        "id='" + id + '\'' +
                        ", usr=" + usr +
                        ", attrs=" + attrs +
                        '}';
            }

        }

    }

}



