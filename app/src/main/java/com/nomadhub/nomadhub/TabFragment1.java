package com.nomadhub.nomadhub;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class TabFragment1 extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mSupportMapFragment;
    LocationManager locationManager;
    LocationListener locationListener;
    Context mActivity;
    public static GoogleMap mapStatic;
    static FragmentManager fragmentManager;
    List<String> eventsList;
    List<LatLng> eventLtList;
    Event hub;
    LatLng hubLocation;
    Date hubDate;
    InputStream stream;
    String hubName = "";
    String hubAddress = "";
    ArrayList<Bitmap> imageArray = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_1,container,false);
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapView, mSupportMapFragment).commit();
        }

        mSupportMapFragment.getMapAsync(this);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        eventsList = new ArrayList<>();
        eventLtList = new ArrayList<>();


        try{
            ArrayList<Event> savedList = gson.fromJson(sharedPreferences.getString("savedEvents", null),new TypeToken<ArrayList<Event>>() {
            }.getType());
            eventsList = savedList.
        }catch (Exception e){

        }

        locationManager = (LocationManager) inflater.getContext().getSystemService(Context.LOCATION_SERVICE);
        mActivity = inflater.getContext();

        fragmentManager = getFragmentManager();

        return view;
        }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mapStatic = googleMap;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {

                googleMap.clear();
                getHubs();
                LatLng mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(mUserLocation).title("Your position")).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.boy));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mUserLocation, 12));

                Geocoder geocoder = new Geocoder(mActivity,Locale.getDefault());
                try {
                    List<Address> currentCity = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    ((MainActivity) getActivity()).setActionBarTitle(currentCity.get(0).getLocality());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONDownloader jsonDownloader = new JSONDownloader();
                jsonDownloader.execute(location.getLatitude(),location.getLongitude());

                //getEvents(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,200,locationListener);
        }

    }

    public void getHubs() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Hub");
        query.orderByAscending("updatedAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e != null || !(objects.size() > 0)){
                    Toast.makeText(getActivity(), "There was an error", Toast.LENGTH_SHORT).show();
                }else{
                    for (ParseObject tObj : objects){
                        hubName = tObj.get("name").toString();
                        hubDate = (Date) tObj.get("dateEvent");
                        hubAddress = tObj.get("address").toString();
                        ParseGeoPoint parseGeoPoint = tObj.getParseGeoPoint("location");

                        hubLocation = new LatLng(parseGeoPoint.getLatitude(),parseGeoPoint.getLongitude());
                        mapStatic.addMarker(new MarkerOptions().position(new LatLng(parseGeoPoint.getLatitude(),parseGeoPoint.getLongitude())).title(hubName).icon(BitmapDescriptorFactory.fromResource(R.drawable.laptopicon)));
                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 200, locationListener);

            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }

        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

    }




    public class JSONDownloader extends AsyncTask<Double,Void,String>{

        @Override
        protected String doInBackground(Double... doubles) {
            try {

                URL url = new URL("https://api.meetup.com/find/upcoming_events?key=86d5e1e423c42794e373b24c2a4e11&lat=" + doubles[0] + "&lon=" + doubles[1] + "&text=expat&fields=featured_photo&topic_category=4446&radius=10&page=10&sign=true");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                int iData = stream.read();
                String sData = "";

                while(iData != -1){

                    char c = (char) iData;
                    sData += c;
                    iData = stream.read();

                }

                JSONObject rawObject = new JSONObject(sData);
                JSONArray eventsArray = rawObject.getJSONArray("events");
                int numberOfEvents = eventsArray.length();

                for (int i = 0; i<numberOfEvents;i++){

                    try{

                        JSONObject photosObject = eventsArray.getJSONObject(i).getJSONObject("featured_photo");
                        URL photoURL = new URL(photosObject.getString("photo_link"));

                        HttpURLConnection photoConnection = (HttpURLConnection) photoURL.openConnection();
                        photoConnection.setDoInput(true);
                        photoConnection.connect();
                        InputStream photoInput = photoConnection.getInputStream();
                        imageArray.add(BitmapFactory.decodeStream(photoInput));



                    }catch (Exception e){

                        imageArray.add(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.hubud)));

                    }

                }

                return sData;

            } catch (Exception e) {

                e.printStackTrace();
                Log.i("GRAND",e.toString());
                return null;

            }
        }

        @Override
        protected void onPostExecute(String s) {

            TabFragment2.hubList.clear();

            try{
                JSONObject rawObject = new JSONObject(s);
                JSONArray eventsArray = rawObject.getJSONArray("events");
                int numberOfEvents = eventsArray.length();
                Log.i("OLIVNUMBER",String.valueOf(numberOfEvents));

                for (int i = 0; i<numberOfEvents;i++){

                    JSONObject nameObject = eventsArray.getJSONObject(i);
                    JSONObject latlngObject = eventsArray.getJSONObject(i).getJSONObject("group");

                    String nameEvent = nameObject.getString("name");
                    String addressEvent = latlngObject.getString("localized_location");
                    LatLng latLngEvent = new LatLng(latlngObject.getDouble("lat"),latlngObject.getDouble("lon"));
                    Bitmap eventBitmap = imageArray.get(i);
                    String dateEvent = nameObject.getString("local_date");

                    TabFragment2.hubList.add(new Event(nameEvent,addressEvent,eventBitmap,latLngEvent,dateEvent));
                    TabFragment2.adapter.notifyDataSetChanged();

                }

                String saveListEvents = gson.toJson(TabFragment2.hubList);
                sharedPreferences.edit().putString("savedEvents",saveListEvents).apply();

            }catch (Exception e){
                Log.i("POLLUX",e.toString());
            }

        }
    }

}