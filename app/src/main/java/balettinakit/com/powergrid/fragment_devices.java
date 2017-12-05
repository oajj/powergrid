package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/28/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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


        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        ArrayList<device> data = new ArrayList<>();
        data.add(new device("pesukone", "on", "nevermind", "hä"));
        data.add(new device("pesukone1", "on", "nevermind", "hä"));
        data.add(new device("pesukone2", "on", "nevermind", "hä"));


        recycler_adapter adapter = new recycler_adapter(data);
        recyclerView.setAdapter(adapter);

        return v;
    }
}