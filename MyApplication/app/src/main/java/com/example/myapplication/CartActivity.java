package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adaptor.CartAdaptor;
import com.example.myapplication.Adaptor.ProductAdaptor;
import com.example.myapplication.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;

    private List<Product> productList;
    private List<String> getIdProductFromCart;

    TextView totalFee, tax, delivery, total, payment;
    private int total_fee, total_cost;
    private ImageView homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initView();
        initList();
        getBundle();
        returnHome();
        checkout();
    }

    private void returnHome() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });
    }

    private void initView() {
        recyclerViewList = findViewById(R.id.rcv_product);
        totalFee = findViewById(R.id.totalFee);
        tax = findViewById(R.id.tax);
        delivery = findViewById(R.id.delivery);
        total = findViewById(R.id.total);
        homeBtn = findViewById(R.id.homeButton);
        payment = findViewById(R.id.tvPayment);
    }

    private void initList(){

        LinearLayoutManager linearLayoutManager_cart = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager_cart);

        productList = new ArrayList<Product>();
        getIdProductFromCart = new ArrayList<String>();

        adapter = new CartAdaptor(this, productList);
        recyclerViewList.setAdapter(adapter);
    }

    private void getBundle() {

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewList.addItemDecoration(dividerItemDecoration);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                total_fee = 0;
                total_cost = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product.getAmount() > 0){
                        productList.add(product);
                        total_fee = total_fee + product.getAmount();
                        total_cost = total_cost + product.getAmount() * product.getPrice_normal();
                        getIdProductFromCart.add(product.getId());
                    }
                }

                totalFee.setText(String.valueOf(total_fee).concat(" item"));
                if (total_fee > 0){
                    delivery.setText("5 $");
                }
                else delivery.setText("0 $");
                tax.setText(String.valueOf((double) Math.ceil(total_cost * 0.05 * 10) / 10).concat(" $"));
                total.setText(String.valueOf(total_cost + (double) Math.ceil(total_cost * 0.05 * 10) / 10).concat(" $"));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CartActivity.this, "Get data from firebase fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkout() {
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_fee > 0) {
                    Toast.makeText(CartActivity.this, "Your order will be delivered soon!", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference ref;
                    for(String id : getIdProductFromCart){
                        ref = db.getReference("Products/" + id + "/amount");
                        ref.setValue(0);
                    }
                }
                else {
                    Toast.makeText(CartActivity.this, "Your card is empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}