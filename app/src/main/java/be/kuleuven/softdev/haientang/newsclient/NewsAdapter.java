package be.kuleuven.softdev.haientang.newsclient;

import be.kuleuven.softdev.haientang.newsclient.model.NewsItem;
import java.util.List;

//import org.apache.http.client.methods.AbortableHttpRequest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter implements OnScrollListener{

    private List<NewsItem> mList;
    private LayoutInflater mInflater;
    private ListView mListView;
    private ImageLoader imageLoader;

    private int mStart;
    private int mEnd;
    private boolean isFirstIn;



    public NewsAdapter(Context context,List<NewsItem> data,ListView listView){

        mList=data;
        mInflater=LayoutInflater.from(context);
        mListView=listView;
        isFirstIn = true;

        imageLoader =new ImageLoader(mListView);
        imageLoader.mUrls = new String[mList.size()]; //这几句话在murls当中加入pictures的url,对于我们而言就是把photoname给加进去
        for(int i=0;i<mList.size();i++){
            imageLoader.mUrls[i] = mList.get(i).image;
        }
        mListView.setOnScrollListener(this);  //这里用了setonscrolllistener，那么对他手下两个方法的操作在哪里呢？
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }  //得到前面数据的具体内容

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {   //这里就是给那些数据贴上tag

        ViewHolder viewHolder=null;
        if(convertView==null){   //对convertview初始化
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.news_item,null);
            viewHolder.ivImage=(ImageView) convertView.findViewById(R.id.image);
            viewHolder.ivLikes=(ImageView) convertView.findViewById(R.id.likesIcon);
            viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.title);
            viewHolder.tvDate=(TextView) convertView.findViewById(R.id.date);
            viewHolder.tvlikes=(TextView) convertView.findViewById(R.id.likes);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();  //对已经存在的进行操作
        }

        viewHolder.ivImage.setTag(mList.get(position).image);  //对图片进行贴标签，标签是图片的url
        viewHolder.ivImage.setImageResource(R.drawable.loading);   //未加载完成的，使用这个图片来代替

        //开始收割了，对图片进行显示。
        imageLoader.showImage(viewHolder.ivImage,mList.get(position).image);

        viewHolder.tvTitle.setText(mList.get(position).title);
        viewHolder.tvDate.setText(mList.get(position).date);
        viewHolder.tvlikes.setText(""+mList.get(position).likes);
        viewHolder.ivLikes.setImageResource(R.drawable.like);

        return convertView; //
    }

    //viewholder是什么？ 其实，也就是一个存放着目标内容的载体，在我们的例子里，网上的动态图，是like图，是title是tags
    class ViewHolder{
        ImageView ivImage;
        TextView tvTitle;
        ImageView ivLikes;
        TextView tvDate;
        TextView tvlikes;

    }

    //对setonscrolllistener的两个方法进行操作
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadImages(mStart,mEnd);  //当不再滑动时，对图片进行加载
        }else{
            imageLoader.cancelAllAsyncTask();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        mStart=firstVisibleItem;
        mEnd=firstVisibleItem+visibleItemCount;

        if(isFirstIn&&visibleItemCount>0){
            imageLoader.loadImages(mStart,mEnd);
            isFirstIn=false;
        }

    }

}
