package com.example.easyshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import activity.LoginActivity;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class DetailsView extends AppCompatActivity implements Serializable {
    private static final String TAG = DetailsView.class.getSimpleName();


    private ListView listView;
    private ArrayList<String> ProductsNames = new ArrayList<>();
    private SQLiteHandler db;
    private ArrayList<String> ProductsDesc = new ArrayList<>();
    private SessionManager session;
    private ArrayList<String> ShopsNames = new ArrayList<>();
    private ArrayList<String> Prices = new ArrayList<>();
    private ArrayList<String> Offers = new ArrayList<>();
    private ArrayList<String> Long = new ArrayList<>();
    private ArrayList<String> Lat = new ArrayList<>();
    private ArrayList<String> dist = new ArrayList<>();
    private Button btnPrice;
    private Button btndistance;
    private Intent intent=new Intent();
    private Intent intent2=new Intent();
    private ArrayList<String> id = new ArrayList<>();
    Location p;


    public static String distance(double lat1,double lat2, double lon1, double lon2)
    {

        // math تحتوي وحدة
        // toRadians على الدالة
        // والتي تحوّل الزوايا من نظام الدرجات إلى نظام نصف القطر
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // صيغة هافرساين
        double dlon = Math.sqrt((lon2 - lon1)*(lon2 - lon1));
        double dlat = Math.sqrt((lat2 - lat1)*(lat2 - lat1));
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // نصف قطر الكرة الأرضية بوحدات الكيلومتر
        double r = 6371;

        // حساب النتيجة
        return((int)(c * r)+"");
    }

    private void loadIntoListView(JSONArray jsonArray) throws JSONException {

        //creating a json array from the json string
//        JSONArray jsonArray = new JSONArray(json);


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
                    id.add(obj.getString("id"));
                    dist.add(distance(Double.parseDouble(Lat.get(i)),p.getLatitude(),Double.parseDouble(Long.get(i)),p.getLongitude()));
                }
            } else {
                ProductsNames.add(s);
                ProductsDesc.add(obj.getString("ProductDesc"));
                ShopsNames.add(obj.getString("ShopName"));
                Prices.add(obj.getString("price"));
                Offers.add(obj.getString("available_Special_offers"));
                Long.add(obj.getString("Shoplong"));
                Lat.add(obj.getString("ShopLat"));
                id.add(obj.getString("id"));
                dist.add(distance(Double.parseDouble(Lat.get(i)),p.getLatitude(),Double.parseDouble(Long.get(i)),p.getLongitude()));
            }

        }

        Toast.makeText(getApplicationContext(), ProductsNames.toString()+ShopsNames.toString(), Toast.LENGTH_LONG).show();
    }

    private void checkLogin(final Intent intent) {



        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://192.168.1.3/project/details.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();

                try {
                    JSONArray jObj = new JSONArray(response);

                    loadIntoListView(jObj);
                    Log.d(TAG, "Login ana asfa : "+jObj.toString()+"\n"+ response);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("killId",intent.getStringExtra("MyValue"));
//                params.put("password", password);

                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

        // Adding request to request queue
    }

    private void save(final Intent intent2, final String linkmaster_id) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://192.168.1.3/project/save.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
//                hideDialog();

                try {
                    JSONArray jObj = new JSONArray(response);

                    loadIntoListView(jObj);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters
                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_idd",);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DetailsView.this);
                String name = preferences.getString("user_idd", "");
                if(!name.equalsIgnoreCase(""))
                {
                    Log.d(TAG,"RRRRRRRRRRRRRRRRRRRRR+ ___"+name+"888888888888888");

                }
                params.put("user_idd",name);
                params.put("linkmaster_id",linkmaster_id);

                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getJSON("http://192.168.1.3/project/details.php");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        // Setting header
        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("List Of Shops");

        intent = getIntent();
        checkLogin(intent);


        listView = (ListView) findViewById(R.id.list);

        listView.addHeaderView(textView);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));

        btnPrice = (Button) findViewById(R.id.btnfilterPrice);
        btndistance = (Button) findViewById(R.id.btnfilterdistance);

        LocationManager loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        p = loc.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // For populating list data
        final DetailsViewList shopsList = new DetailsViewList(this, ProductsNames, ProductsDesc, ShopsNames, Prices, Offers,dist,Lat,Long);

        listView.setAdapter(shopsList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "You Selected " + ShopsNames.get(position - 1) + " as Shop Navigation", Toast.LENGTH_SHORT).show();
                DisplayTrack(getAddress(p.getLatitude(),p.getLongitude()),getAddress(Double.parseDouble(Lat.get(position - 1)),Double.parseDouble(Long.get(position - 1))));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                intent2 = getIntent();
                save(intent2, id.get(position - 1));
                Log.d(TAG, "ya details :");
                return true;
            }
        });
        btnPrice.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Sort p1 = new Sort(ProductsNames, ProductsDesc, ShopsNames, Prices, Offers, dist, Lat, Long,id);
                p1.sortPrice();
                ProductsNames = p1.getProductsNames();
                ProductsDesc = p1.getProductsDesc();
                ShopsNames = p1.getShopsNames();
                Prices = p1.getPrices();
                Offers = p1.getOffers();
                dist = p1.getDist();
                Lat = p1.getLat();
                Long = p1.getLong();
                id=p1.getId();
                shopsList.notifyDataSetChanged();

                final DetailsViewList shopsList = new DetailsViewList(DetailsView.this, ProductsNames, ProductsDesc, ShopsNames, Prices, Offers,dist,Lat,Long);
                listView.setAdapter(shopsList);
                Toast.makeText(getApplicationContext(), "Shops after price sorting " + ShopsNames.toString().toString(), Toast.LENGTH_LONG).show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Toast.makeText(getApplicationContext(), "You Selected " + ShopsNames.get(position - 1) + " as Shop Navigation", Toast.LENGTH_SHORT).show();
                        DisplayTrack(getAddress(p.getLatitude(),p.getLongitude()),getAddress(Double.parseDouble(Lat.get(position - 1)),Double.parseDouble(Long.get(position - 1))));
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                        intent2 = getIntent();
                        save(intent2, id.get(position - 1));
                        Log.d(TAG, "Login ana asfa ya details Pricebutton : ");
                        return true;
                    }
                    });

            }
        });
        btndistance.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Sort p1 = new Sort(ProductsNames, ProductsDesc, ShopsNames, Prices, Offers, dist, Lat, Long,id);
                p1.sortDisatnce();
                ProductsNames = p1.getProductsNames();
                ProductsDesc = p1.getProductsDesc();
                ShopsNames = p1.getShopsNames();
                Prices = p1.getPrices();
                Offers = p1.getOffers();
                dist = p1.getDist();
                Lat = p1.getLat();
                Long = p1.getLong();
                id=p1.getId();
                shopsList.notifyDataSetChanged();

                final DetailsViewList shopsList = new DetailsViewList(DetailsView.this, ProductsNames, ProductsDesc, ShopsNames, Prices, Offers,dist,Lat,Long);
                listView.setAdapter(shopsList);
                Toast.makeText(getApplicationContext(), "Shops after distance sorting " +  ShopsNames.toString().toString(), Toast.LENGTH_LONG).show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Toast.makeText(getApplicationContext(), "You Selected " + ShopsNames.get(position - 1) + " as Shop Navigation", Toast.LENGTH_SHORT).show();
                        DisplayTrack(getAddress(p.getLatitude(),p.getLongitude()),getAddress(Double.parseDouble(Lat.get(position - 1)),Double.parseDouble(Long.get(position - 1))));
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        intent2 = getIntent();
                        save(intent2, id.get(position - 1));
                        Log.d(TAG, "Login ana asfa ya details distance button : ");

                        final DetailsViewList shopsList = new DetailsViewList(DetailsView.this, ProductsNames, ProductsDesc, ShopsNames, Prices, Offers,dist,Lat,Long);
                        listView.setAdapter(shopsList);
                        return true;
                    }
                });
            }

        });

    }
    private void DisplayTrack(String sSource,String sDestination){
        try{
            //when maps is installed
            //initialize Uri
            Uri uri=Uri.parse("https://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
            //initialize intent with action view
            Intent intent10=new Intent(Intent.ACTION_VIEW,uri);
            //set Package
            intent10.setPackage("com.google.android.apps.maps");
            //set flag
            intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //start activity
            startActivity(intent10);
        } catch (ActivityNotFoundException e) {
            //when maps is not installed
            //initialize Uri
            Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            //initialize intent with action view
            Intent intent10=new Intent(Intent.ACTION_VIEW,uri);
            //set flag
            intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //start activity
            startActivity(intent10);
        }

    }
    public String getAddress(double lat, double lng)  {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            return add;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }

    }

}