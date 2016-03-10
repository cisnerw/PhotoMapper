package com.epitech.william.photomapper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import com.epitech.paul.photomapper.LocatedPicture;

/**
 * Created by willi_000 on 25/02/2016.
 */
public class LocatedPictureAdapter extends RecyclerView.Adapter<LocatedPictureAdapter.LocatedPictureViewHolder> {

    private List<LocatedPicture> mList;

    public LocatedPictureAdapter(List<LocatedPicture> list) {
        mList = list;
    }

    @Override
    public LocatedPictureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_layout, parent, false);

        return new LocatedPictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocatedPictureViewHolder holder, int position) {
        LocatedPicture locatedPicture = mList.get(position);
        File imgFile = new File(locatedPicture.getPicturePath());
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(locatedPicture.getPicturePath());
            holder.vImageView.setImageBitmap(bitmap);
            holder.vTextView.setText(locatedPicture.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class LocatedPictureViewHolder extends RecyclerView.ViewHolder {

        protected ImageView vImageView;
        protected TextView vTextView;
        public LocatedPictureViewHolder(View itemView) {
            super(itemView);
            vImageView = (ImageView) itemView.findViewById(R.id.item_img);
            vTextView = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
