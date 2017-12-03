package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class recycler_adapter extends RecyclerView
        .Adapter<recycler_adapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<device> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name,condition,location,price;

        public DataObjectHolder(View itemView) {
            super(itemView);
         /*   name = (TextView) itemView.findViewById(R.id.textView4);
            condition = (TextView) itemView.findViewById(R.id.textView5);
            location= itemView.findViewById(R.id.textView_location);
            price = itemView.findViewById(R.id.price);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);*/
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public recycler_adapter(ArrayList<device> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
    /*    holder.name.setText(mDataset.get(position).getName());
        holder.condition.setText(mDataset.get(position).getHash());
        holder.price.setText(mDataset.get(position).getId()+" e");
        holder.location.setText(mDataset.get(position).getState());*/
    }

    public void addItem(device dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
    public void deleteAll(int index) {
        mDataset.clear();
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}