package name.eipi.loopdaw.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import name.eipi.loopdaw.AudioSession;
import name.eipi.loopdaw.R;
import name.eipi.loopdaw.fragment.TrackFragment;
import name.eipi.loopdaw.main.LoopDAWApp;
import name.eipi.loopdaw.model.Project;
import name.eipi.loopdaw.model.Track;
import name.eipi.loopdaw.util.AudioMixer;
import name.eipi.loopdaw.util.LoopDAWLogger;

public class EditActivity extends BaseActivity {

    private static final LoopDAWLogger logger =  LoopDAWLogger.getInstance();

    TextView trackList;
    private Project project;
    boolean mStartPlaying = true;
    private AudioSession audioSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Bundle extras = getIntent().getExtras();
        int projectContext = extras.getInt("projectID", -1);
        if (projectContext == -1) {
            projectContext = savedInstanceState.getInt("projectID");
        }
        if (projectContext != -1) {
            project = ((LoopDAWApp) getApplication()).projectList.get(projectContext);
        }

        trackList = (TextView) findViewById(R.id.trackListEmpty);
        if (project != null) {
            trackFragment = TrackFragment.newInstance(project);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.track_fragment_layout, trackFragment);
            transaction.commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        int projectContext = extras.getInt("projectID", -1);
        if (projectContext != -1) {
            project = ((LoopDAWApp) getApplication()).projectList.get(projectContext);
        }

        trackList = (TextView) findViewById(R.id.trackListEmpty);

//        trackFragment = TrackFragment.newInstance(project);
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.track_fragment_layout, trackFragment);
//        transaction.commit();

        if (project != null && trackList != null) {
            if (project.getClips().isEmpty()) {
                trackList.setText(getString(R.string.trackListEmptyMessage));
            } else {
                trackList.setText(null);
            }
            project.save();
            trackFragment.notifyDataChanged();

            final ImageButton editButton = (ImageButton) findViewById(R.id.edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(final View v) {
                    // item clicked

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
                            Snackbar.make(v, "Project info saved!!",
                                    Snackbar.LENGTH_LONG).show();
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

                }
            });

            final ImageButton favouriteButton = (ImageButton) findViewById(R.id.fav_button);
            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
//                    final Project project = ((LoopDAWApp) (v.getContext().getApplicationContext())).projectList.get(getAdapterPosition());
                    project.setFavourite(!project.isFavourite());
                    favouriteButton.setImageResource(project.isFavourite() ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
                }
            });

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioSession == null) {
            audioSession = AudioSession.getInstance(this);
        }
        audioSession.play(false, project);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        int projectContext = savedInstanceState.getInt("projectID", -1);
        if (projectContext != -1) {
            project = ((LoopDAWApp) getApplication()).projectList.get(projectContext);
        }

        trackList = (TextView) findViewById(R.id.trackListEmpty);

        if (project != null && trackList != null) {
            if (project.getClips().isEmpty()) {
                trackList.setText(getString(R.string.trackListEmptyMessage));
            } else {
                trackList.setText(null);
            }
            project.save();
            trackFragment.notifyDataChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putInt("projectID", (project != null) ? app.projectList.indexOf(project) : -1);
        // etc.
    }

    public void actionNewTrack(View view) {
        Track track = Track.newInstance(project);
        project.getClips().add(track);
        trackFragment.notifyDataChanged();

    }

    public void actionPlay(View view) {

        ImageButton button = (ImageButton) view.findViewById(R.id.play_button);
        if (mStartPlaying) {
            logger.msg("EditActivity.actionPlayAll - in mStartPlaying");
            button.setImageResource(R.drawable.ic_stop_black_24dp);
        } else {
            logger.msg("EditActivity.actionPlayAll - in !mStartPlaying");
            button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
        if (audioSession == null) {
            audioSession = AudioSession.getInstance(this);
        }
        audioSession.play(mStartPlaying, project);
        mStartPlaying = !mStartPlaying;
    }

    public void actionExport(View view) {
        new AudioMixer(project).renderProject();
        //TODO - allow export of project data to Google Drive.

    }

}
