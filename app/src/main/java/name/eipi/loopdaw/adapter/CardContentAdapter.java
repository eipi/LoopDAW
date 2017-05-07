package name.eipi.loopdaw.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.activity.EditActivity;
import name.eipi.loopdaw.fragment.CardContentFragment;
import name.eipi.loopdaw.fragment.FavsCardContentFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.util.NavigationUtils;

/**
 * Created by eipi on 30/04/2017.
 */

public class CardContentAdapter extends RecyclerView.Adapter<CardContentAdapter.CardViewHolder> {

    private final List<Project> content;

    public CardContentAdapter(Context context, final List contentIn) {
        Resources resources = context.getResources();
        // todo - replace with java-based resource injection.
        this.content = contentIn;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()), parent, content);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Project project = content.get(position);
        holder.title.setText(project.getName());
        holder.description.setText(content.get(position).getClips().size() + " tracks");
        holder.favouriteButton.setImageResource(project.isFavourite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
        holder.editButton.setImageResource((project.getDescription() != null && !project.getDescription().isEmpty()) ? R.drawable.ic_assignment_black_24dp : R.drawable.ic_edit_black_24dp);
        holder.currentItem = project;
    }

    @Override
    public int getItemCount() {
        return content.size();
    }



    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public Project currentItem;
        public TextView title;
        public TextView subtitle;
        public TextView description;
        public ImageButton editButton;
        public ImageButton favouriteButton;
        public ImageButton shareButton;

        public CardViewHolder(LayoutInflater inflater, final ViewGroup parent, final List<Project> projectList) {

            super(inflater.inflate(R.layout.item_card, parent, false));

            title = (TextView) itemView.findViewById(R.id.card_title);
            subtitle = (TextView) itemView.findViewById(R.id.card_subtitle);
            description = (TextView) itemView.findViewById(R.id.card_text);
            editButton = (ImageButton) itemView.findViewById(R.id.edit_button);
            favouriteButton = (ImageButton) itemView.findViewById(R.id.fav_button);
            shareButton = (ImageButton) itemView.findViewById(R.id.share_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Bundle activityInfo = new Bundle(); // Creates a new Bundle object
                    Project project = projectList.get(getAdapterPosition());
                    activityInfo.putInt("projectID", ((LoopDAWApp) (v.getContext().getApplicationContext())).projectList.indexOf(project));
                    NavigationUtils.goToActivity(v.getContext(), EditActivity.class, activityInfo);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override public boolean onLongClick(final View v) {
                final Project project = projectList.get(getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to Delete the \'Project\' " + project.getName() + "?");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        projectList.remove(project);
                        // TODO - persist delete
                        ((LoopDAWApp) v.getContext().getApplicationContext()).deleteProject(project);
                        CardContentFragment.dataList.remove(project);
                        FavsCardContentFragment.dataList.remove(project);
                        CardContentFragment.adapter.notifyDataSetChanged();
                        FavsCardContentFragment.adapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }});
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    final Project project = projectList.get(getAdapterPosition());
//                    final Project project = ((LoopDAWApp) (v.getContext().getApplicationContext())).projectList.get(getAdapterPosition());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Attach notes");
                    // I'm using fragment here so I'm using getView() to provide ViewGroup
                    // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                    View viewInflated = LayoutInflater.from(v.getContext()).inflate(R.layout.edit_project_popup, null, false);
                    // Set up the input
                    final EditText descInput = (EditText) viewInflated.findViewById(R.id.projectDescriptionInput);
                    descInput.setText(project.getDescription());
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    builder.setView(viewInflated);

                    // Set up the buttons
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            project.setDescription(descInput.getText().toString());
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                    editButton.setImageResource((project.getDescription() != null && !project.getDescription().isEmpty()) ? R.drawable.ic_assignment_black_24dp : R.drawable.ic_edit_black_24dp);
//                    Snackbar.make(v, "Hello Snackbar!",
//                            Snackbar.LENGTH_LONG).show();
                }
            });
            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
//                    final Project project = ((LoopDAWApp) (v.getContext().getApplicationContext())).projectList.get(getAdapterPosition());
                    final Project project = projectList.get(getAdapterPosition());
                    project.setFavourite(!project.isFavourite());
                    favouriteButton.setImageResource(project.isFavourite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
                }
            });
        }
    }

}
