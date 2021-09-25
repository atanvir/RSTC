package com.example.rsrtcs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PassRouteSelection extends AppCompatActivity {

    Button dp,fp,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_route_selection);

        dp = findViewById(R.id.dist_wise_pass);
        fp = findViewById(R.id.fixed_pass);
      //  btnBack = findViewById(R.id.back);

//        dp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                dist_ll.setVisibility(View.VISIBLE);
////                fix_ll.setVisibility(View.INVISIBLE);
//
//                Intent intent = new Intent(PassRouteSelection.this,PassTypeActivity.class);
//                startActivity(intent);
//
//            }
//        });


        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fix_ll.setVisibility(View.VISIBLE);
//                dist_ll.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(PassRouteSelection.this,ConcessionDetail.class);
                startActivity(intent);
                finish();
            }
        });

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(PassRouteSelection.this,ImageCaptureActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }
}