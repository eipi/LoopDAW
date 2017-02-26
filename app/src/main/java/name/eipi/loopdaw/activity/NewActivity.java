package name.eipi.loopdaw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.File;

import name.eipi.loopdaw.R;
import name.eipi.loopdaw.model.Project;

public class NewActivity extends BaseActivity implements View.OnClickListener {

    private String 		projectName;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Button saveButton = (Button) findViewById(R.id.saveProjectBtn);
        name = (EditText) findViewById(R.id.nameEditText);
        saveButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        projectName = name.getText().toString();

        if ((projectName.length() > 0)) {
            Project c = new Project(projectName);
            String baseFileDir = getExternalCacheDir().getAbsolutePath()
                    + File.separator + "projects" + File.separator + projectName + File.separator;
            c.setBaseFilePath(baseFileDir);
            File file = new File(baseFileDir + "project.info");
            file.getParentFile().mkdirs();
            app.projectList.add(c);
            Bundle activityInfo = new Bundle(); // Creates a new Bundle object
            int itemId = c.getId();
            activityInfo.putInt("projectID", itemId);
            goToActivity(this, EditActivity.class, activityInfo);
        } else
            Toast.makeText(
                    this,
                    "You must enter a name for the project.",
                    Toast.LENGTH_SHORT).show();
    }
}
