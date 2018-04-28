package be.kuleuven.softdev.haientang.newsclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EconomyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economy);
        GoToNewsOne();
    }


    public void GoToNewsOne() {
        TextView NewsOne = (TextView) findViewById(R.id.EconomyNewsOne);
        NewsOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//switch to new activity
                Intent intent = new Intent(EconomyActivity.this, NewsShowActivity.class);
                startActivity(intent);
            }
        });
    }

}
