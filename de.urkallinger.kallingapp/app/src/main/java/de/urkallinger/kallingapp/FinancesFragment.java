package de.urkallinger.kallingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Philipp on 15.12.2016.
 */

public class FinancesFragment extends Fragment {
    public FinancesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_finances, container, false);
        getActivity().setTitle(R.string.nav_finances);
        return rootView;
    }
}
