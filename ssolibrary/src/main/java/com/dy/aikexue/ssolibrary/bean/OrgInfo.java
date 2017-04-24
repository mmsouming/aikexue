package com.dy.aikexue.ssolibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhong on 2017/2/22.
 */

public class OrgInfo implements Serializable {

    private AuthorityBean authority;
    private String introduction;
    private String name;
    private ProtocolBean protocol;
    private String website;
    private List<String> imgs;
    private List<String> logo;
    private List<String> tags;
    private List<String> industry;
    private int scale;
    private String city;
    private String province;

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setIndustry(List<String> industry) {
        this.industry = industry;
    }

    public List<String> getIndustry() {
        return industry;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public AuthorityBean getAuthority() {
        return authority;
    }

    public void setAuthority(AuthorityBean authority) {
        this.authority = authority;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProtocolBean getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolBean protocol) {
        this.protocol = protocol;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<String> getLogo() {
        return logo;
    }

    public void setLogo(List<String> logo) {
        this.logo = logo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public static class AuthorityBean implements Serializable {
        /**
         * CUSTOMIZE_PROTOCOL : 100
         */

        private int CUSTOMIZE_PROTOCOL;

        public int getCUSTOMIZE_PROTOCOL() {
            return CUSTOMIZE_PROTOCOL;
        }

        public void setCUSTOMIZE_PROTOCOL(int CUSTOMIZE_PROTOCOL) {
            this.CUSTOMIZE_PROTOCOL = CUSTOMIZE_PROTOCOL;
        }
    }

    public static class ProtocolBean implements Serializable {
        /**
         * fileName : PH2机构人才协议.docx
         * fileUrl : http://fs.dev.gdy.io/TvdODt==.docx
         */

        private String fileName;
        private String fileUrl;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
}
