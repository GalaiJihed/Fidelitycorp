package com.example.fidelitycorporation.Frags;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fidelitycorporation.Adapter.Stat1Adapter;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.Entities.Client;
import com.example.fidelitycorporation.Entities.Product;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.Reviews.MyMarkerView;
import com.example.fidelitycorporation.helper.SessionManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {


    private  RecyclerView listviewMostActive;
    private  RecyclerView listviewTopCustomer;
    private  RecyclerView listviewTopProduct;
    List<Entry> valsComp1 = new ArrayList<>();
    private  ArrayList<Client> clients = new ArrayList<Client>();
    private  ArrayList<Client> topCustomers = new ArrayList<Client>();
    private  ArrayList<Client> mostActiveCustomers = new ArrayList<Client>();
    private  ArrayList<Client> topProducts = new ArrayList<Client>();

    private Context context;
    private Stat1Adapter topCustomersAdapater;
    private Stat1Adapter mostActiveCustomersAdapter;
    private Stat1Adapter topProductAdapater;
    private View root;
    private SharedPreferences sharedPref;
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    private SessionManager session;


    public ReviewFragment() {
        // Required empty public constructor
    }

    protected final String[] products = new String[] {
            "Express", "Cappuccin", "Direct", "The", "Mojito", "Eau"
    };
    protected final float[] productsPrice = new float[]{
            4545f,6000f,8000f,7500f,300f,8230f
    };
    private final int count = 31;
    private final int count2 = 7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_review, container, false);

        getStats();
        //Setting Tabs
        TabHost tabs = (TabHost) root.findViewById(R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("Statistics");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Statistics");
        tabs.addTab(spec);
//        TabHost.TabSpec spec1 = tabs.newTabSpec("Trends");
//        spec1.setContent(R.id.tab2);
//        spec1.setIndicator("Trends");
//        tabs.addTab(spec1);
//        TabHost.TabSpec spec2 = tabs.newTabSpec("Others");
//        spec2.setContent(R.id.tab3);
//        spec2.setIndicator("Others");
//        tabs.addTab(spec2);
        tabs.setCurrentTab(0);


//        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
//            @Override
//            public void onTabChanged(String tabId) {
//                if(tabId.equals("Statistics"))
//                {
//                    LineChart chart = getView().findViewById(R.id.Linechart);
//                    SettingLineChartView(chart);
//                }
//                else if (tabId.equals("Trends"))
//                {
//                    SettingCombinedChartViewTrends(getView());
//                    BarChart mBarChart1 = getView().findViewById(R.id.BarChart1);
//                    BarChart mBarChart2 = getView().findViewById(R.id.BarChart2);
//                    BarChart mBarChart3 = getView().findViewById(R.id.BarChart3);
//                    generateGraphBarChart(mBarChart1,"#55D8FE");
//                    generateGraphBarChart(mBarChart2,"#A3A0FB");
//                    generateGraphBarChart(mBarChart3,"#5EE2A0");
//
//                    TextView weeknbr = getView().findViewById(R.id.weeklyViewnbr);
//                    TextView totalnbr = getView().findViewById(R.id.totalViewsnbr);
//
//                    startCountAnimation(weeknbr,Integer.parseInt(weeknbr.getText().toString()));
//                    startCountAnimation(totalnbr,Integer.parseInt(totalnbr.getText().toString()));
//                }
//                else if(tabId.equals("Others"))
//                {
//                    PieChart chart1 = getView().findViewById(R.id.pieChart);
//                    setupPieChart(chart1);
//
//                    BarChart chart2 = getView().findViewById(R.id.barChart);
//                    setupMultipleBarData(chart2);
//
//                    PieChart chart3 = getView().findViewById(R.id.pieChart1);
//                    setupMoneyPieChart(chart3);
//                    BarChart chart4 = getView().findViewById(R.id.barChart1);
//                    setupNegativeBarData(chart4);
//                }
//            }
//        });
        return root ;

    }

    public void SettingLineChartView(LineChart chart,List<Entry> valsComp1)
    {
        LineChart mLineChart = chart;
        mLineChart.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mLineChart.setDrawGridBackground(true);
        mLineChart.setGridBackgroundColor(Color.parseColor("#EEEDED"));


//        Entry c1e1 = new Entry(0f, 800f);
//        valsComp1.add(c1e1);
//        Entry c1e2 = new Entry(1f, 800f);
//        valsComp1.add(c1e2);
//        Entry c1e3= new Entry(2f, 800f);
//        valsComp1.add(c1e3);
//        Entry c1e4= new Entry(3f, 800f);
//        valsComp1.add(c1e4);
//        Entry c1e5= new Entry(4f, 800f);
//        valsComp1.add(c1e5);
//        Entry c1e6= new Entry(5f, 800f);
//        valsComp1.add(c1e6);
//        Entry c1e7= new Entry(6f, 800f);
//        valsComp1.add(c1e7);
//        Entry c1e8= new Entry(7f, 800f);
//        valsComp1.add(c1e8);
//        Entry c1e9= new Entry(8f, 800f);
//        valsComp1.add(c1e9);
//        Entry c1e10= new Entry(9f, 800f);
//        valsComp1.add(c1e10);
//        Entry c1e11= new Entry(10f, 800f);
//        valsComp1.add(c1e11);
//        Entry c1e12= new Entry(11f, 800f);
//        valsComp1.add(c1e12);

//        Entry c2e1 = new Entry(0f, 19000f); // 0 == quarter 1
//        valsComp2.add(c2e1);
//        Entry c2e2 = new Entry(1f, 7500f); // 1 == quarter 2 ...
//        valsComp2.add(c2e2);
//        Entry c2e3 = new Entry(2f, 16500f); // 1 == quarter 2 ...
//        valsComp2.add(c2e3);
//        Entry c2e4 = new Entry(3f, 12500f); // 1 == quarter 2 ...
//        valsComp2.add(c2e4);
//        Entry c2e5 = new Entry(4f, 16500f); // 1 == quarter 2 ...
//        valsComp2.add(c2e5);
//        Entry c2e6 = new Entry(5f, 10000f); // 1 == quarter 2 ...
//        valsComp2.add(c2e6);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Product Sold");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColor(Color.parseColor("#2EC4B6"));
//        LineDataSet setComp2 = new LineDataSet(valsComp2, "Total Views");
//        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
//        setComp2.setColor(Color.parseColor("#00574B"));

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
//        dataSets.add(setComp2);
        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.invalidate();
//
        //Styling DataSets
        setComp1.setHighlightEnabled(true);
        setComp1.setDrawHighlightIndicators(true);
        setComp1.setDrawCircleHole(true);
        setComp1.setCircleColor(Color.parseColor("#2EC4B6"));
        setComp1.setCircleRadius(5f);
        setComp1.setColor(Color.parseColor("#2EC4B6"));
        setComp1.setLineWidth(3f);
        setComp1.setValueTextSize(0f);
        setComp1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//
//        setComp2.setHighlightEnabled(true);
//        setComp2.setDrawHighlightIndicators(true);
//        setComp2.setDrawCircleHole(true);
//        setComp2.setCircleColor(Color.parseColor("#00574B"));
//        setComp2.setCircleRadius(5f);
//        setComp2.setColor(Color.parseColor("#00574B"));
//        setComp2.setLineWidth(3f);
//        setComp2.setValueTextSize(0f);
//        setComp2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//
//
//
        //description disable
        Description description = mLineChart.getDescription();
        description.setEnabled(false);
        description.setText("Statistics");
        description.setTextSize(20f);
        description.setPosition(300,70);
//        //formatter
        final String[] quarters = new String[] { "Jan", "Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" };
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        //Styling XAxis
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
       // xAxis.setTextSize(15f);
        //Styling YAxis
       YAxis yAxisLeft = mLineChart.getAxisLeft();
       yAxisLeft.setEnabled(false);


        //yAxisLeft.setTextSize(15f);
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setGranularity(1f);
       // yAxisRight.mAxisRange=2f;
       // yAxisRight.setTextSize(15f);

        //Styling Legend
        Legend legend = mLineChart.getLegend();
       // legend.setTextSize(15f);
       // legend.setFormSize(15f);

        //Animation
        mLineChart.animateX(5000, Easing.EaseInQuad);
    }


    public void getStats()  {

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
            JSONObject jsonBody = new JSONObject();
            sharedPref =  getActivity().getSharedPreferences(
                    PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
            int idStore =  sharedPref.getInt("idStore",0);
            jsonBody.put("StoreId", idStore);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_STATS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Fragment STATS", response.toString());
                    // Check the JWT Token
                    try {
                        JSONObject allStats = new JSONObject(response);

                        // BEGIN STAT 1 ( TOP CUSTOMER )
                        JSONArray statTopCustomers =  allStats.getJSONArray("stat1");
                        for (int i = 0; i < statTopCustomers.length(); i++) {
                            JSONObject item = statTopCustomers.getJSONObject(i);
                            String firstName = item.getString("client_firstName");
                            String lastName = item.getString("client_lastName");
                            String points = item.getString("clientstore_pointsInCurrentStore");
                            String image = item.getString("client_Image");
                            Client c = new Client();
                            c.setFirstName(firstName);
                            c.setLastName(lastName);
                            c.setPoints(points);
                            c.setImage(image);
                            topCustomers.add(c);
                        }
                        listviewTopCustomer = root.findViewById(R.id.frag_review_topcust_recycleview);
                        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(context);
                        topCustomersAdapater = new Stat1Adapter(context, R.layout.list_row_topcustomerstat, topCustomers);
                        listviewTopCustomer.setLayoutManager(layoutManager1);
                        listviewTopCustomer.setHasFixedSize(true);
                        listviewTopCustomer.setAdapter(topCustomersAdapater);
                        listviewTopCustomer.setItemViewCacheSize(20);
                        listviewTopCustomer.setDrawingCacheEnabled(true);
                        topCustomersAdapater.notifyDataSetChanged();
                        // END STAT 1 ( TOP CUSTOMER )
                        // BEGIN STAT 2 ( Most Active  CUSTOMER )

                        JSONArray statMostActiveCustomers =  allStats.getJSONArray("stat2");
                        for (int i = 0; i < statMostActiveCustomers.length(); i++) {
                            JSONObject item = statMostActiveCustomers.getJSONObject(i);
                            String firstName = item.getString("client_firstName");
                            String lastName = item.getString("client_lastName");
                            String image = item.getString("client_Image");
                            String nbr = item.getString("nbr");
                            Client c = new Client();
                            c.setFirstName(firstName);
                            c.setLastName(lastName);
                            c.setPoints(nbr);
                            c.setImage(image);
                            mostActiveCustomers.add(c);
                        }
                         listviewMostActive = root.findViewById(R.id.frag_review_mostactive_recycleview);
                         RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(context);
                         listviewMostActive.setLayoutManager(layoutManager2);
                         listviewMostActive.setHasFixedSize(true);
                         mostActiveCustomersAdapter = new Stat1Adapter(context, R.layout.list_row_mostactivecustomersstat, mostActiveCustomers);
                         listviewMostActive.setAdapter(mostActiveCustomersAdapter);
                         listviewMostActive.setItemViewCacheSize(20);
                         listviewMostActive.setDrawingCacheEnabled(true);
                         listviewMostActive.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                         mostActiveCustomersAdapter.notifyDataSetChanged();
                        // END STAT 2 ( Most Active  CUSTOMER )

                        // BEGIN STAT 3 ( TOP Products  CUSTOMER )

                        JSONArray topProductsCustomers =  allStats.getJSONArray("stat3");
                        for (int i = 0; i < topProductsCustomers.length(); i++) {
                            JSONObject item = topProductsCustomers.getJSONObject(i);
                            String productName = item.getString("product_ProductName");
                            String lastName = "";
                            String nbr = item.getString("nbr");
                            String image = item.getString("product_Image");
                            Client c = new Client();
                            c.setFirstName(productName);
                            c.setLastName(lastName);
                            c.setPoints(nbr);
                            c.setImage(image);
                            topProducts.add(c);
                        }
                        listviewTopProduct = root.findViewById(R.id.frag_review_topproduct_recycleview);
                        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(context);
                        listviewTopProduct.setLayoutManager(layoutManager3);
                        listviewTopProduct.setHasFixedSize(true);
                        topProductAdapater = new Stat1Adapter(context, R.layout.list_row_topcustomerstat, topProducts);
                        listviewTopProduct.setAdapter(topProductAdapater);
                        listviewTopProduct.setItemViewCacheSize(20);
                        listviewTopProduct.setDrawingCacheEnabled(true);
                        listviewTopProduct.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        topProductAdapater.notifyDataSetChanged();
                        // END STAT 3 (TOP PRODUCT )

                        // BEGIN CHART





                        JSONArray chartarray =  allStats.getJSONArray("chart");
                        for (int i = 0; i < chartarray.length(); i++) {
                            JSONObject item = chartarray.getJSONObject(i);
                            Log.e("item", item.toString());

                            // Entry c1e2 = new Entry((float) item.getDouble("mth"), Float.parseFloat(item.getString("nbr")));
                            int index = item.getInt("mth");
                            //valsComp1.get(index).setY(500f);
                            //Toast.makeText(getContext(), "Item" + valsComp1.get(index).toString(), Toast.LENGTH_LONG).show();
                            valsComp1.add(new Entry((float) item.getDouble("mth")-1, (float) item.getDouble("nbr")));
                            LineChart chart = root.findViewById(R.id.Linechart);
                            SettingLineChartView(chart, valsComp1);


                        }
                        // END CHART


                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());

                    Toast.makeText(getContext(),
                            "A problem has been occured , please retry later.", Toast.LENGTH_LONG)
                            .show();

                }
            }) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    session = new SessionManager(getContext());
                    params.put("auth", session.getUser());
                    return params;
                }


                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.toString());
                        try {
                            responseString = new String(response.data, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





























//    public void SettingCombinedChartViewTrends(View v)
//    {
//        CombinedChart mCombinedChart = (CombinedChart) v.findViewById(R.id.CombinedChart);
//        mCombinedChart.setBackgroundColor(Color.WHITE);
//        mCombinedChart.setDrawGridBackground(false);
//        mCombinedChart.setDrawBarShadow(false);
//        mCombinedChart.setHighlightFullBarEnabled(false);
//
//        //Draw Bar behind Lines
//        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
//                CombinedChart.DrawOrder.BAR,   CombinedChart.DrawOrder.LINE
//        });
//
//        Legend l = mCombinedChart.getLegend();
//        l.setWordWrapEnabled(true);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//
//        //Set Data
//        CombinedData data = new CombinedData();
//
//        data.setData(generateLineDataCombined());
//        data.setData(generateBarDataCombined());
//
//        mCombinedChart.setData(data);
//        mCombinedChart.invalidate();
//        mCombinedChart.animateXY(10000,10000, Easing.EaseInOutSine);
//    }

    private LineData generateLineDataCombined() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 5)));

        LineDataSet set = new LineDataSet(entries, "Point Sold");
        set.setColor(Color.parseColor("#2EC4B6"));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.parseColor("#2EC4B6"));
        set.setCircleRadius(5f);
        set.setFillColor(Color.parseColor("#2EC4B6"));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.parseColor("#2EC4B6"));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarDataCombined() {
        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = (int) start; i < start + count; i++) {
            float val = (float) (Math.random() * (count + 1));
            values.add(new BarEntry(i, val));
        }
        BarDataSet set1;
        set1 = new BarDataSet(values, "Total Views");
        set1.setColor(Color.parseColor("#F0F2F8"));
        set1.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.9f);
        return data;
    }
    private BarData generateBarData(String color) {
        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = (int) start; i < start + count2; i++) {
            float val = (float) (Math.random() * (count2 + 1));
            values.add(new BarEntry(i, val));
        }
        BarDataSet set1;
        set1 = new BarDataSet(values,"");
        set1.setColor(Color.parseColor(color));
        set1.setBarShadowColor(Color.WHITE);
        set1.setDrawValues(false);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(0f);
        data.setBarWidth(0.9f);
        return data;
    }

    private void generateGraphBarChart(BarChart b, String color)
    {
        b.setDrawGridBackground(false);
        b.setDrawBarShadow(true);
        b.setHighlightFullBarEnabled(false);
        b.setDrawValueAboveBar(false);
        b.getDescription().setEnabled(false);
        b.setPinchZoom(false);

        XAxis xAxis = b.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setEnabled(false);
        xAxis.setTextSize(0f);

        YAxis leftAxis = b.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);

        YAxis rightAxis = b.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);

        b.getLegend().setEnabled(false);
        b.setData(generateBarData(color));
        b.setBackgroundColor(Color.WHITE);
        b.invalidate();
        b.animateXY(3000,3000,Easing.EaseInCirc);
    }

    private void startCountAnimation(final TextView text, Integer number) {
        ValueAnimator animator = ValueAnimator.ofInt(0, number);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                text.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }
    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }
    public void setupMoneyPieChart(PieChart chart)
    {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        //chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);

        // Center Text
        chart.setCenterText("48% Saved");
        chart.setExtraOffsets(15.f, 5.f, 15.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(90f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(true);

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(48,"Money Saved"));
        entries.add(new PieEntry(52,"Total Spent"));




        PieDataSet dataSet = new PieDataSet(entries, "Money");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);


        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.parseColor("#A4A1FB"));
        colors.add(Color.parseColor("#EDECFE"));


        dataSet.setColors(colors);

        /*dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);*/
        //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


        PieData data = new PieData(dataSet);
        data.setValueTextSize(0.f);
        data.setValueTextColor(Color.parseColor("#FF8A8F9C"));
        chart.setData(data);
    }
    public void setupPieChart(PieChart chart)
    {
        //chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        //chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);

        // Center Text
        chart.setCenterText("23093 Sales");
        chart.setExtraOffsets(15.f, 0.f, 15.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < count2; i++) {
            entries.add(new PieEntry(productsPrice[i%productsPrice.length], products[i % products.length]));
            // new PieEntry((float) Math.random());
        }


        PieDataSet dataSet = new PieDataSet(entries, "Sales");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(10f);


        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());


        dataSet.setColors(colors);

        /*dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);*/
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.parseColor("#FF8A8F9C"));
        chart.setData(data);
    }

    public void setupMultipleBarData(BarChart chart)
    {

        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);
        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.parseColor("#8A8F9C"));
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        float groupSpace = 0.2f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
        int groupCount = 7;
        int startYear = 0;
        int endYear = startYear + groupCount;

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();
        float randomMultiplier = 36 * 100000f;
        for (int i = startYear; i < endYear; i++) {
            values1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            values2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            values3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            // values4.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
        }
        BarDataSet set1, set2, set3;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);

            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "Express");
            set1.setColor(Color.parseColor("#A3A0FB"));
            set2 = new BarDataSet(values2, "Cappucin");
            set2.setColor(Color.parseColor("#56D9FE"));
            set3 = new BarDataSet(values3, "Direct");
            set3.setColor(Color.parseColor("#4AD991"));


            BarData data = new BarData(set1, set2, set3);
            data.setValueFormatter(new LargeValueFormatter());


            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startYear + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(startYear, groupSpace, barSpace);
        chart.invalidate();
        chart.animateXY(1400,1400,Easing.EaseInBack);
    }

    public void setupNegativeBarData(BarChart chart) {
        chart.setBackgroundColor(Color.WHITE);
        chart.setExtraTopOffset(-30f);
        chart.setExtraBottomOffset(10f);
        chart.setExtraLeftOffset(10f);
        chart.setExtraRightOffset(10f);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(5);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = chart.getAxisLeft();
        left.setDrawLabels(true);
        left.setSpaceTop(25f);
        left.setSpaceBottom(25f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(true);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);

        // THIS IS THE ORIGINAL DATA YOU WANT TO PLOT
        final List<Data> data = new ArrayList<>();
        data.add(new Data(0f, 7000f, "Jan-Feb"));
        data.add(new Data(1f, 4800f, "Feb-Mar"));
        data.add(new Data(2f, -1900f, "Mar-Apr"));
        data.add(new Data(3f, -1200f, "Apr-May"));
        data.add(new Data(4f, 2280.1f, "May-Jun"));

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return data.get(Math.min(Math.max((int) value, 0), data.size() - 1)).xAxisValue;
            }
        });
        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);

        for (int i = 0; i < data.size(); i++) {

            Data d = data.get(i);
            BarEntry entry = new BarEntry(d.xValue, d.yValue);
            values.add(entry);

            // specific colors
            if (d.yValue >= 0)
                colors.add(green);
            else
                colors.add(red);
        }

        BarDataSet set;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "Values");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData Bardata = new BarData(set);
            Bardata.setValueTextSize(13f);
            // data.setValueTypeface(tfRegular);
            Bardata.setValueFormatter(new Formatter());
            Bardata.setBarWidth(0.8f);

            chart.setData(Bardata);
            chart.invalidate();
            chart.animateXY(1400,1400,Easing.Linear);
        }
    }


    public class Data {

        final String xAxisValue;
        final float yValue;
        final float xValue;

        Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }
    private class Formatter extends ValueFormatter
    {

        private final DecimalFormat mFormat;

        Formatter() {
            mFormat = new DecimalFormat("######.0");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }
}
