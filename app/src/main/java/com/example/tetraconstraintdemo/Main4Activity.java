package com.example.tetraconstraintdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {
     TextView location11;
    TextView location12;
    TextView location13;
    TextView route1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String route = bundle.getString("Route");
        String location1 = bundle.getString("Location1");
        String location2 = bundle.getString("Location2");
        String  location3 = bundle.getString("Location3");
        location11 = findViewById(R.id.location1);
        location12 = findViewById(R.id.location2);
        location13 = findViewById(R.id.location3);
        route1 = findViewById(R.id.route1);
        location11.setText(location1);
        location12.setText(location2);
        location13.setText(location3);
        route1.setText(route);
    }
}
