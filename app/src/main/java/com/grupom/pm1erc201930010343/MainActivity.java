package com.grupom.pm1erc201930010343;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.grupom.pm1erc201930010343.Config.RestApiMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CaptureBitmapView captureBitmapView;
    EditText txtNombre, txtTelefono, txtLat, txtLon;
    Button btnSave,btnListarContactos;
    Bitmap imagen;

    public static String latitud = "";
    public static String longitud = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
        }

        btnSave = (Button) findViewById(R.id.btnGuardar);
        btnListarContactos = (Button) findViewById(R.id.btnContactos);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtLat = (EditText) findViewById(R.id.txtLat);
        txtLon = (EditText) findViewById(R.id.txtLon);

        LinearLayout mContent = (LinearLayout) findViewById(R.id.signLayout);
        captureBitmapView = new CaptureBitmapView(this, null);
        mContent.addView(captureBitmapView, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);

        btnListarContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ListarContactos.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    validarDatos();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Debe Firmar",Toast.LENGTH_LONG).show();
                }
            }
        });

        //valida si tiene los permisos de ser asi manda a llamar el metodo locationStart()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
    }

    private void validarDatos() {
        if(txtNombre.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (txtTelefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (txtLat.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un latitud" ,Toast.LENGTH_LONG).show();
        }else if (txtLon.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un longitud" ,Toast.LENGTH_LONG).show();
        }
        else {
            crearUsuario();
        }
    }

/*
  private Object Firma (){
        try {
            Sing(captureBitmapView.getBitmap());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            limpiar();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error a guardar Datos ",Toast.LENGTH_LONG).show();
        }
      return null;
  }
*/

    private String Sing() {

        try {
            Bitmap sign = captureBitmapView.getBitmap();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            MediaStore.Images.Media.insertImage(getContentResolver(), imagen, imageFileName , "yourDescription");

            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            sign.compress(Bitmap.CompressFormat.JPEG, 70, ba);
            byte[] arrayFoto = ba.toByteArray();
            String encode = Base64.encodeToString(arrayFoto, Base64.DEFAULT);

            return encode;
        }catch (Exception ex)
        {
            ex.toString();
        }
        return "";
    }



    private void crearUsuario() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();

        //String firmaString = GetStringImage();

        //setear los parametros mediante put
        parametros.put("nombre", txtNombre.getText().toString());
        parametros.put("telefono", txtTelefono.getText().toString());
        parametros.put("latitud", txtLat.getText().toString());
        parametros.put("longitud", txtLon.getText().toString());
        //parametros.put("foto", firmaString);
        parametros.put("firma", String.valueOf(Sing()));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointCreateUsuario,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "String Response " + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

        limpiar();
    }

    private void limpiar() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtLon.setText("");
        txtLat.setText("");
        captureBitmapView.ClearCanvas();
    }

    //Metodo latitud y longitud//
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            //SE VA A LA CONFIGURACION DEL SISTEMA PARA QUE ACTIVE EL GPS UNA VEZ QUE INICIA LA APLICACION
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }


    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();


            MainActivity.setLatitud(loc.getLatitude()+"");
            MainActivity.setLongitud(loc.getLongitude()+"");
            txtLat.setText(loc.getLatitude()+"");
            txtLon.setText(loc.getLongitude()+"");
            this.mainActivity.setLocation(loc);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    ///

    public static void setLatitud(String latitud) {
        MainActivity.latitud = latitud;
    }

    public static void setLongitud(String longitud) {
        MainActivity.longitud = longitud;
    }
}

