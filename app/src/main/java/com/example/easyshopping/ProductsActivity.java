package com.example.easyshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import app.AppConfig;

public class ProductsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> ProductsNames = new ArrayList<>();

    private ArrayList<String> ProductsDesc = new ArrayList<>();

    private ArrayList<String>imageid = new ArrayList<>();
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

            //getting the name from the json object and putting it inside string array
            ProductsNames.add(obj.getString("name"));
            ProductsDesc.add(obj.getString("description"));
            imageid.add( obj.getString("image_url"));
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getJSON("http://192.168.1.2/project/index.php");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        // Setting header
        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("List Of Products");

        listView=(ListView)findViewById(R.id.list);
//        Toast.makeText(getApplicationContext(), ProductsNames.toString(), Toast.LENGTH_SHORT).show();
//        System.out.println(listView.toString()+" list");
//        System.out.println(textView.toString()+" text");

        listView.addHeaderView(textView);
//        System.out.println(ProductsNames.toString());
        // For populating list data
        ProductList productList = new ProductList(this, ProductsNames, ProductsDesc, imageid);
        listView.setAdapter(productList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),"You Selected "+ ProductsNames.get(position-1)+ " as Product",Toast.LENGTH_SHORT).show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        if (position==0){
                            Intent intent6=new Intent(view.getContext(),DetailsView.class);
                            startActivityForResult(intent6,0);
//                        }

//                        if (position==1){
//                            Intent intent6=new Intent(view.getContext(),AlPrince.class);
//                            startActivityForResult(intent6,1);
//                        }
//                        if (position==2){
//                            Intent intent6=new Intent(view.getContext(),ElEkhtyar.class);
//                            startActivityForResult(intent6,2);
//                        }


                    }
                });
            }
        });
    }
}
