package be.kuleuven.softdev.haientang.newsclient;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class AdvertiseActivity extends AppCompatActivity {

    private static final String UPLOAD_URL = "http://a17-sd606.studev.groept.be/imageUpload.php";  //the php url
    private static final int IMAGE_REQUEST_CODE_ONE = 3;
    private static final int IMAGE_REQUEST_CODE_TWO = 4;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Bitmap bitmap;
    private Button btnUpload;
    private Uri filePathUp;
    private Uri filePathDown;

    EditText titleEdit,tagsEdit,content1Edit,image1Edit,image2Edit;
    Spinner categorySpin;
    TextView dateTv;
    Button submitBut;
    ImageView calenderImg,image1,image2;


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

        requestStoragePermission();

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

        image1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE_ONE);
            }}
        );

        image2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), IMAGE_REQUEST_CODE_TWO);
            }}
        );

        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipart();
            }
        });


    }

    private void init(){
        titleEdit = (EditText) findViewById(R.id.title);
        categorySpin = (Spinner) findViewById(R.id.categSpinner);
        dateTv = (TextView) findViewById(R.id.date);
        tagsEdit = (EditText) findViewById(R.id.Tags);
        image1Edit = (EditText) findViewById(R.id.image1);
        image2Edit= (EditText) findViewById(R.id.image2);
        content1Edit = (EditText) findViewById(R.id.firstPart);
        //content2Edit = (EditText) findViewById(R.id.secondPart);
        submitBut = (Button) findViewById(R.id.ButSubmit);
        calenderImg=(ImageView) findViewById(R.id.calendImg);

        mCurrentDate=Calendar.getInstance();
        day=mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month=mCurrentDate.get(Calendar.MONTH)+1;
        year=mCurrentDate.get(Calendar.YEAR);
        date=year+"-"+month+'-'+day;

        image1=(ImageView) findViewById(R.id.upImage);
        image2=(ImageView) findViewById(R.id.downImage);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE_ONE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUp = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUp);
                image1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==IMAGE_REQUEST_CODE_TWO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathDown = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathDown);
                image2.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadMultipart() {
        String captionUp = image1Edit.getText().toString().trim();
        String captionDown = image2Edit.getText().toString().trim();

        //getting the actual path of the image
        String pathUp = getPath(filePathUp);
        String pathDown=getPath(filePathDown);

        //Uploading code up
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(pathUp, "image") //Adding file
                    .addParameter("caption", captionUp) //Adding text parameter to the request
                    .addParameter("position","up")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //Uploading code down
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(pathDown, "image") //Adding file
                    .addParameter("caption", captionUp) //Adding text parameter to the request
                    .addParameter("position","down")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
