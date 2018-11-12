package com.developer.abhishek.calleru.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.abhishek.calleru.R;
import com.developer.abhishek.calleru.models.Users;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.CustomSearchAdapter>{

    private List<Users> usersList;

    public SearchAdapter(List<Users> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public CustomSearchAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_item, viewGroup, false);
        return new CustomSearchAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomSearchAdapter customSearchAdapter, int position) {
        try {
            customSearchAdapter.nameTv.setText(usersList.get(position).getName());
            customSearchAdapter.numberTv.setText(usersList.get(position).getMobileNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class CustomSearchAdapter extends RecyclerView.ViewHolder {

        @BindView(R.id.nameTv)
        TextView nameTv;
        @BindView(R.id.numberTv)
        TextView numberTv;

        public CustomSearchAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
