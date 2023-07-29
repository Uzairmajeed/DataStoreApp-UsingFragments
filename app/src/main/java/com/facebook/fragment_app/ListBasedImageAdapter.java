package com.facebook.fragment_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListBasedImageAdapter extends RecyclerView.Adapter<ListBasedImageAdapter.ListBasedImageViewHolder> {
    private Context context;
    private List<ImageData> imageDataList;

    public ListBasedImageAdapter(Context context, List<ImageData> imageDataList) {
        this.context = context;
        this.imageDataList = imageDataList;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ImageData imageData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ListBasedImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_based_image, parent, false);
        return new ListBasedImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBasedImageViewHolder holder, int position) {
        ImageData imageData = imageDataList.get(position);

        holder.textViewName.setText(imageData.getName());

        if (imageData.getImagePath() == null || imageData.getImagePath().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.image2); // Set a default image
        } else {
            Glide.with(context)
                    .load(imageData.getImagePath())
                    .placeholder(R.drawable.image2) // Set a placeholder image while loading
                    .error(R.drawable.image2) // Set an error image if loading fails
                    .into(holder.imageView);
        }

        // Set click listener for the image
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(imageData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    class ListBasedImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;

        public ListBasedImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
