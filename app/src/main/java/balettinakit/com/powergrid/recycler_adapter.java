package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

public class recycler_adapter extends RecyclerView
        .Adapter<recycler_adapter
        .DataObjectHolder> {
    private static String LOG_TAG = "jari";
    private static MyClickListener myClickListener;
    Context cont;
    private DatabaseReference mDatabase;

    private ArrayList<device> mDataset;
    ArrayList<device> data = new ArrayList<>();

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
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final int pos = position;
        final DataObjectHolder h = holder;



        holder.name.setText(mDataset.get(position).getName());
        holder.condition.setText(mDataset.get(position).getState());
        holder.usage.setText(mDataset.get(position).getUsage());
        holder.img.setImageResource(R.drawable.ic_launcher_background);
        holder.tier.setText(Integer.toString(mDataset.get(position).getTier()));
        setText(holder,position);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(cont, h.btn);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_dropdown, popup.getMenu());
                popup.show();


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.menu_force_active:
                                forceActive(pos);
                                mDataset.get(position).setState("POWER_FORCED_ON");
                                break;
                            case R.id.menu_state:
                                switchState(pos, !isActive(pos));
                                mDataset.get(position).setState("POWER_OFF");
                                break;
                            case R.id.menu_stats:
                                Toast.makeText(cont, "Work in progress", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.menu_tier_1:
                                setTier(pos, 1);
                                holder.tier.setText("1");
                                break;
                            case R.id.menu_tier_2:
                                setTier(pos, 2);
                                holder.tier.setText("2");
                                break;
                            case R.id.menu_tier_3:
                                setTier(pos, 3);
                                holder.tier.setText("3");
                                break;
                        }

                        setText(holder,position);
                        return false;
                    }
                });
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

    private void setText(DataObjectHolder holder, int id){
        Log.d("t23",getState(id));
        switch (getState(id)){
            case "POWER_FORCED_ON":
                holder.condition.setText("Forced on");
                break;
            case "POWER_OFF":
                holder.condition.setText("Off");
                break;
            case "POWER_ON":
                holder.condition.setText("On");
                break;
            case "POWER_QUEUED":
                holder.condition.setText("Queued");
                break;
            case "POWER_UNKNOWN":
                holder.condition.setText("Unknown");
                break;
            default:
                holder.condition.setText("Unknown");
                break;
        }
    }

    public void setTier(int id, int tier){
        final int idd = id;
        final int tierr = tier;
        fetchData f = new fetchData() {
            @Override
            public void doInBackground() {
                try {
                    Connection c = new Connection(cont.getResources().getString(R.string.host),1234);
                    c.login(0,"");
                    c.deviceSetTier(idd, tierr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new LongOperation().execute(f);

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String getState(int id){
        String t ="Unknown";
        int i=0;
        while(i<mDataset.size()){

            if(mDataset.get(i).getToken()==id){
                t= mDataset.get(i).getState();
            }
            i++;
        }

        return t;
    }

    public void forceActive(int id){
        final int idd = id;
        fetchData f = new fetchData() {
            @Override
            public void doInBackground() {
                try {
                    Connection c = new Connection(cont.getResources().getString(R.string.host),1234);
                    c.login(0,"");
                    c.deviceForceOn(idd);
                    if(idd==8){
                        FirebaseApp.initializeApp(cont);

                //                        mDatabase = FirebaseDatabase.getInstance().getReference();
  //                      mDatabase.child("on").setValue(0);
                    }
                    Log.d("t56",Integer.toString(idd));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new LongOperation().execute(f);
    }

    public void switchState(int id, Boolean on){
        final int idd = id;
        fetchData f = new fetchData() {
            @Override
            public void doInBackground() {
                try {
                    Connection c = new Connection(cont.getResources().getString(R.string.host),1234);
                    c.login(0,"");
                    c.deviceTurnOff(idd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new LongOperation().execute(f);
    }
    public Boolean isActive(int id){
        return true;
    }
    public enum state{
        STATE_ON, STATE_FORCED_ON, STATE_OFF, STATE_QUEUED, STATE_UNKNOWN
    }
        public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name, condition, usage, tier;
        ImageView img;
        ImageButton btn;

        public DataObjectHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.menu_more);
            name = itemView.findViewById(R.id.name);
            condition = itemView.findViewById(R.id.state);
            usage = itemView.findViewById(R.id.usage);
            img= itemView.findViewById(R.id.img);
            tier = itemView.findViewById(R.id.tier);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
    private class LongOperation extends AsyncTask<fetchData, Void, String> {

        @Override
        protected String doInBackground(fetchData... params) {
            params[0].doInBackground();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}