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

import de.urkallinger.kallingapp.model.NewsListElement;

public class MainFragment  extends Fragment {

    NewsListAdapter listAdapter;
    ExpandableListView expListView;
    List<NewsListElement> news;

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

        //List<String> news = new ArrayList<>();
        news.add(new NewsListElement("Kalling gewinnt im Lotto!!", "geil oda?"));
        news.add(new NewsListElement("Grillo in Aktion", "geil oda?"));
        news.add(new NewsListElement("Koi ist neuer Sexiest Fish Alive", "geil oda?"));
        news.add(new NewsListElement("Christian Billig ändert seinen Namen offiziell in Chrip Billig", "geil oda?"));
        news.add(new NewsListElement("Jessi fällt mit Stuhl um - schon wieder!", "geil oda?"));
        news.add(new NewsListElement("Apfelsaft entwickelt Eigenleben!", "geil oda?"));
        news.add(new NewsListElement("Brutal lange Überschrift einer Neuigkeit die absolut keinen Inhalt hat und nur zu Testzwecken da ist und deshalb extra extra extra lang ist!!!", "geil oda?"));
    }
}