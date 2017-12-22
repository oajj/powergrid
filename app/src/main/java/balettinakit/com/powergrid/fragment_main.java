package balettinakit.com.powergrid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.IOException;
import java.util.ArrayList;

public class fragment_main extends Fragment {

    LinearLayout rl;
    View v;
    int[] list;
    Fragment f;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);

        list = this.getArguments().getIntArray("data");

        //ToDo add method for getting text color from resources

        //ToDo remove spaghetti code

        rl = v.findViewById(R.id.main_layout);
        rl.addView(getCurrentUsage());
        rl.addView(getUsageChart30Days());
        f = this;

        getActivity().setTitle(getString(R.string.app_name));

        SwipeRefreshLayout SwipeToRefshers = v.findViewById(R.id.swiper);

        SwipeToRefshers.setOnRefreshListener(refreshListener());

        return v;
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener(){
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Connection c = new Connection(getResources().getString(R.string.host), 1234);
                    c.login(0, "");
                    list = c.houseGetHistory(0, -1);

                    final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Class fragmentClass = fragment_main.class;

                    fragment = null;

                    try {

                        fragment = (Fragment) fragmentClass.newInstance();

                        Bundle args = new Bundle();
                        int[] i = c.houseGetHistory(0, -1);
                        args.putIntArray("data", i);
                        fragment.setArguments(args);

                        //Extremely bad implementation of updating data. Will the fixed in future update
                        //ToDo fix bad implementation of refreshing activity
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


                    } catch (IllegalAccessException | java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private CardView getUsageChart30Days() {

        ArrayList<Entry> entries = new ArrayList<>();

        int x = 0;
        for (int i : list) {
            entries.add(new Entry(x, i));
            x++;
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.text_usage_wh));
        dataSet.setColor(R.color.colorAccent);
        dataSet.setValueTextColor(R.color.colorAccent);
        dataSet.setColors(new int[]{R.color.colorAccent}, getActivity());

        Description des = new Description();
        des.setText(getString(R.string.text_usage_30_d));
        des.setTextSize(15);
        des.setTextColor(R.color.colorAccent);

        LineData lineData = new LineData(dataSet);
        LineChart chart = new LineChart(getActivity());
        chart.setDescription(des);
        chart.setData(lineData);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.invalidate();

        TextView t = new TextView(getActivity());
        TextView usage = new TextView(getActivity());

        LinearLayout l = new LinearLayout(getActivity());

        t.setText(R.string.text_power_usage);
        t.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorText, null));
        t.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        t.setTextSize(30);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params3.setMargins(40, 0, 0, 0);
        t.setLayoutParams(params3);

        l.addView(t);
        usage.setText("+3%");
        usage.setTextSize(28);
        usage.setTextColor(Color.RED);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(50, 0, 0, 0);
        usage.setLayoutParams(params);
        l.addView(usage);

        LinearLayout lin = new LinearLayout(getActivity());
        lin.setOrientation(LinearLayout.VERTICAL);
        lin.addView(l);
        lin.addView(chart);
        chart.getLayoutParams().height = 700;
        chart.requestLayout();

        CardView card = getDefaultCardView();
        card.addView(lin);
        return card;
    }

    public CardView getCurrentUsage() {

        CardView c = getDefaultCardView();
        LinearLayout l = new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.VERTICAL);
        TextView currentUsage = new TextView(getActivity());
        currentUsage.setText(String.format("%s%s%s", getString(R.string.text_current_usage), "30", getString(R.string.text_wh)));
        currentUsage.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorText, null));
        currentUsage.setTextSize(30);
        currentUsage.setLayoutParams(getCustomParams());
        c.addView(currentUsage);

        return c;
    }

    public LinearLayout.LayoutParams getDefaultCardViewParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(50, 50, 50, 5);
        return params;
    }

    public LinearLayout.LayoutParams getCustomParams() {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 10, 10, 10);
        return params;
    }

    public CardView getDefaultCardView() {
        CardView c = new CardView(getActivity());
        c.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorText, null));
        c.setElevation(20);
        c.setLayoutParams(getDefaultCardViewParams());
        return c;
    }
}