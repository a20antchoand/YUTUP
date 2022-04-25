package com.example.practica5.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practica5.R;

import java.util.List;
import java.util.Locale;

public class ListAdapterMusica  extends RecyclerView.Adapter<ListAdapterMusica.ViewHolder> {

    private List<ListElementMusica> mData;
    private LayoutInflater mInflater;
    private Context context;
    final onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClickListener(ListElementMusica item);
    }

    public ListAdapterMusica(List<ListElementMusica> itemList, Context context, onItemClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int ViewType) {
        View view = mInflater.from(parent.getContext()).inflate(R.layout.list_element_musica, null);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElementMusica> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nom, durada;

        ViewHolder(View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom);
            durada = itemView.findViewById(R.id.durada);
        }

        void bindData (final ListElementMusica item) {
            nom.setText(item.getTitol().substring(0,1).toUpperCase(Locale.ROOT) + item.getTitol().substring(1));

            int segons = item.getDurada() / 1000;

            durada.setText(segons/60 + ":" + String.format("%02d",segons%60).substring(0,2));

            itemView.setOnClickListener(view -> {
                listener.onItemClickListener(item);
            });

        }
    }

}
