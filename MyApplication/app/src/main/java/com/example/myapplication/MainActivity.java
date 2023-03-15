package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Adaptor.CategoryAdaptor;
import com.example.myapplication.Adaptor.ProductPopularAdaptor;
import com.example.myapplication.Model.Category;
import com.example.myapplication.Model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView rcv_CategoryList;

    private RecyclerView rcv_ProductPopularList;
    private ProductPopularAdaptor productPopularAdaptor;
    private List<Product> productList;


    private LinearLayout homeBtn;
    private LinearLayout mapBtn;
    private LinearLayout noticeBtn;
    private LinearLayout profileBtn;
    private FloatingActionButton cartBtn;
    private ImageView profileImg;
    private ImageView searchBtn;
    private EditText keySearchText;

    private String keySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        recyclerViewCategory();
        recycleViewProductPopular();

        handleButton();
    }

    private void handleButton() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        // nhay den notification
        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoticeActivity.class));
            }
        });

        // nhay den profile
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });


        // nhay den cart
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        // search san pham
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get keysearch
                keySearch = keySearchText.getText().toString();

                Intent intent = new Intent(searchBtn.getContext(), SearchActivity.class);
                intent.putExtra("keySearch", keySearch);
                searchBtn.getContext().startActivity((intent));
            }
        });
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcv_CategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<Category> category = new ArrayList<>();
        category.add(new Category("CT001", "Burger", "cat1_burger_foreground"));
        category.add(new Category("CT002","Chicken", "cat2_chicken_foreground"));
        category.add(new Category("CT003","Potato", "cat3_potato_foreground"));
        category.add(new Category("CT004","Hotdog", "cat2_hotdog_foreground"));
        category.add(new Category("CT005","Pizza", "cat5_pizza_foreground"));
        category.add(new Category("CT006","Drinks", "cat6_drink_foreground"));

        adapter = new CategoryAdaptor(category);
        rcv_CategoryList.setAdapter((adapter));
    }

    private void initUi() {
        rcv_CategoryList = findViewById(R.id.rcv_category);
        rcv_ProductPopularList = findViewById(R.id.rcv_popular);

        homeBtn = findViewById(R.id.homeBtn);
        mapBtn = findViewById(R.id.mapBtn);
        noticeBtn = findViewById(R.id.noticeBtn);
        profileBtn = findViewById(R.id.profileBtn);
        cartBtn = findViewById(R.id.cartBtn);
        profileImg = findViewById(R.id.profileImg);
        searchBtn = findViewById(R.id.searchBtn);
        keySearchText = findViewById(R.id.keySearch);
    }

    private void recycleViewProductPopular() {

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        rcv_ProductPopularList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcv_ProductPopularList.addItemDecoration(dividerItemDecoration);

        productList = new ArrayList<>();
        productPopularAdaptor = new ProductPopularAdaptor(productList);
        rcv_ProductPopularList.setAdapter((productPopularAdaptor));


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Products");

//        productList.add(new Product("0", "1", "2", "3", "4", 5, 6, 7));
//        Toast.makeText(MainActivity.this, productList.get(0).toString(), Toast.LENGTH_SHORT).show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                productList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    i++;
                    if (i % 3 == 0) {
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
                    }
                }

                productPopularAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Get List Product Popular Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}