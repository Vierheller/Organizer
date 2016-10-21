package de.grau.organizer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import de.grau.organizer.R;

import de.grau.organizer.adapters.listeners.OnClickListener;
import de.grau.organizer.classes.Event;

/**
 * Created by Vierheller on 21.10.2016.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<Event> mDataset;
    private RecyclerView mRecyclerView;
    private OnClickListener listener = null;
    private View.OnCreateContextMenuListener mOnCreateContextMenuListener;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        int mPosition;
        View itemView;
        TextView tv_title;
        TextView tv_startTime;
        TextView tv_endTime;
        TextView tv_category;
        ImageView iv_Priority;

        public ViewHolder(View itemView, TextView tv_title, TextView tv_startTime, TextView tv_endTime, TextView tv_category, ImageView iv_Priority) {
            super(itemView);
            this.itemView = itemView;
            this.tv_title = tv_title;
            this.tv_startTime = tv_startTime;
            this.tv_endTime = tv_endTime;
            this.tv_category = tv_category;
            this.iv_Priority = iv_Priority;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRecyclerViewAdapter(List<Event> myDataset, View.OnCreateContextMenuListener onCreateContextMenuListener, RecyclerView recyclerView) {
        mDataset = myDataset;
        mRecyclerView = recyclerView;
        mOnCreateContextMenuListener = onCreateContextMenuListener;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eventslist_row, parent, false);

        TextView title = (TextView) view.findViewById(R.id.eventlist_title);
        TextView start = (TextView) view.findViewById(R.id.eventlist_start);
        TextView end = (TextView) view.findViewById(R.id.eventlist_end);
        TextView category = (TextView) view.findViewById(R.id.eventlist_category);
        ImageView ivPriority = (ImageView) view.findViewById(R.id.eventlist_priority);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                //Not attached, nothing to do
                if(listener == null)
                    return;
                listener.onClick(v, itemPosition);
            }

        });

        view.setOnCreateContextMenuListener(mOnCreateContextMenuListener);

        ViewHolder vh = new ViewHolder(view, title, start, end, category, ivPriority);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Event currentEvent = mDataset.get(position);
        holder.mPosition = position;
        holder.tv_title.setText(mDataset.get(position).getName());
        //End date may be null?, category is not even possible to set

        holder.tv_startTime.setText("");
        if (currentEvent.getStart()!=null){
            holder.tv_startTime.setText(String.format("%02d:%02d",currentEvent.getTime(Event.DateTime.START, Calendar.HOUR_OF_DAY),currentEvent.getTime(Event.DateTime.START,Calendar.MINUTE)));
        }
        holder.tv_endTime.setText("");
        if (currentEvent.getEnd()!=null){
            holder.tv_endTime.setText(String.format("%02d:%02d",currentEvent.getTime(Event.DateTime.END, Calendar.HOUR_OF_DAY),currentEvent.getTime(Event.DateTime.END,Calendar.MINUTE)));
        }
        holder.tv_category.setText("");
        if(currentEvent.getCategory() != null){
            holder.tv_category.setText(currentEvent.getCategory().getName());
        }

        holder.iv_Priority.setBackgroundColor(currentEvent.getPriorityColor());

    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
