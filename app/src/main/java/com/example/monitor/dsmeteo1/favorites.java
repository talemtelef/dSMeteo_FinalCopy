package com.example.monitor.dsmeteo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class favorites extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.NavBot);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public  boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favoritosid:
                        Intent intent = new Intent(favorites.this, favorites.class);
                        startActivity(intent);
                        break;
                    case R.id.ubicacionid:
                        Intent intent2 = new Intent(favorites.this, Ubication.class);
                        startActivity(intent2);
                        break;
                    case R.id.buscarid:
                        Intent intent3 = new Intent(favorites.this, Select.class);
                        startActivity(intent3);
                        break;
                    case R.id.powerid:
                        Intent intent4 = new Intent(favorites.this, LoginActivity.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });



    }
}
