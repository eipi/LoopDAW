package name.eipi.loopdaw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

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

            app.projectList.add(c);
            goToActivity(this,MainActivity.class, null);
        } else
            Toast.makeText(
                    this,
                    "You must enter a name for the project.",
                    Toast.LENGTH_SHORT).show();
    }
}
