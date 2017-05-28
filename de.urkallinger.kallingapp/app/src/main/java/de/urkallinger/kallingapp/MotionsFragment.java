package de.urkallinger.kallingapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.urkallinger.kallingapp.model.Motion;
import de.urkallinger.kallingapp.model.User;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Philipp on 15.12.2016.
 */

public class MotionsFragment extends Fragment {

    private MotionArrayAdapter motionArrayAdapter;

    public MotionsFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_motions, container, false);
        getActivity().setTitle(R.string.nav_motions);

        final List<Motion> motionList = new ArrayList<>();

        User uk = new User().setUsername("Urkallinger");

        motionList.add(new Motion("Antrag auf Erweiterung des KGB",
                "Hiermit beantrage ich eine Erweiterung des KGB. Der §3 sollte um xy erweitert werden.",
                uk,
                new Date()));
        motionList.add(new Motion("Antrag auf Bestrafung von Christian Chrip Billig",
                "Hiermit wird die Bestrafung von Chrip benatragt.",
                uk,
                new Date()));
        motionList.add(new Motion("Antrag auf Kallingratssitzung am 10.11.2017",
                "Ich beantrage eine Kallingratssitzung am 10.11.2017 wegen xy.",
                uk,
                new Date()));
        motionList.add(new Motion("Antrag auf Bau einer neuen Garage",
                "Kalling benötigt eine weitere Garage für noch mehr Fahrzeuge.",
                uk,
                new Date()));
        motionList.add(new Motion("Antrag auf Kallingratssitzung am 3.3.2017",
                "Ich beantrage eine Kallingratssitzung am 10.11.2017 wegen xy.",
                uk,
                new Date()));
        motionList.add(new Motion("Antrag auf echt langen Antrag",
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n" +
                        "\n" +
                        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n" +
                        "\n" +
                        "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.   \n" +
                        "\n" +
                        "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.   \n" +
                        "\n" +
                        "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis.   \n" +
                        "\n" +
                        "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, At accusam aliquyam diam diam dolore dolores duo eirmod eos erat, et nonumy sed tempor et et invidunt justo labore Stet clita ea et gubergren, kasd magna no rebum. sanctus sea sed takimata ut vero voluptua. est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur",
                uk,
                new Date()));

        motionArrayAdapter = new MotionArrayAdapter(
                getActivity(),
                R.layout.motion_list_item,
                R.id.motion_list_item_textview,
                motionList
        );

        ListView listView = (ListView) rootView.findViewById(R.id.motion_list);
        listView.setAdapter(motionArrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Motion motion = motionArrayAdapter.getItem(position);
                Log.d("CLICK", motion.getTitle());

                Intent intent = new Intent(getActivity(), MotionDetailActivity.class);
                intent.putExtra("Motion", motion);
                startActivityForResult(intent,1);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_add_motion);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewMotionForm.class);
                startActivityForResult(intent, 1);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Motion motion = (Motion) data.getSerializableExtra("Motion");
                Log.d("Motion", motion.getTitle());
                motionArrayAdapter.insertUpdate(motion);
            }
        }
    }
}
