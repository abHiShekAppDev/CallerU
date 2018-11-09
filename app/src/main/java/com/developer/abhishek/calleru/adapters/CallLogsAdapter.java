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
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.CustomCallLogsAdapter>{

    private List<CallLogs> callLogsList;
    private Context context;

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
            customCallLogsAdapter.callerNumberTv.setText(callLogsList.get(position).getCallNumber());
            customCallLogsAdapter.callDurationTv.setText(callLogsList.get(position).getCallDuration());
            customCallLogsAdapter.callTimeTv.setText(callLogsList.get(position).getCallDate());

            String callType = callLogsList.get(position).getCallType();
            if(callType != null){
                /*if(callType.equalsIgnoreCase("INCOMING")){
                    Picasso.get().load(R.drawable.ic_menu_camera).into(customCallLogsAdapter.callTypeIv);

                }else if(callType.equalsIgnoreCase("MISSED")){
                    Picasso.get().load(R.drawable.ic_menu_gallery).into(customCallLogsAdapter.callTypeIv);
                }else{
                    Picasso.get().load(R.drawable.ic_menu_slideshow).into(customCallLogsAdapter.callTypeIv);
                }*/
                customCallLogsAdapter.callTypeIv.setText(callType.substring(0,1));
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
        TextView callTypeIv;

        public CustomCallLogsAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
