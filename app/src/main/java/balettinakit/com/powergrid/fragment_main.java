package balettinakit.com.powergrid;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class fragment_main extends Fragment {

    LinearLayout rl;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.fragment_main,container, false);
        rl = (LinearLayout) v.findViewById(R.id.main_layout);
        rl.addView(getCurrentUsage("30"));
        rl.addView(getUsageChart30Days());
        return v;
    }

    public CardView getUsageChart30Days(){
        LineChart chart = new LineChart(getActivity());

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(4, 5));
        entries.add(new Entry(10, 6));
        entries.add(new Entry(12, 6));
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
        chart.animateX(2000, Easing.EasingOption.EaseInElastic );

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

}