package balettinakit.com.powergrid;
/**
 * Created by Olli Peura on 11/28/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class fragment_devices extends Fragment {

    ArrayList<device> data;
    RecyclerView recyclerView;
    Connection c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_devices, container, false);
        recyclerView = v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //get data from server
        //java 8
        fetchData f = () -> {
            data = new ArrayList<>();

            try {
                c = new Connection(getResources().getString(R.string.host), 1234);
            } catch (IOException e) {
                e.printStackTrace();
            }

            c.login(0, "");

            int i = 0;

            ArrayList<String> result = new ArrayList<>(Arrays.asList(c.houseGetDevices()));


            for (String l : result) {
                data.add(new device(l, c.deviceGetPowerState(i).toString(), getString(R.string.usage_default), i, c.deviceGetTier(i)));
                i++;
            }

            recycler_adapter adapter = new recycler_adapter(data);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        };
       new dataFetcher().doFetch(f);
        return v;
    }
    }


