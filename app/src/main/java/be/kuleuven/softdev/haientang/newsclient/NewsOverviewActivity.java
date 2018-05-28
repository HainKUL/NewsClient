package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

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

public class NewsOverviewActivity extends AppCompatActivity {
    private ListView lvNews;
    private List<NewsItem> newsItemList;
    int userID;
    private ArrayList<Integer> newsIDs;
    ImageView SearchIcon, profilePic;
    Button SportsBut,EconomyBut,ChinaBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);

        initAllRef();

        clickIconSearch();
        clickProfilePicBackToLogin();

        clickButtonSports();
        clickButtonEconomy();
        clickButtonChina();

        setUserProfile(userID);
        NewsAsyncTask();
        clickListviewForFullNews();
    }

    private void initAllRef(){
        lvNews = (ListView) findViewById(R.id.lvNews);
        newsItemList=new ArrayList();
        userID=getIntent().getExtras().getInt("userID");
        newsIDs=new ArrayList<>();

        SearchIcon =(ImageView) findViewById(R.id.searchIcon);
        profilePic =(ImageView) findViewById(R.id.profile);
        SportsBut = (Button) findViewById(R.id.Sports);
        EconomyBut =(Button) findViewById(R.id.Economy);
        ChinaBut = (Button) findViewById(R.id.China);
    }

    private void clickIconSearch() {
        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsOverviewActivity.this, SearchActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
    }

    public void clickProfilePicBackToLogin() {
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsOverviewActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void clickButtonSports() {
        SportsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("category","Sports");
                startActivity(intent);
            }
        });
    }

    public void clickButtonEconomy() {
        EconomyBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("category","Economy");
                startActivity(intent);
            }
        });
    }

    public void clickButtonChina() {
        ChinaBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("category","China");
                startActivity(intent);
            }
        });
    }

    private void NewsAsyncTask(){  //这里的url就是从学校服务器
        String url="http://api.a17-sd606.studev.groept.be/selectBreakingNews";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++) {
                                JSONObject jo = jArr.getJSONObject(i);
                                newsIDs.add(jo.getInt("newsID"));
                                String newsTitle=jo.getString("title");
                                String newsDate=jo.getString("date");
                                String image="http://a17-sd606.studev.groept.be/Image/"+jo.getString("frontPhoto");
                                int newsLikes=jo.getInt("likes");
                                newsItemList.add(new NewsItem(image,newsTitle,newsDate,newsLikes));
                            }
                            NewsAdapter newsAdapter = new NewsAdapter(NewsOverviewActivity.this,newsItemList,lvNews);
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

    public void clickListviewForFullNews() {
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent=new Intent(view.getContext(),NewsShowActivity.class);
                myIntent.putExtra("newsID",newsIDs.get(position));
                myIntent.putExtra("userID",userID);
                startActivityForResult(myIntent,position);
            }
        });
    }

    public void setUserProfile(int userID) {
        if(userID!=0) {//not guest
            String url="http://a17-sd606.studev.groept.be/User/"+userID;
            RequestQueue mQueue = Volley.newRequestQueue(this);
            ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache() {
                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                }
                @Override
                public Bitmap getBitmap(String url) {
                    return null;
                }
            });

            ImageLoader.ImageListener listener = ImageLoader.getImageListener(profilePic,
                    R.drawable.profile, R.drawable.profile);
            imageLoader.get(url,
                    listener, 600, 600);//load pic  from server

        }
        else {//for guest
            profilePic.setImageResource(R.drawable.profile);
        }
     }
}
