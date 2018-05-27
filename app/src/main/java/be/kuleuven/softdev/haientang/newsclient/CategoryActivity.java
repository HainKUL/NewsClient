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

import be.kuleuven.softdev.haientang.newsclient.model.NewsItem;

public class CategoryActivity extends AppCompatActivity {
    private ListView lvNews;
    private ArrayList<NewsItem> newsItems;
    private NewsItemAdapter adapter;

    int[] ids;
    String category;
    TextView cateTitle;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initAllRef();
        top5NewsOrderedByDate("http://api.a17-sd606.studev.groept.be/selectCategoryNews/"+category);
        clickButtonHome();
    }

    private void initAllRef(){
        lvNews = (ListView) findViewById(R.id.lvNews);
        newsItems=new ArrayList<>();

        ids=new int[5];
        category= getIntent().getExtras().getString("category");
        cateTitle=(TextView) findViewById(R.id.CategoryTitle);

        cateTitle.setText(category);
        userID=getIntent().getExtras().getInt("userID");
    }

    public void top5NewsOrderedByDate(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
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
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(stringRequest);
    }

    private void populateNewsListView() {
        adapter = new NewsItemAdapter(CategoryActivity.this, newsItems);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //search full news by id
                Intent intent = new Intent(getApplicationContext(), NewsShowActivity.class);
                intent.putExtra("newsID", ids[position]);
                intent.putExtra("userID",userID);
                startActivity(intent);
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

//    public void getImageByNewsID(int i, int newsID) {//newsID is the foreign key in photo tabele
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
//
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
//        ImageLoader.ImageListener listener = ImageLoader.getImageListener(newsImages.get(j),
//                R.drawable.home, R.drawable.home);
//        imageLoader.get(url,
//                listener, 600, 600);
//    }
}
