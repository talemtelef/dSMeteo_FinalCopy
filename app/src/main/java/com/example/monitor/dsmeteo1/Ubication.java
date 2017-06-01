package com.example.monitor.dsmeteo1;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class Ubication extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Ubication";
    private SectionsPageAdapter mSectionsPageAdapter;

    //CONEXION BASE DATOS
    private static final String url = "jdbc:mysql://192.168.1.102:3306/ciudades";
    private static final String user = "root";
    private static final String pass = "monitor2016";

    private static String localidad = "";


    private Spinner spProvincias, spLocalidades;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubication);
        this.spProvincias = (Spinner) findViewById(R.id.sp_provincia);
        this.spLocalidades = (Spinner) findViewById(R.id.sp_localidad);
        this.spLocalidades.setEnabled(false);
        loadSpinnerProvincias();


        //menu
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.NavBot);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ubicacionid:
                        Intent intent2 = new Intent(Ubication.this, Ubication.class);
                        startActivity(intent2);
                        break;

                    case R.id.powerid:
                        Intent intent4 = new Intent(Ubication.this, LoginActivity.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });


    }

    private void loadSpinnerProvincias() {

        // Create an ArrayAdapter using the string array and a default spinner
        // layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.provincias, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.spProvincias.setAdapter(adapter);

        // This activity implements the AdapterView.OnItemSelectedListener
        this.spProvincias.setOnItemSelectedListener(this);
        this.spLocalidades.setOnItemSelectedListener(this);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

        switch (parent.getId()) {
            case R.id.sp_provincia:

                // Retrieves an array
                TypedArray arrayLocalidades = getResources().obtainTypedArray(
                        R.array.array_provincia_a_localidades);
                if(pos>0) {
                    this.spLocalidades.setEnabled(true);
                    CharSequence[] localidades = arrayLocalidades.getTextArray(pos);
                    arrayLocalidades.recycle();

                    // Create an ArrayAdapter using the string array and a default
                    // spinner layout
                    ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                            this, android.R.layout.simple_spinner_item,
                            android.R.id.text1, localidades);

                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // Apply the adapter to the spinner
                    this.spLocalidades.setAdapter(adapter);
                }else{
                    this.spLocalidades.setEnabled(false);
                    this.spLocalidades.setAdapter(null);
                }

                break;

            case R.id.sp_localidad:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Callback method to be invoked when the selection disappears from this
        // view. The selection can disappear for instance when touch is
        // activated or when the adapter becomes empty.
    }


    /**
     * Shows the selected strings from spinners.
     *
     * @param v
     *            The view that was clicked.
     */


    public void showLocalidadSelected(View v) throws ExecutionException, InterruptedException {

//        Button boton_buscar = (Button)findViewById(R.id.boton_buscar_usuario);
//
//        Intent x = getIntent();
//        Bundle b = x.getExtras();
//        final String logeado = b.getString("logeado");

        localidad = spLocalidades.getSelectedItem().toString();


        final MyTask devolver = new MyTask();
        devolver.execute().get();

//        if (logeado.equals("si")) {
//            boton_buscar.setOnClickListener(new View.OnClickListener() {
//                //
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(Ubication.this, URegistradoActivity.class).putExtra("localidad", (CharSequence) devolver));
//                }
//
//            });
//        } else if (logeado.equals("no")){
//            boton_buscar.setOnClickListener(new View.OnClickListener() {
//                //
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(Ubication.this, InvitadoActivity.class).putExtra("localidad", (CharSequence) devolver));
//                }
//
//            });
//        }

        Toast.makeText(
                getApplicationContext(),
                getString(R.string.message, spLocalidades.getSelectedItem()
                        .toString(), spProvincias.getSelectedItem().toString()),
                Toast.LENGTH_LONG).show();

    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String codigo = "";

            try {


                Class.forName("com.mysql.jdbc.Driver");
                Log.v("Mysql","carga correcta del driver");
                Connection con = DriverManager.getConnection(url, user, pass);

                Statement st = con.createStatement();
                String sql =("SELECT CONCAT(CPRO, CMUN) as codigo FROM ciudad inner join provincias ON ciudad.CPRO = provincias.id_pro where NOMBRE= '"+localidad+"';");

                final ResultSet rs = st.executeQuery(sql);
                rs.next();
                codigo = rs.getString(1);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();

            }

            return codigo;
        }

        @Override
        protected void onPostExecute(final String result){

            //super.onPostExecute(result);

            Intent x = getIntent();
            Bundle b = x.getExtras();
            final String logeado = b.getString("logeado");


                    if (logeado.equals("si")) {
                        startActivity(new Intent(Ubication.this, URegistradoActivity.class).putExtra("localidad", result));
                    } else if (logeado.equals("no")){
                        startActivity(new Intent(Ubication.this, InvitadoActivity.class).putExtra("localidad",result));
                    }


        }
    }


}