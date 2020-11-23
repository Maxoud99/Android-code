package com.example.easyshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import app.AppConfig;

public class DetailsView extends AppCompatActivity implements Serializable {

    private ListView listView;
    private ArrayList<String> ProductsNames = new ArrayList<>();

    private ArrayList<String> ProductsDesc = new ArrayList<>();

    private ArrayList<String> ShopsNames = new ArrayList<>();
    private ArrayList<String> Prices = new ArrayList<>();
    private ArrayList<String> Offers = new ArrayList<>();
    private ArrayList<String> Long = new ArrayList<>();
    private ArrayList<String> Lat = new ArrayList<>();
    Location p;
    //this method is actually fetching the json string
    private void getJSON(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                System.out.println("I am in postExec");


                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
//                    System.out.println(e.toString());
                }
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {


                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    private void loadIntoListView(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);


        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);
            String s = obj.getString("ProductName");
            if (i > 0) {
                if (ProductsNames.get(0).equals(s)) {
                    //getting the name from the json object and putting it inside string array
                    ProductsNames.add(s);
                    ProductsDesc.add(obj.getString("ProductDesc"));
                    ShopsNames.add(obj.getString("ShopName"));
                    Prices.add(obj.getString("price"));
                    Offers.add(obj.getString("available_Special_offers"));
                    Long.add(obj.getString("Shoplong"));
                    Lat.add(obj.getString("ShopLat"));
                }
            } else {
                ProductsNames.add(s);
                ProductsDesc.add(obj.getString("ProductDesc"));
                ShopsNames.add(obj.getString("ShopName"));
                Prices.add(obj.getString("price"));
                Offers.add(obj.getString("available_Special_offers"));
                Long.add(obj.getString("Shoplong"));
                Lat.add(obj.getString("ShopLat"));
            }

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getJSON("http://192.168.1.2/project/details.php");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        // Setting header
        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("List Of Shops");
        // textView.setTextColor(Color.parseColor("#00000000"));

        listView = (ListView) findViewById(R.id.list);

        listView.addHeaderView(textView);
        LocationManager loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        p = loc.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               //current longitude and latitude

//        Gson gson = new Gson();
//        p= gson.fromJson(getIntent().getStringExtra("myjson"), Location.class);

//        intent.putExtra("MyClass", obj);
            //  p= (Location) getIntent().getSerializableExtra("MyClass");

//        MapsActivity mp1=new MapsActivity();
//        p= mp1.currentLocation;
//
            // For populating list data
            DetailsViewList shopsList = new DetailsViewList(this, ProductsNames, ProductsDesc, ShopsNames, Prices, Offers, Long, Lat, p);

            listView.setAdapter(shopsList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Toast.makeText(getApplicationContext(), "You Selected " + ShopsNames.get(position - 1) + " as Shop", Toast.LENGTH_SHORT).show();
                }
            });
        }
//    protected void onStart() {
//        super.onStart();
////        MapsActivity mp1=new MapsActivity();
//       p= mp1.fetchLocation();
//    }
    }
