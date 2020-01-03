package com.cintech.urbaincity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    ArrayList<DataCollected> datas;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    private CircleImageView imageData;

    public  DataAdapter(final String typeCollect){
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        datas = FirebaseUtil.mDatas;

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DataCollected dt = dataSnapshot.getValue(DataCollected.class);
                Log.d("Data",dt.getDescription());
                dt.setId(dataSnapshot.getKey());

                if (dt.getType().equals(typeCollect)){
                    datas.add(dt);
                    notifyItemInserted(datas.size()-1);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildListener);
    }
    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.data_list_item,parent,false);

        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.DataViewHolder holder, int position) {
        DataCollected data = datas.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDate;
        TextView tvDescription;
        TextView tvLatitude;
        TextView tvLongitude;


        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.date);
            tvDescription = (TextView) itemView.findViewById(R.id.data_description);
            tvLatitude = (TextView) itemView.findViewById(R.id.latitude);
            tvLongitude = (TextView) itemView.findViewById(R.id.longitude);
            imageData = (CircleImageView) itemView.findViewById(R.id.data_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            DataCollected selectedDeal = datas.get(position);
            Intent intent = new Intent(view.getContext(),ShowDataOnMap.class);
            intent.putExtra("Data",selectedDeal);
            view.getContext().startActivity(intent);
        }

        public void bind(DataCollected data) {


            tvDate.setText(data.getDate());
            tvDescription.setText(data.getDescription());
            tvLatitude.setText(data.getLatitude().substring(0,4));
            tvLongitude.setText(data.getLongitude().substring(0,4));
            showImage(data.getImageUrl());

        }

        private void showImage(String url){
            if (url != null && url.isEmpty() == false){
                Picasso.with(imageData.getContext())
                        .load(url)
                        .resize(220,220)
                        .centerCrop()
                        .into(imageData);
            }
        }
    }
}
