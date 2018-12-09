package com.nomadhub.nomadhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabFragment2 extends Fragment {

    RecyclerView recyclerView;
    static List<Event> hubList = new ArrayList<>();

    Bitmap image;
    static HubAdapter adapter;


    static ItemClickListener myItemClickListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =  inflater.inflate(R.layout.fragment_tab_2, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerTab2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myItemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                Intent intent = new Intent(getActivity(),HubDetailsActivity.class);
                intent.putExtra("name",hubList.get(pos).getTitle());
                double[] oLocation = {hubList.get(pos).getLatLng().latitude,hubList.get(pos).getLatLng().longitude};
                intent.putExtra("location",oLocation);
                intent.putExtra("hubID",pos);
                startActivity(intent);

                /*
                TabFragment1.mapStatic.addMarker(new MarkerOptions().title(TabFragment2.hubList.get(pos).getTitle()).position(TabFragment2.hubList.get(pos).getLatLng()));
                TabFragment1.mapStatic.moveCamera(CameraUpdateFactory.newLatLngZoom(TabFragment2.hubList.get(pos).getLatLng(),2));
                */
            }
        };


        adapter = new HubAdapter(hubList);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}