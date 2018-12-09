package com.nomadhub.nomadhub;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HubViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView titleTV;
    private TextView addressTV;
    private ImageView imageView;
    private TextView dateTV;
    ItemClickListener itemClickListener;

    public HubViewHolder(View itemView){
        super(itemView);

        titleTV = itemView.findViewById(R.id.titleHub);
        addressTV = itemView.findViewById(R.id.subTitleHub);
        imageView = itemView.findViewById(R.id.imageHub);
        dateTV = itemView.findViewById(R.id.dateHub);
        itemView.setOnClickListener(this);

    }


    public void bind(Event hub){
        titleTV.setText(Html.fromHtml(hub.getTitle()));
        addressTV.setText(hub.getAddress());
        dateTV.setText(hub.getDate());
        imageView.setImageBitmap(hub.getImage());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
}