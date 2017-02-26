package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.model.Project;

/**
 * Created by avd1 on 07/02/2017.
 */

public class ProjectItem {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    View view;

    public ProjectItem(Context context, ViewGroup parent,
                      View.OnClickListener deleteListener, Project project)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.project_line_item, parent, false);
        view.setId(project.getId());

        updateControls(project);

        ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
        imgDelete.setTag(project);
        imgDelete.setOnClickListener(deleteListener);
    }

    private void updateControls(Project project) {
        ((TextView) view.findViewById(R.id.rowProjectName)).setText(project.getName());
        ImageView imgIcon = (ImageView) view.findViewById(R.id.RowImage);

        if (project.isFavourite() == true) {
            imgIcon.setImageResource(R.drawable.ic_favourite_on);
        } else {
            imgIcon.setImageResource(R.drawable.ic_favourite_off);
        }

    }

}
