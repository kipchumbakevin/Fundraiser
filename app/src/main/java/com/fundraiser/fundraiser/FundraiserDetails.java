package com.fundraiser.fundraiser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fundraiser.fundraiser.adapters.ContributionsAdapter;
import com.fundraiser.fundraiser.models.ContributionsModel;
import com.fundraiser.fundraiser.models.MessageModel;
import com.fundraiser.fundraiser.models.TotalAmountModel;
import com.fundraiser.fundraiser.networking.RetrofitClient;
import com.fundraiser.fundraiser.utils.SharedPreferencesConfig;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundraiserDetails extends AppCompatActivity {

    Button contribute;
    TextView share_report,receive,title,invite,creator,total,deletefund;
    String dt,passcode,titl,id,pin,cre,phone;
    ImageView arrowback;
    RecyclerView recyclerView;
    SharedPreferencesConfig sharedPreferencesConfig;
    ProgressBar pr;
    ContributionsAdapter contributionsAdapter;
    private final ArrayList<ContributionsModel> mContributionArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundraiser_details);
        contribute = findViewById(R.id.make_contribution);
        share_report = findViewById(R.id.share_report);
        receive = findViewById(R.id.receive_intent);
        arrowback = findViewById(R.id.arrow_back);
        invite = findViewById(R.id.invite);
        title = findViewById(R.id.title);
        deletefund = findViewById(R.id.delete_fund);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        pr = findViewById(R.id.pr);
        total = findViewById(R.id.total);
        creator = findViewById(R.id.creator);
        recyclerView = findViewById(R.id.contributionsrecycler);
        contributionsAdapter = new ContributionsAdapter(this,mContributionArrayList);
        recyclerView.setAdapter(contributionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        passcode = getIntent().getExtras().getString("PASSCODE");
        pin = getIntent().getExtras().getString("PIN");
        titl = getIntent().getExtras().getString("TITLE");
        cre = getIntent().getExtras().getString("CREAT");
        id = getIntent().getExtras().getString("ID");
        phone = getIntent().getExtras().getString("PHONE");

        if (phone.equals(sharedPreferencesConfig.readClientsPhone())){
            share_report.setVisibility(View.GONE);
            deletefund.setVisibility(View.GONE);
            seeAll();
        }
        if (getIntent().hasExtra("DETAILS")){
            dt = getIntent().getExtras().getString("DETAILS");
            receive.setText(dt);
        }
        title.setText(titl);
        creator.setText(cre);
        deletefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFu();
            }
        });

        contribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FundraiserDetails.this,PaymentOptions.class);
                intent.putExtra("ID",id);
                intent.putExtra("PASSCODE",passcode);
                intent.putExtra("PIN",pin);
                intent.putExtra("TITLE",titl);
                intent.putExtra("CREAT",cre);
                intent.putExtra("PHONE",phone);
                startActivity(intent);
                finish();
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });
        arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!receive.getText().toString().isEmpty() && receive.getText().toString().equals("details")){
                    Intent intent = new Intent(FundraiserDetails.this,MyFundraisers.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(FundraiserDetails.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        totalAmount();
        viewContributions();
    }

    private void deleteFu() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete")
                .setMessage("Are your sure you want to delete?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call<MessageModel> call = RetrofitClient.getInstance(FundraiserDetails.this)
                                .getApiConnector()
                                .deleteF(id);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                if (response.isSuccessful()) {
                                    Intent intent = new Intent(FundraiserDetails.this,MyFundraisers.class);
                                    startActivity(intent);
                                } else {
                                }
                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                            }

                        });
                    }
                });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void seeAll() {
        Call<MessageModel> call = RetrofitClient.getInstance(FundraiserDetails.this)
                .getApiConnector()
                .seeAl(id);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                if (response.isSuccessful()) {


                } else {
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
            }

        });
    }

    private void viewContributions() {
        pr.setVisibility(View.VISIBLE);
        mContributionArrayList.clear();
        Call<List<ContributionsModel>> call = RetrofitClient.getInstance(FundraiserDetails.this)
                .getApiConnector()
                .contriB(id);
        call.enqueue(new Callback<List<ContributionsModel>>() {
            @Override
            public void onResponse(Call<List<ContributionsModel>> call, Response<List<ContributionsModel>> response) {
                pr.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size()>0){
                        mContributionArrayList.addAll(response.body());
                        contributionsAdapter.notifyDataSetChanged();
//                            if (mInterstitialAd.isLoaded()){
//                                mInterstitialAd.show();
//                            }
//                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }else {
                        // no_messages.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(FundraiserDetails.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ContributionsModel>> call, Throwable t) {
                pr.setVisibility(View.GONE);
                Toast.makeText(FundraiserDetails.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void totalAmount() {
        Call<TotalAmountModel> call = RetrofitClient.getInstance(FundraiserDetails.this)
                .getApiConnector()
                .totalA(id);
        call.enqueue(new Callback<TotalAmountModel>() {
            @Override
            public void onResponse(Call<TotalAmountModel> call, Response<TotalAmountModel> response) {
                if (response.code()==201) {
                    if (response.body().getNum()>0){
                        NumberFormat numberFormat = new DecimalFormat("#,###");
                        double mynumber = response.body().getNum();
                        total.setVisibility(View.VISIBLE);
                        total.setText("Total amount: "+numberFormat.format(mynumber));
                    }


                }
                else {
                    // Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TotalAmountModel> call, Throwable t) {
                //Toast.makeText(MainActivity.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void invite() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String shareBody = title.getText().toString() +
                "Download FundRaiser App now at https://play.google.com/store/apps/details?id=" + FundraiserDetails.this.getPackageName() +"\n to send your contribution\nNB:Use this Search ID:"+pin+" to easily find the fundraiser and this Passcode:"+passcode+" to access it";
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Invite via"));
    }
}
