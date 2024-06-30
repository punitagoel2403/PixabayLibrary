package com.pixabay.pixabaylib.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pixabay.pixabaylib.R;
import com.pixabay.pixabaylib.model.ImagesModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridImagesAdapter extends RecyclerView.Adapter<GridImagesAdapter.GridImageViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<ImagesModel> imagesModelList;
    private final OnPreviewListener onPreviewListener;


    public GridImagesAdapter(Context context, List<ImagesModel> imagesModelList, OnPreviewListener listener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.imagesModelList = imagesModelList;
        this.onPreviewListener = listener;
    }

    @NonNull
    @Override
    public GridImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.images_item, parent, false);
        return new GridImageViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull GridImageViewHolder holder, int position) {
        ImagesModel current = imagesModelList.get(position);
        holder.setImage(current);

    }

    @Override
    public int getItemCount() {
        if (imagesModelList.size() > 0) {
            return imagesModelList.size();
        } else {
            return 0;
        }
    }

    public interface OnPreviewListener {
        void onPreviewImageClicked(String url);
    }

    public class GridImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final Context mContext;
        private String url;

        public GridImageViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_iv);
            this.mContext = context;
            imageView.setOnClickListener(this);

        }

        private void setImage(ImagesModel imagesModel) {
            Picasso.get().load(imagesModel.getWebformatURL()).resize(100, 100).centerCrop(Gravity.CENTER).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.image_iv) {
                int position = getAdapterPosition();
                url = imagesModelList.get(position).getWebformatURL();
                onPreviewListener.onPreviewImageClicked(url);
            }
        }
    }
}

