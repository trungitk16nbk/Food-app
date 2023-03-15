package com.example.myapplication.Adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.ManagementCart;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.ViewHolder> {
    private List<Product> productList;
    StorageReference refStorage;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    public CartAdaptor(Context context , List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvProductPrice.setText(String.valueOf(product.getPrice_normal()).concat(" $"));
        holder.tvNumOfProduct.setText(String.valueOf(product.getAmount()));
        holder.tvTotalPrice.setText(String.valueOf(product.getAmount() * product.getPrice_normal()).concat(" $"));

        refStorage = FirebaseStorage.getInstance().getReference().child("Products/" + product.getId() + ".png");
        ref = db.getReference("Products/" + product.getId() + "/amount");
        try {
            final File localFile = File.createTempFile(product.getId(), "png");
            refStorage.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Toast.makeText(ProductPopularAdaptor, "Get Image", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.ivPicProduct.setImageBitmap(bitmap);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setAmount(product.getAmount() + 1);
                DatabaseReference ref_2 = db.getReference("Products/" + product.getId() + "/amount");
                ref_2.setValue(product.getAmount());
            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getAmount() > 0) {
                    product.setAmount(product.getAmount() - 1);
                    DatabaseReference ref_2 = db.getReference("Products/" + product.getId() + "/amount");
                    ref_2.setValue(product.getAmount());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvProductPrice, tvTotalPrice, tvNumOfProduct;
        ImageView ivPicProduct, ivMinus, ivPlus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName_01);
            tvProductPrice = itemView.findViewById(R.id.tv_ProductPrice);
            tvTotalPrice = itemView.findViewById(R.id.tv_TotalPrice);
            tvNumOfProduct = itemView.findViewById(R.id.tv_numOfProduct);
            ivPicProduct = itemView.findViewById(R.id.ivAvatar_01);
            ivMinus = itemView.findViewById(R.id.iv_minus);
            ivPlus = itemView.findViewById(R.id.iv_plus);
        }
    }
}
