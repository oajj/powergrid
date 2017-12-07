package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.ConnectException;
import java.util.ArrayList;

public class recycler_adapter extends RecyclerView
        .Adapter<recycler_adapter
        .DataObjectHolder> {
    private static String LOG_TAG = "jari";
    private static MyClickListener myClickListener;
    private ArrayList<device> mDataset;
    Context cont;

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
            cont = parent.getContext();
            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final DataObjectHolder h = holder;
        holder.name.setText(mDataset.get(position).getName());
        holder.condition.setText(mDataset.get(position).getState());
        holder.usage.setText(mDataset.get(position).getUsage());
        holder.img.setImageResource(pictureAdapter.adaptPicture(mDataset.get(position).getName()));

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(cont, h.btn);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_dropdown, popup.getMenu());
                popup.show();
            }
        });
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
        ImageView img;
        ImageButton btn;
        public DataObjectHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.menu_more);
            name = itemView.findViewById(R.id.name);
            condition = itemView.findViewById(R.id.state);
            usage = itemView.findViewById(R.id.usage);
            img= itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }
}