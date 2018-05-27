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

import be.kuleuven.softdev.haientang.newsclient.model.NewsItem;

public class NewsOverviewActivity extends AppCompatActivity {
    private ListView lvNews;
    private ArrayList<NewsItem> newsItems;
    private NewsItemAdapter adapter;

    int[] ids;
    ImageView SearchIcon;
    //ImageView profile;
    Button SportsBut,EconomyBut,ChinaBut;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);

        initAllRef();

        top5NewsOrderedByLikes("http://api.a17-sd606.studev.groept.be/selectBreakingNews");

        //clickProfilPicBackToLogin();
        clickButtonSearch();
        clickButtonSports();
        clickButtonEconomy();
        clickButtonChina();

        //setUserProfile(userID);
    }

    private void initAllRef(){
        lvNews = (ListView) findViewById(R.id.lvNews);
        newsItems=new ArrayList<>();

        ids=new int[5];
        SearchIcon =(ImageView) findViewById(R.id.searchIcon);
        SportsBut = (Button) findViewById(R.id.Sports);
        EconomyBut =(Button) findViewById(R.id.Economy);
        ChinaBut = (Button) findViewById(R.id.China);
        //profile=(ImageView) findViewById(R.id.profile);
        //userID=getIntent().getExtras().getInt("userID");
    }

    public void top5NewsOrderedByLikes(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<5;i++) {
                                JSONObject jo=jArr.getJSONObject(i);
                                ids[i]=jo.getInt("newsID");
                                String newsTitle=jo.getString("title");
                                String newsDate=jo.getString("date");
                                int newsLikes=jo.getInt("likes");
                                newsItems.add(new NewsItem("",newsTitle,newsDate,newsLikes));
                                //getImageByNewsID(i,ids[i]);
                            }

                            populateNewsListView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(stringRequest);
    }

    private void populateNewsListView(){
        adapter=new NewsItemAdapter(NewsOverviewActivity.this,newsItems);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //search full news by id
                Intent intent=new Intent(getApplicationContext(),NewsShowActivity.class);
                intent.putExtra("newsID",ids[position]);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
    }

//    public void clickProfilPicBackToLogin() {
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(NewsOverviewActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

    private void clickButtonSearch() {
        SearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsOverviewActivity.this, SearchActivity.class);
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

//    public void getImageByNewsID(int i, int newsID) {//newsID is the foreign key in photo table
//        String url="http://api.a17-sd606.studev.groept.be/selectPhotosOnFrontFace/"+newsID;
//        final int j=i;
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jArr=new JSONArray(response);
//                            JSONObject jo=jArr.getJSONObject(0);
//                            String name=jo.getString("photoName");
//                            showImageByName("http://a17-sd606.studev.groept.be/Image/"+name,j);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }}
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {}
//        });
//
//        queue.add(stringRequest);
//    }
//
//    public void showImageByName(String url,int j) {  //through which you can show image.  the url is the image`s url
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache() {
//            @Override
//            public void putBitmap(String url, Bitmap bitmap) {
//            }
//            @Override
//            public Bitmap getBitmap(String url) {
//                return null;
//            }
//        });
//
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(TNImages.get(j),
//                R.drawable.home, R.drawable.home);
//        imageLoader.get(url,
//                listener, 600, 600);
//    }

//    public void setUserProfile(int userID)
//    {
//        if(userID!=0)
//        {
//            String url="http://a17-sd606.studev.groept.be/User/"+userID;
//            RequestQueue mQueue = Volley.newRequestQueue(this);
//            ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache() {
//                @Override
//                public void putBitmap(String url, Bitmap bitmap) {
//                }
//                @Override
//                public Bitmap getBitmap(String url) {
//                    return null;
//                }
//            });
//
//            ImageLoader.ImageListener listener = ImageLoader.getImageListener(profile,
//                    R.drawable.home, R.drawable.home);
//            imageLoader.get(url,
//                    listener, 600, 600);
//
//        }
//        else
//        {
//            profile.setImageResource(R.drawable.profile);
//        }
//     }
}
