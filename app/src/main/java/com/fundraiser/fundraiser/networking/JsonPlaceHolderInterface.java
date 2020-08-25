package com.fundraiser.fundraiser.networking;

import com.fundraiser.fundraiser.FundraiserDetails;
import com.fundraiser.fundraiser.PaymentOptions;
import com.fundraiser.fundraiser.models.ContributionsModel;
import com.fundraiser.fundraiser.models.FundraisersModel;
import com.fundraiser.fundraiser.models.MessageModel;
import com.fundraiser.fundraiser.models.Payments;
import com.fundraiser.fundraiser.models.TotalAmountModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderInterface {
    @FormUrlEncoded
    @POST("api/createfund")
    Call<MessageModel> createF(
            @Field("title")String tt,
            @Field("phone")String pp,
            @Field("passcode")String pass,
            @Field("mpesaPhone")String mphone,
            @Field("mpesaPaybillAccount")String paybac,
            @Field("mpesaPaybill")String payb,
            @Field("bankAccount")String bank,
            @Field("paypalAccount")String paypal
    );
    @FormUrlEncoded
    @POST("api/fetchown")
    Call<List<FundraisersModel>> fetchMyAll(
            @Field("phone")String phone
    );
    @FormUrlEncoded
    @POST("api/fetchpayments")
    Call<Payments> paymentO(
            @Field("id")String id
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<MessageModel> reg(
            @Field("phone")String pp,
            @Field("name")String nam,
            @Field("password")String pass
    );
    @FormUrlEncoded
    @POST("api/delete")
    Call<MessageModel> deleteF(
            @Field("id")String id
    );
    @FormUrlEncoded
    @POST("api/fetchcontribution")
    Call<List<ContributionsModel>> contriB(
            @Field("id")String id
    );
    @FormUrlEncoded
    @POST("api/seeall")
    Call<MessageModel> seeAl(
            @Field("id")String id
    );
    @FormUrlEncoded
    @POST("api/contribute")
    Call<MessageModel> contributeF(
            @Field("id")String id,
            @Field("phone")String phone,
            @Field("amount")String amou
    );
    @POST("api/fetchbyid")
    Call<List<FundraisersModel>> fetchById(
            @Field("id")String query
    );
    @GET("api/fetchfundraisers")
    Call<List<FundraisersModel>> fetchAll();
    @FormUrlEncoded
    @POST("api/totalamount")
    Call<TotalAmountModel> totalA(
            @Field("id")String id
    );
}
