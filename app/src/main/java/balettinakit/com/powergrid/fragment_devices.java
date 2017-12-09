package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/28/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class fragment_devices extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_devices, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<device> data = new ArrayList<>();

        //add some sample data. Available names are defined in pictureAdapter
        data.add(new device("dishwasher", "on", "nevermind", "hä", false));
        data.add(new device("light", "on", "nevermind", "hä", true));
        data.add(new device("warmer", "on", "nevermind", "hä", false));
        recycler_adapter adapter = new recycler_adapter(data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return v;
    }

}