package com.example.fidelitycorporation.Frags;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fidelitycorporation.R;

public class ViewUsageFragment extends Fragment {

    private TextView subscriptionType;
    private  ProgressBar progressBarSms,progressBarNotification;
    private  TextView progressBarSmsTxt,ProgressBarNotificationTxt;
    private TextView viewSmsUsage;
    private TextView viewNotifcationNsage;
    private TextView viewExpireDate;



    public ViewUsageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_viewusage, container, false);
        viewSmsUsage = root.findViewById(R.id.viewusage_sms);
        progressBarSms = root.findViewById(R.id.viewusage_progressBarSMS);
        progressBarSmsTxt = root.findViewById(R.id.viewusage_progressBarSMSText);
        progressBarNotification = root.findViewById(R.id.viewusage_progressBarNotification);
        ProgressBarNotificationTxt = root.findViewById(R.id.viewusage_progressBarNotificationText);
        ProgressBarNotificationTxt = root.findViewById(R.id.viewusage_progressBarNotificationText);
        viewExpireDate = root.findViewById(R.id.viewusage_subscriptionExpireDate);



        viewNotifcationNsage = root.findViewById(R.id.viewusage_notification);
        subscriptionType = root.findViewById(R.id.viewusage_subscriptionType);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("ManagerInformations", Context.MODE_PRIVATE);
        Log.e("ViewUsageFragment","SubscriptionType " + sharedPref.getString("SubscriptionType",null)+" Number OF SMS "+ sharedPref.getInt("NumberOfSMS",0));
        viewSmsUsage.setText(Integer.toString(sharedPref.getInt("NumberOfSMS",0))+"/100");
        viewNotifcationNsage.setText(Integer.toString(sharedPref.getInt("NumberOfNotification",0))+"/100");
        subscriptionType.setText(sharedPref.getString("SubscriptionType",null));
        viewExpireDate.setText(sharedPref.getString("SubscriptionExpire",null));

        progressBarSms.setProgress(100-sharedPref.getInt("NumberOfSMS",0));
        progressBarSmsTxt.setText(Integer.toString(100-sharedPref.getInt("NumberOfSMS",0))+"%");

        progressBarNotification.setProgress(100-sharedPref.getInt("NumberOfSMS",0));
        ProgressBarNotificationTxt.setText(Integer.toString(100-sharedPref.getInt("NumberOfSMS",0))+"%");


        return root;

    }
}

