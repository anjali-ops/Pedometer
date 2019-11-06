package com.example.pedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    private Button github_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        github_button = (Button) findViewById(R.id.github_button);

        github_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedButtons("http://github.com/sumitmaurya96");
            }
        });
    }

    public void clickedButtons(String Url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Url));
        startActivity(intent);
    }
}
