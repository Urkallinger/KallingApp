package de.urkallinger.kallingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import de.urkallinger.kallingapp.datastructure.Motion;

public class MotionDetailActivity extends AppCompatActivity {

    private ViewHolder holder;
    private Motion motion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_detail);
        setTitle("Antrag");

        Intent intent = getIntent();
        motion = (Motion) intent.getSerializableExtra("Motion");

        holder = new ViewHolder();
        holder.title = (TextView) findViewById(R.id.motion_title);
        holder.description = (TextView) findViewById(R.id.motion_description);
        holder.info = (TextView) findViewById(R.id.motion_info);

        updateView();
    }

    private void updateView() {
        holder.title.setText(motion.getTitle());
        holder.description.setText(motion.getDescription());
        holder.info.setText(motion.getCreator().getUsername() + ", " + formatDate(motion.getCreationDate()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.motion_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.motion_detail_edit:
                Intent intent = new Intent(this, NewMotionForm.class);
                intent.putExtra("Motion", motion);
                startActivityForResult(intent, 1);
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                motion = (Motion) data.getSerializableExtra("Motion");
                updateView();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Motion", motion);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String formatDate(Date date) {
        DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        TimeZone tz = TimeZone.getDefault();
        sdf.setTimeZone(tz);
        return sdf.format(calendar.getTime());
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        TextView info;
    }
}
