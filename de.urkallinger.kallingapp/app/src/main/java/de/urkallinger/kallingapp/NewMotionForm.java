package de.urkallinger.kallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Date;

import de.urkallinger.kallingapp.model.Motion;
import de.urkallinger.kallingapp.model.User;

public class NewMotionForm extends AppCompatActivity {

    private ViewHolder holder = new ViewHolder();
    private Motion motion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_motion_form);
        setTitle(R.string.motion_add_new);

        holder.title = (EditText) findViewById(R.id.motion_type);
        holder.description = (EditText) findViewById(R.id.motion_description);

        Intent intent = getIntent();
        motion = (Motion) intent.getSerializableExtra("Motion");
        if(motion != null) {
            holder.title.setText(motion.getTitle());
            holder.description.setText(motion.getDescription());
        } else {
            motion = new Motion("","", new User().setUsername("Urkallinger"), new Date());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.motion_add_new_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.motion_menu_save:

                if(checkRequiredFields()) {
                    motion.setTitle(holder.title.getText().toString())
                            .setDescription(holder.description.getText().toString())
                            .setCreatedAt(new Date());

                    Intent intent = new Intent();
                    intent.putExtra("Motion", motion);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;

            case R.id.motion_menu_add_attachment:
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Überprüft ob alle benötigten Textfelder ausgefüllt wurden.
     *
     * @return <code>true</code> wenn alle benötigten Felder ausgefüllt wurden, <code>false</code> wenn nicht.
     */
    private boolean checkRequiredFields() {
        boolean retVal = true;
        String errorMsg = getResources().getString(R.string.empty_textfield_error);
        if (holder.title.getText().toString().trim().isEmpty()) {
            holder.title.setError(errorMsg);
            retVal = false;
        }

        if (holder.description.getText().toString().trim().isEmpty()) {
            holder.description.setError(errorMsg);
            retVal = false;
        }

        return retVal;
    }

    static class ViewHolder {
        EditText title;
        EditText description;
    }
}
