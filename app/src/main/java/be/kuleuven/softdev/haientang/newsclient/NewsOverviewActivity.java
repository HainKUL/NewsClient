package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Message;
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
    private static final String TAG = "NewsOverviewActivity";
    private ListView lvNews;
   // private ArrayList<NewsItem> newsItems;
   // private NewsItemAdapter adapter;
    private String url="http://api.a17-sd606.studev.groept.be/selectBreakingNews";
    private List<NewsItem> newsItemList=new ArrayList();

    private ArrayList<Integer> ids;
    ImageView SearchIcon;
    ImageView profile;
    Button SportsBut,EconomyBut,ChinaBut;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);

        initAllRef();
        clickProfilePicBackToLogin();
        clickButtonSearch();
        clickButtonSports();
        clickButtonEconomy();
        clickButtonChina();

        //getNewsInfo(url);
        clickListview();
       setUserProfile(userID);
       new NewsAsyncTask().execute(url);
    }

    private void initAllRef(){
        lvNews = (ListView) findViewById(R.id.lvNews);
       // newsItems=new ArrayList<>();

        ids=new ArrayList<>();
        SearchIcon =(ImageView) findViewById(R.id.searchIcon);
        SportsBut = (Button) findViewById(R.id.Sports);
        EconomyBut =(Button) findViewById(R.id.Economy);
        ChinaBut = (Button) findViewById(R.id.China);
        profile=(ImageView) findViewById(R.id.profile);
        userID=getIntent().getExtras().getInt("userID");
    }



    private List<NewsItem> getNewsInfo(String url){  //这里的url就是从学校服务器

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++) {   //maybe later,we can change "5" to jArr.length
                                JSONObject jo = jArr.getJSONObject(i);
                                ids.add(jo.getInt("newsID"));
                                String newsTitle=jo.getString("title");
                                String newsDate=jo.getString("date");
                                String image="http://a17-sd606.studev.groept.be/Image/"+jo.getString("frontPhoto");
                                int newsLikes=jo.getInt("likes");
                                newsItemList.add(new NewsItem(image,newsTitle,newsDate,newsLikes));

                             //   Toast.makeText(NewsOverviewActivity.this,newsItemList.get(i).image, Toast.LENGTH_SHORT).show();
                            }
                            /*NewsAdapter newsAdapter = new NewsAdapter(NewsOverviewActivity.this,newsItemList,lvNews);
                            lvNews.setAdapter(newsAdapter);*/
                            uploadNewsInfoByThead(newsItemList);

                            //populateNewsListView();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        queue.add(stringRequest);
        return newsItemList;
    }



    /*private void populateNewsListView(){
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
    }*/

    public void clickProfilePicBackToLogin() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsOverviewActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

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

    public void clickListview()
    {
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击listview中的内容转到相关地方
                Intent myIntent=new Intent(view.getContext(),NewsShowActivity.class);
                myIntent.putExtra("newsID",ids.get(position));
                startActivityForResult(myIntent,position);
            }
        });
    }



    public void setUserProfile(int userID)
    {
        if(userID!=0)
        {
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

            ImageLoader.ImageListener listener = ImageLoader.getImageListener(profile,
                    R.drawable.profile, R.drawable.profile);
            imageLoader.get(url,
                    listener, 600, 600);

        }
        else
        {
            profile.setImageResource(R.drawable.profile);
        }
     }

    class NewsAsyncTask extends AsyncTask<String, Void, List<NewsItem>> {

        @Override
        protected List<NewsItem> doInBackground(String... params) {
            return getNewsInfo(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewsItem> result) {
            super.onPostExecute(result);

            NewsAdapter newsAdapter = new NewsAdapter(NewsOverviewActivity.this,result,lvNews);
            lvNews.setAdapter(newsAdapter);

        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);

                NewsAdapter newsAdapter = new NewsAdapter(NewsOverviewActivity.this,(List<NewsItem>) msg.obj,lvNews);
                lvNews.setAdapter(newsAdapter);

        };
    };


    public void uploadNewsInfoByThead(final List<NewsItem> ni) {
        new Thread() {
            public void run() {
                Message message = Message.obtain();
                message.obj = ni;
                mHandler.sendMessage(message);
            };
        }.start();
    }
}
