package com.example.gabri.finalprojectnewversion.CBCNews;

/**
 * News object
 * @author Natalia Nunes
 */
public class News {
    /**
     * article title
     */
    private String title;
    /**
     * article body
     */
    private String body;

    /**
     * article url
     */
    private String url;

    /**
     * News constructor
     * @param title new title
     */
    public News ( String title ) {
        setTitle(title);
    }

    /**
     * Set title
     * @param title news title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get title
     *
     * @return news title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set the article body
     * @param body article body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * gets article body
     * @return article body
     */
    public String getBody() {
        return body;
    }

    /**
     * gets article url
     * @return article url
     */
    public String getUrl() {
        return url;
    }

    /**
     * sets article URL
     * @param url  article url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
