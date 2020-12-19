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
    private ArrayList<String> distance;
    private ArrayList<String> Lat;
    private ArrayList<String> Long;

    private Activity context;
    private  Location l;

    public DetailsViewList(Activity context, ArrayList<String> ProductsNames, ArrayList<String> ProductsDesc, ArrayList<String> ShopsNames, ArrayList<String> Prices, ArrayList<String> Offers, ArrayList<String>distance, ArrayList<String> Lat, ArrayList<String> Long) {
        super(context, R.layout.row_item_details, ProductsNames);
        this.context = context;
        this.ProductsNames = ProductsNames;
        this.ProductsDesc = ProductsDesc;
        this.ShopsNames = ShopsNames;
        this.Prices = Prices;
        this.Offers = Offers;
        this.distance=distance;
        this.Lat=Lat;
        this.Long=Long;

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
           textViewProductsName.setText("PRODUCT NAME");
           textViewProductsDescName.setText("DESCRIPTION");
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
        textViewDistance.setText(distance.get(position) +" km");

        return  row;
    }
}
