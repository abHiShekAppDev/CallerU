package com.developer.abhishek.calleru.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.abhishek.calleru.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomNotificationAdapter>{

    private final List<String> notificationList;

    public NotificationAdapter(List<String> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public CustomNotificationAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_list_item,viewGroup,false);
        return new CustomNotificationAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomNotificationAdapter customNotificationAdapter, int position) {
        if(notificationList.size()>position){
            String currentNumber = "+91 "+notificationList.get(position).substring(0,11);
            String newNumber = "+91 "+notificationList.get(position).substring(11);
            customNotificationAdapter.currentNumberTv.setText(currentNumber);
            customNotificationAdapter.newNumberTv.setText(newNumber);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class CustomNotificationAdapter extends RecyclerView.ViewHolder{

        @BindView(R.id.currenNumberInNoti)
        TextView currentNumberTv;
        @BindView(R.id.newNumberInNotif)
        TextView newNumberTv;

        public CustomNotificationAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
