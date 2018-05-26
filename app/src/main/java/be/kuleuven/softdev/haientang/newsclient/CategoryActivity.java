package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class CategoryActivity extends AppCompatActivity {

    int[] ids;
    String category;
    TextView cateTitle;
    ArrayList<ImageView> newsImages = new ArrayList<ImageView>();
    ArrayList<TextView> newsTitles = new ArrayList<TextView>();
    ArrayList<TextView> newsDates = new ArrayList<TextView>();
    ArrayList<TextView> newsLikes = new ArrayList<TextView>();
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initAllRef();

        cateTitle.setText(category);
        top5NewsOrderedByDate("http://api.a17-sd606.studev.groept.be/selectCategoryNews/"+category);
        searchFullNewsById();
        clickButtonHome();
    }

    private void initAllRef(){
        ids=new int[5];
        category= getIntent().getExtras().getString("category");
        cateTitle=(TextView) findViewById(R.id.CategoryTitle);
        userID=getIntent().getExtras().getInt("userID");

        newsImages.add((ImageView) findViewById(R.id.newsImage1));
        newsImages.add((ImageView) findViewById(R.id.newsImage2));
        newsImages.add((ImageView) findViewById(R.id.newsImage3));
        newsImages.add((ImageView) findViewById(R.id.newsImage4));
        newsImages.add((ImageView) findViewById(R.id.newsImage5));

        newsTitles.add((TextView) findViewById(R.id.newsTitle1));
        newsTitles.add((TextView) findViewById(R.id.newsTitle2));
        newsTitles.add((TextView) findViewById(R.id.newsTitle3));
        newsTitles.add((TextView) findViewById(R.id.newsTitle4));
        newsTitles.add((TextView) findViewById(R.id.newsTitle5));

        newsDates.add((TextView) findViewById(R.id.newsDate1));
        newsDates.add((TextView) findViewById(R.id.newsDate2));
        newsDates.add((TextView) findViewById(R.id.newsDate3));
        newsDates.add((TextView) findViewById(R.id.newsDate4));
        newsDates.add((TextView) findViewById(R.id.newsDate5));

        newsLikes.add((TextView) findViewById(R.id.likesNr1));
        newsLikes.add((TextView) findViewById(R.id.likesNr2));
        newsLikes.add((TextView) findViewById(R.id.likesNr3));
        newsLikes.add((TextView) findViewById(R.id.likesNr4));
        newsLikes.add((TextView) findViewById(R.id.likesNr5));
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
                                String title=jo.getString("title");
                                String date=jo.getString("date");
                                int likes=jo.getInt("likes");

                                newsTitles.get(i).setText(title);
                                newsDates.get(i).setText(date);
                                newsLikes.get(i).setText(""+likes);

                                getImageByNewsID(i,ids[i]);
                            }
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

    public void searchFullNewsById() {
        for(int i=0;i<5;i++){
            final int j = i;
            newsTitles.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(),NewsShowActivity.class);
                    intent.putExtra("newsID",ids[j]);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                }
            });
        }
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

    public void getImageByNewsID(int i, int newsID) {//newsID is the foreign key in photo tabele
        String url="http://api.a17-sd606.studev.groept.be/selectPhotosOnFrontFace/"+newsID;
        final int j=i;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            JSONObject jo=jArr.getJSONObject(0);
                            String name=jo.getString("photoName");

                            showImageByName("http://a17-sd606.studev.groept.be/Image/"+name,j);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(stringRequest);
    }

    public void showImageByName(String url,int j) {  //through which you can show image.  the url is the image`s url
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

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(newsImages.get(j),
                R.drawable.home, R.drawable.home);
        imageLoader.get(url,
                listener, 600, 600);
    }
}
