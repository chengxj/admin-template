package com.edgar.module.sys.view;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;

/**
 * AnagularJS需要的路由对象
 * 
 * @author Edgar Zhang
 * @version 1.0
 */
public class AngularRoute implements Comparable<AngularRoute> {
        @Getter
        @Setter
        private String id;

        @Getter
        private String url;
        @Getter
        private String basename;
        @Getter
        private String path;
        @Getter
        @Setter
        private String name;
        @Getter
        @Setter
        private boolean isMenu;
        @Getter
        @Setter
        private int sorted = 1;
        @Getter
        @Setter
        private String parentId;

        /**
         * 设计路由的地址
         * 
         * @param url
         *                地址
         */
        public void setUrl(String url) {
                this.url = url;
                String newUrl = StringUtils.substringBefore(url, "/:");
                this.basename = StringUtils.substringAfterLast(newUrl, "/");
                this.path = StringUtils.substringBeforeLast(newUrl, "/");
        }

        @Override
        public int compareTo(AngularRoute o) {
                if (sorted == o.getSorted()) {
                        return 0;
                }
                if (sorted > o.getSorted()) {
                        return 1;
                }
                return -1;
        }

}
