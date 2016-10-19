package com.owen.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.owen.tipview.TipView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOne = (Button) findViewById(R.id.button1);
        Button btnTwo = (Button) findViewById(R.id.button2);

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_one");
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: btn_two");
            }
        });


        final TipView tipView = new TipView(this);
        tipView.setClickable(true);  // 这句话可以拦截点击事件，这样 contentView 里的 Button 就不能点击了
        tipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) getWindow().getDecorView()).removeView(tipView);
            }
        });
        ((ViewGroup) getWindow().getDecorView()).addView(tipView);
    }
}
