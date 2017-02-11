package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import name.eipi.loopdaw.model.Project;

/**
 * Created by avd1 on 07/02/2017.
 */

public class ProjectListAdapter extends ArrayAdapter<Project> {

    public ProjectListAdapter(Context context, View.OnClickListener listener, List<Project> projects) {
        super(context, R.layout.projectLineItem, projects);
    }

    public ProjectListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public ProjectListAdapter(Context context, int resource, Project[] objects) {
        super(context, resource, objects);
    }

    public ProjectListAdapter(Context context, int resource, int textViewResourceId, Project[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public ProjectListAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);
    }

    public ProjectListAdapter(Context context, int resource, int textViewResourceId, List<Project> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
