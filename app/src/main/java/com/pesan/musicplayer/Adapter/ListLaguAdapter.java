package com.pesan.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pesan.musicplayer.Model.ModelListMusic;
import com.pesan.musicplayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListLaguAdapter extends RecyclerView.Adapter<com.pesan.musicplayer.Adapter.ListLaguAdapter.ViewHolder> {

    private final List<ModelListMusic> items;
    private final com.pesan.musicplayer.Adapter.ListLaguAdapter.onSelectData onSelectData;
    private final Context mContext;

    public interface onSelectData {
        void onSelected(ModelListMusic modelListLagu);
    }

    public ListLaguAdapter(Context context, List<ModelListMusic> items, com.pesan.musicplayer.Adapter.ListLaguAdapter.onSelectData xSelectData) {
        this.mContext = context;
        this.items = items;
        this.onSelectData = xSelectData;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_music, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelListMusic data = items.get(position);

        //Get Image
        Glide.with(mContext)
                .load(data.strCoverLagu)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.drawable.ic_broken_image)
                .into(holder.imgCover);

        holder.tvBand.setText(data.strNamaBand);
        holder.tvTitleMusic.setText(data.strJudulMusic);
        holder.cvListMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectData.onSelected(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Class Holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvBand;
        public TextView tvTitleMusic;
        public CardView cvListMusic;
        public ImageView imgCover;

        public ViewHolder(View itemView) {
            super(itemView);
            cvListMusic = itemView.findViewById(R.id.cvListMusic);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvBand = itemView.findViewById(R.id.tvBand);
            tvTitleMusic = itemView.findViewById(R.id.tvTitleMusic);
        }
    }

}
