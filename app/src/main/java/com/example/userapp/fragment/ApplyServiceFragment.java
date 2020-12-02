package com.example.userapp.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.activity.ServiceActivity;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.ApplyServiceResponse;
import com.example.userapp.model.ZoneListResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyServiceFragment extends Fragment implements View.OnClickListener, PaymentResultListener {

    private EditText editTextApplicatntName, editTextApplicatntNameMarathi, editTextMobileNumber, editTextEmailId, editTextAddress, editTextAddressMarathi;
    private EditText editTextTreeType, editTextTotal, editTextReson, edittextGardenDepartmentService;
    private Spinner spinnerWard;
    private EditText editTextDocument1, editTextDocument2, editTextDocument3, editTextDocument4, editTextDocument5;
    private Button buttonDocument1, buttonDocument2, buttonDocument3, buttonDocument4, buttonDocument5, button_apply_service;

    private static final int DOCUMENT_REQUEST_1 = 1;
    private static final int CAPTURE_DOCUMENT_REQUEST_1 = 101;
    private static final int PIC_DOCUMENT_REQUEST_1 = 102;
    private static final int DOCUMENT_REQUEST_2 = 2;
    private static final int CAPTURE_DOCUMENT_REQUEST_2 = 201;
    private static final int PIC_DOCUMENT_REQUEST_2 = 202;
    private static final int DOCUMENT_REQUEST_3 = 3;
    private static final int CAPTURE_DOCUMENT_REQUEST_3 = 301;
    private static final int PIC_DOCUMENT_REQUEST_3 = 302;
    private static final int DOCUMENT_REQUEST_4 = 4;
    private static final int CAPTURE_DOCUMENT_REQUEST_4 = 401;
    private static final int PIC_DOCUMENT_REQUEST_4 = 402;
    private static final int DOCUMENT_REQUEST_5 = 5;
    private static final int CAPTURE_DOCUMENT_REQUEST_5 = 501;
    private static final int PIC_DOCUMENT_REQUEST_5 = 502;

    public static String TAG = "ApplyServiceFragment";
    private Context mContext;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private String documentPath1 = null;
    private String documentPath2 = null;
    private String documentPath3 = null;
    private String documentPath4 = null;
    private String documentPath5 = null;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    private String token;
    private ZoneListResponse zoneListResponse;
    private ArrayList<ZoneListResponse.Zonelist> zonelists = new ArrayList<>();
    private List<String> zones = new ArrayList<>();
    private String typeRequest;
    private String wardId;
    private ApplyServiceResponse applyServiceResponse;
    private ServiceActivity serviceActivity;

    public ApplyServiceFragment(String typeRequest, ServiceActivity serviceActivity) {
        this.typeRequest = typeRequest;
        this.serviceActivity = serviceActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply_service, container, false);
        mContext = getActivity();
        progressDialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editTextApplicatntName = (EditText) view.findViewById(R.id.edittext_name_of_applicant_service);
        editTextApplicatntNameMarathi = (EditText) view.findViewById(R.id.edittext_name_of_applicant_marathi_service);
        editTextMobileNumber = (EditText) view.findViewById(R.id.edittext_mobile_number_service);
        editTextEmailId = (EditText) view.findViewById(R.id.edittext_email_id_service);
        editTextAddress = (EditText) view.findViewById(R.id.edittext_address_service);
        editTextAddressMarathi = (EditText) view.findViewById(R.id.edittext_address_marathi_service);
        editTextTreeType = (EditText) view.findViewById(R.id.edittext_tree_type_service);
        editTextTotal = (EditText) view.findViewById(R.id.edittext_total_service);
        editTextReson = (EditText) view.findViewById(R.id.edittext_reason_service);

        editTextDocument1 = (EditText) view.findViewById(R.id.edittext_document1_service);
        editTextDocument2 = (EditText) view.findViewById(R.id.edittext_document2_service);
        editTextDocument3 = (EditText) view.findViewById(R.id.edittext_document3_service);
        editTextDocument4 = (EditText) view.findViewById(R.id.edittext_document4_service);
        editTextDocument5 = (EditText) view.findViewById(R.id.edittext_document5_service);

        spinnerWard = (Spinner) view.findViewById(R.id.spinner_select_ward_service);
        edittextGardenDepartmentService = (EditText) view.findViewById(R.id.edittext_garden_department_service);

        buttonDocument1 = (Button) view.findViewById(R.id.button_browse_document1_service);
        buttonDocument2 = (Button) view.findViewById(R.id.button_browse_document2_service);
        buttonDocument3 = (Button) view.findViewById(R.id.button_browse_document3_service);
        buttonDocument4 = (Button) view.findViewById(R.id.button_browse_document4_service);
        buttonDocument5 = (Button) view.findViewById(R.id.button_browse_document5_service);
        button_apply_service = (Button) view.findViewById(R.id.button_apply_service);

        buttonDocument1.setOnClickListener(this);
        buttonDocument2.setOnClickListener(this);
        buttonDocument3.setOnClickListener(this);
        buttonDocument4.setOnClickListener(this);
        buttonDocument5.setOnClickListener(this);
        button_apply_service.setOnClickListener(this);

        editTextApplicatntName.setText(sharedpreferences.getString(ConstantHandler.USER_NAME, ""));
        editTextApplicatntNameMarathi.setText(sharedpreferences.getString(ConstantHandler.USER_NAME_MARATHI, ""));
        editTextMobileNumber.setText(sharedpreferences.getString(ConstantHandler.USER_MOBILE, ""));
        editTextEmailId.setText(sharedpreferences.getString(ConstantHandler.USER_EMAIL, ""));
        editTextAddress.setText(sharedpreferences.getString(ConstantHandler.USER_ADDRESS, ""));
        editTextAddressMarathi.setText(sharedpreferences.getString(ConstantHandler.USER_ADDRESS_MARATHI, ""));
        token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");
        getZoneList(token);

        return view;
    }

    private void getZoneList(String token) {

        if (isOnline()) {
            progressDialog.setTitle("Fetching Details.. Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ZoneListResponse> zoneListResponseCall = apiInterface.getZoneList("Bearer " + token);
            zoneListResponseCall.enqueue(new Callback<ZoneListResponse>() {
                @Override
                public void onResponse(Call<ZoneListResponse> call, Response<ZoneListResponse> response) {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            zoneListResponse = response.body();
                            zonelists = new ArrayList<>();
                            for (int i = 0; i < zoneListResponse.getZonelist().size(); i++) {
                                zonelists.add(zoneListResponse.getZonelist().get(i));
                            }
                            zones.add(0, "--Select Ward--");
                            for (int i = 0; i < zonelists.size(); i++) {
                                zones.add(zonelists.get(i).getId() + "   " + zonelists.get(i).getName());
                            }
                            ArrayAdapter<String> adapter =
                                    new ArrayAdapter<String>(mContext, R.layout.spinner_item_layout, zones);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerWard.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ZoneListResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_browse_document1_service:
                selectImage(DOCUMENT_REQUEST_1);
                break;
            case R.id.button_browse_document2_service:
                selectImage(DOCUMENT_REQUEST_2);
                break;
            case R.id.button_browse_document3_service:
                selectImage(DOCUMENT_REQUEST_3);
                break;
            case R.id.button_browse_document4_service:
                selectImage(DOCUMENT_REQUEST_4);
                break;
            case R.id.button_browse_document5_service:
                selectImage(DOCUMENT_REQUEST_5);
                break;
            case R.id.button_apply_service:
                validateAndApply();
                break;
        }
    }

    private void validateAndApply() {

        String treePrajati = editTextTreeType.getText().toString();
        String treeTotal = editTextTotal.getText().toString();
        String reason = editTextTreeType.getText().toString();
        String ward = spinnerWard.getSelectedItem().toString();

        if (ward.equalsIgnoreCase("--Select Ward--")) {
            Toast.makeText(mContext, "Select the Ward", Toast.LENGTH_SHORT).show();
            return;
        } else {
            wardId = ward.substring(0, 1);
            Log.d(TAG, "validateAndApply: " + ward);
        }

        if (treePrajati.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Enter Tree type", Toast.LENGTH_SHORT).show();
            return;
        } else if (treeTotal.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Enter Total number of trees", Toast.LENGTH_SHORT).show();
            return;
        } else if (reason.equalsIgnoreCase("")) {
            Toast.makeText(mContext, "Enter reason", Toast.LENGTH_SHORT).show();
            return;
        } else if (documentPath1 == null) {
            Toast.makeText(mContext, "Please Select Document 1", Toast.LENGTH_SHORT).show();
            return;
        } else if (documentPath2 == null) {
            Toast.makeText(mContext, "Please Select Document 1", Toast.LENGTH_SHORT).show();
            return;
        } else if (documentPath3 == null) {
            Toast.makeText(mContext, "Please Select Document 1", Toast.LENGTH_SHORT).show();
            return;
        } else if (documentPath4 == null) {
            documentPath4 = "";
        } else if (documentPath5 == null) {
            documentPath5 = "";
        } else {
            applyForServiceWS(treePrajati, treeTotal, reason, wardId, typeRequest, documentPath1, documentPath2, documentPath3, documentPath4, documentPath5);
        }
    }

    private void applyForServiceWS(String treePrajati, String treeTotal, String reason, String wardId, String typeRequest, String documentPath1, String documentPath2, String documentPath3, String documentPath4, String documentPath5) {

        if (isOnline()) {
            File file1 = new File(documentPath1);
            File file11 = reduceFileSizeToUpload(file1);
            File file2 = new File(documentPath2);
            File file22 = reduceFileSizeToUpload(file2);
            File file3 = new File(documentPath3);
            File file33 = reduceFileSizeToUpload(file3);
            File file4 = new File(documentPath4);
            File file44 = reduceFileSizeToUpload(file4);
            File file5 = new File(documentPath5);
            File file55 = reduceFileSizeToUpload(file5);
            RequestBody requestFile4;
            MultipartBody.Part doc4;
            RequestBody requestFile5;
            MultipartBody.Part doc5;

            final RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file11);
            MultipartBody.Part doc1 = MultipartBody.Part.createFormData("permission_claim_letter_712", file11.getName(), requestFile1);

            final RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file22);
            MultipartBody.Part doc2 = MultipartBody.Part.createFormData("permission_sadyasthititila_trees_photo", file22.getName(), requestFile2);

            final RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file33);
            MultipartBody.Part doc3 = MultipartBody.Part.createFormData("permission_joint_ownership_consents", file33.getName(), requestFile3);

            if (file4.exists()) {
                requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), file44);
                doc4 = MultipartBody.Part.createFormData("permission_tree_location", file44.getName(), requestFile4);
            } else {
                requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                doc4 = MultipartBody.Part.createFormData("permission_tree_location", "", requestFile4);
            }
            if (file5.exists()) {
                requestFile5 = RequestBody.create(MediaType.parse("multipart/form-data"), file55);
                doc5 = MultipartBody.Part.createFormData("permission_construction_permission_letter", file55.getName(), requestFile5);
            } else {
                requestFile5 = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                doc5 = MultipartBody.Part.createFormData("permission_construction_permission_letter", "", requestFile5);
            }

            RequestBody rb_treePrajati = RequestBody.create(MediaType.parse("text/plain"), treePrajati);
            RequestBody rb_treeTotal = RequestBody.create(MediaType.parse("text/plain"), treeTotal);
            RequestBody rb_reason = RequestBody.create(MediaType.parse("text/plain"), reason);
            RequestBody rb_wardId = RequestBody.create(MediaType.parse("text/plain"), wardId);
            RequestBody rb_typeRequest = RequestBody.create(MediaType.parse("text/plain"), typeRequest);

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);


            progressDialog.setTitle("Applying..Please Wait");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ApplyServiceResponse> applyServiceResponseCall = apiInterface.applyForService(headers, rb_treePrajati, rb_treeTotal, rb_reason, rb_wardId, rb_typeRequest, doc1, doc2, doc3, doc4, doc5);
            applyServiceResponseCall.enqueue(new Callback<ApplyServiceResponse>() {
                @Override
                public void onResponse(Call<ApplyServiceResponse> call, Response<ApplyServiceResponse> response) {
                    Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            applyServiceResponse = response.body();
                            if(!applyServiceResponse.getOrderid().equalsIgnoreCase("")){
                                startPayment(applyServiceResponse.getOrderid().toString());
                            }

                        }
                    }

                }

                @Override
                public void onFailure(Call<ApplyServiceResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    private void selectImage(final int requestId) {

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        } else {
            final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (requestId == 1) {
                            startActivityForResult(intent, CAPTURE_DOCUMENT_REQUEST_1);
                        } else if (requestId == 2) {
                            startActivityForResult(intent, CAPTURE_DOCUMENT_REQUEST_2);
                        } else if (requestId == 3) {
                            startActivityForResult(intent, CAPTURE_DOCUMENT_REQUEST_3);
                        } else if (requestId == 4) {
                            startActivityForResult(intent, CAPTURE_DOCUMENT_REQUEST_4);
                        } else if (requestId == 5) {
                            startActivityForResult(intent, CAPTURE_DOCUMENT_REQUEST_5);
                        }

                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if (requestId == 1) {
                            startActivityForResult(pickPhoto, PIC_DOCUMENT_REQUEST_1);
                        } else if (requestId == 2) {
                            startActivityForResult(pickPhoto, PIC_DOCUMENT_REQUEST_2);
                        } else if (requestId == 3) {
                            startActivityForResult(pickPhoto, PIC_DOCUMENT_REQUEST_3);
                        } else if (requestId == 4) {
                            startActivityForResult(pickPhoto, PIC_DOCUMENT_REQUEST_4);
                        } else if (requestId == 5) {
                            startActivityForResult(pickPhoto, PIC_DOCUMENT_REQUEST_5);
                        }
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
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case CAPTURE_DOCUMENT_REQUEST_1:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath1 = getCameraCapturedImagePath(data);
                        editTextDocument1.setText(documentPath1);
                    }
                }
                break;
            case CAPTURE_DOCUMENT_REQUEST_2:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath2 = getCameraCapturedImagePath(data);
                        editTextDocument2.setText(documentPath2);
                    }
                }
                break;
            case CAPTURE_DOCUMENT_REQUEST_3:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath3 = getCameraCapturedImagePath(data);
                        editTextDocument3.setText(documentPath3);
                    }
                }
                break;
            case CAPTURE_DOCUMENT_REQUEST_4:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath4 = getCameraCapturedImagePath(data);
                        editTextDocument4.setText(documentPath4);
                    }
                }
                break;
            case CAPTURE_DOCUMENT_REQUEST_5:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath5 = getCameraCapturedImagePath(data);
                        editTextDocument5.setText(documentPath5);
                    }
                }
                break;
            case PIC_DOCUMENT_REQUEST_1:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath1 = getPickedDocumentPath(data);
                        editTextDocument1.setText(documentPath1);
                    }
                }
                break;
            case PIC_DOCUMENT_REQUEST_2:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath2 = getPickedDocumentPath(data);
                        editTextDocument2.setText(documentPath2);
                    }
                }
                break;
            case PIC_DOCUMENT_REQUEST_3:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath3 = getPickedDocumentPath(data);
                        editTextDocument3.setText(documentPath3);
                    }
                }
                break;
            case PIC_DOCUMENT_REQUEST_4:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath4 = getPickedDocumentPath(data);
                        editTextDocument4.setText(documentPath4);
                    }
                }
                break;
            case PIC_DOCUMENT_REQUEST_5:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        documentPath5 = getPickedDocumentPath(data);
                        editTextDocument5.setText(documentPath5);
                    }
                }
                break;
        }
    }

    private String getPickedDocumentPath(Intent data) {
        String path = null;

        Uri selectedImage = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            Log.e("Activity", "Pick from Gallery::>>> ");

            path = getRealPathFromURI(selectedImage);
            Log.d(TAG, "onActivityResult: Gallery : " + path);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return path;
    }

    private String getCameraCapturedImagePath(Intent data) {
        String path = null;

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        path = saveImage(thumbnail);
        destination = new File(path);
        return path;
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

    private boolean hasPermissions(ApplyServiceFragment applyServiceFragment, String[] permissions) {
        if (mContext != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    public File reduceFileSizeToUpload(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    private void startPayment(String orderId) {


        Checkout checkout = new Checkout();

        final Activity activity = serviceActivity;

        try {
            JSONObject options = new JSONObject();

            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", orderId);//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(mContext, "Error : "+s, Toast.LENGTH_SHORT).show();
    }
}
