package com.epitech.william.photomapper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.epitech.paul.photomapper.GalleryController;
import com.epitech.paul.photomapper.LocatedPicture;

/**
 * Created by Paul on 16/03/2016.
 * Maps controller extends gallery's because maps layout include gallery's.
 */
public class MapsController extends GalleryController {

    private MapsFragment mMapsFragment;

    public MapsController(MainActivity mainActivity, MapsFragment mapsFragment) {
        super(mainActivity);
        mMapsFragment = mapsFragment;
        mCanGotoMap = false;
    }

    @Override
    public boolean onListItemClicked(RecyclerView.ViewHolder viewHolder, LocatedPicture locatedPicture) {
        if (super.onListItemClicked(viewHolder, locatedPicture)) {
            if (mDeleteMode) {
                // todo delete marker
            } else {
                mMapsFragment.changeSelectedMarker(viewHolder.getAdapterPosition());
            }
        }
        return true;
    }
}
