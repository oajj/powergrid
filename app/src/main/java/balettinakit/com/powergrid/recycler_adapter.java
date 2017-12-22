package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class recycler_adapter extends RecyclerView
        .Adapter<recycler_adapter
        .DataObjectHolder> {

    private Context context;
    private ArrayList<device> mDataset;

    recycler_adapter(ArrayList<device> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);

        context = parent.getContext();

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        //Linting rules tell not to use position from arguments
        //ToDo find out if argument position should be used

        holder.name.setText(mDataset.get(holder.getAdapterPosition()).getName());
        holder.condition.setText(mDataset.get(holder.getAdapterPosition()).getState());
        holder.usage.setText(mDataset.get(holder.getAdapterPosition()).getUsage());
        holder.img.setImageResource(R.drawable.ic_launcher_background);
        holder.tier.setText(String.valueOf(mDataset.get(holder.getAdapterPosition()).getTier()));
        setText(holder, position);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create the popupMenu of card
                PopupMenu popup = new PopupMenu(context, holder.btn);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_dropdown, popup.getMenu());
                popup.show();

                //ToDo convert String states to enums

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.menu_force_active:
                                forceActive(holder.getAdapterPosition());
                                mDataset.get(holder.getAdapterPosition()).setState("POWER_FORCED_ON");
                                break;

                            case R.id.menu_state:
                                switchState(holder.getAdapterPosition());
                                mDataset.get(holder.getAdapterPosition()).setState("POWER_OFF");
                                break;

                            case R.id.menu_stats:
                                Toast.makeText(context, R.string.card_wip, Toast.LENGTH_LONG).show();
                                break;

                            case R.id.menu_tier_1:
                                setTier(holder.getAdapterPosition(), 1);
                                holder.tier.setText("1");
                                break;

                            case R.id.menu_tier_2:
                                setTier(holder.getAdapterPosition(), 2);
                                holder.tier.setText("2");
                                break;

                            case R.id.menu_tier_3:
                                setTier(holder.getAdapterPosition(), 3);
                                holder.tier.setText("3");
                                break;
                        }

                        setText(holder, holder.getAdapterPosition());
                        return false;
                    }
                });
            }
        });
    }

    /**
     * Sets the text to condition -textView
     * @param holder DataObjectHolder contains all the views
     * @param id The id of the device
     */
    private void setText(DataObjectHolder holder, int id) {

        switch (getState(id)) {

            case "POWER_FORCED_ON":
                holder.condition.setText(R.string.text_power_forced);
                break;

            case "POWER_OFF":
                holder.condition.setText(R.string.text_power_off);
                break;

            case "POWER_ON":
                holder.condition.setText(R.string.text_power_on);
                break;

            case "POWER_QUEUED":
                holder.condition.setText(R.string.text_power_queued);
                break;

            case "POWER_UNKNOWN":
                holder.condition.setText(R.string.text_power_unknown);
                break;

            default:
                holder.condition.setText(R.string.text_power_unkown);
                break;
        }
    }


    /**
     * sets the tier of device
     *
     * @param id   the id of the device which tier is wanted to set
     * @param tier the tier to set
     */
    private void setTier(int id, final int tier) {

        final int idd = id;
        fetchData f = new fetchData() {
            @Override
            public void doInBackground() {
                try {

                    Connection c = new Connection(context.getResources().getString(R.string.host), 1234);
                    c.login(0, "");
                    c.deviceSetTier(idd, tier);

                } catch (IOException e) {
                    e.printStackTrace();
                    //ToDo add error handling
                }
            }
        };

        dataFetcher.doFetch(f);

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Returns the state of device
     *
     * @param id the id of the device which state is wanted to get
     * @return returns the state of device
     */
    private String getState(int id) {

        String t = "STATE_UNKNOWN";
        int i = 0;

        //Iterate through dataset
        while (i < mDataset.size()) {

            if (mDataset.get(i).getId() == id) {
                t = mDataset.get(i).getState();
            }

            i++;
        }

        return t;
    }

    /**
     * forced a device active
     *
     * @param id the id of the device which is wanted to force active
     */
    private void forceActive(final int id) {

        fetchData f = new fetchData() {
            @Override
            public void doInBackground() {
                try {
                    Connection c = new Connection(context.getResources().getString(R.string.host), 1234);
                    c.login(0, "");
                    c.deviceForceOn(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        dataFetcher.doFetch(f);
    }

    /**
     * @param id the id of the device which state is wanted to switch
     */
    private void switchState(final int id) {

        fetchData f = new fetchData() {
            @Override
            public void doInBackground() {
                try {
                    Connection c = new Connection(context.getResources().getString(R.string.host), 1234);
                    c.login(0, "");
                    c.deviceTurnOff(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        dataFetcher.doFetch(f);
    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        TextView name, condition, usage, tier;
        ImageView img;
        ImageButton btn;

        DataObjectHolder(View itemView) {
            super(itemView);

            btn = itemView.findViewById(R.id.menu_more);
            name = itemView.findViewById(R.id.name);
            condition = itemView.findViewById(R.id.state);
            usage = itemView.findViewById(R.id.usage);
            img = itemView.findViewById(R.id.img);
            tier = itemView.findViewById(R.id.tier);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //ToDo add click handling
        }
    }

}