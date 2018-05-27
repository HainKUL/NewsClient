package be.kuleuven.softdev.haientang.newsclient.model;

/**
 * Created by Haien on 5/26/2018.
 */

public class NewsItem {
    public String image,title,date;
    public int likes;

    public NewsItem(String image, String title, String date, int likes) {
        this.image = image;
        this.title = title;
        this.date = date;
        this.likes = likes;
    }
}
