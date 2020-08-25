package com.fundraiser.fundraiser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fundraiser.fundraiser.adapters.AllFundraisersAdapter;
import com.fundraiser.fundraiser.models.FundraisersModel;
import com.fundraiser.fundraiser.networking.RetrofitClient;
import com.fundraiser.fundraiser.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFundraisers extends AppCompatActivity {
    Button create;
    String cr,dt;
    ImageView arrowback;
    TextView newC;
    SharedPreferencesConfig sharedPreferencesConfig;
    RecyclerView recyclerView;
    AllFundraisersAdapter allFundraisersAdapter;
    ProgressBar pr;
    private final ArrayList<FundraisersModel>mMyFunds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fundraisers);
        create = findViewById(R.id.create_fundraiser);
        arrowback = findViewById(R.id.arrow_back);
        pr = findViewById(R.id.pr);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        recyclerView = findViewById(R.id.fundrecycler);
        allFundraisersAdapter = new AllFundraisersAdapter(this,mMyFunds);
        recyclerView.setAdapter(allFundraisersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cr = "create";
        dt = "details";

        arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFundraisers.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyFundraisers.this,CreateFundraiser.class);
                intent.putExtra("CREATE",cr);
                startActivity(intent);
            }
        });
        viewMyFs();


    }

    private void viewMyFs() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        pr.setVisibility(View.VISIBLE);
        mMyFunds.clear();
        Call<List<FundraisersModel>> call = RetrofitClient.getInstance(MyFundraisers.this)
                .getApiConnector()
                .fetchMyAll(phone);
        call.enqueue(new Callback<List<FundraisersModel>>() {
            @Override
            public void onResponse(Call<List<FundraisersModel>> call, Response<List<FundraisersModel>> response) {
                pr.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().size()>0){
                        mMyFunds.addAll(response.body());
                        allFundraisersAdapter.notifyDataSetChanged();
//                            if (mInterstitialAd.isLoaded()){
//                                mInterstitialAd.show();
//                            }
//                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }else {
                        // no_messages.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(MyFundraisers.this, "Server error " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FundraisersModel>> call, Throwable t) {
                pr.setVisibility(View.GONE);
                Toast.makeText(MyFundraisers.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
