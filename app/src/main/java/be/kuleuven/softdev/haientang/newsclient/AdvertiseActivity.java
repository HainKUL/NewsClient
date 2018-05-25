package be.kuleuven.softdev.haientang.newsclient;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

public class AdvertiseActivity extends AppCompatActivity {

    EditText titleEdit,tagsEdit,content1Edit;
    Spinner categorySpin;
    TextView dateTv;
    Button submitBut;
    ImageView calenderImg;

    int day,month,year;
    Calendar mCurrentDate;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        init();

        dateTv.setText(date);//date by default
        calenderImg.setOnClickListener(new View.OnClickListener() {//choose the date from calendar and display it
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(AdvertiseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        dateTv.setText(y+"-"+(m+1)+"-"+d);
                    }
                },year,month-1,day);
                datePickerDialog.show();
            }
        });

        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=titleEdit.getText().toString();
                String category=categorySpin.getSelectedItem().toString();
                String tags= tagsEdit.getText().toString();
                String content=content1Edit.getText().toString();
                String url="http://api.a17-sd606.studev.groept.be/postNews/"
                        +title +"/" +content+"/"+tags+"/"+category+"/"+date;
                //dateTv.setText(Html.fromHtml(content,Html.FROM_HTML_MODE_LEGACY));

                postNews(url);
            }
        });
    }

    private void init(){
        titleEdit = (EditText) findViewById(R.id.title);
        categorySpin = (Spinner) findViewById(R.id.categSpinner);
        dateTv = (TextView) findViewById(R.id.date);
        tagsEdit = (EditText) findViewById(R.id.Tags);
        //image1Edit = (EditText) findViewById(R.id.image1);
        //image2Edit= (EditText) findViewById(R.id.image2);
        content1Edit = (EditText) findViewById(R.id.firstPart);
        //content2Edit = (EditText) findViewById(R.id.secondPart);
        submitBut = (Button) findViewById(R.id.ButSubmit);
        calenderImg=(ImageView) findViewById(R.id.calendImg);

        mCurrentDate=Calendar.getInstance();
        day=mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month=mCurrentDate.get(Calendar.MONTH)+1;
        year=mCurrentDate.get(Calendar.YEAR);
        date=year+"-"+month+'-'+day;
    }

    private void postNews(String url){
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Post succeed! To be reviewed by administrator!", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {Toast.makeText(getApplicationContext(), "Oops,please try again later!", Toast.LENGTH_SHORT).show();}
        });
        queue.add(stringRequest);
    }
}
