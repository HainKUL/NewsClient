package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class RegisterActivity extends AppCompatActivity {//maybe add extra features, such as sending verification code by email

    //declare globle variables here
    EditText firstNameTxt,surnameTxt,emailTxt,passwd,rePasswd;
    Button submitBut;
    String URL;
    //String URL="http://api.a17-sd606.studev.groept.be/database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //1. get references to the buttons and textbox
        firstNameTxt = (EditText) findViewById(R.id.firstName);
        surnameTxt = (EditText) findViewById(R.id.surname);
        emailTxt = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        rePasswd = (EditText) findViewById(R.id.repeatPasswd);
        submitBut = (Button) findViewById(R.id.butSubmit);

        //button on click
        submitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //request method--to send data to database
                Requests("http://api.a17-sd606.studev.groept.be/database/");
            }
        });
    }

    public void Requests(String url)
    {
        URL=url+firstNameTxt.getText()+"/"+surnameTxt.getText()+"/"+emailTxt.getText()+"/"+passwd.getText();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        if(!firstNameTxt.getText().toString().isEmpty()
                &&!surnameTxt.getText().toString().isEmpty()
                &&!emailTxt.getText().toString().isEmpty()
                &&!passwd.getText().toString().isEmpty()
                &&!rePasswd.getText().toString().isEmpty())
        {//all fields are filled in, send to database and later on switch to login dialog
            Toast.makeText(this, "Registration succeed!", Toast.LENGTH_SHORT).show();
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, "Oops,please try again later!", Toast.LENGTH_SHORT).show();
                }
            }
            );
            queue.add(stringRequest);// Add the request to the RequestQueue.

            switchToLogin();//new method to switch to login dialog

        }else {
            Toast.makeText(this, "Please fill in any empty fields...", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchToLogin()
    {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);//create alert dialog
        View mView=getLayoutInflater().inflate(R.layout.dialog_login,null);//referencing the alert dialog to the login dialog
        //define the view inside the login layout
        final EditText mEmail=(EditText) mView.findViewById(R.id.etEmail);
        final EditText mpasswd=(EditText) mView.findViewById(R.id.etPasswd);
        Button mLogin=(Button) mView.findViewById(R.id.butLogin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//here to switch to another activity or not
                if(!mEmail.getText().toString().isEmpty()&&!mpasswd.getText().toString().isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegisterActivity.this,NewsOverviewActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill in any empty fields...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }
}
