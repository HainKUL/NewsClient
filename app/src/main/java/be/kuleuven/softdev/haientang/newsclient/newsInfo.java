package be.kuleuven.softdev.haientang.newsclient;

/**
 * Created by 79285 on 5/19/2018.
 */

public class newsInfo {
   public String newsTitle;
   public String newsTag;

   public newsInfo(String newsTitle,String newsTag)
    {
        this.newsTitle=newsTitle;
        this.newsTag=newsTag;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTag() {
        return newsTag;
    }

    public void setNewsTag(String newsTag) {
        this.newsTag = newsTag;
    }
}
