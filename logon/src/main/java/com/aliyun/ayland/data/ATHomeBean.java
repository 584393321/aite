package com.aliyun.ayland.data;

public class ATHomeBean {
    private String href_url;
    private String pic_url;

    public String getHref_url() {
        return href_url;
    }

    public void setHref_url(String href_url) {
        this.href_url = href_url;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    @Override
    public String toString() {
        return "BannerBean{" +
                "href_url='" + href_url + '\'' +
                ", pic_url='" + pic_url + '\'' +
                '}';
    }
}
