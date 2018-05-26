package be.kuleuven.softdev.haientang.newsclient;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewsShowActivity extends AppCompatActivity {
    int newsID,likesNr;
    ImageView homeIcon,thumbUpIcon;

    TextView newsTitle,newsDate,newsTags,newsContent,newsLikes;
    EditText commentBoard;
    Button submitBut;
    ImageView ivUp;
    ImageView ivDown;
    String[] imageUrl=new String[2];
    String[] imagePosition=new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show);

        initAllRef();

        displayFullNewsByID();
        displayUpToTenComments();
        addLike();
        addComments();
        backToNewsOverview();
        getImageInfo("http://api.a17-sd606.studev.groept.be/addPhotos/"+newsID);

    }

    private void initAllRef() {
        newsID = getIntent().getExtras().getInt("newsID");

        homeIcon = (ImageView) findViewById(R.id.home);
        thumbUpIcon = (ImageView) findViewById(R.id.likesIcon);
        newsTitle=(TextView) findViewById(R.id.Title);
        newsDate = (TextView) findViewById(R.id.newsDate);
        newsTags=(TextView) findViewById(R.id.Tags);
        newsContent = (TextView) findViewById(R.id.Content);
        newsLikes=(TextView) findViewById(R.id.likesNr);
        commentBoard = (EditText) findViewById(R.id.CommentBoard);
        submitBut = (Button) findViewById(R.id.ButSubmit);
        ivUp=(ImageView) findViewById(R.id.newsImageUp);
        ivDown=(ImageView)findViewById(R.id.newsImageDown);
    }

    public void displayFullNewsByID() {
        String url="http://api.a17-sd606.studev.groept.be/selectNewsToDisplay/"+newsID;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.N)
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                                JSONObject jo=jArr.getJSONObject(0);

                                String tt=jo.getString("title");
                                String tg=jo.getString("tags");
                                String cnt=jo.getString("content");
                                String d=jo.getString("date");
                                likesNr = jo.getInt("likes");

                                newsTitle.setText(tt);
                                newsTags.setText(tg);
                                newsLikes.setText(""+likesNr);
                                newsContent.setText(Html.fromHtml(cnt, Html.FROM_HTML_MODE_LEGACY));
                                newsDate.setText(d);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void addLike() {
        thumbUpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likesNr++;
                String url="http://api.a17-sd606.studev.groept.be/addLikes/"+likesNr+"/"+newsID;

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                newsLikes.setText(""+likesNr);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
    }

    public void addComments() {
        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get instant time
                Calendar mCurrentDate = Calendar.getInstance();;
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String dt = format.format(mCurrentDate.getTime());
                String url="http://api.a17-sd606.studev.groept.be/addComments/"+newsID+"/"+commentBoard.getText().toString()+dt;

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                refreshCommentsLists(commentBoard.getText().toString(),dt);
                                commentBoard.setText("");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(stringRequest);
            }
        });
    }

    public void refreshCommentsLists(String comment,String dateTime) {
        // 1.create Java objects for all views and viewgroups
        LinearLayout llh = new LinearLayout(this);
        LinearLayout llv = new LinearLayout(this);
        ImageView pic = new ImageView(this);
        TextView time = new TextView(this);
        TextView newComment= new TextView(this);

        //2.define properties for each
        LinearLayout.LayoutParams dimension1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams dimension2 = new LinearLayout.LayoutParams(80, 80);
        llh.setLayoutParams(dimension1);
        llv.setLayoutParams(dimension1);
        pic.setLayoutParams(dimension2);
        time.setLayoutParams(dimension1);
        newComment.setLayoutParams(dimension1);

        //3.set other properties
        llh.setOrientation(LinearLayout.HORIZONTAL);
        llv.setOrientation(LinearLayout.VERTICAL);
        time.setText(dateTime);
        newComment.setText(comment);
        pic.setLayoutParams(dimension2);

        //4.add to viewGroup
        llv.addView(time);
        llv.addView(newComment);
        llh.addView(pic);
        llh.addView(llv);
        LinearLayout ll = (LinearLayout) findViewById(R.id.myLinearLayout);
        ll.addView(llh);
    }

    private void displayUpToTenComments(){
        String url="http://api.a17-sd606.studev.groept.be/displayFiveComments/"+newsID;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<jArr.length();i++){
                                JSONObject jo=jArr.getJSONObject(i);
                                String c=jo.getString("content");
                                String t=jo.getString("datetime");
                                refreshCommentsLists(c,t);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Failed to load the comments!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void backToNewsOverview() {
        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to news_overview activity
                Intent intent = new Intent(NewsShowActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getImageInfo(String url) {//display top 5 breaking news on newsOverview
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<2;i++) {
                                JSONObject jo=jArr.getJSONObject(i);
                                String name=jo.getString("photoName");
                               String position=jo.getString("photoPosition");
                                showImage("http://a17-sd606.studev.groept.be/Image/"+name,position);
                            }
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


    public void showImage(String url,String pos)  //through which you can show image.  the url is the image`s url
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

           if(pos.equals("up"))
           {
               ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivUp,
                       R.drawable.home, R.drawable.home);
               imageLoader.get(url,
                       listener, 600, 600);
           }
           else if(pos.equals("down"))
           {

               ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivDown,
                       R.drawable.home, R.drawable.home);
               imageLoader.get(url,
                       listener, 600, 600);
           }
           else
           {
               ivUp.setImageResource(R.drawable.home);
               ivDown.setImageResource(R.drawable.home);
           }



    }

}

