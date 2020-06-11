package com.labour.lar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.labour.lar.activity.RegistActivity;
import com.labour.lar.cache.UserCache;
import com.labour.lar.module.User;

public class WelcomeActivity extends AppCompatActivity {
    private UserCache userCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        userCache = UserCache.getInstance(this);
        User user = userCache.get();
        if(user == null){
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
        } else {
            startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        }
    }
}
