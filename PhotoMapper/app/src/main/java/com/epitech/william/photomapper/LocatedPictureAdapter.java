package com.epitech.william.photomapper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import com.epitech.paul.photomapper.BitmapHelper;
import com.epitech.paul.photomapper.LocatedPicture;

/**
 * Created by willi_000 on 25/02/2016.
 */
public class LocatedPictureAdapter extends RecyclerView.Adapter<LocatedPictureAdapter.LocatedPictureViewHolder> {

    private class LocatedPictureLoadingParams {

        public LocatedPictureLoadingParams(LocatedPicture locatedPicture, ImageView imageView, TextView textView) {
            this.imageView = imageView;
            this.textView = textView;
            this.locatedPicture = locatedPicture;
        }

        public ImageView imageView;
        public TextView textView;
        public LocatedPicture locatedPicture;
    };

    private class LocatedPictureLoadingTask extends AsyncTask<LocatedPictureLoadingParams, Void, Bitmap> {

        LocatedPictureLoadingParams params = null;

        // Méthode exécutée au début de l'execution de la tâche asynchrone
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... result){
            super.onProgressUpdate(result);
        }

        @Override
        protected Bitmap doInBackground(LocatedPictureLoadingParams... arg0) {
            params = arg0[0];
            LocatedPicture locatedPicture = arg0[0].locatedPicture;
            File imgFile = new File(locatedPicture.getPicturePath());
            if (imgFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(locatedPicture.getPicturePath(), options);

                int width = options.outWidth;
                int dimension = (int) mResources.getDimension(R.dimen.thumbnail_size);
                options.inJustDecodeBounds = false;
                options.inSampleSize = Math.max(1, width / dimension);
                return BitmapFactory.decodeFile(locatedPicture.getPicturePath(), options);
            }
            return null;
        }

        // Méthode exécutée à la fin de l'execution de la tâche asynchrone
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            if (params != null && result != null) {
                params.imageView.setImageBitmap(result);
                params.textView.setText(params.locatedPicture.getTitle());
            }
        }
    }

    private List<LocatedPicture> mList;
    private Resources mResources;
    private static OnItemClickListener itemClickListener = null;

    public LocatedPictureAdapter(List<LocatedPicture> list, Resources resources) {
        mList = list;
        mResources = resources;
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
        LocatedPictureLoadingTask loadingTask = new LocatedPictureLoadingTask();
        loadingTask.execute(new LocatedPictureLoadingParams(locatedPicture, holder.vImageView, holder.vTextView));

//        File imgFile = new File(locatedPicture.getPicturePath());
//        if (imgFile.exists()) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            Bitmap bitmap = BitmapFactory.decodeFile(locatedPicture.getPicturePath(), options);
//            options.inSampleSize = Math.max(1, (int) (options.outWidth / mResources.getDimension(R.dimen.thumbnail_size)));
//            options.inJustDecodeBounds = false;
//            bitmap = BitmapFactory.decodeFile(locatedPicture.getPicturePath(), options);
//
//            if (bitmap != null) {
//                holder.vImageView.setImageBitmap(bitmap);
//                holder.vTextView.setText(locatedPicture.getTitle());
//            }
//        }
    }

    public void setItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class LocatedPictureViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{

        protected ImageView vImageView;
        protected TextView vTextView;
        public LocatedPictureViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            vImageView = (ImageView) itemView.findViewById(R.id.item_img);
            vTextView = (TextView) itemView.findViewById(R.id.item_title);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
