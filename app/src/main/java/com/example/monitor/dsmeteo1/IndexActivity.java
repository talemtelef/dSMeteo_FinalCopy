package com.example.monitor.dsmeteo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.widget.Button;

import static com.example.monitor.dsmeteo1.R.layout.index_activity;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(index_activity);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, LoginActivity.class));
            }

        });

        Button registrar = (Button) findViewById(R.id.registrar);
        registrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, Registro.class));
            }

        });


        Button invitado = (Button) findViewById(R.id.invitado);
        invitado.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, Ubication.class).putExtra("logeado","no"));
            }

        });

    }

}