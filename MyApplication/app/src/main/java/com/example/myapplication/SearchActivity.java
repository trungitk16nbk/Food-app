package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Adaptor.ProductAdaptor;
import com.example.myapplication.Model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rcv_Detail;
    private RecyclerView.Adapter productAdapter;

    private ArrayList<Product> productList;

    private LinearLayout homeBtn;
    private LinearLayout mapBtn;
    private LinearLayout noticeBtn;
    private LinearLayout profileBtn;
    private FloatingActionButton cartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initUi();
        getDataFromFirebase();
        buttonBottomApp();
    }

    private void initUi() {
        rcv_Detail = findViewById(R.id.rcv_detail_search);
        rcv_Detail.setHasFixedSize(true);

        LinearLayoutManager layoutManager_detail = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_Detail.setLayoutManager(layoutManager_detail);

        productList = new ArrayList<Product>();

        productAdapter = new ProductAdaptor(this, productList);
        rcv_Detail.setAdapter(productAdapter);

        homeBtn = findViewById(R.id.homeBtn);
        mapBtn = findViewById(R.id.mapBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        profileBtn = findViewById(R.id.profileBtn);
        cartBtn = findViewById(R.id.cartBtn);
    }

    private void getDataFromFirebase(){
        String keySearch = (String) getIntent().getSerializableExtra("keySearch");

        Toast.makeText(SearchActivity.this, keySearch, Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product.getName().toLowerCase(Locale.ROOT).contains(keySearch.toLowerCase(Locale.ROOT))){
                        productList.add(product);
                    }
                }

                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SearchActivity.this, "Get data from firebase fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buttonBottomApp() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, MapsActivity.class));
            }
        });

        // nhay den notification
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, NoticeActivity.class));
            }
        });

        // nhay den profile
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
            }
        });


        // nhay den cart
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this, CartActivity.class));
            }
        });
    }
}