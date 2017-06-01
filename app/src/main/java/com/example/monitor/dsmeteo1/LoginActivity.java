package com.example.monitor.dsmeteo1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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



public class LoginActivity extends AppCompatActivity {
    private static final String url = "jdbc:mysql://192.168.1.102:3306/ciudades";
    private static final String user = "root";
    private static final String pass = "monitor2016";

    private EditText Password, EMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Password = (EditText) findViewById(R.id.Password);
        EMail = (EditText) findViewById(R.id.EMail);
        Button ingress = (Button) findViewById(R.id.ingresar);

        ingress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyTask().execute();
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void>{
        private String mensaje;
        private String fEmail="", fPassword="";

        String dPassword = Password.getText().toString();
        String dEMail = EMail.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Class.forName("com.mysql.jdbc.Driver");
                Log.v("Mysql","carga correcta del driver");
                Connection con = DriverManager.getConnection(url, user, pass);

                Statement st = con.createStatement();
                String sql = ("SELECT * FROM ciudades.usuario where Email = '"+dEMail+"' AND Contraseña = '"+dPassword+"' ; ");

                final ResultSet rs = st.executeQuery(sql);
                rs.next();
                fPassword = rs.getString(3);
                fEmail = rs.getString(4);

                if (dPassword.equals(fPassword) && dEMail.equals(fEmail)) {
                    mensaje = "Login Correcto";
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
                mensaje = "Login Incorrecto";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            Password.setText(fEmail);
            EMail.setText(fPassword);

            Alerta(mensaje);
            borrarDatos();

            if (mensaje.equals("Login Correcto")){
                startActivity(new Intent(LoginActivity.this, Ubication.class).putExtra("logeado","si"));
            }

            super.onPostExecute(result);
        }
    }

    private void borrarDatos(){

        Password.setText("");
        EMail.setText("");

    }

    public void Alerta(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.show();
    }

}