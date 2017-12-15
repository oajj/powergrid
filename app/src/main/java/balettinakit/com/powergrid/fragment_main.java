package balettinakit.com.powergrid;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
        v=inflater.inflate(R.layout.fragment_main,container, false);

        list = this.getArguments().getIntArray("data");


        rl = (LinearLayout) v.findViewById(R.id.main_layout);
        rl.addView(getCurrentUsage("30"));
        rl.addView(getUsageChart30Days());
        f = this;
        getActivity().setTitle("Powergrid");
        SwipeRefreshLayout SwipeToRefshers = v.findViewById(R.id.swiper);
        SwipeToRefshers.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new background().execute(new fetchData() {
                            @Override
                            public void doInBackground() {
                                try {
                                    Connection c = new Connection(getResources().getString(R.string.host), 1234);
                                    c.login(0, "");
                                    list = c.houseGetHistory(0, -1);

                                    final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    Class fragmentClass = fragment_main.class;

                                    fragment = null;

                                    try {
                                        fragment = (Fragment) fragmentClass.newInstance();


                                        new background().execute(new fetchData() {
                                            @Override
                                            public void doInBackground() {
                                                try {
                                                    Connection c = new Connection(getResources().getString(R.string.host), 1234);
                                                    c.login(0, "");
                                                    Bundle args = new Bundle();
                                                    int[] i = c.houseGetHistory(0, -1);
                                                    args.putIntArray("data", i);
                                                    fragment.setArguments(args);
                                                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (java.lang.InstantiationException e) {
                                        e.printStackTrace();
                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
        );

        return v;
    }

    public CardView getUsageChart30Days(){

        LineChart chart = new LineChart(getActivity());
        ArrayList<Entry> entries = new ArrayList<Entry>();
        int y = 0;
        for (int i : list) {
            entries.add(new Entry(y, i));
            y++;
        }

        LineDataSet dataSet = new LineDataSet(entries, "Usage in Wh");
        dataSet.setColor(R.color.colorAccent);
        dataSet.setValueTextColor(R.color.colorAccent);
        Description des = new Description();
        des.setText("Usage in 30 days");
        des.setTextSize(15);
        des.setTextColor(R.color.colorAccent);
        dataSet.setColors(new int[] { R.color.colorAccent}, getActivity());
        chart.setDescription(des);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.invalidate();

        TextView t = new TextView(getActivity());
        TextView usage = new TextView(getActivity());

        LinearLayout l = new LinearLayout(getActivity());
        t.setText("Power usage");
        t.setTextColor(getResources().getColor(R.color.colorText));

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

    public CardView getCurrentUsage(String usage){

        CardView c = getDefaultCardView();
        LinearLayout l = new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.VERTICAL);
        TextView currentUsage = new TextView(getActivity());
        currentUsage.setText("Current usage: "+usage+" /Wh");
        currentUsage.setTextColor(getResources().getColor(R.color.colorText));
        currentUsage.setTextSize(30);
        currentUsage.setLayoutParams(getCustomParams(20, 10, 10, 10));
        c.addView(currentUsage);
        return c;
    }

    public LinearLayout.LayoutParams getDefaultCardViewParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(50, 50, 50, 5);
        return  params;
    }

    public LinearLayout.LayoutParams getCustomParams(int left, int rigt, int top, int bottom){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, rigt, bottom);
        return params;
    }

    public CardView getDefaultCardView(){
        CardView c = new CardView(getActivity());
        c.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        c.setElevation(20);
        c.setLayoutParams(getDefaultCardViewParams());
        return c;
    }

    private class background extends AsyncTask<fetchData, Void, String> {

        @Override
        protected String doInBackground(fetchData... params) {
            params[0].doInBackground();

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}