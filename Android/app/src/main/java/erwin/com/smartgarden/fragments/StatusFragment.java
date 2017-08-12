package erwin.com.smartgarden.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import erwin.com.smartgarden.R;
import erwin.com.smartgarden.SessionManager;
import erwin.com.smartgarden.requests.Kelembaban;
import erwin.com.smartgarden.requests.SmartGardenAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatusFragment extends Fragment implements
        OnChartValueSelectedListener {

    private LineChart mChart;
    private TextView tv_suhuTanaman1;

    private static String baseUrl = "";

    private ArrayList<Kelembaban> kelembabanArrayList;

    private int jumlahDataTampil = 100;

    public StatusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        kelembabanArrayList = new ArrayList<>();

        tv_suhuTanaman1 = (TextView) view.findViewById(R.id.tv_suhuTanaman1);

        mChart = (LineChart) view.findViewById(R.id.lc_tanaman1);
        mChart.setOnChartValueSelectedListener(this);

        // disable description text
        mChart.getDescription().setEnabled(false);
//        mChart.getDescription().setText("Suhu Tanaman 1"); //should enable above first

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.RED);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(Color.GREEN);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setGranularity(1f); // one minute
        xl.setEnabled(true);

        xl.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.MINUTES.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.GREEN);
        leftAxis.setAxisMaximum(60f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        updateChart();

        return view;
    }

    private void addEntry() {

        LineData data = mChart.getData();

        if (data != null) {

            //clear line chart values
            mChart.clearValues();

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            for(Kelembaban kelembaban : kelembabanArrayList){
//                data.addEntry(new Entry(set.getEntryCount(), Float.parseFloat(kelembaban.getNilai())), 0);

                long tanggal = TimeUnit.MILLISECONDS.toMinutes(tanggal2long(kelembaban.getTanggal()));
                float newTanggal = tanggal;

                data.addEntry(new Entry(newTanggal, Float.parseFloat(kelembaban.getNilai())), 0);
                data.notifyDataChanged();
            }

            tv_suhuTanaman1.setText(kelembabanArrayList.get(kelembabanArrayList.size()-1).getNilai());

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(jumlahDataTampil);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
//            mChart.moveViewToX(data.getEntryCount());
             mChart.moveViewToX(data.getXMax()-jumlahDataTampil);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Suhu Tanaman 1");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    private Thread thread;
    private boolean mPaused;
    private boolean mFinished;

    private void updateChart() {

        mPaused = false;
        mFinished = false;

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                retrieveData();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!mFinished){
                    // Don't generate garbage runnables inside the loop.
                    getActivity().runOnUiThread(runnable);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (thread) {
                        while (mPaused) {
                            try {
                                thread.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm");

        long millis = TimeUnit.MINUTES.toMillis((long) e.getX());
        String tanggal = mFormat.format(new Date(millis));

        Toast.makeText(getActivity().getApplicationContext(),
                "Kelembaban pada " + tanggal + " : " + Float.toString(e.getY()),
                Toast.LENGTH_LONG).show();
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Toast.makeText(getActivity().getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
        Log.i("Nothing selected", "Nothing selected.");
    }

    private void retrieveData(){

        HashMap<String, String> user = new SessionManager(getActivity().getApplicationContext()).getUserDetails();
        baseUrl = user.get(SessionManager.SERVER);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SmartGardenAPI smartGardenAPI = retrofit.create(SmartGardenAPI.class);
        Call<List<Kelembaban>> call = smartGardenAPI.getKelembaban();

        call.enqueue(new Callback<List<Kelembaban>>() {

            @Override
            public void onResponse(Call<List<Kelembaban>> call, Response<List<Kelembaban>> response) {
                List<Kelembaban> kelembabanList = response.body();

                kelembabanArrayList.clear();

                for(Kelembaban kelembaban : kelembabanList){
                    kelembabanArrayList.add(kelembaban);
                }

                addEntry();
            }

            @Override
            public void onFailure(Call<List<Kelembaban>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "ERROR :\n" + t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private long tanggal2long(String tanggal){

        //2017-05-06T08:05:14.000Z
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        Date date = null;
        try {
            date = sdf.parse(tanggal.replaceAll("Z$", "+0000"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    @Override
    public void onPause() {
        super.onPause();

        synchronized (thread) {
            mPaused = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        synchronized (thread) {
            mPaused = false;
            thread.notifyAll();
        }
    }
}
