package com.example.monitor.dsmeteo1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Registro extends AppCompatActivity {

    private static final String url = "jdbc:mysql://192.168.1.102:3306/ciudades";
    private static final String user = "root";
    private static final String pass = "monitor2016";
    private EditText Nombre, Password, EMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Nombre = (EditText) findViewById(R.id.Nombre);
        Password = (EditText) findViewById(R.id.Password);
        EMail = (EditText) findViewById(R.id.EMail);
        Button ingress = (Button) findViewById(R.id.Registrar2);
        ingress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String fNombre= Nombre.getText().toString();
        String fPassword= Password.getText().toString();
        String fEMail=EMail.getText().toString();
        Connection con = null;
        Statement st;
        private String mensaje;


        @Override
        protected Void doInBackground(Void... params) {

            try{

                Class.forName("com.mysql.jdbc.Driver");
                Log.v("Mysql","carga correcta del driver");
                con = DriverManager.getConnection(url, user, pass);

                if (fNombre.length() == 0 || fPassword.length()==0 || fEMail.length() ==0){
                    mensaje = "Campo vacío";
                    return null;
                }

                st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

                int resultado=st.executeUpdate("insert into usuario (Nombre, Contraseña, Email) values ('"+fNombre+"','"+fPassword+"','"+fEMail+"')");

                if (resultado>0) {
                    mensaje = "Ingreso correcto";
                }


            } catch (SQLException e) {
                if(e.getErrorCode() == 1062){
                    mensaje = "Email repetido";
                }
            } catch (ClassNotFoundException e) {
//                Intent myIntent = new Intent(Registro.this, Ubication.class);
//                startActivity(myIntent);
                e.printStackTrace();
            }finally {
                if(con != null){
                    try {
                        st.close();
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
            Password.setText(fEMail);
            EMail.setText(fPassword);

            Alerta(mensaje);
            borrarDatos();

            if (mensaje.equals("Ingreso correcto")){
                startActivity(new Intent(Registro.this, Ubication.class).putExtra("logeado","si"));
            }

            super.onPostExecute(result);
        }
    }

    private void borrarDatos(){
        Nombre.setText("");
        Password.setText("");
        EMail.setText("");

    }
    public void Alerta(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.show();
    }

}
