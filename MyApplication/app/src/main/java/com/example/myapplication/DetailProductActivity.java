package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailProductActivity extends AppCompatActivity {

    private TextView tv_title, tv_price, tv_numOfProduct, tv_description;
    private ImageView iv_picProduct, iv_plus, iv_minus;
    private LinearLayout add_button;
    private Product product;
    int numberOrder = 1;
    private ImageView homeBtn;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    StorageReference refStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);


        initView();
        getBundle();
        returnHome();
    }

    private void returnHome() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailProductActivity.this, MainActivity.class));
            }
        });
    }

    private void initView()
    {
        tv_title = findViewById(R.id.tvTitle);
        tv_price = findViewById(R.id.tv_price);
        tv_numOfProduct = findViewById(R.id.tvNumOfProduct);
        tv_description = findViewById(R.id.tv_Description);

        iv_picProduct = findViewById(R.id.ivPicProduct);
        iv_minus = findViewById(R.id.ivMinus);
        iv_plus = findViewById(R.id.ivPlus);

        add_button = findViewById(R.id.add_button);

        homeBtn = findViewById(R.id.homeButton);
    }

    private void getBundle()
    {
        product = (Product) getIntent().getSerializableExtra("object");

        ref = db.getReference("Products/" + product.getId() + "/amount");

        tv_title.setText(product.getName());
        tv_price.setText(String.valueOf(product.getPrice_normal()).concat(" $"));
        tv_description.setText(product.getDescription());
        tv_numOfProduct.setText(String.valueOf(numberOrder));

        refStorage = FirebaseStorage.getInstance().getReference().child("Products/" + product.getId() + ".png");
        try {
            final File localFile = File.createTempFile(product.getId(), "png");
            refStorage.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Toast.makeText(ProductPopularAdaptor, "Get Image", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            iv_picProduct.setImageBitmap(bitmap);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberOrder += 1;
                tv_numOfProduct.setText(String.valueOf(numberOrder));
            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOrder > 1) {
                    numberOrder = numberOrder - 1;
                }
                tv_numOfProduct.setText(String.valueOf(numberOrder));
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change amount database on firebase
                ref.setValue(numberOrder);

                Toast.makeText(DetailProductActivity.this, "Added product to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}