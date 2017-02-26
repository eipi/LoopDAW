package name.eipi.loopdaw.fragment;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import name.eipi.loopdaw.activity.BaseActivity;
import name.eipi.loopdaw.activity.RecordActivity;
import name.eipi.loopdaw.adapter.ProjectListAdapter;
import name.eipi.loopdaw.model.Project;

/**
 * Created by avd1 on 07/02/2017.
 */

public class ProjectFragment extends ListFragment implements View.OnClickListener {

    private static final ProjectFragment INSTANCE;
    protected BaseActivity activity;
    public static ProjectListAdapter listAdapter;
    protected ListView listView;

    static {
        INSTANCE = new ProjectFragment();
    }

    public ProjectFragment getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listAdapter = new ProjectListAdapter(activity, this, activity.app.projectList);
        setListAdapter(listAdapter);
    }

    public ProjectFragment() {
        // Required empty public constructor
    }

    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        this.activity = (BaseActivity) context;
    }



    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Project)
        {
            onProjectDelete ((Project) view.getTag());
        }
    }

    private void onProjectDelete(final Project project)
    {
        String stringName = project.getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to Delete the \'Project\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                activity.app.projectList.remove(project); // remove from our list
                listAdapter.projectList.remove(project); // update adapters data
                listAdapter.notifyDataSetChanged(); // refresh adapter
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
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        int itemId = activity.app.projectList.get(position).getId();
        activityInfo.putInt("projectID", itemId);

        Intent goEdit = new Intent(getActivity(), RecordActivity.class); // Creates a new Intent
    /* Add the bundle to the intent here */
        goEdit.putExtras(activityInfo);
        getActivity().startActivity(goEdit); // Launch the Intent
    }
}
