package com.example.exercise;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.model.GradientColor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import org.json.JSONArray;


public class FragmentEarth extends Fragment {

    //******************************Declare TextViews***********************************
    //**Date**
    TextView earthDateTextView;

    //***************Today****************
    //**Temperature**
    TextView earthMaxTempView;
    TextView earthMinTempView;
    //**Pressure, Sunrise, Sunset and UV Condition**
    TextView earthPressureView;
    TextView earthSunriseView;
    TextView earthSunsetView;
    TextView earthUVView;

    //**************Forecast**************
    //*Notation*
    //  eF == earth Forecast
    //  d0 == d+0, d1 == d+1, d2 == d+2, ..., d6 == d+6
    //**MaxTemp**
    TextView eFd0MaxTempView;
    TextView eFd1MaxTempView;
    TextView eFd2MaxTempView;
    TextView eFd3MaxTempView;
    TextView eFd4MaxTempView;
    TextView eFd5MaxTempView;
    TextView eFd6MaxTempView;

    //**MinTemp**
    TextView eFd0MinTempView;
    TextView eFd1MinTempView;
    TextView eFd2MinTempView;
    TextView eFd3MinTempView;
    TextView eFd4MinTempView;
    TextView eFd5MinTempView;
    TextView eFd6MinTempView;

    // **Date**
    TextView eFd0DTView;
    TextView eFd1DTView;
    TextView eFd2DTView;
    TextView eFd3DTView;
    TextView eFd4DTView;
    TextView eFd5DTView;
    TextView eFd6DTView;

    //**BarChart**
    BarChart eFBarChart;


    //******************************Declare APIs***********************************
    //String worldWeatherMapApiKey = "f7ba5036adfc078bc9d35926eb3b86ca";
    //String earthGeocodingApi = "http://api.openweathermap.org/geo/1.0/direct?q=Seoul&limit=5&appid=f7ba5036adfc078bc9d35926eb3b86ca";
    //Latitude = 37.532600, Longitude: 127.024612
    //String earthApiTodayURL = "https://api.openweathermap.org/data/2.5/weather?lat=37.5666791&lon=126.9782914&appid=f7ba5036adfc078bc9d35926eb3b86ca";
    String earthApiForecastURL = "https://api.openweathermap.org/data/2.5/onecall?lat=37.5666791&lon=126.9782914&exclude=hourly,minutely&appid=f7ba5036adfc078bc9d35926eb3b86ca";

    public static FragmentEarth newInstance(int number) {
        FragmentEarth fragment_e = new FragmentEarth();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment_e.setArguments(bundle);
        return fragment_e;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v2 = inflater.inflate(R.layout.fragment_earth, container, false);

        //**date**
        earthDateTextView = v2.findViewById(R.id.EarthDateText);

        //********Today*******
        //**Temperature**
        earthMaxTempView = v2.findViewById(R.id.EarthMaxTemp);
        earthMinTempView = v2.findViewById(R.id.EarthMinTemp);
        //**Pressure, Sunrise, Sunset and UV Condition**
        earthPressureView = v2.findViewById(R.id.EarthPressure);
        earthSunriseView = v2.findViewById(R.id.EarthSunrise);
        earthSunsetView = v2.findViewById(R.id.EarthSunset);
        earthUVView = v2.findViewById(R.id.EarthUV);

        //********Forecast*******
        //**Forecast Max Temperature**
        eFd0MaxTempView = v2.findViewById(R.id.d0MaxTemp);
        eFd1MaxTempView = v2.findViewById(R.id.d1MaxTemp);
        eFd2MaxTempView = v2.findViewById(R.id.d2MaxTemp);
        eFd3MaxTempView = v2.findViewById(R.id.d3MaxTemp);
        eFd4MaxTempView = v2.findViewById(R.id.d4MaxTemp);
        eFd5MaxTempView = v2.findViewById(R.id.d5MaxTemp);
        eFd6MaxTempView = v2.findViewById(R.id.d6MaxTemp);
        //**Forecast Min Temperature**
        eFd0MinTempView = v2.findViewById(R.id.d0MinTemp);
        eFd1MinTempView = v2.findViewById(R.id.d1MinTemp);
        eFd2MinTempView = v2.findViewById(R.id.d2MinTemp);
        eFd3MinTempView = v2.findViewById(R.id.d3MinTemp);
        eFd4MinTempView = v2.findViewById(R.id.d4MinTemp);
        eFd5MinTempView = v2.findViewById(R.id.d5MinTemp);
        eFd6MinTempView = v2.findViewById(R.id.d6MinTemp);
        // **Forecast Date**
        eFd0DTView = v2.findViewById(R.id.d0day);
        eFd1DTView = v2.findViewById(R.id.d1day);
        eFd2DTView = v2.findViewById(R.id.d2day);
        eFd3DTView = v2.findViewById(R.id.d3day);
        eFd4DTView = v2.findViewById(R.id.d4day);
        eFd5DTView = v2.findViewById(R.id.d5day);
        eFd6DTView = v2.findViewById(R.id.d6day);

        //**Bar Chart**
        eFBarChart = v2.findViewById(R.id.eBarChart);

        getDate();

        new Thread(this::getJSON).start();


        return v2;
    }


    private void getDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        earthDateTextView.setText(dFormat.format(date));
    }


    //******************************Loading Earth Api***********************************
    @SuppressLint("SetTextI18n")
    public void getJSON() {
        try {
            //**Forecast API**
            URL URL = new URL(earthApiForecastURL);
            HttpURLConnection con = (HttpURLConnection)URL.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input;
            StringBuilder response = new StringBuilder();
            while ((input = br.readLine()) != null) {
                response.append(input);
            }
            br.close();

            String Data = response.toString();
            Log.d("data", Data);
            JSONObject jsonObj = new JSONObject(Data);


            //**********************Forecast************************
            JSONArray jsonDailyArr = (JSONArray) jsonObj.get("daily");
            //**TextView ArrayList**
            ArrayList<TextView> eFMaxTempArr = new ArrayList<>(
                    Arrays.asList(eFd0MaxTempView, eFd1MaxTempView, eFd2MaxTempView, eFd3MaxTempView, eFd4MaxTempView, eFd5MaxTempView, eFd6MaxTempView)
            );
            ArrayList<TextView> eFMinTempArr = new ArrayList<>(
                    Arrays.asList(eFd0MinTempView, eFd1MinTempView, eFd2MinTempView, eFd3MinTempView, eFd4MinTempView, eFd5MinTempView, eFd6MinTempView)
            );
            ArrayList<TextView> eFDTArr = new ArrayList<>(
                    Arrays.asList(eFd0DTView, eFd1DTView, eFd2DTView, eFd3DTView, eFd4DTView, eFd5DTView, eFd6DTView)
            );

            ArrayList<String> maxTempArr = new ArrayList<>();
            ArrayList<String> minTempArr = new ArrayList<>();
            ArrayList<String> fDateArr = new ArrayList<>();

            //**setText**
            for (int i = 0; i<=6; ++i) {
                //Log.d("data", jsonDailyArr.getString(i));
                JSONObject jsonDailyObj = jsonDailyArr.getJSONObject(i); //
                JSONObject jsonTempObj = (JSONObject) jsonDailyObj.get("temp");
                double earthMaxTemp = jsonTempObj.getDouble("max") - 273.15;
                double earthMinTemp = jsonTempObj.getDouble("min") - 273.15;
                earthMaxTemp = Math.round(earthMaxTemp * 10) / 10.0;
                earthMinTemp = Math.round(earthMinTemp * 10) / 10.0;
                String earthMaxTempStr = String.format("%.0f",earthMaxTemp);
                String earthMinTempStr = String.format("%.0f",earthMinTemp);

                long forecastDTMillis = (jsonDailyObj.getLong("dt") + 32400L)* 1000L;
                SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd");
                Date forecastDT = new Date(forecastDTMillis);

                maxTempArr.add(earthMaxTempStr);
                minTempArr.add(earthMinTempStr);
                fDateArr.add(timeFormat.format(forecastDT));
            }

            new Thread(() -> {
                ArrayList<Float> minTempFloatArr = new ArrayList<>();
                ArrayList<Float> maxTempFloatArr = new ArrayList<>();
                float minTempTot = 3000.F;
                float maxTempTot = -2000.F;

                for (int i=0; i<=6; i++){
                    float minTempDay = Float.parseFloat(minTempArr.get(i));
                    float maxTempDay = Float.parseFloat(maxTempArr.get(i));
                    if (minTempDay < minTempTot) minTempTot = minTempDay;
                    if (maxTempDay > maxTempTot) maxTempTot = maxTempDay;
                    minTempFloatArr.add(minTempDay);
                    maxTempFloatArr.add(maxTempDay);
                }

                List<BarEntry> entries = new ArrayList<>();

                for (int i=0; i<=6; i++){
                    float y1 = maxTempTot - maxTempFloatArr.get(i);
                    float y2 = maxTempFloatArr.get(i) - minTempFloatArr.get(i);
                    float y3 = minTempFloatArr.get(i) - minTempTot;
                    entries.add(new BarEntry(i, new float[] {y3, y2, y1}));
                }

                BarDataSet bSet = new BarDataSet(entries, " ");
                bSet.setDrawValues(false);
                BarData bData = new BarData(bSet);
                List<GradientColor> gradientColors = new ArrayList<>();
                gradientColors.add(new GradientColor(Color.parseColor("#D1D1D1"), Color.parseColor("#D1D1D1")));
                gradientColors.add(new GradientColor(Color.parseColor("#A5D48A"), Color.parseColor("#F59056")));
                gradientColors.add(new GradientColor(Color.parseColor("#D1D1D1"), Color.parseColor("#D1D1D1")));
                bSet.setGradientColors(gradientColors);
                bData.setBarWidth(0.4f);

                eFBarChart.setData(bData);
                eFBarChart.invalidate();
                eFBarChart.setDescription(null);
                eFBarChart.getXAxis().setEnabled(false);
                eFBarChart.getLegend().setEnabled(false);

                YAxis left = eFBarChart.getAxisLeft();
                left.setDrawLabels(false); // no axis labels
                left.setDrawAxisLine(false); // no axis line
                left.setDrawGridLines(false); // no grid lines
                YAxis right = eFBarChart.getAxisRight();
                right.setEnabled(false);
                right.setDrawLabels(false); // no axis labels
                right.setDrawAxisLine(false); // no axis line
                right.setDrawGridLines(false); // no grid lines
            }).start();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    earthMaxTempView.setText(maxTempArr.get(0) + "°C");
                    earthMinTempView.setText(minTempArr.get(0) + "°C");

                    for (int i = 0; i <= 6; i++) {
                        eFMaxTempArr.get(i).setText(maxTempArr.get(i) + "°");
                        eFMinTempArr.get(i).setText(minTempArr.get(i) + "°");
                        eFDTArr.get(i).setText(fDateArr.get(i));
                    }
                }
            });
            //******Pressure, Sunrise, Sunset and UV Condition******
            //+32400L: UTC -> KST, 32400sec = 9hour
            //*1000: msec -> sec
            JSONObject jsonCurrentObj = (JSONObject) jsonObj.get("current");
            String earthPressure = jsonCurrentObj.getString("pressure");
            long earthSunriseMillis = (jsonCurrentObj.getLong("sunrise") )* 1000L;
            long earthSunsetMillis = (jsonCurrentObj.getLong("sunset") ) * 1000L;
            double earthUV = jsonCurrentObj.getDouble("uvi");
            String earthUVStr;
            if (earthUV < 3){
                earthUVStr = "Low";
            } else if (earthUV >= 3 && earthUV < 6){
                earthUVStr = "Moderate";
            }else if (earthUV >= 6 && earthUV < 8){
                earthUVStr = "High";
            } else if (earthUV >= 8 && earthUV < 10){
                earthUVStr = "Very High";
            } else{
                earthUVStr = "error";
            }

            Log.d("data", earthUVStr);

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Date earthSunrise = new Date(earthSunriseMillis);
            Date earthSunset = new Date(earthSunsetMillis);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    earthPressureView.setText(earthPressure + "hPa");
                    earthSunriseView.setText(timeFormat.format(earthSunrise));
                    earthSunsetView.setText(timeFormat.format(earthSunset));
                    earthUVView.setText(earthUVStr);
                }
            });


        } catch  (Exception e) {
            e.printStackTrace();
        }


    }


}

