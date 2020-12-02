package com.example.userapp.services;

import androidx.annotation.NonNull;

import com.example.userapp.model.ApplicationDetailsResponse;
import com.example.userapp.model.ApplicationListResponse;
import com.example.userapp.model.ApplyServiceResponse;
import com.example.userapp.model.ChangePasswordResponse;
import com.example.userapp.model.DashboardResponse;
import com.example.userapp.model.DownloadCertificateResponse;
import com.example.userapp.model.ForgotPasswordResponse;
import com.example.userapp.model.SignInResponse;
import com.example.userapp.model.SignUpResponse;
import com.example.userapp.model.UpdateProfileResponse;
import com.example.userapp.model.ViewReceiptResponse;
import com.example.userapp.model.ZoneListResponse;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<SignInResponse> login_user(@Field("username") String mobile_no, @Field("password") String password);

    @FormUrlEncoded
    @POST("registeruser")
    Call<SignUpResponse> registerUser(@Field("full_name") String strFullName, @Field("marathi_full_name") String strFullNameMarathi, @Field("mobile_number") String strMobileNo, @Field("email_address") String strEmailId, @Field("full_address") String strAddress, @Field("marathi_full_address") String strAddressMarathi, @Field("account_password") String strPassword);

    @POST("viewrequest")
    Call<ApplicationListResponse> getApplicationsList(@Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("viewrequestbyid")
    Call<ApplicationDetailsResponse> getApplicationsDetailsById(@Header("Authorization") String authHeader, @Field("requestid") int appId);

    @FormUrlEncoded
    @POST("forgotpassword")
    Call<ForgotPasswordResponse> sendPasswordResetLink(@Field("user_email")String email);

    @FormUrlEncoded
    @POST("updatepassword")
    Call<ChangePasswordResponse> updatePassword(@Header("Authorization") String authHeader, @Field("new_password") String strPassword);

    @Multipart
    @NonNull
    @POST("updateprofile")
    Call<UpdateProfileResponse> updateProfileWithImage(@HeaderMap Map<String, String> token, @Part MultipartBody.Part image, @Part("full_name") RequestBody fullName,@Part("marathi_full_name") RequestBody fullNameMarathi,@Part("mobile_number") RequestBody mobile,@Part("email_address") RequestBody email,@Part("full_address") RequestBody address, @Part("marathi_full_address") RequestBody addressMarathi);

    @FormUrlEncoded
    @POST("updateprofile")
    Call<UpdateProfileResponse> updateProfileWithoutImage(@Header("Authorization") String authHeader,@Field("full_name") String strFullName,@Field("marathi_full_name") String strFullNameMarathi,@Field("mobile_number") String strMobileNo,@Field("email_address") String strEmailId,@Field("full_address") String strAddress, @Field("marathi_full_address") String strAddressMarathi);

    @GET("zonelist")
    Call<ZoneListResponse> getZoneList(@Header("Authorization") String authHeader);

    @Multipart
    @NonNull
    @POST("registerrequest")
    Call<ApplyServiceResponse> applyForService(@HeaderMap Map<String, String> headers,@Part("tree_prajati") RequestBody rb_treePrajati,@Part("number_of_tree_total") RequestBody rb_treeTotal,@Part("request_reason") RequestBody rb_reason,@Part("tree_zone") RequestBody rb_wardId,@Part("type_of_request") RequestBody rb_typeRequest,@Part MultipartBody.Part doc1,@Part MultipartBody.Part doc2,@Part MultipartBody.Part doc3,@Part MultipartBody.Part doc4,@Part MultipartBody.Part doc5);

    @FormUrlEncoded
    @POST("")
    Call<ViewReceiptResponse> getReceiptDetails(@Header("Authorization") String authHeader,@Field("app_id") String appId);

    @FormUrlEncoded
    @POST("")
    Call<DownloadCertificateResponse> downloadCertificate(@Header("Authorization") String authHeader,@Field("app_id") String appId);

    @GET("")
    Call<DashboardResponse> getDashboardDetails(@Header("Authorization") String authHeader);
}
