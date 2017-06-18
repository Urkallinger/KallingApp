package de.urkallinger.kallingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import de.urkallinger.kallingapp.datastructure.News;

public class MainFragment  extends Fragment {

    NewsListAdapter listAdapter;
    ExpandableListView expListView;
    List<News> news;

    public MainFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().setTitle(R.string.nav_news);

        setHasOptionsMenu(true);

        expListView = (ExpandableListView) rootView.findViewById(R.id.news_list);

        // preparing list data
        prepareListData();

        listAdapter = new NewsListAdapter(getActivity(), news);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        menu.clear(); // Altes Menü entfernen

        // Beispiel
        inflater.inflate(R.menu.motion_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
         default: break;
        }

        return false;
    }

    private void prepareListData() {
        news = new ArrayList<>();
    }
}