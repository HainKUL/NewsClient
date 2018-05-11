package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//还有title的传输，tag的传输等等
public class NewsShowActivity extends AppCompatActivity {

    private int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show);
        Requests();
        updateComment();
        showComments();
        ButtonHome();
        updateLikes();
    }


    public void Requests() {  //get news content from json server
        //get the id from the previous layout
        int id=getIntent().getExtras().getInt("id");

        final TextView mTextView = (TextView) findViewById(R.id.Content);
        final TextView TitleView=(TextView) findViewById(R.id.ShowTitle);
        final TextView tagView=(TextView) findViewById(R.id.newsShowTags);
        final TextView likesView=(TextView) findViewById(R.id.likes);
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url="http://api.a17-sd606.studev.groept.be/selectNewsToDisplay/"+id;


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<1;i++)
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String NewsContent=jo.getString("NewsContent").toString();
                                String NewsTitle=jo.getString("Title");
                                String NewsTag=jo.getString("Tag");
                                num=jo.getInt("Likes");
                                mTextView.setText(NewsContent);
                                TitleView.setText(NewsTitle);
                                tagView.setText(NewsTag);
                                likesView.setText("Likes:"+num);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    //好了，可以上传评论了。接下来的任务是加载评论，且刷新后可见。
    public void updateComment()
    {
        final EditText tv=(EditText) findViewById(R.id.CommentBoard);

        final Button updateComment = (Button) findViewById(R.id.ButtonComment);
        final int idNews=getIntent().getExtras().getInt("id");//news id

        updateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                //int idComment=getLengthOfComments("http://api.a17-sd606.studev.groept.be/getCommentsLength");
                final String content=tv.getText().toString();
                updateToJson("http://api.a17-sd606.studev.groept.be/addComments/"+idNews+"/"+content);
                tv.setText(null);
                showComments();
            }
        });
    }

    public void updateLikes()
    {
        //likesImg
        ImageView like=(ImageView) findViewById(R.id.likesImg);
        final TextView likesView=(TextView) findViewById(R.id.likes);
        final int idnews=getIntent().getExtras().getInt("id");

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                updateToJson("http://api.a17-sd606.studev.groept.be/addLike/"+num+"/"+idnews);
                likesView.setText("Likes:"+num);
            }
        });
    }

    public void updateToJson(String url) {
        //get the id from the previous layout
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    //http://api.a17-sd606.studev.groept.be/showCommentContent/
    public void showComments() {
        int idnews=getIntent().getExtras().getInt("id");
        String url="http://api.a17-sd606.studev.groept.be/showCommentContent/"+idnews;

        final TextView comment1=(TextView) findViewById(R.id.commentOne);  //now sort by id, but later we can sort them by time
        final TextView comment2=(TextView) findViewById(R.id.commentTwo);
        final TextView comment3=(TextView) findViewById(R.id.commentThree);
        final TextView comment4=(TextView) findViewById(R.id.commentFour);
        final TextView comment5=(TextView) findViewById(R.id.commentFive);

        final ArrayList<TextView> commentList=new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);
        commentList.add(comment4);
        commentList.add(comment5);

        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {
                            JSONArray jArr=new JSONArray(response);
                            for(int i=0;i<5;i++)
                            {
                                JSONObject jo=jArr.getJSONObject(i);
                                String content=jo.getString("content");
                                commentList.get(i).setText(content);
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
        ImageView SearchBut=(ImageView) findViewById(R.id.home);
        SearchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(NewsShowActivity.this, NewsOverviewActivity.class);
                startActivity(intent);
            }
        });
    }



}
