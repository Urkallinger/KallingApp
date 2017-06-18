package de.urkallinger.kallingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.urkallinger.kallingapp.datastructure.Motion;


public class MotionArrayAdapter extends ArrayAdapter<Motion> {

    private List<Motion> list;


    public MotionArrayAdapter(Context context, int resource, int textViewResourceId, List<Motion> objects) {
        super(context, resource, textViewResourceId, objects);
        this.list = new ArrayList<>(objects);
    }

    @Override
    public void add(Motion motion) {
        this.list.add(motion);
        notifyDataSetChanged();
    }

    public void insertUpdate(Motion motion) {
        Motion m;
        for(int i = 0; i < list.size(); i++) {
            m = list.get(i);
            if(m.getId() == motion.getId()) {
                list.set(i, motion);
                notifyDataSetChanged();
                return;
            }
        }

        this.add(motion);
    }

    @Override
    public Motion getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //creating the ViewHolder we defined earlier.
        ViewHolder holder = new ViewHolder();

        //creating LayoutInflator for inflating the row layout.
        LayoutInflater inflator = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflating the row layout we defined earlier.
        convertView = inflator.inflate(R.layout.motion_list_item, null);

        //setting the views into the ViewHolder.
        holder.title = (TextView) convertView.findViewById(R.id.motion_list_item_textview);

        //setting data into the the ViewHolder.
        holder.title.setText(list.get(position).getTitle());

        //return the row view.
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    static class ViewHolder
    {
        TextView title;
    }
}
