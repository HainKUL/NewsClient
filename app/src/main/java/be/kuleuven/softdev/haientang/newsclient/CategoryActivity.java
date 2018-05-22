package be.kuleuven.softdev.haientang.newsclient;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    static private int[] ids=new int[5];
    private String category=new String();
    private TextView title;
    private ArrayList<TextView> titleList=new ArrayList<>();
    private ArrayList<TextView> tagList=new ArrayList<>();
    private ArrayList<ImageView> imageList=new ArrayList<>();

    private TextView title1;
    private TextView title2;
    private TextView title3;
    private TextView title4;
    private TextView title5;

    private TextView tag1;
    private TextView tag2;
    private TextView tag3;
    private TextView tag4;
    private TextView tag5;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;




    //private List<Map<String,Object>> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        category=getIntent().getExtras().getString("category");
        title=(TextView) findViewById(R.id.CategoryTitle);
        title.setText(category);
        init();
        RequestsTopTwoNews("http://api.a17-sd606.studev.groept.be/selectTopTwoNews/"+category);
        GoToNews();
        ButtonHome();
    }

    public void init()
    {
        title1=(TextView) findViewById(R.id.newsTitle1);
        title2=(TextView) findViewById(R.id.newsTitle2);
        title3=(TextView) findViewById(R.id.newsTitle3);
        title4=(TextView) findViewById(R.id.newsTitle4);
        title5=(TextView) findViewById(R.id.newsTitle5);
        titleList.add(title1);
        titleList.add(title2);
        titleList.add(title3);
        titleList.add(title4);
        titleList.add(title5);

        tag1=(TextView) findViewById(R.id.newsTag1);
        tag2=(TextView) findViewById(R.id.newsTag2);
        tag3=(TextView) findViewById(R.id.newsTag3);
        tag4=(TextView) findViewById(R.id.newsTag4);
        tag5=(TextView) findViewById(R.id.newsTag5);
        tagList.add(tag1);
        tagList.add(tag2);
        tagList.add(tag3);
        tagList.add(tag4);
        tagList.add(tag5);


        img1=(ImageView) findViewById(R.id.newsImage1);
        img2=(ImageView) findViewById(R.id.newsImage2);
        img3=(ImageView) findViewById(R.id.newsImage3);
        img4=(ImageView) findViewById(R.id.newsImage4);
        img5=(ImageView) findViewById(R.id.newsImage5);
        imageList.add(img1);
        imageList.add(img2);
        imageList.add(img3);
        imageList.add(img4);
        imageList.add(img5);

    }


    public void GoToNews() {

        titleList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[0]);
                startActivity(intent);
            }
        });
        titleList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[1]);
                startActivity(intent);
            }
        });
        titleList.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[2]);
                startActivity(intent);
            }
        });
        titleList.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[3]);
                startActivity(intent);
            }
        });
        titleList.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsShowActivity.class);
                intent.putExtra("newsID",ids[4]);
                startActivity(intent);
            }
        });

    }


    public void RequestsTopTwoNews(String url) {

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<5;i++) //here we just select the tpo 10
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String newsTitle=jo.getString("title");
                                String newsTag=jo.getString("tags");
                                ids[i]=jo.getInt("newsID");
                                titleList.get(i).setText(newsTitle);
                                tagList.get(i).setText(newsTag);
                                displayImage(i,ids[i]);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //之后再增加tag 等东西

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    public void ButtonHome()
    {
        ImageView SearchBut=(ImageView) findViewById(R.id.Ima_Category);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(CategoryActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void displayImage(int i,int id) {//display top 5 breaking news on newsOverview
        // Instantiate the RequestQueue.
        String url;
        final int j=i;
        url="http://api.a17-sd606.studev.groept.be/selectPhotosOnFrontFace/"+id;

        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            JSONObject jo=jArr.getJSONObject(0);
                            String name=jo.getString("photo_name");
                            showImage("http://a17-sd606.studev.groept.be/Image/"+name,j);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }



    public void showImage(String url,int j)  //through which you can show image.  the url is the image`s url
    {

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

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageList.get(j),
                R.drawable.home, R.drawable.home);
        imageLoader.get(url,
                listener, 600, 600);

    }



}
