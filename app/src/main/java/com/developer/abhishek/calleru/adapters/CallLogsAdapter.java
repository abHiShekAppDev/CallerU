package com.developer.abhishek.calleru.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.models.CallLogs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.CustomCallLogsAdapter>{

    private final List<CallLogs> callLogsList;
    private final Context context;

    public CallLogsAdapter(List<CallLogs> callLogsList, Context context) {
        this.callLogsList = callLogsList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomCallLogsAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.call_log_list_item,viewGroup,false);
        return new CustomCallLogsAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCallLogsAdapter customCallLogsAdapter, int position) {
        if(callLogsList != null && callLogsList.get(position) != null){

            if(callLogsList.get(position).getSavedName() != null && !callLogsList.get(position).getSavedName().isEmpty()){
                customCallLogsAdapter.callerNumberTv.setText(callLogsList.get(position).getSavedName());
            }else{
                customCallLogsAdapter.callerNumberTv.setText(callLogsList.get(position).getCallNumber());
            }
            customCallLogsAdapter.callDurationTv.setText(callLogsList.get(position).getCallDuration());
            customCallLogsAdapter.callTimeTv.setText(callLogsList.get(position).getCallDate());

            String callType = callLogsList.get(position).getCallType();
            if(callType != null){
                if(callType.equalsIgnoreCase("INCOMING")){
                    customCallLogsAdapter.callTypeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_received_black_24dp));
                }else if(callType.equalsIgnoreCase("MISSED")){
                    customCallLogsAdapter.callTypeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_missed_black_24dp));
                }else if(callType.equalsIgnoreCase("OUTGOING")){
                    customCallLogsAdapter.callTypeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_call_made_black_24dp));
                }else if(callType.equalsIgnoreCase("REJECTED")){
                    customCallLogsAdapter.callTypeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cancel_black_24dp));
                }else if(callType.equalsIgnoreCase("BLOCKED")){
                    customCallLogsAdapter.callTypeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_block_black_24dp));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return callLogsList.size();
    }

    public class CustomCallLogsAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.callerNumberTv)
        TextView callerNumberTv;
        @BindView(R.id.callDurationTv)
        TextView callDurationTv;
        @BindView(R.id.callTimeTv)
        TextView callTimeTv;
        @BindView(R.id.callTypeIv)
        ImageView callTypeIv;

        public CustomCallLogsAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
