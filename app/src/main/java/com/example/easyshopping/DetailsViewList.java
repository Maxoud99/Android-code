package com.example.easyshopping;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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

import java.io.Serializable;
import java.util.ArrayList;


public class DetailsViewList extends ArrayAdapter implements Serializable {
    private ArrayList<String> ProductsNames;
    private ArrayList<String> ProductsDesc;
    private ArrayList<String> ShopsNames;
    private ArrayList<String> Prices;
    private ArrayList<String> Offers;
    private ArrayList<String> Long;
    private ArrayList<String> Lat;
    private Activity context;
    private  Location l;

    public DetailsViewList(Activity context, ArrayList<String> ProductsNames, ArrayList<String> ProductsDesc, ArrayList<String> ShopsNames, ArrayList<String> Prices, ArrayList<String> Offers, ArrayList<String> Long, ArrayList<String> Lat, Location l) {
        super(context, R.layout.row_item_details, ProductsNames);
        this.context = context;
        this.ProductsNames = ProductsNames;
        this.ProductsDesc = ProductsDesc;
        this.ShopsNames = ShopsNames;
        this.Prices = Prices;
        this.Offers = Offers;
        this.Long = Long;
        this.Lat = Lat;
        this.l=l;

    }


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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item_details, null, true);
        TextView textViewProductsName = (TextView) row.findViewById(R.id.textViewProductsName);
        TextView textViewProductsDescName = (TextView) row.findViewById(R.id.textViewProductsDescName);
        TextView textViewProducts = (TextView) row.findViewById(R.id.textViewProducts);
        TextView textViewProductsDesc = (TextView) row.findViewById(R.id.textViewProductsDesc);
        TextView textViewShopsNames = (TextView) row.findViewById(R.id.textViewShopsNames);
        TextView textViewPrices = (TextView) row.findViewById(R.id.textViewPrices);
        TextView textViewOffers = (TextView) row.findViewById(R.id.textViewOffers);
        TextView textViewDistance = (TextView) row.findViewById(R.id.textViewDistance);
//        TextView textViewLat = (TextView) row.findViewById(R.id.textViewLat);
       if(position==0){
           textViewProductsName.setText("product name");
           textViewProductsDescName.setText("description");
        textViewProducts.setText(ProductsNames.get(position));
        textViewProductsDesc.setText(ProductsDesc.get(position));
        }
   else{
           textViewProductsName.setText("");
           textViewProductsDescName.setText("");
            textViewProducts.setText("");
            textViewProductsDesc.setText("");
        }
        textViewShopsNames.setText(ShopsNames.get(position));
        textViewPrices.setText(Prices.get(position)+" L.E");
        textViewOffers.setText(Offers.get(position));
        textViewDistance.setText(distance(Double.parseDouble(Lat.get(position)),l.getLatitude(),Double.parseDouble(Long.get(position)),l.getLongitude())+" km");

        return  row;
    }
}
