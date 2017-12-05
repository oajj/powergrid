package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class recycler_adapter extends RecyclerView
        .Adapter<recycler_adapter
        .DataObjectHolder> {
    private static String LOG_TAG = "jari";
    private static MyClickListener myClickListener;
    private ArrayList<device> mDataset;

    public recycler_adapter(ArrayList<device> myDataset) {
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
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
        holder.name.setText(mDataset.get(position).getName());
        holder.condition.setText(mDataset.get(position).getState());
        holder.usage.setText(mDataset.get(position).getUsage());
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

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name, condition, usage;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            condition = itemView.findViewById(R.id.state);
            usage = itemView.findViewById(R.id.usage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }
}