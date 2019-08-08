package com.example.tetraconstraintdemo.screens;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tetraconstraintdemo.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InventorySavingActivity extends AppCompatActivity {
    TextView location11;
    TextView location12;
    TextView location13;
    TextView route1;

    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_saving);

        Bundle bundle = getIntent().getExtras();


        final String route = bundle.getString("Route");
        final String location1 = bundle.getString("Location1");
        final String location2 = bundle.getString("Location2");
        final String location3 = bundle.getString("Location3");
        location11 = findViewById(R.id.location1);
        location12 = findViewById(R.id.location2);
        location13 = findViewById(R.id.location3);
        route1 = findViewById(R.id.route1);
        Button saveInventory = findViewById(R.id.saveInventory);
        location11.setText(location1);
        location12.setText(location2);
        location13.setText(location3);
        route1.setText(route);

        loadingDialog = new ProgressDialog(this);


        saveInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingDialog.setTitle("Saving to inventory");
                loadingDialog.setCancelable(false);
                loadingDialog.show();

                Map<String, String> locationData = new HashMap<>();
                locationData.put("Location1",location1);
                locationData.put("Location2",location2);
                locationData.put("Location3",location3);
                locationData.put("route1",route);


                FirebaseDatabase.getInstance().getReference().child("saved_inventory_data").setValue(locationData, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        loadingDialog.dismiss();

                        if(databaseError == null){
                            new AlertDialog.Builder(InventorySavingActivity.this)
                                    .setTitle("Saving to inventory success")
                                    .setMessage("Successfully saved to database")
                                    .setPositiveButton("DISMISS", null)
                                    .show();
                        }else {
                            new AlertDialog.Builder(InventorySavingActivity.this)
                                    .setTitle("Saving to inventory failed")
                                    .setMessage(databaseError.getMessage())
                                    .setPositiveButton("DISMISS", null)
                                    .show();
                        }
                    }
                });
            }
        });
    }
}
