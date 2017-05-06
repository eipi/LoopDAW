package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.activity.EditActivity;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.util.NavigationUtils;

/**
 * Created by eipi on 30/04/2017.
 */

public class CardContentAdapter extends RecyclerView.Adapter<CardContentAdapter.CardViewHolder> {

    private final String[] mPlaces;
    private final String[] mPlaceDesc;
    private final Drawable[] mPlacePictures;

    private final List<Project> content;

    public CardContentAdapter(Context context, final List contentIn) {
        Resources resources = context.getResources();
        // todo - replace with java-based resource injection.
        content = contentIn;
        mPlaces = resources.getStringArray(R.array.places);
        mPlaceDesc = resources.getStringArray(R.array.place_desc);
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        mPlacePictures = new Drawable[a.length()];
        for (int i = 0; i < mPlacePictures.length; i++) {
            //smPlacePictures[i] = a.getDrawable(i);
        }
        a.recycle();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()), parent);

    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);
        holder.name.setText(content.get(position).getName());
        holder.description.setText(content.get(position).getClips().size() + " tracks");
        holder.currentItem = content.get(position);
    }

    @Override
    public int getItemCount() {
        return content.size();
    }



    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public Object currentItem;
        public ImageView picture;
        public TextView name;
        public TextView description;

        public CardViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Bundle activityInfo = new Bundle(); // Creates a new Bundle object
                    activityInfo.putInt("projectID", getAdapterPosition());

                    NavigationUtils.goToActivity(v.getContext(), EditActivity.class, activityInfo);
                }
            });
        }
    }

}
