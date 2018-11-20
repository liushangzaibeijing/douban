package com.xiu.crawling.douban.proxypool.domain;

/**
 * Created by tony on 2017/10/19.
 * 爬取出来的页面
 */
public class Page {

    /**
     * 代理网站的url地址
     */
    private String url;
    /**
     * 请求的响应码
     */
    private int statusCode;
    /**
     * 请求返回的页面
     */
    private String html;//response content

    @Override
    public boolean equals(Object o) {

        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Page page = (Page) o;

        return url.equals(page.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
