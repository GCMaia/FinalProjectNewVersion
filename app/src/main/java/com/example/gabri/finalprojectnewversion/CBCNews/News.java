package com.example.gabri.finalprojectnewversion.CBCNews;

public class News {

  private String title;
  private String body;
  private String url;

  public News ( String title ) {
    setTitle(title);
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getBody() {
    return body;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}