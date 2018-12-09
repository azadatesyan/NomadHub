package com.nomadhub.nomadhub;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class HubAdapter extends RecyclerView.Adapter<HubViewHolder> {

    private List<Event> eventsList;

    public HubAdapter(List<Event> list) {
        this.eventsList = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @NonNull
    @Override
    public HubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_cards,viewGroup,false);
        return new HubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HubViewHolder holder, int position) {
        Event hub = eventsList.get(position);
        holder.setItemClickListener(TabFragment2.myItemClickListener);
        holder.bind(hub);
    }


    @Override
    public int getItemCount() {
        return eventsList.size();
    }

}