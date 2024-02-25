package com.wts.chillarpay.client;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface interfaceAPI {

    @FormUrlEncoded
    @POST("Login")
    Call<JsonObject> Login(@Header("Authorization") String auth,
            @Field("username") String username,
            @Field("password") String password,
            @Field("token") String token,
            @Field("DeviceInfo") String DeviceInfo,
            @Field("Loginby") String Loginby);

    @FormUrlEncoded
    @POST("Changepassword")
    Call<JsonObject> changepassword(@Header("Authorization") String auth,
                                @Field("userid") String userid,
                                @Field("token") String token,
                                @Field("DeviceInfo") String DeviceInfo,
                                @Field("password") String password,
                                @Field("newpassword") String newpassword);

    @FormUrlEncoded
    @POST("Getcommision")
    Call<JsonObject> getcommision(@Header("Authorization") String auth,
                                  @Field("userid") String userid,
                                  @Field("token") String token,
                                  @Field("DeviceInfo") String DeviceInfo);

    @FormUrlEncoded
    @POST("getusers")
    Call<JsonObject> getusers(@Header("Authorization") String auth,
                                   @Field("userid") String userid,
                                   @Field("token") String token,
                                   @Field("DeviceInfo") String DeviceInfo);

    @FormUrlEncoded
    @POST("creditbalance")
    Call<JsonObject> creditbalance(@Header("Authorization") String auth,
                                 @Field("userid") String userid,
                                 @Field("token") String token,
                                 @Field("DeviceInfo") String DeviceInfo,
                                 @Field("comment") String comment,
                                 @Field("amount") String amount,
                                 @Field("creditto") String creditto);

    @FormUrlEncoded
    @POST("debitbalance")
    Call<JsonObject> debitbalance(@Header("Authorization") String auth,
                                   @Field("userid") String userid,
                                   @Field("token") String token,
                                   @Field("DeviceInfo") String DeviceInfo,
                                   @Field("comment") String comment,
                                   @Field("amount") String amount,
                                   @Field("creditto") String creditto);

    @FormUrlEncoded
    @POST("Getservice")
    Call<JsonObject> Getservice(@Header("Authorization") String auth,
                                  @Field("userid") String userid,
                                  @Field("token") String token,
                                  @Field("DeviceInfo") String DeviceInfo);

    @FormUrlEncoded
    @POST("Getoperators")
    Call<JsonObject> getoperators(@Header("Authorization") String auth,
                                @Field("userid") String userid,
                                @Field("token") String token,
                                @Field("DeviceInfo") String DeviceInfo,
                                @Field("serviceid") String serviceid);

    @FormUrlEncoded
    @POST("GetBalance")
    Call<JsonObject> getBalance(@Header("Authorization") String auth,
                                  @Field("userid") String userid,
                                  @Field("token") String token,
                                  @Field("DeviceInfo") String DeviceInfo);

    @FormUrlEncoded
    @POST("PaymentRequest")
    Call<JsonObject> paymentrequest(@Header("Authorization") String auth,
                                @Field("userid") String userid,
                                @Field("comment") String comment,
                                @Field("amount") String amount,
                                @Field("bankrefno") String bankrefno,
                                @Field("paymentmode") String paymentmode,
                                @Field("token") String token,
                                @Field("DeviceInfo") String DeviceInfo);

    @FormUrlEncoded
    @POST("Creditreport")
    Call<JsonObject> creditreport(@Header("Authorization") String auth,
                                    @Field("userid") String userid,
                                    @Field("token") String token,
                                    @Field("DeviceInfo") String DeviceInfo,
                                    @Field("searchby") String searchby,
                                    @Field("from") String from,
                                    @Field("to") String to);

    @FormUrlEncoded
    @POST("Debitreport")
    Call<JsonObject> debitreport(@Header("Authorization") String auth,
                                  @Field("userid") String userid,
                                  @Field("token") String token,
                                  @Field("DeviceInfo") String DeviceInfo,
                                  @Field("searchby") String searchby,
                                  @Field("from") String from,
                                  @Field("to") String to);

    @FormUrlEncoded
    @POST("Recharge")
    Call<JsonObject> recharge(@Header("Authorization") String auth,
                                 @Field("userid") String userid,
                                 @Field("token") String token,
                                 @Field("DeviceInfo") String DeviceInfo,
                                 @Field("amount") String amount,
                                 @Field("operatorid") String operatorid,
                                 @Field("number") String number,
                                 @Field("stype") String stype,
                                 @Field("Loginby") String Loginby);

    @FormUrlEncoded
    @POST("Getreport")
    Call<JsonObject> getreport(@Header("Authorization") String auth,
                              @Field("userid") String userid,
                              @Field("token") String token,
                              @Field("DeviceInfo") String DeviceInfo,
                              @Field("searchby") String searchby,
                              @Field("from") String from,
                              @Field("to") String to);

    @FormUrlEncoded
    @POST("Getledger")
    Call<JsonObject> getledger(@Header("Authorization") String auth,
                               @Field("userid") String userid,
                               @Field("token") String token,
                               @Field("DeviceInfo") String DeviceInfo,
                               @Field("searchby") String searchby,
                               @Field("from") String from,
                               @Field("to") String to);

    @FormUrlEncoded
    @POST("Credentialcheck")
    Call<JsonObject> checkCredential(@Header("Authorization") String auth,
                               @Field("username") String username,
                               @Field("password") String password,
                               @Field("DeviceId") String DeviceId);


}