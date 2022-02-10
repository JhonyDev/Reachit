package com.lma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lma.R;
import com.lma.info.Info;
import com.lma.model.Device;
import com.lma.model.Super;

import java.util.List;


public class TypeRecyclerViewAdapter extends RecyclerView.Adapter<TypeRecyclerViewHolder> implements Info {
    Context context;
    List<Super> listInstances;
    int type;

    public TypeRecyclerViewAdapter(Context context, List<Super> listInstances, int type) {
        this.context = context;
        this.listInstances = listInstances;
        this.type = type;
    }

    @NonNull
    @Override
    public TypeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeRecyclerViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.rv_devices, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeRecyclerViewHolder holder, int position) {
        initNotices(holder, position);
    }

    private void initNotices(TypeRecyclerViewHolder holder, int position) {
        Device device = (Device) listInstances.get(position);
        holder.tvName.setText(device.getName());
        holder.tvIMEI.setText(device.getIMEI());
        holder.tvModelNumber.setText(device.getModelNumber());
        holder.btnMap.setOnClickListener(view -> btnMap());
        holder.btnRing.setOnClickListener(view -> btnRing());
        holder.btnReqCall.setOnClickListener(view -> btnReqCall());
    }

    private void btnReqCall() {
//        TODO: REQUEST A CALL FROM END DEVICE\
        /**
         * SEND MESSAGE TO THE DEVICE
         *
         * */

    }

    private void btnRing() {
//        TODO: RING THE DEVICE

    }

    private void btnMap() {
//        TODO: GET LOCATION AND DISPLAY IT ON THE MAP

    }

    @Override
    public int getItemCount() {
        return listInstances.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }
}
