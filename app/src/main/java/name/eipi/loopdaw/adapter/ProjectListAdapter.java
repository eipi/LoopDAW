package name.eipi.loopdaw.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.model.Project;

/**
 * Created by avd1 on 07/02/2017.
 */

public class ProjectListAdapter extends ArrayAdapter<Project> {

    private Context context;
    private View.OnClickListener deleteListener;
    public List<Project> projectList;

    public ProjectListAdapter(Context context, View.OnClickListener deleteListener, List<Project> projectList)
    {
        super(context, R.layout.project_line_item, projectList);

        this.context = context;
        this.deleteListener = deleteListener;
        this.projectList = projectList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProjectItem item = new ProjectItem(context, parent, deleteListener,
                projectList.get(position));
        return item.view;
    }

    @Override
    public int getCount()
    {
        return projectList.size();
    }

    @Override
    public Project getItem(int position)
    {
        return projectList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getPosition(Project c)
    {
        return projectList.indexOf(c);
    }

}
