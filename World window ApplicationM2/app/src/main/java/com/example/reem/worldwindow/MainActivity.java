package com.example.reem.worldwindow;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private boolean get = true;
    private boolean first = true;
    FloatingActionButton buttonSend;
    private boolean location_finished = false;
    private String messageContent;
    private EditText message;
    private ListView messagesList;
    private List<Chat_Model> chatModel = new ArrayList<>();
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private String uuid, name, baseCountry, destCountry;
    private MyAdabter myAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private String locationCountry;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String url = "https://thawing-forest-80216.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            first = savedInstanceState.getBoolean("first");
            get = savedInstanceState.getBoolean("get");
            ArrayList<String> messages = savedInstanceState.getStringArrayList("message");
            boolean[] isSent = savedInstanceState.getBooleanArray("isSent");
            uuid = savedInstanceState.getString("uuid");
            int i = 0;
            for (String mess : messages) {

                chatModel.add(new Chat_Model(mess, isSent[i++]));
            }

        }


        setContentView(R.layout.activity_main);
        message = (EditText) findViewById(R.id.message);
        messagesList = findViewById(R.id.messagesList);
        SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference), MODE_PRIVATE);
        name = sharedPreference.getString(getResources().getString(R.string.username), "");
        myAdapter = new MyAdabter(chatModel, this);
        messagesList.setAdapter(myAdapter);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (first)
            sendRequest("");


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Toast.makeText(this, "No Internet connection !", Toast.LENGTH_LONG).show();

//        sendRequest("");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (uuid.equals("") && savedInstanceState != null) {
            first = savedInstanceState.getBoolean("first");
            get = savedInstanceState.getBoolean("get");
            ArrayList<String> messages = savedInstanceState.getStringArrayList("message");
            boolean[] isSent = savedInstanceState.getBooleanArray("isSent");
            uuid = savedInstanceState.getString("uuid");
            int i = 0;
            for (String mess : messages) {

                chatModel.add(new Chat_Model(mess, isSent[i++]));
            }

            setContentView(R.layout.activity_main);
            message = (EditText) findViewById(R.id.message);
            messagesList = findViewById(R.id.messagesList);
            SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference), MODE_PRIVATE);
            name = sharedPreference.getString(getResources().getString(R.string.username), "");
            myAdapter = new MyAdabter(chatModel, this);
            messagesList.setAdapter(myAdapter);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        get = false;
        buttonSend = findViewById(R.id.sendButton);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageContent = message.getText().toString();
                if (!(messageContent.equals(""))) {
                    chatModel.add(new Chat_Model(messageContent, true));
                    myAdapter.notifyDataSetChanged();
                    sendRequest(messageContent);
                    message.setText("");
                }

            }
        });
    }


    public void onClickName(View view) {
        if (!(name.equals(""))) {
            chatModel.add(new Chat_Model(name, true));
            myAdapter.notifyDataSetChanged();
            sendRequest(name);
        } else {
            Toast.makeText(this, "You Did not enter your name before", Toast.LENGTH_LONG).show();
        }
//        getCountryfromLocation();
//        Toast.makeText(this, locationCountry, Toast.LENGTH_LONG).show();

    }
    public void onClickLocation1(View view) {
        getCountryfromLocation();
        if(locationCountry==null || locationCountry.equals("")) // ----------------------------added this
            Toast.makeText(this, "Open GPS", Toast.LENGTH_LONG).show();
        else {
            chatModel.add(new Chat_Model(locationCountry, true));
            sendRequest(locationCountry);           // ------------------------------------added this
        }
        myAdapter.notifyDataSetChanged();

    }
    public void onClickLocation2(View view) {
        String loc=getCountryfromSIM();
        if(loc==null)
            Toast.makeText(this, "Open 3G", Toast.LENGTH_LONG).show();
        else{

            chatModel.add(new Chat_Model(loc, true));
            myAdapter.notifyDataSetChanged();
            sendRequest(loc);}

    }





    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sendRequest(String message) {
        requestQueue = Volley.newRequestQueue(this);


        if (first && !get) {  // ---------------------------------------------------------------------changed this
            first = false;
            SharedPreferences sharedPreference = getSharedPreferences(getResources().getString(R.string.shared_preference), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreference.edit();
            editor.putString(getResources().getString(R.string.username), message);
            editor.commit();
            name = message;
        }

        if (!isNetworkAvailable()) {
            final String message2 = message;
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("No Internet connection")
                    .setMessage("No Internet connection.")
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendRequest(message2);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            sendRequest2(message);
        }
    }
    protected void sendRequest2(String message){
        if (get) {

            stringRequest = new StringRequest(Request.Method.GET, url + "welcome", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    chatModel.add(new Chat_Model(TandeefGet(response), false));
                    myAdapter.notifyDataSetChanged();
                    getUUID(response);
                    get = false;
                    Log.e("finished get", uuid);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    chatModel.add(new Chat_Model(error.toString() + "----error", false));
                    myAdapter.notifyDataSetChanged();

                }
            });
            requestQueue.add(stringRequest);
        } else {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("message", message);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("debug", "jsonObject failed");
            }
            requestQueue.add(new JsonObjectRequest(Request.Method.POST, url + "chat", jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //handle response
                    String responseMessage = null;
                    Log.e("degug", "responded");
                    try {
                        responseMessage = response.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    chatModel.add(new Chat_Model(responseMessage, false));
                    myAdapter.notifyDataSetChanged();


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    Log.e("debug", error.toString());

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", uuid);
                    return headers;
                }
            });


        }


    }


    public String TandeefGet(String x) {
        String[] y = x.split(":");
        y = y[1].split(",");

        return y[0].substring(1, y[0].length() - 1);
    }

    public String TandeefPost(String x) {
        String[] y = x.split(":");
        y = y[1].split(",");

        return y[1].substring(0, y[1].length() - 2);
    }

    public void getUUID(String response) {
        String[] x = response.split(",");
        String[] y = x[1].split(":");
        uuid = y[1].substring(1, y[1].length() - 3);

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        ArrayList<String> messages = new ArrayList<String>();
        boolean[] isSent = new boolean[chatModel.size()];
        int i = 0;
        for (Chat_Model chat : chatModel) {
            messages.add(chat.getChatmessage());
            isSent[i++] = chat.isSend();
        }
        savedInstanceState.putBoolean("first", first);
        savedInstanceState.putBoolean("get", get);
        savedInstanceState.putStringArrayList("message", messages);
        savedInstanceState.putBooleanArray("isSent", isSent);
        savedInstanceState.putString("uuid", uuid);
    }

    protected String getCountryfromSIM() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        if (countryCode == null)
            return null;

        Locale loc = new Locale("", countryCode);
        return loc.getDisplayCountry();

    }

    protected String getCountryfromLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            return null;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Geocoder myLocation = new Geocoder(MainActivity.this);
                            List<Address> myList = null;
                            try {
                                myList = myLocation.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (myList != null) {
                                try {

                                    locationCountry = myList.get(0).getCountryName();
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                });

        return locationCountry;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        // Logic to handle location object
                                        Geocoder myLocation = new Geocoder(MainActivity.this);
                                        List<Address> myList = null;
                                        try {
                                            myList = myLocation.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if (myList != null) {
                                            try {

                                                locationCountry = myList.get(0).getCountryName();
                                            } catch (Exception e) {

                                            }
                                        }
                                    }
                                }
                            });

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                location_finished = true;
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
