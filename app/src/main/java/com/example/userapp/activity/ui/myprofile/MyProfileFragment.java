package com.example.userapp.activity.ui.myprofile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.userapp.R;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.UpdateProfileResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private EditText editTextFullName_myProfile,editTextFullNameMarathi_myProfile,editTextMobile_myProfile,editTextEmail_myProfile,editTextAddress_myProfile,editTextAddressMarathi_myProfile;
    private EditText editTextUsername_myProfile;
    private Button btnChangeProfile;
    private Context mContext;
    private ImageView buttonChangePhoto;
    private CircleImageView imageview_profilePic;

    private String strFullName, strFullNameMarathi, strMobileNo, strEmailId, strAddress, strAddressMarathi;
    private String strUsername, strPassword, strConfirmPassword;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private String token;
    public static String TAG = "MyProfileFragment";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    private UpdateProfileResponse updateProfileResponse;
    private String profilePic;
    private String strProfilePic,strUserName, strUserNameMarathi,strUserEmail,strUserMobile,strUserUsername, strUserAddress,strUserAddressMarathi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        mContext = getActivity();
        progressDialog = new ProgressDialog(mContext);
        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editTextFullName_myProfile = (EditText)root.findViewById(R.id.editTextFullName_myProfile);
        editTextFullNameMarathi_myProfile = (EditText)root.findViewById(R.id.editTextFullNameMarathi_myProfile);
        editTextMobile_myProfile = (EditText)root.findViewById(R.id.editTextMobile_myProfile);
        editTextEmail_myProfile = (EditText)root.findViewById(R.id.editTextEmail_myProfile);
        editTextAddress_myProfile = (EditText)root.findViewById(R.id.editTextAddress_myProfile);
        editTextAddressMarathi_myProfile = (EditText)root.findViewById(R.id.editTextAddressMarathi_myProfile);
        editTextUsername_myProfile = (EditText)root.findViewById(R.id.editTextUsername_myProfile);


        btnChangeProfile = (Button)root.findViewById(R.id.btnChangeProfile);
        buttonChangePhoto = (ImageView)root.findViewById(R.id.buttonChangePhoto);
        imageview_profilePic = (CircleImageView)root.findViewById(R.id.imageview_profilePic);

        Log.d(TAG, "onCreateView: Mobile "+sharedpreferences.getString(ConstantHandler.USER_MOBILE,""));
        token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");
        profilePic = sharedpreferences.getString(ConstantHandler.USER_PROFILE_PIC,"");
        if(!profilePic.equalsIgnoreCase("")){
            Picasso.get().load(profilePic).into(imageview_profilePic);
        }
        if(sharedpreferences.contains(ConstantHandler.USER_ID)){

            editTextFullName_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_NAME,""));
            editTextFullNameMarathi_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_NAME_MARATHI,""));
            editTextMobile_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_MOBILE,""));
            editTextEmail_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_EMAIL,""));
            editTextAddress_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_ADDRESS,""));
            editTextAddressMarathi_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_ADDRESS_MARATHI,""));
           // editTextUsername_myProfile.setText(sharedpreferences.getString(ConstantHandler.USER_UNAME,""));
        }


        btnChangeProfile.setOnClickListener(this);
        buttonChangePhoto.setOnClickListener(this);

        return root;
    }

    private void validateFields() {

        strFullName = editTextFullName_myProfile.getText().toString();
        strFullNameMarathi = editTextFullNameMarathi_myProfile.getText().toString();
        strMobileNo = editTextMobile_myProfile.getText().toString();
        strEmailId = editTextEmail_myProfile.getText().toString();
        strAddress = editTextAddress_myProfile.getText().toString();
        strAddressMarathi = editTextAddressMarathi_myProfile.getText().toString();
        //strUsername = editTextUsername_myProfile.getText().toString();

        if(strFullName.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter fullname", Toast.LENGTH_SHORT).show();
            return;
        }else if(strMobileNo.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter mobile number", Toast.LENGTH_SHORT).show();
            return;
        }else if(strEmailId.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter emailID", Toast.LENGTH_SHORT).show();
            return;
        }else if(!strEmailId.matches(emailPattern)){
            Toast.makeText(mContext, "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
        } else if(strAddress.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter Address", Toast.LENGTH_SHORT).show();
            return;
        } else {
            updateProfile(strFullName,strFullNameMarathi,strMobileNo,strEmailId,strAddress,strAddressMarathi,imgPath,token);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChangeProfile:
                validateFields();
                //Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonChangePhoto:
                selectImage();
                break;
        }
    }

    private void selectImage() {

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }else {
            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageview_profilePic.setImageBitmap(thumbnail);
            imgPath = saveImage(thumbnail);
            destination = new File(imgPath);
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                Log.d(TAG, "onActivityResult: Gallery File"+destination);
                Log.d(TAG, "onActivityResult: Gallery String ImagePath : "+imgPath);
                destination = new File(imgPath.toString());
                imageview_profilePic.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File directory
                = new File(Environment.getExternalStorageDirectory()
                + "/" + getResources().getString(R.string.app_name));
        // have the object build the directory structure, if needed.
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            File f = new File(directory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean hasPermissions(MyProfileFragment myProfileFragment, String[] permissions) {
        if (mContext != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateProfile(String strFullName, String strFullNameMarathi, String strMobileNo, String strEmailId, String strAddress, String strAddressMarathi,String imgPath, String token) {

        if(isOnline()){
            if(imgPath != null){
                updateProfileWithImage(imgPath);
            }else {
                progressDialog.setTitle("Updating Profile Please Wait..!!");
                progressDialog.show();

                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<UpdateProfileResponse> updateProfileResponseCall = apiInterface.updateProfileWithoutImage("Bearer "+token,strFullName,strFullNameMarathi,strMobileNo,strEmailId,strAddress,strAddressMarathi);
                updateProfileResponseCall.enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        if(response.isSuccessful()){
                            if(response.body().getStatus().equalsIgnoreCase("success")){
                                updateProfileResponse = response.body();
                                Toast.makeText(mContext, "success :"+updateProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();

                                strUserName = updateProfileResponse.getUpdatedData().getName().toString();
                                strUserEmail = updateProfileResponse.getUpdatedData().getEmail().toString();
                                strUserMobile = updateProfileResponse.getUpdatedData().getMobileNumber().toString();
                                strUserUsername = updateProfileResponse.getUpdatedData().getUsername().toString();
                                strUserNameMarathi = updateProfileResponse.getUpdatedData().getMarathiName().toString();
                                strUserAddress = updateProfileResponse.getUpdatedData().getAddress().toString();
                                strUserAddressMarathi = updateProfileResponse.getUpdatedData().getMarathiAddress().toString();
                                strProfilePic = updateProfileResponse.getUpdatedData().getProfilePic().toString();

                                updateLocalDatabase(strUserName,strUserEmail,strUserMobile,strUserUsername,strUserNameMarathi,strUserAddress,strUserAddressMarathi,strProfilePic);
                            }else {
                                Toast.makeText(mContext, updateProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "failure :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void updateLocalDatabase(String strUserName, String strUserEmail, String strUserMobile, String strUserUsername, String strUserNameMarathi, String strUserAddress, String strUserAddressMarathi, String strProfilePic) {
        //update profile in Local Database
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(ConstantHandler.USER_NAME,strUserName);
        editor.putString(ConstantHandler.USER_UNAME,strUserUsername);
        editor.putString(ConstantHandler.USER_MOBILE,strUserMobile);
        editor.putString(ConstantHandler.USER_EMAIL,strUserEmail);
        editor.putString(ConstantHandler.USER_NAME_MARATHI,strUserNameMarathi);
        editor.putString(ConstantHandler.USER_ADDRESS_MARATHI,strUserAddressMarathi);
        editor.putString(ConstantHandler.USER_ADDRESS,strUserAddress);
        editor.putString(ConstantHandler.USER_PROFILE_PIC,strProfilePic);
        editor.apply();
    }


    private void updateProfileWithImage(String imgPath) {

        File file = new File(imgPath);
        Log.d(TAG, "updateProfile: File "+file.length());
        // create RequestBody instance from file
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("user_profile_photo", file.getName(), requestFile);

        RequestBody fullName = RequestBody.create(MediaType.parse("text/plain"), strFullName);
        RequestBody fullNameMarathi = RequestBody.create(MediaType.parse("text/plain"), strFullNameMarathi);
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"),strMobileNo);
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), strEmailId);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), strAddress);
        RequestBody addressMarathi = RequestBody.create(MediaType.parse("text/plain"), strAddressMarathi);
        //RequestBody username = RequestBody.create(MediaType.parse("text/plain"), strUsername);


        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer "+token);

        progressDialog.setTitle("Updating Profile Please Wait..!!");
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateProfileResponse> updateProfileResponseCall = apiInterface.updateProfileWithImage(headers,image,fullName,fullNameMarathi,mobile,email,address,addressMarathi);
        updateProfileResponseCall.enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getStatus().equalsIgnoreCase("success")){
                        updateProfileResponse = response.body();
                        Toast.makeText(mContext, "success :"+updateProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        strUserName = updateProfileResponse.getUpdatedData().getName().toString();
                        strUserEmail = updateProfileResponse.getUpdatedData().getEmail().toString();
                        strUserMobile = updateProfileResponse.getUpdatedData().getMobileNumber().toString();
                        strUserUsername = updateProfileResponse.getUpdatedData().getUsername().toString();
                        strUserNameMarathi = updateProfileResponse.getUpdatedData().getMarathiName().toString();
                        strUserAddress = updateProfileResponse.getUpdatedData().getAddress().toString();
                        strUserAddressMarathi = updateProfileResponse.getUpdatedData().getMarathiAddress().toString();
                        strProfilePic = updateProfileResponse.getUpdatedData().getProfilePic().toString();

                        updateLocalDatabase(strUserName,strUserEmail,strUserMobile,strUserUsername,strUserNameMarathi,strUserAddress,strUserAddressMarathi,strProfilePic);
                    }else {
                        Toast.makeText(mContext, updateProfileResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext, "failure :"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline() {

        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(mContext, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}