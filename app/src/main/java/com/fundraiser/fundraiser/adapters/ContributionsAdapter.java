package com.fundraiser.fundraiser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fundraiser.fundraiser.R;
import com.fundraiser.fundraiser.models.ContributionsModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ContributionsAdapter extends RecyclerView.Adapter<ContributionsAdapter.ViewHolders> {
    private final Context mContext;
    private final ArrayList<ContributionsModel> mContributionsArray;
    private final LayoutInflater mLayoutInflator;

    public ContributionsAdapter(Context context, ArrayList<ContributionsModel>arrayList){
        mContext = context;
        mContributionsArray = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.contributions_layout,parent,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolders holder, int position) {
        ContributionsModel contributionsModel = mContributionsArray.get(position);
        holder.name.setText(contributionsModel.getName());
        NumberFormat numberFormat = new DecimalFormat("#,###");
        double mynumber = Double.parseDouble(contributionsModel.getAmount());
        holder.amount.setText(numberFormat.format(mynumber));
        if (contributionsModel.getSeen()==0){
            holder.newnew.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mContributionsArray.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder {
        TextView name,amount,newnew;
        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            newnew = itemView.findViewById(R.id.newnew);
        }
    }
}
