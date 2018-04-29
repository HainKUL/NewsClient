package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //declare globle variables
    Button loginBut,registerBut,guestBut;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. get references to the buttons
        loginBut=(Button) findViewById(R.id.butLogin);
        registerBut=(Button) findViewById(R.id.butRegister);
        guestBut=(Button) findViewById(R.id.butGuest);

        //2. set click listeners to the buttons
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//here to create other dialog
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);//create alert dialog
                //create a new view,login view(mView) instead of main_activity view
                View mView=getLayoutInflater().inflate(R.layout.dialog_login,null);//referencing the alert dialog to the login dialog
                //define the view inside the login layout
                //EditText mEmail=(EditText) findViewById(R.id.etEmail);----email not in the main activity
                final EditText mEmail=(EditText) mView.findViewById(R.id.etEmail);
                final EditText mpasswd=(EditText) mView.findViewById(R.id.etPasswd);
                Button mLogin=(Button) mView.findViewById(R.id.butLogin);

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//here to switch to another activity or not
                        if(!mEmail.getText().toString().isEmpty()&&!mpasswd.getText().toString().isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,NewsOverviewActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this, "Please fill in any empty fields...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });

        registerBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        guestBut.setOnClickListener(new View.OnClickListener() {//need to pass information telling the database the guest
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NewsOverviewActivity.class);
                startActivity(intent);
            }
        });

    }

}
