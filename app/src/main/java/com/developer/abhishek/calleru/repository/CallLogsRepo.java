package com.developer.abhishek.calleru.repository;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.developer.abhishek.calleru.models.CallLogs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogsRepo {

    private MutableLiveData<List<CallLogs>> callLogsMutableLiveData = new MutableLiveData<>();
    private List<CallLogs> callLogsList = new ArrayList<>();
    private Context context;

    public MutableLiveData<List<CallLogs>> getCallLogsMutableLiveData(Context context) {
        this.context = context;
        new BackgroundTask().execute();
        return callLogsMutableLiveData;
    }

    private String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String callTime = String.valueOf(formatter.format(date));
        String currentTime = String.valueOf(formatter.format(new Date()));

        String time = null;

        //  checking if call is in same year
        if(callTime.substring(0,4).equalsIgnoreCase(currentTime.substring(0,4))){

            //  checking if call is in same month
            if(callTime.substring(5,7).equalsIgnoreCase(currentTime.substring(5,7))){

                //  checking if call is in same day
                if(callTime.substring(8,10).equalsIgnoreCase(currentTime.substring(8,10))){
                    time = callTime.substring(11);
                }else if((Integer.parseInt(currentTime.substring(8,10))-Integer.parseInt(callTime.substring(8,10))) == 1){
                    time = "yesterday";
                }else{
                    time = callTime.substring(5,10);
                }
                Log.d(String.valueOf(callTime)+" : ",String.valueOf((Integer.parseInt(currentTime.substring(8,10))-Integer.parseInt(callTime.substring(8,10)))));
            }else{
                time = callTime.substring(5,10);
            }
        }else{
            time = String.valueOf(Integer.parseInt(currentTime.substring(0,4))-Integer.parseInt(callTime.substring(0,4))) + "year ago";
        }

        return time;
    }

    private void removePreviousDuplication(String callType, String callNumber){

        if(callLogsList.size() > 0){
            String previousCallNumber = callLogsList.get(callLogsList.size()-1).getCallNumber();

            if(previousCallNumber.length() >= 10 && callNumber.length() >= 10){
                //  check if previous number is same to this number
                if(previousCallNumber.substring(previousCallNumber.length()-10).equalsIgnoreCase(callNumber.substring(callNumber.length()-10))) {
                    if (callType.equalsIgnoreCase(callLogsList.get(callLogsList.size() - 1).getCallType())) {
                        callLogsList.remove(callLogsList.size() - 1);
                    }
                }
            }else{
                if(previousCallNumber.substring(previousCallNumber.length()).equalsIgnoreCase(callNumber.substring(callNumber.length()))) {
                    if (callType.equalsIgnoreCase(callLogsList.get(callLogsList.size() - 1).getCallType())) {
                        callLogsList.remove(callLogsList.size() - 1);
                    }
                }
            }
        }
    }

    private String formatCallDuration(int callDur){
        String callDuration = null;

        if(callDur == 0){
            callDuration = "Cancelled";
        }else{
            if(callDur/60 != 0){
                callDuration = String.valueOf(callDur/60)+" min "+String.valueOf(callDur%60)+" sec";
            }else{
                callDuration = String.valueOf(callDur%60)+" sec";
            }
        }

        return callDuration;
    }

    class BackgroundTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");

            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

            while (cursor.moveToNext()){
                int callTypeCode = Integer.parseInt(cursor.getString(type));
                String callDateStr = cursor.getString(date);

                String callNumber = cursor.getString(number);
                String savedName = cursor.getString(name);

                String callType = null;
                switch(callTypeCode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        callType = "MISSED";
                        break;

                    case CallLog.Calls.REJECTED_TYPE:
                        callType = "REJECTED";
                        break;

                    case CallLog.Calls.BLOCKED_TYPE:
                        callType = "BLOCKED";
                        break;

                    default:
                        callType = "UNKNOWN";
                }

                removePreviousDuplication(callType,callNumber);

                Date callDate = new Date(Long.valueOf(callDateStr));
                String callTime = formatDate(callDate);

                String callDuration = formatCallDuration(Integer.parseInt(cursor.getString(duration)));

                callLogsList.add(new CallLogs(callType,callNumber,callDuration,callTime,savedName));
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callLogsMutableLiveData.setValue(callLogsList);
        }
    }
}
