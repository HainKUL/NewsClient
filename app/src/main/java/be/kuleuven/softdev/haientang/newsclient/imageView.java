package be.kuleuven.softdev.haientang.newsclient;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class imageView extends AppCompatActivity {

    private String url =null;
    private String id="1";
    ImageLoader imageLoader;
    NetworkImageView previewImage;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

       previewImage = (NetworkImageView) findViewById(R.id.previewImage);
        EditText editId = (EditText) findViewById(R.id.name);
        Button ViewImage = (Button) findViewById(R.id.viewButton);
        //id=editId.getText().toString();


    }

    public void FetchImage()
    {
        requestQueue = Volley.newRequestQueue(this);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://a17-sd606.studev.groept.be/imageFetch.php",//这里填写php文件所在的地址（前者是服务器），用的是POST的方法
                new Response.Listener<String>() {
                    @Override


                    public void onResponse(String response) {


                        try {
                            JSONObject res = new JSONObject(response);//对应php中的response和echo json——encode
                            JSONArray thread = res.getJSONArray("name");
                            for (int i = 0; i < thread.length(); i++) {
                                JSONObject obj = thread.getJSONObject(i);
                                url = obj.getString("imageUrl");// photo这里记得是图片的url


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        //Adding request to the queue
        requestQueue.add(stringRequest);

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String url, Bitmap bitmap) {
            }

            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }
        });

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(previewImage,
                0,android.R.drawable  //图片加载过程就设为零
                        .ic_dialog_alert);

        imageLoader.get("http://a17-sd606.studev.groept.be/Image/adrien-robert-505037-unsplash.jpg", listener);
        previewImage.setImageUrl(url, imageLoader); //这里再用一个internetView来加载图片

    }

}
