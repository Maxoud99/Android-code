package com.example.easyshopping;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ProductList extends ArrayAdapter {
    private ArrayList<String> ProductsNames;
    private ArrayList<String> ProductsDesc;
    private ArrayList<String>imageid;
    private Activity context;
//    private URL url ;
    public ProductList(Activity context, ArrayList<String> ProductsNames, ArrayList<String> ProductsDesc, ArrayList<String>imageid) {
        super(context, R.layout.row_item, ProductsNames);
        this.context = context;
        this.ProductsNames = ProductsNames;
        this.ProductsDesc = ProductsDesc;
        this.imageid = imageid;

    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.row_item, null, true);
        TextView textViewProducts = (TextView) row.findViewById(R.id.textViewProducts);
        TextView textViewProductsDesc = (TextView) row.findViewById(R.id.textViewProductsDesc);
        ImageView imageProduct = (ImageView) row.findViewById(R.id.imageViewProduct);

        textViewProducts.setText(ProductsNames.get(position));
        textViewProductsDesc.setText(ProductsDesc.get(position));
//        Picasso.with(context)
//                .load("https://cf4.s3.souqcdn.com/item/2020/05/21/12/63/29/24/2/item_XL_126329242_20270f400ab1f.jpg")
//                .resize(8,7).into((ImageView)row.findViewById(R.id.imageViewProduct));
//        System.out.println("https://cf4.s3.souqcdn.com/item/2020/05/21/12/63/29/24/2/item_XL_126329242_20270f400ab1f.jpg");
//
//        try {
//            url = new URL(imageid.get(position));
            new DownloadImageTask((ImageView)row.findViewById(R.id.imageViewProduct))
                    .execute(imageid.get(position));
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }

        ////////////////////////////////////////////////////////////

//        Bitmap bmp = null;
//        try {
//            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        imageProduct.setImageBitmap(bmp);
//       imageProduct.setImageResource();
        return  row;
    }
}
