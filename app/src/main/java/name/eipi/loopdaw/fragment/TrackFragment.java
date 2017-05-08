package name.eipi.loopdaw.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.File;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.activity.BaseActivity;
import name.eipi.loopdaw.activity.RecordActivity;
import name.eipi.loopdaw.activity.ViewerActivity;
import name.eipi.loopdaw.adapter.TrackListAdapter;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;

/**
 * Created by avd1 on 07/02/2017.
 */

public class TrackFragment extends ListFragment implements View.OnClickListener {

    protected BaseActivity activity;
    public TrackListAdapter listAdapter;
    protected ListView listView;
    private Project project;

    public void notifyDataChanged() {
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            int projectId = savedInstanceState.getInt("projectID");
            project = ((LoopDAWApp) activity.getApplication()).projectList.get(projectId);
        }
        if (project != null) {
            listAdapter = new TrackListAdapter(activity, this, project.getClips());
            setListAdapter(listAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("projectID", (project != null) ? ((LoopDAWApp) activity.getApplication()).projectList.indexOf(project) : -1);
        // etc.
    }


    public TrackFragment() {
        // Required empty public constructor
    }

    public static TrackFragment newInstance(Project project) {
        TrackFragment fragment = new TrackFragment();
        fragment.project = project;
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
        if (view.getTag() instanceof Track)
        {
            onTrackDelete ((Track) view.getTag());
        }
    }

    private void onTrackDelete(final Track track)
    {
        int stringName = track.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to Delete the \'Track\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                project.getClips().remove(track); // remove from our list
                listAdapter.trackList.remove(track); // update adapters data
                listAdapter.notifyDataSetChanged(); // refresh adapter
                File trackFile = new File(track.getFilePath());
                trackFile.delete();
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
        Activity host = (Activity) v.getContext();
        LoopDAWApp app = (LoopDAWApp) host.getApplication();
        int projId = app.projectList.indexOf(project);

        Bundle activityInfo = new Bundle(); // Creates a new Bundle object
        activityInfo.putInt("projectID", projId);
        activityInfo.putInt("trackID", position);

        String fName = project.getClips().get(position).getFilePath();
        File file;

        if (fName != null && (file = new File(fName)).exists()) {
            Intent goView = new Intent(getActivity(), ViewerActivity.class);
            goView.putExtras(activityInfo);
            getActivity().startActivity(goView); // Launch the Intent
        } else {
            Intent goRecord = new Intent(getActivity(), RecordActivity.class);
            goRecord.putExtras(activityInfo);
            getActivity().startActivity(goRecord); // Launch the Intent
        }
    }
}
