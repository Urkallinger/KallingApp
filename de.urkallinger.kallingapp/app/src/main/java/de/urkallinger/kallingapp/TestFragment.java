package de.urkallinger.kallingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.urkallinger.kallingapp.data.DataHandler;
import de.urkallinger.kallingapp.data.KallingCallback;
import de.urkallinger.kallingapp.datastructure.Motion;
import de.urkallinger.kallingapp.datastructure.User;
import de.urkallinger.kallingapp.datastructure.params.Param;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;


public class TestFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private ViewHolder holder;
    private DataHandler dataHandler = DataHandler.getInstance();


    public TestFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);
        getActivity().setTitle("TEST");

        setHasOptionsMenu(true);

        holder = new ViewHolder();
        holder.title = (TextView) rootView.findViewById(R.id.test_title);
        holder.description = (TextView) rootView.findViewById(R.id.test_description);
        holder.start = (Button) rootView.findViewById(R.id.test_start);
        holder.clear = (Button) rootView.findViewById(R.id.test_clear);

        holder.title.setText("TEST");
        holder.description.setText("");

        holder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDescription("", false);
            }
        });

        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMotionTitles();
            }
        });

        return rootView;
    }

    private void showUserNameForId(String id) {
        dataHandler.getUser(id, new KallingCallback<User>() {
            @Override
            public void onFailure(Exception e) {
                updateDescription("failed", true);
            }

            @Override
            public void onSuccess(User u) {
                updateDescription(u.getUsername(), true);
            }
        });
    }

    private void login() {
        dataHandler.login("Kallingari", "Kallinga", new KallingCallback<Boolean>() {
            @Override
            public void onFailure(Exception e) {
                updateDescription("failed", true);
            }

            @Override
            public void onSuccess(Boolean data) {
                updateDescription(data.toString() + "\n", true);
            }
        });
    }

    private void showUserNames() {
        dataHandler.getUsers(new KallingCallback<List<User>>() {
            @Override
            public void onFailure(Exception e) {
                updateDescription("failed", true);
            }

            @Override
            public void onSuccess(List<User> data) {
                for (User u : data) {
                    updateDescription(u.getUsername(), true);
                }
            }
        });
    }

    private void showMotionTitles() {
        dataHandler.getMotions(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                updateDescription("FAILED", false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ObjectMapper mapper = new ObjectMapper();
                String body = response.body().string();
                if (response.isSuccessful()) {
                    List<Motion> motionList = Arrays.asList(mapper.readValue(body, Motion[].class));

                    for(Motion m : motionList) {
                        updateDescription(m.getTitle() + "\n", true);
                    }
                } else {
                    Param.Message msg = mapper.readValue(body, Param.Message.class);
                    updateDescription(response.code() + ": " + msg.getMsg() + "\n", true);
                }
            }
        });
    }

    private void updateDescription(final String description, final boolean append) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(append) {
                    holder.description.append(description);
                } else {
                    holder.description.setText(description);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear(); // Altes Menü entfernen

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                break;
        }

        return false;
    }

    static class ViewHolder {
        TextView title;
        TextView description;
        Button start;
        Button clear;
    }
}