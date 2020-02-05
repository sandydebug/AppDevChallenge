package com.example.appdevchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Weather extends Fragment {

    SearchView editText;
    TextView textView1,textView2,textView3,textView4,textView5,textView6;
    ImageButton search;
    ImageView weathericon;
    private ProgressDialog progressDialog;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_weather,container,false);
        editText = v.findViewById(R.id.search1);
        textView1 = v.findViewById(R.id.CityName);
        textView2 = v.findViewById(R.id.WeatherDescription);
        textView3 = v.findViewById(R.id.MaxTemp);
        textView4 = v.findViewById(R.id.MinTemp);
        textView5 = v.findViewById(R.id.Temp);
        textView6 = v.findViewById(R.id.date);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        final String date = simpleDateFormat.format(calendar.getTime());
        textView6.setText(date);
        weathericon = v.findViewById(R.id.WeatherIcon);
        progressDialog=new ProgressDialog(getContext());
        editText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    showWeather(s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        return v;
    }

    public void showWeather(String name) throws MalformedURLException {

        progressDialog.setMessage("Let's get the weather in "+editText.getQuery().toString());
        progressDialog.setCancelable(false);
        progressDialog.show();


        InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);

        try {
            String city = URLEncoder.encode(name,"UTF-8");
            download el =new download();
            el.execute("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=4b07e21af089ba3f3b3584d664624375");

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(),"Enter Valid City Name",Toast.LENGTH_LONG).show();
        }


    }

    public class download extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            String store = "";
            HttpURLConnection urlConnection;

            try {
                URL url = new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);

                int data =reader.read();
                while(data !=-1){
                    char ch =(char)data;
                    store+=ch;
                    data = reader.read();

                }

                return store;

            } catch (Exception e) {

            }


            return store;
        }

        @Override
        protected void onPostExecute(String store) {
            super.onPostExecute(store);

            try {
                if (!store.equals("")) {
                    JSONObject jsonObject = new JSONObject(store);
                    String info = jsonObject.getString("weather");
                    String city = jsonObject.getString("name");
                    String info2 = jsonObject.getString("main");
                    String info3 = jsonObject.getString("sys");
                    JSONArray arr = new JSONArray(info);
                    JSONObject arr2 = new JSONObject(info2);
                    JSONObject arr3 = new JSONObject(info3);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject part = arr.getJSONObject(i);
                        Double tem = Double.parseDouble(arr2.getString("temp")) - 273;
                        String temp = String.valueOf(tem).substring(0, 2);
                        Double temmin = Double.parseDouble(arr2.getString("temp_min")) - 273;
                        String tempmin = String.valueOf(temmin).substring(0, 2);
                        Double temmax = Double.parseDouble(arr2.getString("temp_max")) - 273;
                        String tempmax = String.valueOf(temmax).substring(0, 2);
                        if(part.getString("main").contains("Clouds")){
                            Picasso.get().load(R.drawable.clouds).fit().into(weathericon);
                        }
                        else if(part.getString("main").equals("Clear")){
                            Picasso.get().load(R.drawable.wet).fit().into(weathericon);
                        }
                        else if(part.getString("main").contains("Rain")){
                            Picasso.get().load(R.drawable.rain).fit().into(weathericon);
                        }
                        else if(part.getString("main").contains("Storm")){
                            Picasso.get().load(R.drawable.storm).fit().into(weathericon);
                        }
                        else if(part.getString("main").contains("Smoke")){
                            Picasso.get().load(R.drawable.smoke).fit().into(weathericon);
                        }
                        else{
                            Picasso.get().load(R.drawable.general).fit().into(weathericon);
                        }
                        textView5.setText(temp + (char) 0x00B0);
                        textView4.setText(tempmin + (char) 0x00B0);
                        textView3.setText(tempmax + (char) 0x00B0);
                        textView1.setText(city+","+arr3.getString("country"));
                        textView2.setText(part.getString("main"));
                        textView6.setText(getDate(Long.parseLong(jsonObject.getString("dt"))));
                        progressDialog.dismiss();
                    }

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Enter Valid City Name",Toast.LENGTH_SHORT).show();
                }
            }catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


        }
    }


    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        String date = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(cal.getTime());
        return date;
    }

}
