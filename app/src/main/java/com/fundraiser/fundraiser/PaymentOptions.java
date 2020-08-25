package com.fundraiser.fundraiser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fundraiser.fundraiser.models.MessageModel;
import com.fundraiser.fundraiser.models.Payments;
import com.fundraiser.fundraiser.models.TotalAmountModel;
import com.fundraiser.fundraiser.networking.RetrofitClient;
import com.fundraiser.fundraiser.utils.SharedPreferencesConfig;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentOptions extends AppCompatActivity {

    LinearLayoutCompat mpesaNo,payBill,bank,paypal;
    ImageView arrowback;
    TextView mpesaPhone,mpesaPaybill,mpesaPaybillAccount,bankAcc,paypalAcc;
    Payments payments;
    String id,passcode,pin,title,creat,phone;
    SharedPreferencesConfig sharedPreferencesConfig;
    ProgressBar pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        arrowback = findViewById(R.id.back);
        mpesaNo = findViewById(R.id.linear_mpesa_number);
        payBill  =findViewById(R.id.linear_paybill);
        bank = findViewById(R.id.linear_bank);
        pr = findViewById(R.id.pr);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        paypal = findViewById(R.id.linear_paypal);
        mpesaPhone = findViewById(R.id.mpesa_phone);
        mpesaPaybillAccount = findViewById(R.id.mpesa_paybillaccount);
        mpesaPaybill = findViewById(R.id.mpesa_paybill);
        bankAcc = findViewById(R.id.bank_account);
        payments = new Payments();
        paypalAcc = findViewById(R.id.paypal_account);
        id = getIntent().getExtras().getString("ID");
        passcode=getIntent().getExtras().getString("PASSCODE");
        pin = getIntent().getExtras().getString("PIN");
        title = getIntent().getExtras().getString("TITLE");
        creat = getIntent().getExtras().getString("CREAT");
        phone = getIntent().getExtras().getString("PHONE");

        arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentOptions.this,FundraiserDetails.class);
                startActivity(intent);
                finish();
            }
        });
        mpesaNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contri();
            }
        });
        paymentOptions();
    }

    private void contri() {
        TextView cancel,done;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.contribute,null);
        final EditText amount = view.findViewById(R.id.amount);
        final ProgressBar pr = view.findViewById(R.id.pr);
        cancel = view.findViewById(R.id.cancel);
        done = view.findViewById(R.id.done);

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
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
                pr.setVisibility(View.VISIBLE);
                final String phone = sharedPreferencesConfig.readClientsPhone();
                String amou = amount.getText().toString();
                Call<MessageModel> call = RetrofitClient.getInstance(PaymentOptions.this)
                        .getApiConnector()
                        .contributeF(id,phone,amou);
                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        pr.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(PaymentOptions.this,FundraiserDetails.class);
                            intent.putExtra("ID",id);
                            intent.putExtra("PASSCODE",passcode);
                            intent.putExtra("PIN",pin);
                            intent.putExtra("TITLE",title);
                            intent.putExtra("CREAT",creat);
                            intent.putExtra("PHONE",phone);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PaymentOptions.this,"Server error",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        pr.setVisibility(View.GONE);
                        Toast.makeText(PaymentOptions.this,"Network error",Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

    }

    private void paymentOptions() {
        pr.setVisibility(View.VISIBLE);
        Call<Payments> call = RetrofitClient.getInstance(PaymentOptions.this)
                .getApiConnector()
                .paymentO(id);
        call.enqueue(new Callback<Payments>() {
            @Override
            public void onResponse(Call<Payments> call, Response<Payments> response) {
                pr.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getMpesaPhone()!=null){
                        mpesaNo.setVisibility(View.VISIBLE);
                        mpesaPhone.setText(response.body().getMpesaPhone());
                    }
                    if (response.body().getMpesaPaybill()!=null && response.body().getMpesaAccountNumber()!=null){
                        payBill.setVisibility(View.VISIBLE);
                        mpesaPaybill.setText(String.valueOf(payments.getMpesaPaybill()));
                        mpesaPaybillAccount.setText(String.valueOf(response.body().getMpesaAccountNumber()));
                    }
                    if (response.body().getBankAccount()!=null){
                        bank.setVisibility(View.VISIBLE);
                        bankAcc.setText(String.valueOf(response.body().getBankAccount()));
                    }
                    if (response.body().getPaypalAccount()!=null){
                        paypal.setVisibility(View.VISIBLE);
                        paypalAcc.setText(String.valueOf(String.valueOf(response.body().getPaypalAccount())));
                    }
                }
                else {
                     Toast.makeText(PaymentOptions.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Payments> call, Throwable t) {
                pr.setVisibility(View.GONE);
                Toast.makeText(PaymentOptions.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PaymentOptions.this,FundraiserDetails.class);
        intent.putExtra("ID",id);
        intent.putExtra("PASSCODE",passcode);
        intent.putExtra("PIN",pin);
        intent.putExtra("TITLE",title);
        intent.putExtra("CREAT",creat);
        intent.putExtra("PHONE",phone);
        startActivity(intent);
    }
}
