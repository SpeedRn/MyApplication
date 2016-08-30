package com.example.sdpc.myapplication.model;

import java.util.List;

/**
 * Created by sdpc on 16-8-29.
 */
public class BeanTest {
    /**
     * pid : com.plugin
     * name : 插件名称
     * desktopeffectpic : http://static.scloud.letv.com/800000026-wallpaper/a328ee42-05fe-4993/desktop.jpg
     * desktopicon : http://static.scloud.letv.com/800000026-wallpaper/a328ee42-05fe-4993/desktop.jpg
     * desktopcornerpic : http://static.scloud.letv.com/800000026-wallpaper/a328ee42-05fe-4993/desktop.jpg
     * viewname : [{"lang":"us","content":"显示名称"}]
     * pVersionName : 游戏中心
     * pVersionCode : 20151230
     * description : [{"lang":"us","content":"描述描述描述"}]
     * uiVersion : ui>5.0&&ui<6.0
     * versionExpression : code>20151223&&code<20151230
     * updatecomment : [{"lang":"us","content":"更新说明"}]
     * uiType : ["us","full"]
     * tvModel : ["X50","S240F"]
     * strategy : 1
     * url : http://S3/
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String pid;
        private String name;
        private String desktopeffectpic;
        private String desktopicon;
        private String desktopcornerpic;
        private String pVersionName;
        private String pVersionCode;
        private String uiVersion;
        private String versionExpression;
        private int strategy;
        private String url;
        /**
         * lang : us
         * content : 显示名称
         */

        private List<ViewnameBean> viewname;
        /**
         * lang : us
         * content : 描述描述描述
         */

        private List<DescriptionBean> description;
        /**
         * lang : us
         * content : 更新说明
         */

        private List<UpdatecommentBean> updatecomment;
        private List<String> uiType;
        private List<String> tvModel;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesktopeffectpic() {
            return desktopeffectpic;
        }

        public void setDesktopeffectpic(String desktopeffectpic) {
            this.desktopeffectpic = desktopeffectpic;
        }

        public String getDesktopicon() {
            return desktopicon;
        }

        public void setDesktopicon(String desktopicon) {
            this.desktopicon = desktopicon;
        }

        public String getDesktopcornerpic() {
            return desktopcornerpic;
        }

        public void setDesktopcornerpic(String desktopcornerpic) {
            this.desktopcornerpic = desktopcornerpic;
        }

        public String getPVersionName() {
            return pVersionName;
        }

        public void setPVersionName(String pVersionName) {
            this.pVersionName = pVersionName;
        }

        public String getPVersionCode() {
            return pVersionCode;
        }

        public void setPVersionCode(String pVersionCode) {
            this.pVersionCode = pVersionCode;
        }

        public String getUiVersion() {
            return uiVersion;
        }

        public void setUiVersion(String uiVersion) {
            this.uiVersion = uiVersion;
        }

        public String getVersionExpression() {
            return versionExpression;
        }

        public void setVersionExpression(String versionExpression) {
            this.versionExpression = versionExpression;
        }

        public int getStrategy() {
            return strategy;
        }

        public void setStrategy(int strategy) {
            this.strategy = strategy;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<ViewnameBean> getViewname() {
            return viewname;
        }

        public void setViewname(List<ViewnameBean> viewname) {
            this.viewname = viewname;
        }

        public List<DescriptionBean> getDescription() {
            return description;
        }

        public void setDescription(List<DescriptionBean> description) {
            this.description = description;
        }

        public List<UpdatecommentBean> getUpdatecomment() {
            return updatecomment;
        }

        public void setUpdatecomment(List<UpdatecommentBean> updatecomment) {
            this.updatecomment = updatecomment;
        }

        public List<String> getUiType() {
            return uiType;
        }

        public void setUiType(List<String> uiType) {
            this.uiType = uiType;
        }

        public List<String> getTvModel() {
            return tvModel;
        }

        public void setTvModel(List<String> tvModel) {
            this.tvModel = tvModel;
        }

        public static class ViewnameBean {
            private String lang;
            private String content;

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class DescriptionBean {
            private String lang;
            private String content;

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        public static class UpdatecommentBean {
            private String lang;
            private String content;

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
