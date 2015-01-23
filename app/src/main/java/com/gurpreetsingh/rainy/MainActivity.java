package com.gurpreetsingh.rainy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather currentData;

    @InjectView(R.id.timeLabel) TextView timeLabel;
    @InjectView(R.id.temperatureLabel) TextView temperatureLabel;
    @InjectView(R.id.humidityValue) TextView humidityValue;
    @InjectView(R.id.precipValue) TextView precipValue;
    @InjectView(R.id.summaryLabel) TextView summaryLabel;
    @InjectView(R.id.iconImageView) ImageView iconImageView;
    @InjectView(R.id.refreshImageView) ImageView refreshImageView;
    @InjectView(R.id.progbar)ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        progbar.setVisibility(View.INVISIBLE);

        final double latitude = 42.98252;
        final double longitude = -81.25397;

        refreshImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getForecast(latitude, longitude);
            }
        });
        getForecast(latitude, longitude);
        Log.d(TAG, "Main UI Code is Running");

    }

    private void getForecast(double latitude, double longitude)
    {
        final String APIKEY = "178886c134537d8bbb04fd98b60edfbd";

        String forecastURL = "https://api.forecast.io/forecast/" + APIKEY + "/" + latitude + "," + longitude;

        if (NetworkAvailable())
        {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastURL).build();

            Call call = client.newCall(request);
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Request request, IOException e)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            toggleRefresh();
                        }
                    });
                    alertUser();
                }

                @Override
                public void onResponse(Response response) throws IOException
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            toggleRefresh();
                        }
                    });

                    try
                    {
                        String JSONData = response.body().string();
                        Log.v(TAG, JSONData);

                        if (!response.isSuccessful())
                        {
                            alertUser();
                        }
                        else
                        {
                            currentData = getDetails(JSONData);
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    updateDisplay();
                                }
                            });
                        }
                    }
                    catch (IOException | JSONException e)
                    {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, getString(R.string.network_unavailable_text), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh()
    {
        if(progbar.getVisibility() == View.INVISIBLE)
        {
            progbar.setVisibility(View.VISIBLE);
            refreshImageView.setVisibility(View.INVISIBLE);
        }
        else
        {
            progbar.setVisibility(View.INVISIBLE);
            refreshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void updateDisplay()
    {
        temperatureLabel.setText(Double.toString(currentData.getTemperature()));
        timeLabel.setText("At " + currentData.getFormattedTime() + " it will be");
        humidityValue.setText(Double.toString(currentData.getHumidity()));
        precipValue.setText(Double.toString(currentData.getPrecipChance()) + "%");
        summaryLabel.setText(currentData.getSummary());

        Drawable drawable = getResources().getDrawable(currentData.getIconID());

        iconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getDetails(String jsonData) throws JSONException
    {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");

        return new CurrentWeather(
                currently.getString("icon"),
                currently.getDouble("humidity"),
                currently.getLong("time"),
                currently.getDouble("temperature"),
                currently.getDouble("precipProbability"),
                currently.getString("summary"),
                forecast.getString("timezone")
        );

    }//End of getDetails method


    private boolean NetworkAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkData = manager.getActiveNetworkInfo();

        //Return true if networkData exists and is connected, false if it not existent or if its not connected
        return !(networkData == null || !networkData.isConnected());
    }

    private void alertUser()
    {
        DialogFragment dialog = new DialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }//End of alertUser method
}//End of MainActivity class


/*
    JSON FORMAT EXAMPLE

    [
        {
            "name" : "blah",
            "id" : "6969"
        }

        {
            "name" : "whew",
            "id" : "1289"
        }
    ]

    An array of objects, each storing two key value pairs
 */