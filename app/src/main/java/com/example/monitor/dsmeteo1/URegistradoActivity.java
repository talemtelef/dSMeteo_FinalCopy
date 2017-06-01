package com.example.monitor.dsmeteo1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class URegistradoActivity extends AppCompatActivity {

    TextView Ciudad;

    TextView MinMax;
    TextView MinMaxMañana;
    TextView MinMaxPasadoMañana;

    ImageView imageViewIconos;
    ImageView imageViewIconos_Mañana;
    ImageView imageViewIconosPasadoMañana;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrado_activity_main);

        Ciudad = (TextView) this.findViewById(R.id.Ciudad);
        MinMax = (TextView) this.findViewById(R.id.MinMax);
        MinMaxMañana = (TextView) this.findViewById(R.id.MinMax_Mañana) ;
        MinMaxPasadoMañana = (TextView) this.findViewById(R.id.MinMaxPasado) ;

        imageViewIconos=(ImageView)this.findViewById(R.id.imageViewIconos);
        imageViewIconos_Mañana=(ImageView)this.findViewById(R.id.imageViewIconos_Mañana);
        imageViewIconosPasadoMañana=(ImageView)this.findViewById(R.id.imageViewIconosPasadoMañana);

        cargar();

    }




    public void cargar(){

        Intent y = getIntent();
        Bundle b = y.getExtras();
        final String codigo_resp = b.getString("localidad");

        URegistradoActivity.Ciudad com=new Ciudad();

        Temperatura temperatura_asignacion = new Temperatura();
        TemperaturaMañana temperaturaMañana=new TemperaturaMañana();
        TemperaturaPasadoMañana temperaturaPasadoMañana = new TemperaturaPasadoMañana();

        Precipitaciones precipitaciones=new Precipitaciones();
        PrecipitacionesMañana precipitacionesMañana=new PrecipitacionesMañana();
        PrecipitacionesPasadoMañana precipitacionesPasadoMañana=new PrecipitacionesPasadoMañana();

        //Ciudad Seleccionada en la database.
        String ciudad_database = "http://www.aemet.es/xml/municipios/localidad_"+codigo_resp+".xml";

        com.execute(ciudad_database);

        temperatura_asignacion.execute(ciudad_database);
        temperaturaMañana.execute(ciudad_database);
        temperaturaPasadoMañana.execute(ciudad_database);

        precipitaciones.execute(ciudad_database);
        precipitacionesMañana.execute(ciudad_database);
        precipitacionesPasadoMañana.execute(ciudad_database);


    }


    private class Ciudad extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String ciudad="";

            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.
                NodeList listanombre = doc.getElementsByTagName("nombre");
                NodeList listaPrecip = doc.getElementsByTagName("prob_precipitacion");

                NodeList listaTempMax = doc.getElementsByTagName("maxima");
                NodeList listaTempMin = doc.getElementsByTagName("minima");

                NodeList listaDireccViento = doc.getElementsByTagName("direccion");
                NodeList listaVientoVelocidad = doc.getElementsByTagName("velocidad");


                //MUESTRA DE LA LOCALIDAD SELECCIONADA:
                ciudad += listanombre.item(0).getTextContent() + "\n";

                /*

                //CALCULAMOS LA MEDIA DE LAS PROBABILIDADES DE PRECIPITACIÓN, ASEGURANDONOS ASÍ QUE NO COJEMOS VALORES NULOS DE LOS DISTINTOS PERIODOS DE PROBABILIDAD.
                double media_prob_precipitacion = 0;
                for (int i = 0; i <= 6; i++) {
                    String valor = listaPrecip.item(i).getTextContent();
                    if (valor == null || valor.equals("")) {
                        valor = "0";
                    }
                    media_prob_precipitacion += Double.parseDouble(valor);
                }
                media_prob_precipitacion = media_prob_precipitacion / 7;

                //ASIGNAMOS LA MEDIA DE LAS PROB. DE PRECIPITACION AL TEXTVIEW.
                cadena += "-Precipitaciones:" +
                        (long)(media_prob_precipitacion > 0 ? media_prob_precipitacion + 0.01 : media_prob_precipitacion - 0.01) + "% " +"\n";


                //Asignamos el viento:

                cadena += "-Vientos:" + "\n" +
                        "Dirección:  " +
                        listaDireccViento.item(0).getTextContent() + "\n"+
                        "Velocidad:  " +
                        listaVientoVelocidad.item(0).getTextContent()+"km/h" + "\n" ;
                */


            }
            catch(Exception ex){
                ex.printStackTrace();
            }finally {
                return ciudad;
            }


        }

        @Override
        protected void onPostExecute(String result){

            Ciudad.setText(result);


        }

    }

//Temperaturas del API:
    private class Temperatura extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String temperatura = "";
            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.


                NodeList listaTempMax = doc.getElementsByTagName("maxima");
                NodeList listaTempMin = doc.getElementsByTagName("minima");

                //CALCULAMOS TEMPERATURA MEDIA
                double media_temp ;
                double maximo = 0;
                double minimo = 0;

                String valormax = listaTempMax.item(0).getTextContent();
                if (valormax == null || valormax.equals("")) {
                    valormax = "0";
                }
                maximo += Double.parseDouble(valormax);
                String valormin = listaTempMin.item(0).getTextContent();
                if (valormin == null || valormin.equals("")) {
                    valormin = "0";
                }
                minimo += Double.parseDouble(valormin);

                media_temp = (maximo+minimo) / 2;


                //ASIGNAMOS LA TEMPERATURA MEDIA, MAXIMA Y MINIMA AL TEXTVIEW.
                temperatura += "  "+media_temp +"ºC"+ "\n" +
                        "Max: "+ listaTempMax.item(0).getTextContent() + "ºC" + "\n"+
                        "Min: " +   listaTempMin.item(0).getTextContent() + "ºC" + "\n";

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                return temperatura;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            MinMax.setText(result);

        }

    }

    private class TemperaturaMañana extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String temperatura = "";
            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.


                NodeList listaTempMax = doc.getElementsByTagName("maxima");
                NodeList listaTempMin = doc.getElementsByTagName("minima");

                //CALCULAMOS TEMPERATURA MEDIA
                double media_temp ;
                double maximo = 0;
                double minimo = 0;

                String valormax = listaTempMax.item(3).getTextContent();
                if (valormax == null || valormax.equals("")) {
                    valormax = "0";
                }
                maximo += Double.parseDouble(valormax);
                String valormin = listaTempMin.item(3).getTextContent();
                if (valormin == null || valormin.equals("")) {
                    valormin = "0";
                }
                minimo += Double.parseDouble(valormin);

                media_temp = (maximo+minimo) / 2;


                //ASIGNAMOS LA TEMPERATURA MEDIA, MAXIMA Y MINIMA AL TEXTVIEW.
                temperatura += "  "+media_temp +"ºC"+ "\n" +
                        "Max: "+ listaTempMax.item(3).getTextContent() + "ºC" + "\n"+
                        "Min: " +   listaTempMin.item(3).getTextContent() + "ºC" + "\n";

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                return temperatura;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            MinMaxMañana.setText(result);

        }

    }

    private class TemperaturaPasadoMañana extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String temperatura = "";
            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.


                NodeList listaTempMax = doc.getElementsByTagName("maxima");
                NodeList listaTempMin = doc.getElementsByTagName("minima");

                //CALCULAMOS TEMPERATURA MEDIA
                double media_temp ;
                double maximo = 0;
                double minimo = 0;

                String valormax = listaTempMax.item(6).getTextContent();
                if (valormax == null || valormax.equals("")) {
                    valormax = "0";
                }
                maximo += Double.parseDouble(valormax);
                String valormin = listaTempMin.item(6).getTextContent();
                if (valormin == null || valormin.equals("")) {
                    valormin = "0";
                }
                minimo += Double.parseDouble(valormin);

                media_temp = (maximo+minimo) / 2;


                //ASIGNAMOS LA TEMPERATURA MEDIA, MAXIMA Y MINIMA AL TEXTVIEW.
                temperatura += "  "+media_temp +"ºC"+ "\n" +
                        "Max: "+ listaTempMax.item(6).getTextContent() + "ºC" + "\n"+
                        "Min: " +   listaTempMin.item(6).getTextContent() + "ºC" + "\n";

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                return temperatura;
            }


        }

        @Override
        protected void onPostExecute(String result) {

            MinMaxPasadoMañana.setText(result);

        }

    }


    private class Precipitaciones extends AsyncTask<String, Void, Drawable> {


        @Override
        protected Drawable doInBackground(String... params) {

            double media_prob_precipitacion = 0;
            String valor_icono="";
            Drawable valor_icono_tipo_drawable=getDrawable(R.drawable.icono0);

            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.

                NodeList listaPrecip = doc.getElementsByTagName("prob_precipitacion");

                //CALCULAMOS LA MEDIA DE LAS PROBABILIDADES DE PRECIPITACIÓN, ASEGURANDONOS ASÍ QUE NO COJEMOS VALORES NULOS DE LOS DISTINTOS PERIODOS DE PROBABILIDAD.

                for (int i = 0; i <= 6; i++) {
                    String valor = listaPrecip.item(i).getTextContent();
                    if (valor == null || valor.equals("")) {
                        valor = "0";
                    }
                    media_prob_precipitacion += Double.parseDouble(valor);
                }
                media_prob_precipitacion = media_prob_precipitacion / 7;
                int media_entera = (int) media_prob_precipitacion;

                //HACEMOS EL FILTRO PARA LAS IMÁGENES.

                if (media_prob_precipitacion<20){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono0);

                }
                else if (media_prob_precipitacion<40 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono1);
                }
                else if (media_prob_precipitacion<60 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono2);
                }
                else if (media_prob_precipitacion<80 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono3);
                }
                else if (media_prob_precipitacion<100 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono4);
                }


            } catch (Exception ex) {
                ex.printStackTrace();

            } return valor_icono_tipo_drawable;

        }

        @Override
        protected void onPostExecute(Drawable result) {

            imageViewIconos.setImageDrawable(result);

        }

    }


    private class PrecipitacionesMañana extends AsyncTask<String, Void, Drawable> {


        @Override
        protected Drawable doInBackground(String... params) {

            double media_prob_precipitacion = 0;

            Drawable valor_icono_tipo_drawable=getDrawable(R.drawable.icono0);

            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.

                NodeList listaPrecip = doc.getElementsByTagName("prob_precipitacion");

                //CALCULAMOS LA MEDIA DE LAS PROBABILIDADES DE PRECIPITACIÓN, ASEGURANDONOS ASÍ QUE NO COJEMOS VALORES NULOS DE LOS DISTINTOS PERIODOS DE PROBABILIDAD.

                for (int i = 7; i <= 13; i++) {
                    String valor = listaPrecip.item(i).getTextContent();
                    if (valor == null || valor.equals("")) {
                        valor = "0";
                    }
                    media_prob_precipitacion += Double.parseDouble(valor);
                }

                media_prob_precipitacion = media_prob_precipitacion / 7;


                //HACEMOS EL FILTRO PARA LAS IMÁGENES.

                if (media_prob_precipitacion<20){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono0);

                }
                else if (media_prob_precipitacion<40 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono1);
                }
                else if (media_prob_precipitacion<60 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono2);
                }
                else if (media_prob_precipitacion<80 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono3);
                }
                else if (media_prob_precipitacion<100 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono4);
                }


            } catch (Exception ex) {
                ex.printStackTrace();

            } return valor_icono_tipo_drawable;

        }

        @Override
        protected void onPostExecute(Drawable result) {

            imageViewIconos_Mañana.setImageDrawable(result);

        }

    }


    private class PrecipitacionesPasadoMañana extends AsyncTask<String, Void, Drawable> {


        @Override
        protected Drawable doInBackground(String... params) {

            double media_prob_precipitacion = 0;
            Drawable valor_icono_tipo_drawable=getDrawable(R.drawable.icono0);

            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();

                InputStream is = con.getInputStream();
                Document doc;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                doc = builder.parse(is);

                //RECUPERAMOS LOS ELEMENTOS DEL API http://www.aemet.es/xml/municipios/localidad_09059.xml , EN ESTE CASO PARA BURGOS.

                NodeList listaPrecip = doc.getElementsByTagName("prob_precipitacion");

                //CALCULAMOS LA MEDIA DE LAS PROBABILIDADES DE PRECIPITACIÓN, ASEGURANDONOS ASÍ QUE NO COJEMOS VALORES NULOS DE LOS DISTINTOS PERIODOS DE PROBABILIDAD.

                for (int i = 14; i <= 16; i++) {
                    String valor = listaPrecip.item(i).getTextContent();
                    if (valor == null || valor.equals("")) {
                        valor = "0";
                    }
                    media_prob_precipitacion += Double.parseDouble(valor);
                }
                media_prob_precipitacion = media_prob_precipitacion / 3;


                //HACEMOS EL FILTRO PARA LAS IMÁGENES.

                if (media_prob_precipitacion<20){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono0);

                }
                else if (media_prob_precipitacion<40 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono1);
                }
                else if (media_prob_precipitacion<60 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono2);
                }
                else if (media_prob_precipitacion<80 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono3);
                }
                else if (media_prob_precipitacion<100 ){
                    valor_icono_tipo_drawable=getDrawable(R.drawable.icono4);
                }


            } catch (Exception ex) {
                ex.printStackTrace();

            } return valor_icono_tipo_drawable;

        }

        @Override
        protected void onPostExecute(Drawable result) {

            imageViewIconosPasadoMañana.setImageDrawable(result);

        }

    }

}