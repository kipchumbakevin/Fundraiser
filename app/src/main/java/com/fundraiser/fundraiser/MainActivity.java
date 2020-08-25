package com.fundraiser.fundraiser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fundraiser.fundraiser.adapters.AllFundraisersAdapter;
import com.fundraiser.fundraiser.models.FundraisersModel;
import com.fundraiser.fundraiser.models.MessageModel;
import com.fundraiser.fundraiser.networking.RetrofitClient;
import com.fundraiser.fundraiser.utils.SharedPreferencesConfig;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button create,mine;
    androidx.appcompat.widget.SearchView searchView;
    SharedPreferencesConfig sharedPreferencesConfig;
    RecyclerView recyclerView;
    AllFundraisersAdapter allFundraisersAdapter;
    ProgressBar pr;
    private final ArrayList<FundraisersModel> mFundArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create = findViewById(R.id.create_fundraiser);
        sharedPreferencesConfig = new SharedPreferencesConfig(MainActivity.this);
        mine = findViewById(R.id.my_fundraisers);
        searchView = findViewById(R.id.search);
        recyclerView = findViewById(R.id.fundrecycler);
        pr = findViewById(R.id.pr);
        allFundraisersAdapter = new AllFundraisersAdapter(this,mFundArrayList);
        recyclerView.setAdapter(allFundraisersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewFundraisers();
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
                    alertMy();
                }else {
                    Intent intent = new Intent(MainActivity.this,MyFundraisers.class);
                    startActivity(intent);
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
                    alertCreate();
                }else {
                    Intent intent = new Intent(MainActivity.this,CreateFundraiser.class);
                    startActivity(intent);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(MainActivity.this,query,Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void viewFundraisers() {
        pr.setVisibility(View.VISIBLE);
            mFundArrayList.clear();
            Call<List<FundraisersModel>> call = RetrofitClient.getInstance(MainActivity.this)
                    .getApiConnector()
                    .fetchAll();
            call.enqueue(new Callback<List<FundraisersModel>>() {
                @Override
                public void onResponse(Call<List<FundraisersModel>> call, Response<List<FundraisersModel>> response) {
                    pr.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        if (response.body().size()>0){
                            mFundArrayList.addAll(response.body());
                            allFundraisersAdapter.notifyDataSetChanged();
//                            if (mInterstitialAd.isLoaded()){
//                                mInterstitialAd.show();
//                            }
//                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }else {
                           // no_messages.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<FundraisersModel>> call, Throwable t) {
                    pr.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }

            });

    }

    private void alertCreate() {
        final EditText name,phone,password;
        TextView cancel,done;
        CountryCodePicker ccp;
        final ProgressBar ppr;
        AlertDialog.Builder alertt = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.addphone,null);
        name = view.findViewById(R.id.full_name);
        ccp = view.findViewById(R.id.ccp);
        phone = view.findViewById(R.id.owner_phone);
        password = view.findViewById(R.id.pass);
        cancel = view.findViewById(R.id.cancel);
        done = view.findViewById(R.id.done);
        ppr = view.findViewById(R.id.pr);
        ccp.registerCarrierNumberEditText(phone);

        alertt.setView(view);
        final AlertDialog alertDialog = alertt.create();
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nam,pp,pass;
                nam = name.getText().toString();
                pp = phone.getText().toString();
                pass = password.getText().toString();
                ppr.setVisibility(View.VISIBLE);
                mFundArrayList.clear();
                Call<MessageModel> call = RetrofitClient.getInstance(MainActivity.this)
                        .getApiConnector()
                        .reg(pp,nam,pass);
                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        ppr.setVisibility(View.GONE);
                        if (response.code()==200) {
                            sharedPreferencesConfig.saveAuthenticationInformation(pp,nam);
                           Toast.makeText(MainActivity.this,"Successfull",Toast.LENGTH_SHORT).show();
                           alertDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this,CreateFundraiser.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        ppr.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
    private void alertMy() {
        final EditText name,phone,password;
        TextView cancel,done;
        CountryCodePicker ccp;
        final ProgressBar ppr;
        AlertDialog.Builder alertt = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.addphone,null);
        name = view.findViewById(R.id.full_name);
        ccp = view.findViewById(R.id.ccp);
        phone = view.findViewById(R.id.owner_phone);
        password = view.findViewById(R.id.pass);
        cancel = view.findViewById(R.id.cancel);
        done = view.findViewById(R.id.done);
        ppr = view.findViewById(R.id.pr);
        ccp.registerCarrierNumberEditText(phone);

        alertt.setView(view);
        final AlertDialog alertDialog = alertt.create();
        alertDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nam,pp,pass;
                nam = name.getText().toString();
                pp = phone.getText().toString();
                pass = password.getText().toString();
                ppr.setVisibility(View.VISIBLE);
                mFundArrayList.clear();
                Call<MessageModel> call = RetrofitClient.getInstance(MainActivity.this)
                        .getApiConnector()
                        .reg(pp,nam,pass);
                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        ppr.setVisibility(View.GONE);
                        if (response.code()==200) {
                            sharedPreferencesConfig.saveAuthenticationInformation(pp,nam);
                            Toast.makeText(MainActivity.this,"Successfull",Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this,MyFundraisers.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        ppr.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}
