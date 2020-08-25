package com.fundraiser.fundraiser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fundraiser.fundraiser.models.MessageModel;
import com.fundraiser.fundraiser.networking.RetrofitClient;
import com.fundraiser.fundraiser.utils.SharedPreferencesConfig;
import com.hbb20.CountryCodePicker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFundraiser extends AppCompatActivity {

    EditText enter_mpesa_number,enterPaybill,enterPaybillAccount,enterBankAccount,enterPaypal,title,passcode;
    TextView submit,receive;
    ImageView info,arrowback;
    String cr;
    ProgressBar progressBar;
    SharedPreferencesConfig sharedPreferencesConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_fundraiser);
        title = findViewById(R.id.title);
        enter_mpesa_number=findViewById(R.id.enter_mpesa_phone);
        enterPaybill= findViewById(R.id.enter_mpesa_paybill);
        enterPaybillAccount = findViewById(R.id.enter_mpesa_paybillaccount);
        enterBankAccount = findViewById(R.id.enter_bank_account);
        enterPaypal = findViewById(R.id.enter_paypal_account);
        passcode = findViewById(R.id.passcode);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        arrowback = findViewById(R.id.arrow_back);
        submit = findViewById(R.id.submit);
        info = findViewById(R.id.info);
        progressBar = findViewById(R.id.pr);
        receive = findViewById(R.id.receive_intent);
        if (getIntent().hasExtra("CREATE")){
            cr = getIntent().getExtras().getString("CREATE");
            receive.setText(cr);
        }

        arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receive.getText().toString().equals("create")){
                    Intent intent = new Intent(CreateFundraiser.this,MyFundraisers.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(CreateFundraiser.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isErrors()){
                }
                else {
                    createFund();
                }
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inform();
            }
        });
    }

    private void createFund() {
        String pp,tt,pass,mphone,paybac,payb,bank,paypal;
        tt = title.getText().toString();
        pp = sharedPreferencesConfig.readClientsPhone();
        pass = passcode.getText().toString();
        if (!enter_mpesa_number.getText().toString().isEmpty()){
            mphone = enter_mpesa_number.getText().toString();
        }else {
            mphone = "";
        }
        if (!enterPaypal.getText().toString().isEmpty()){
            paypal = enterPaypal.getText().toString();
        }else {
            paypal = "";
        }
        if (!enterPaybill.getText().toString().isEmpty()){
            payb = enterPaybill.getText().toString();
        }else {
            payb = "";
        }
        if (!enterPaybillAccount.getText().toString().isEmpty()){
            paybac = enterPaybillAccount.getText().toString();
        }else {
            paybac = "";
        }
        if (!enterBankAccount.getText().toString().isEmpty()){
            bank = enterBankAccount.getText().toString();
        }else {
            bank = "";
        }

        progressBar.setVisibility(View.VISIBLE);
        Call<MessageModel> call = RetrofitClient.getInstance(CreateFundraiser.this)
                .getApiConnector()
                .createF(tt,pp,pass,mphone,paybac,payb,bank,paypal);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code()==201) {
                    Intent intent = new Intent(CreateFundraiser.this,MyFundraisers.class);
                    startActivity(intent);
//                            if (mInterstitialAd.isLoaded()){
//                                mInterstitialAd.show();
//                            }
//                            mInterstitialAd.loadAd(new AdRequest.Builder().build());

                } else {
                    Toast.makeText(CreateFundraiser.this, "Server error ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CreateFundraiser.this, "Network error", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void inform() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CreateFundraiser.this);
        alert.setTitle("Passcode info")
                .setMessage("The passcode will be used by your friends,family,group or any other person willing to contribute to your fundraiser")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private boolean isErrors() {
        if (title.getText().toString().isEmpty() &&
                passcode.getText().toString().isEmpty()){
            Toast.makeText(CreateFundraiser.this,"Enter the title and passcode of your fundraiser",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (title.getText().toString().isEmpty()){
            Toast.makeText(CreateFundraiser.this,"Enter the title of your fundraiser",Toast.LENGTH_SHORT).show();
            return true;
        }

        if (passcode.getText().toString().isEmpty()){
            Toast.makeText(CreateFundraiser.this,"Enter the passcode of your fundraiser",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (enter_mpesa_number.getText().toString().isEmpty() && enterBankAccount.getText().toString().isEmpty()
        && enterPaypal.getText().toString().isEmpty() && enterPaybillAccount.getText().toString().isEmpty()
        && enterPaybill.getText().toString().isEmpty()){
            Toast.makeText(CreateFundraiser.this,"Enter atleast one payment option",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!enterPaybill.getText().toString().isEmpty() && enterPaybillAccount.getText().toString().isEmpty()){
            Toast.makeText(CreateFundraiser.this,"Add your mpesa paybill account number",Toast.LENGTH_SHORT).show();
            return true;
        }
        if (enterPaybill.getText().toString().isEmpty() && !enterPaybillAccount.getText().toString().isEmpty()){
            Toast.makeText(CreateFundraiser.this,"Add your mpesa paybill number",Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            return false;
        }

    }
}
