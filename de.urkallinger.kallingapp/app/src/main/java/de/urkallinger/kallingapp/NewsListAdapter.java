package de.urkallinger.kallingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import de.urkallinger.kallingapp.model.NewsListElement;

public class NewsListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<NewsListElement> news;

    public NewsListAdapter(Context context, List<NewsListElement> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.news.get(groupPosition).getMessage();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.news_list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.news_list_item);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.news.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.news.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        NewsListElement listElement = (NewsListElement) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.news_list_group, null);
        }

        TextView listHeader = (TextView) convertView
                .findViewById(R.id.news_list_header);
        listHeader.setText(listElement.getTitle());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}