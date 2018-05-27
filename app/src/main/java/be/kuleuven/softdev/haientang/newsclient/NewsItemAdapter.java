package be.kuleuven.softdev.haientang.newsclient;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import be.kuleuven.softdev.haientang.newsclient.model.NewsItem;

/**
 * Created by Haien on 5/26/2018.
 */

public class NewsItemAdapter extends BaseAdapter {
    Activity context;
    ArrayList<NewsItem> newsItems;
    private static LayoutInflater inflater=null;

    public NewsItemAdapter(Activity context, ArrayList<NewsItem> newsItems) {
        this.context = context;
        this.newsItems = newsItems;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return newsItems.size();
    }

    @Override
    public NewsItem getItem(int position){
        return newsItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.news_item,null):itemView;

        //ImageView imageV=(ImageView) itemView.findViewById(R.id.image);
        ImageView likeIcon=(ImageView)itemView.findViewById(R.id.likesIcon);
        TextView titleTxt=(TextView) itemView.findViewById(R.id.title);
        TextView dateTxt=(TextView) itemView.findViewById(R.id.date);
        TextView likesTxt=(TextView) itemView.findViewById(R.id.likes);

        NewsItem selectedNews = newsItems.get(position);
        titleTxt.setText(selectedNews.title);
        dateTxt.setText(selectedNews.date);
        likesTxt.setText(""+selectedNews.likes);

        return itemView;
    }

}
