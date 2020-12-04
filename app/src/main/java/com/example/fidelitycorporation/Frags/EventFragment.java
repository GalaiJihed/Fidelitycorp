package com.example.fidelitycorporation.Frags;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.fidelitycorporation.App.AppConfig;
import com.example.fidelitycorporation.App.DataCallBack;
import com.example.fidelitycorporation.Entities.Event;
import com.example.fidelitycorporation.R;
import com.example.fidelitycorporation.helper.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private SessionManager session;
    SharedPreferences sharedPref;
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private Spinner clientsNumber, eventType, notifyWay;
    private TextView eventName;
    private Button createButton;
    private static final int ADD_NOTE = 44;
    Dialog myDialog;
    private CalendarView mCalendarView;
    private List<EventDay> mEventDays = new ArrayList<>();
    private final String PREFERENCE_FILE_KEY_STORE = "id_store";
    private  Event event = new Event();
    //private ArrayList<Event> events = new ArrayList<Event>();


    FloatingActionButton addEvent;

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        session = new SessionManager(getContext());

        mCalendarView = view.findViewById(R.id.calendarView);


        sharedPref = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
        int idStore = sharedPref.getInt("idStore", 0);
        getEvents(idStore);


        //  mEventDays.add(new EventDay(Calendar.getInstance().getFirstDayOfWeek(),));

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                //Log.e("Calendar", eventDay.getCalendar().toString());
                Calendar rightNow = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                String getCurrentDateTime = sdf.format(rightNow.getTime());
                String choosenDate = sdf.format(clickedDayCalendar.getTime());
                if (eventDay.getImageResource() == R.drawable.bday_icon) {
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    ShowPopupEventDetails(getView(),sdf2.format(clickedDayCalendar.getTime()));
                } else {


                    if (getCurrentDateTime.compareTo(choosenDate) > 0) {
                        Toast.makeText(getContext(),
                                "You cannot choose a date before today ", Toast.LENGTH_LONG)
                                .show();

                    } else {
//                    Toast.makeText(getContext(),
//                            eventDay.getCalendar().toString(), Toast.LENGTH_LONG)
//                            .show();
                        ShowPopupNewEvent(getView(), choosenDate);
                    }
                }


            }
        });
//        Toast.makeText(getContext(),
//                Event.events.toString(), Toast.LENGTH_LONG)
//                .show();
        return view;
    }

    public void ShowPopupNewEvent(View v, String eventDate) {
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.add_event_calender);
        //Spinner for client Number
        clientsNumber = myDialog.findViewById(R.id.newevent_popup_spinnerClientNumber);
        ArrayAdapter<CharSequence> adapterClientsNumber = ArrayAdapter.createFromResource(getContext(), R.array.event_number, android.R.layout.simple_spinner_item);
        adapterClientsNumber.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientsNumber.setAdapter(adapterClientsNumber);
        clientsNumber.setOnItemSelectedListener(this);
        //Spinner for event Type
        eventType = myDialog.findViewById(R.id.newevent_popup_spinnereventtype);
        ArrayAdapter<CharSequence> adapterEventType = ArrayAdapter.createFromResource(getContext(), R.array.event_types, android.R.layout.simple_spinner_item);
        adapterEventType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventType.setAdapter(adapterEventType);
        eventType.setOnItemSelectedListener(this);
        // Spinner for Notfication Way
        notifyWay = myDialog.findViewById(R.id.newevent_popup_spinnerNotfiyWay);
        ArrayAdapter<CharSequence> adapterNotifyWay = ArrayAdapter.createFromResource(getContext(), R.array.event_notfiyway, android.R.layout.simple_spinner_item);
        adapterNotifyWay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notifyWay.setAdapter(adapterNotifyWay);
        notifyWay.setOnItemSelectedListener(this);
        // Get Store ID from SharedPref
        sharedPref = getActivity().getSharedPreferences(PREFERENCE_FILE_KEY_STORE, Context.MODE_PRIVATE);
        int idStore = sharedPref.getInt("idStore", 0);

        //Event Name
        eventName = myDialog.findViewById(R.id.newevent_popup_eventname);

        // close button
        ImageView closeButton =  myDialog.findViewById(R.id.newevent_popup_closebtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        //Confirm button action
        createButton = myDialog.findViewById(R.id.newevent_popup_createbutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),
//                        notifyWay.getSelectedItem().toString(), Toast.LENGTH_SHORT)
//                        .show();

                createEvent(eventName.getText().toString(), eventDate, eventType.getSelectedItem().toString(), notifyWay.getSelectedItemPosition(), clientsNumber.getSelectedItemPosition(), idStore);
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    public void ShowPopupEventDetails(View v,String choosendate) {
        int idStore = sharedPref.getInt("idStore", 0);
        getEventDetails(idStore,choosendate);
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.detail_event_calender);
        TextView eventName = myDialog.findViewById(R.id.eventdetail_popup_eventname);
        TextView eventType = myDialog.findViewById(R.id.eventdetail_popup_eventtype);
        TextView eventDate = myDialog.findViewById(R.id.eventdetail_popup_eventDate);
        eventName.setText(event.getEventName());
        eventType.setText(event.getEventType());
        eventDate.setText(event.getEventDate());
//      Log.e("EVENT POPUP", event.toString());
        ImageView closeButton =  myDialog.findViewById(R.id.eventdetail_popup_closebtn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void createEvent(final String EventName, final String EventDate, final String EventType, final int NotifyWay, final int NumberOfClients, final int StoreId) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("EventName", EventName);
            jsonBody.put("EventDate", EventDate);
            jsonBody.put("EventType", EventType);
            jsonBody.put("NotifyWay", NotifyWay);
            jsonBody.put("NumberOfClients", NumberOfClients);
            jsonBody.put("StoreId", StoreId);
            final String requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CREATE_NEW_EVENT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e("VOLLEY", response.toString());
                    // Check the JWT Token
                    Toast.makeText(getContext(),
                            requestBody + " response :" + response, Toast.LENGTH_LONG)
                            .show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getContext(),
                                "Please Verify storeID.", Toast.LENGTH_SHORT)
                                .show();
                    } else
                    if (error.networkResponse.statusCode == 402) {
                        Toast.makeText(getContext(),
                                "You dont have enough SMS Or Notification", Toast.LENGTH_SHORT)
                                .show();
                    } else if (error.networkResponse.statusCode == 200) {
                        Toast.makeText(getContext(),
                                "Event Created.", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if (error.networkResponse.statusCode == 401) {
                        Toast.makeText(getContext(),
                                "A problem has been occured try to logout and relogin", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        Toast.makeText(getContext(),
                                "A problem has been occured Try later", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
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
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    private void getEvents(final int StoreId) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("StoreId", StoreId);
            final String requestBody = jsonBody.toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_STORE_EVENTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    Log.e("EVENT FRAG", response.toString());
                    // Check the JWT Token
//                    Toast.makeText(getContext(),
//                            requestBody + " response :" + response, Toast.LENGTH_LONG)
//                            .show();
                    try {
                        Event.events.clear();

                        JSONArray eventsJson = new JSONArray(response);
                        for (int i = 0; i < eventsJson.length(); i++) {
                            JSONObject eventObject = eventsJson.getJSONObject(i);
                            Event event = new Event();
                            int eventid = eventObject.getInt("id");
                            String eventName = eventObject.getString("eventName");
                            String eventType = eventObject.getString("eventType");
                            String eventDate = eventObject.getString("eventDate");
                            event.setId(eventid);
                            event.setEventName(eventName);
                            event.setEventType(eventType);
                            event.setEventDate(eventDate);
                            Event.events.add(event);



                            //String datedate = eventDate.substring(0,eventDate.indexOf('Z'));
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = sdf.parse(eventDate);


                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(date);


//                            Toast.makeText(getContext(),
//                                    date.toString(), Toast.LENGTH_SHORT)
//                                    .show();

                            //    Calendar calendar1 = Calendar.getInstance();
                            //  calendar1.setTime(sdf.parse("eventDate"));
                            calendar1.add(calendar1.DAY_OF_MONTH, 0);
                            mEventDays.add(new EventDay(calendar1, R.drawable.bday_icon));

//                            Calendar calendar2 = Calendar.getInstance();
//                            calendar2.add(Calendar.DAY_OF_MONTH, 9);
//                            mEventDays.add(new EventDay(calendar2, R.drawable.bday_icon));

//                            Calendar calendar3 = Calendar.getInstance();
//                            calendar3.add(Calendar.DAY_OF_MONTH, 1);
//                            mEventDays.add(new EventDay(calendar3, R.drawable.bday_icon));
//
//                            Calendar calendar4 = Calendar.getInstance();
//                            calendar4.add(Calendar.DAY_OF_MONTH, 13);
//                            mEventDays.add(new EventDay(calendar4, R.drawable.bday_icon));
//                            Calendar min = Calendar.getInstance();
//                            min.add(Calendar.MONTH, -2);

//                            Calendar max = Calendar.getInstance();
//                            max.add(Calendar.MONTH, 2);
//                            List<Calendar> calendars = new ArrayList<>();
                            mCalendarView.setEvents(mEventDays);

                        }
                    } catch (JSONException | ParseException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getContext(),
                                "Please Verify storeID.", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getContext(),
                                "EVents ", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
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

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    private  void getEventDetails(final int StoreId, final String date) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("StoreId", StoreId);
            jsonBody.put("date", date);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_GET_STORE_EVENT_DETAIL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("EVENT FRAG", response.toString());
                    // Check the JWT Token
//                    Toast.makeText(getContext(),
//                            requestBody + " response :" + response, Toast.LENGTH_LONG)
//                            .show();
                    try {

                        JSONObject eventObject = new JSONObject(response);

                        int eventid = eventObject.getInt("id");
                        String eventName = eventObject.getString("eventName");
                        String eventType = eventObject.getString("eventType");
                        String eventDate = eventObject.getString("eventDate");
                        event.setId(eventid);
                        event.setEventName(eventName);
                        event.setEventType(eventType);
                        event.setEventDate(eventDate);
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("VOLLEY", error.toString());
                    if (error.networkResponse.statusCode == 400) {
                        Toast.makeText(getContext(),
                                "Please Verify storeID.", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getContext(),
                                "EVents ", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }) {

                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
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


        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}