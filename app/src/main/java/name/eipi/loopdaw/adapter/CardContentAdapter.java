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
        content = contentIn;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()), parent);

    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Project project = content.get(position);
        holder.title.setText(project.getName());
        holder.description.setText(content.get(position).getClips().size() + " tracks");
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

        public CardViewHolder(LayoutInflater inflater, final ViewGroup parent) {

            super(inflater.inflate(R.layout.item_card, parent, false));
            title = (TextView) itemView.findViewById(R.id.card_title);
            subtitle = (TextView) itemView.findViewById(R.id.card_subtitle);
            description = (TextView) itemView.findViewById(R.id.card_text);
            editButton = (ImageButton) itemView.findViewById(R.id.edit_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Bundle activityInfo = new Bundle(); // Creates a new Bundle object
                    activityInfo.putInt("projectID", getAdapterPosition());
                    NavigationUtils.goToActivity(v.getContext(), EditActivity.class, activityInfo);
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    final Project project = ((LoopDAWApp) (v.getContext().getApplicationContext())).projectList.get(getAdapterPosition());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Edit Project");
                    // I'm using fragment here so I'm using getView() to provide ViewGroup
                    // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                    View viewInflated = LayoutInflater.from(v.getContext()).inflate(R.layout.edit_project_popup, null, false);
                    // Set up the input
                    final EditText nameInput = (EditText) viewInflated.findViewById(R.id.projectNameInput);
                    final EditText descInput = (EditText) viewInflated.findViewById(R.id.projectDescriptionInput);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    builder.setView(viewInflated);

                    // Set up the buttons
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            project.setName(nameInput.getText().toString());
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
                    Snackbar.make(v, "Hello Snackbar!",
                            Snackbar.LENGTH_LONG).show();
                }
            });

        }
    }

}
