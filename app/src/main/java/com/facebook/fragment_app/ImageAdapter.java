package com.facebook.fragment_app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private Fragment parentFragment;
    private List<ImageData> imageDataList;
    private int imageWidth;
    private int imageHeight;
    private OnItemClickListener onItemClickListener;
    private boolean isGridBased;
    private int gridSpanCount;

    public interface OnItemClickListener {
        void onItemClick(ImageData imageData);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public ImageAdapter(Context context, List<ImageData> imageDataList, boolean isGridBased) {
        this.context = context;
        this.imageDataList = imageDataList;
        this.isGridBased = isGridBased;
        this.gridSpanCount = calculateGridSpanCount();
    }

    private int calculateGridSpanCount() {
        int orientation = context.getResources().getConfiguration().orientation;
        if (isGridBased && orientation == Configuration.ORIENTATION_PORTRAIT) {
            return 3;
        } else {
            return 6;
        }
    }


    public void setGridSpanCount() {
        this.gridSpanCount = calculateGridSpanCount();
        notifyDataSetChanged();
    }

    public void setImageDimensions(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
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
        if (isGridBased) {
            // Adjust the image view size for grid layout
            adjustItemSize(holder.itemView);
        } else {
            // Reset the image view size for list layout
            resetItemSize(holder.itemView);
        }


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(imageData);
                }
            }
        });

    }

    private void adjustItemSize(View itemView) {
        LayoutParams layoutParams = itemView.getLayoutParams();
        if (isGridBased) {
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int itemWidth = screenWidth / gridSpanCount;

            layoutParams.width = itemWidth;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
        } else {
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.height = LayoutParams.WRAP_CONTENT;
        }
        itemView.setLayoutParams(layoutParams);
    }


    public void setGridBased(boolean gridBased) {
        isGridBased = gridBased;
        notifyDataSetChanged();
    }


    public void resetItemSize(View itemView) {
        LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        itemView.setLayoutParams(layoutParams);
    }

    public void setGridSpanCount(int spanCount) {
        this.gridSpanCount = spanCount;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return imageDataList.size();
    }

    public ImageData getImageData(int position) {
        if (position >= 0 && position < imageDataList.size()) {
            return imageDataList.get(position);
        }
        return null;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }
}
