package com.fundraiser.fundraiser.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fundraiser.fundraiser.FundraiserDetails;
import com.fundraiser.fundraiser.R;
import com.fundraiser.fundraiser.models.FundraisersModel;

import java.util.ArrayList;

public class AllFundraisersAdapter extends RecyclerView.Adapter<AllFundraisersAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<FundraisersModel> mFundsArrayList;
    private final LayoutInflater mLayoutInflator;

    public AllFundraisersAdapter(Context context, ArrayList<FundraisersModel>arrayList){
        mContext = context;
        mFundsArrayList = arrayList;
        mLayoutInflator = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflator.inflate(R.layout.fund_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FundraisersModel fundraisersModel = mFundsArrayList.get(position);
        holder.pp = fundraisersModel.getPin();
        holder.nn = Integer.toString(holder.pp);
        holder.fundraiser.setText(fundraisersModel.getTitle());
        holder.creator.setText("Creator: "+fundraisersModel.getOwner().getName());
        holder.pass = fundraisersModel.getPasscode();
        holder.fundId.setText("ID:"+holder.nn);
        holder.id = fundraisersModel.getId();
        holder.creat = fundraisersModel.getOwner().getName();
        holder.pho = fundraisersModel.getOwner().getPhone();
    }

    @Override
    public int getItemCount() {
        return mFundsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fundId,fundraiser,creator;
        int pass,pp,id;
        String nn,creat,pho;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fundId = itemView.findViewById(R.id.fund_id);
            fundraiser = itemView.findViewById(R.id.fundraiser);
            creator = itemView.findViewById(R.id.creator_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView cancel,confirm;
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    View view1 = mLayoutInflator.inflate(R.layout.confirm_pass,null);
                    final EditText enter_pass = view1.findViewById(R.id.passcode);
                    cancel = view1.findViewById(R.id.cancel);
                    confirm = view1.findViewById(R.id.done);

                    alert.setView(view1);
                    final AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int ii = Integer.parseInt(enter_pass.getText().toString());
                            if (ii!=pass){
                                Toast.makeText(mContext,"Wrong passcode, please retry",Toast.LENGTH_SHORT).show();
                            }else{
                                String f = fundraiser.getText().toString();
                                String d = Integer.toString(id);
                                String p = Integer.toString(ii);
                                Intent intent = new Intent(mContext, FundraiserDetails.class);
                                intent.putExtra("TITLE",f);
                                intent.putExtra("ID",d);
                                intent.putExtra("PASSCODE",p);
                                intent.putExtra("PIN",nn);
                                intent.putExtra("CREAT",creat);
                                intent.putExtra("PHONE",pho);
                                mContext.startActivity(intent);
                                alertDialog.dismiss();
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });
        }
    }
}
