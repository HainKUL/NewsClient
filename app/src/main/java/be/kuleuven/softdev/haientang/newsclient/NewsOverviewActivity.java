package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class NewsOverviewActivity extends AppCompatActivity {
    int[] ids;
    ArrayList<ImageView> newsImages = new ArrayList<ImageView>();
    ArrayList<ImageView> TNImages = new ArrayList<ImageView>();
    ArrayList<TextView> TNTitles = new ArrayList<TextView>();
    ArrayList<TextView> TNDates = new ArrayList<TextView>();
    ArrayList<TextView> TNLikes = new ArrayList<TextView>();
    ImageView SearchIcon,profile;
    Button SportsBut,EconomyBut,ChinaBut;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_overview);

        initAllRef();

        top5NewsOrderedByLikes("http://api.a17-sd606.studev.groept.be/selectBreakingNews");

        goToLoginInterface();
        clickButtonSearch();
        clickButtonSports();
        clickButtonEconomy();
        clickButtonChina();

        searchFullNewsById();
        setUserProfile(userID);
    }

    private void initAllRef(){
        ids=new int[5];

        SearchIcon = findViewById(R.id.searchIcon);
        SportsBut = findViewById(R.id.Sports);
        EconomyBut = findViewById(R.id.Economy);
        ChinaBut = findViewById(R.id.China);
        userID=getIntent().getExtras().getInt("userID");

        TNImages.add((ImageView) findViewById(R.id.TNImage1));
        TNImages.add((ImageView) findViewById(R.id.TNImage2));
        TNImages.add((ImageView) findViewById(R.id.TNImage3));
        TNImages.add((ImageView) findViewById(R.id.TNImage4));
        TNImages.add((ImageView) findViewById(R.id.TNImage5));

        TNTitles.add((TextView) findViewById(R.id.TNTitle1));
        TNTitles.add((TextView) findViewById(R.id.TNTitle2));
        TNTitles.add((TextView) findViewById(R.id.TNTitle3));
        TNTitles.add((TextView) findViewById(R.id.TNTitle4));
        TNTitles.add((TextView) findViewById(R.id.TNTitle5));

        TNDates.add((TextView) findViewById(R.id.TNDate1));
        TNDates.add((TextView) findViewById(R.id.TNDate2));
        TNDates.add((TextView) findViewById(R.id.TNDate3));
        TNDates.add((TextView) findViewById(R.id.TNDate4));
        TNDates.add((TextView) findViewById(R.id.TNDate5));

        TNLikes.add((TextView) findViewById(R.id.likesNr1));
        TNLikes.add((TextView) findViewById(R.id.likesNr2));
        TNLikes.add((TextView) findViewById(R.id.likesNr3));
        TNLikes.add((TextView) findViewById(R.id.likesNr4));
        TNLikes.add((TextView) findViewById(R.id.likesNr5));

        profile=(ImageView) findViewById(R.id.profile);

    }

    public void goToLoginInterface()
    {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewsOverviewActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
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

                                TNTitles.get(i).setText(newsTitle);
                                TNDates.get(i).setText(newsDate);
                                TNLikes.get(i).setText(""+newsLikes);//to insert integer to database, must use ""

                                getImageByNewsID(i,ids[i]);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });

        queue.add(stringRequest);
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

    public void searchFullNewsById(){
        for(int i=0;i<5;i++){
            final int j = i;
            TNTitles.get(j).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(),NewsShowActivity.class);
                    intent.putExtra("newsID",ids[j]);
                    intent.putExtra("title",TNTitles.get(j).toString());
                    intent.putExtra("date",TNDates.get(j).toString());
                    intent.putExtra("likes",TNLikes.get(j).toString());
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                }
            });
        }
    }

    public void getImageByNewsID(int i, int newsID) {//newsID is the foreign key in photo table
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

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(TNImages.get(j),
                R.drawable.home, R.drawable.home);
        imageLoader.get(url,
                listener, 600, 600);
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
                    R.drawable.home, R.drawable.home);
            imageLoader.get(url,
                    listener, 600, 600);

        }
        else
        {
            profile.setImageResource(R.drawable.profile);
        }
     }
}
