package com.example.myapplication.Adaptor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailProductActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Model.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductPopularAdaptor extends RecyclerView.Adapter<ProductPopularAdaptor.ProductPopularViewHolder> {

    private List<Product> productList;
    private StorageReference refStorage;

    public ProductPopularAdaptor(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);

        return new ProductPopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPopularViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        holder.productName.setText(product.getName());
        holder.productPrice.setText((String.valueOf(product.getPrice_sale())));

        refStorage = FirebaseStorage.getInstance().getReference().child("Products/" + product.getId() + ".png");
        try {
            final File localFile = File.createTempFile(product.getId(), "png");
            refStorage.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Toast.makeText(ProductPopularAdaptor, "Get Image", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.productImage.setImageBitmap(bitmap);
                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.productDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailProductActivity.class);
                intent.putExtra("object", productList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class ProductPopularViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private TextView productPrice;
        private ImageView productImage;
        private Button productDetail;
        private LinearLayout viewholder_popular;

        public ProductPopularViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productDetail = itemView.findViewById(R.id.productDetail);
            viewholder_popular = itemView.findViewById(R.id.viewholder_popular);
        }
    }

}
