package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/28/2017.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class fragment_devices extends Fragment {

    ArrayList<device> data;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_devices, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            login r = new login();
            r.execute();

        return v;
    }

    private class login extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

        Connection c;

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {

            data= new ArrayList<>();

            try {
                c = new Connection(getResources().getString(R.string.host),1234);
            } catch (IOException e) {
                e.printStackTrace();
            }
            c.login(0, "");

            int i =0;

            ArrayList<String> result = new ArrayList<>(Arrays.asList(c.houseGetDevices()));

            for(String l:result){
                data.add(new device(l,c.deviceGetPowerState(i).toString(),"100kwh",i,c.deviceGetTier(i)));
                i++;
            }

         return new ArrayList<>(Arrays.asList(c.houseGetDevices()));
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {

            recycler_adapter adapter = new recycler_adapter(data);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}