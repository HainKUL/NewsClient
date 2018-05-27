package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.softdev.haientang.newsclient.model.NewsItem;

public class CategoryActivity extends AppCompatActivity {
    private ListView lvNews;
    private ArrayList<NewsItem> newsItems;
    private NewsItemAdapter adapter;
    private List<NewsItem> newsItemList;
    private String url;

    int[] ids;
    String category;
    TextView cateTitle;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initAllRef();
        clickButtonHome();
        NewsAsyncTask(url);
        clickListview();
    }

    private void initAllRef(){
        lvNews = (ListView) findViewById(R.id.lvNews);
        newsItems=new ArrayList<>();

        ids=new int[5];
        category= getIntent().getExtras().getString("category");
        cateTitle=(TextView) findViewById(R.id.CategoryTitle);

        cateTitle.setText(category);
        userID=getIntent().getExtras().getInt("userID");
        newsItemList=new ArrayList<>();
        url="http://api.a17-sd606.studev.groept.be/selectCategoryNews/"+category;
    }

    private void NewsAsyncTask(String url){  //这里的url就是从学校服务器

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++) {   //maybe later,we can change "5" to jArr.length
                                JSONObject jo = jArr.getJSONObject(i);
                                ids[i]=jo.getInt("newsID");
                                String newsTitle=jo.getString("title");
                                String newsDate=jo.getString("date");
                                String image="http://a17-sd606.studev.groept.be/Image/"+jo.getString("frontPhoto");
                                int newsLikes=jo.getInt("likes");
                                newsItemList.add(new NewsItem(image,newsTitle,newsDate,newsLikes));

                            }
                            NewsAdapter newsAdapter = new NewsAdapter(CategoryActivity.this,newsItemList,lvNews);
                            lvNews.setAdapter(newsAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(stringRequest);
    }

    public void clickListview()
    {
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击listview中的内容转到相关地方
                Intent myIntent=new Intent(view.getContext(),NewsShowActivity.class);
                myIntent.putExtra("newsID",ids[position]);
                startActivityForResult(myIntent,position);
            }
        });
    }

    private void clickButtonHome() {
        ImageView SearchBut=(ImageView) findViewById(R.id.Ima_home);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }

}
