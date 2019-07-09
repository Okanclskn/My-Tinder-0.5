package com.okan.mytinder.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.okan.mytinder.Adapters.TinderAdapter;
import com.okan.mytinder.LinkClasses.Pictures;
import com.okan.mytinder.LinkClasses.Tinder;
import com.okan.mytinder.LinkClasses.User;
import com.okan.mytinder.Profile;
import com.okan.mytinder.R;
import com.okan.mytinder.api.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TinderFragment extends Fragment {
    private ArrayList<Tinder> tinderArrayList;
    private TinderAdapter tinderAdapter;
    private SwipeFlingAdapterView swipeFlingAdapterView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tinder, container, false);
        swipeFlingAdapterView = v.findViewById(R.id.swipeFling);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        Api api = retrofit.create(Api.class);
        Call<List<Pictures>> call = api.getPictures();

        call.enqueue(new Callback<List<Pictures>>() {
            @Override
            public void onResponse(Call<List<Pictures>> call, Response<List<Pictures>> response) {

                List<Pictures> pictures = response.body();
                for (Pictures p: pictures){
                    Log.d("name",p.getName());
                    Log.d("realname",p.getRealname());
                    Log.d("imageurl",p.getImageurl());
                }
                tinderAdapter = new TinderAdapter(getContext(),pictures);
                swipeFlingAdapterView.setAdapter(tinderAdapter);

            }
            @Override
            public void onFailure(Call<List<Pictures>> call, Throwable t) {
Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
/*
        tinderArrayList =  new ArrayList<>();

        //rdUsers();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tinderArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Tinder tinder = snapshot.getValue(Tinder.class);

                    assert tinder != null;
                    assert firebaseUser != null;

                    if (!tinder.getId().equals(firebaseUser.getUid())){
                        tinderArrayList.add(tinder);

                    }
                }
                tinderAdapter = new TinderAdapter(getContext(),tinderArrayList);
                swipeFlingAdapterView.setAdapter(tinderAdapter);
                Log.d("TinderFragment", "fragment is working");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                tinderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                //tinderArrayList.remove(0);
                tinderAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"Sola kaydır",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRightCardExit(Object o) {
                //tinderArrayList.remove(0);
                tinderAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"Sağa kaydır",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
            Toast.makeText(getContext(),"Bitti",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScroll(float v) {

            }
        });

        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, Object o) {
                Tinder tinder = tinderArrayList.get(position);
                Intent intent = new Intent(getContext(), Profile.class);
                intent.putExtra("userid", tinder.getId());
                String id = tinder.getId();
                Log.i("click",String.valueOf(id));
                getContext().startActivity(intent);
                Toast.makeText(getContext(),"Tıklandı",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
/*
    private void rdUsers() {
         final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tinderArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Tinder tinder = snapshot.getValue(Tinder.class);

                    assert tinder != null;
                    assert firebaseUser != null;

                    if (!tinder.getId().equals(firebaseUser.getUid())){
                        tinderArrayList.add(tinder);

                    }
                }
                tinderAdapter = new TinderAdapter(getContext(),tinderArrayList);
                swipeFlingAdapterView.setAdapter(tinderAdapter);
                Log.d("TinderFragment", "onDataChange");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
*/
}
