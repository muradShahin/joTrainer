package com.murad.project1.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.murad.project1.R;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.Flags;
import com.travijuu.numberpicker.library.NumberPicker;

import org.angmarch.views.NiceSpinner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class register extends AppCompatActivity {
    NiceSpinner niceSpinner;
    NumberPicker numberPicker;
    ImageView profileImg,carImg;
    EditText firstN,lastN,password,mail,phone,age,carType,office;
    Button createButton;
    SweetAlertDialog pd;
    private Uri imgUriProfile;
    private Uri imgUriCar;
    private  StorageReference mStorgeref,mStorgeref2;
    private StorageTask storageTask;
    String downloadUriPro,downloadUriCar;
    private FirebaseAuth firebaseAuth;
    String role;
    LinearLayout linearLayoutTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //to remove the action bar from my screen
        getSupportActionBar().hide();

        //init the components
        profileImg=findViewById(R.id.profImg);
        carImg=findViewById(R.id.carImg);
        firstN=findViewById(R.id.fname);
        lastN=findViewById(R.id.lname);
        password=findViewById(R.id.password);
        mail=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        age=findViewById(R.id.age);
        carType=findViewById(R.id.carT);
        office=findViewById(R.id.office);
        createButton=findViewById(R.id.createBtn);
        linearLayoutTeacher=findViewById(R.id.instrucLay);
        mStorgeref= FirebaseStorage.getInstance().getReference("profileImages");
        mStorgeref2=FirebaseStorage.getInstance().getReference("carImages");
        firebaseAuth=FirebaseAuth.getInstance();

        //getting the role from intent
        role=getIntent().getExtras().getString("role");

        //declaring and making the spinner of cities alive
        niceSpinner = findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("Amman", "Al-Zarqa", "Irbid", "Jarash", "Al-Aqaba"));
        niceSpinner.attachDataSource(dataset);

        niceSpinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            // This example uses String, but your type can be any
            //String item = parent.getItemAtPosition(position)+"";

        });

        //the end of spinner code

        //declaring the experience numerical up down
        numberPicker =  findViewById(R.id.number_picker);
        numberPicker.setMax(15);
        numberPicker.setMin(1);
        numberPicker.setUnit(1);
        numberPicker.setValue(1);
        //the end of numerical up-down declaring

        //profileImage On click handling
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(TextUtils.isEmpty(firstN.getText().toString())) && !(TextUtils.isEmpty(lastN.getText().toString()))){

                    FileChooser(1);

                }else {

                    Toast.makeText(register.this, "fill the first name and the last name before!", Toast.LENGTH_SHORT).show();
                    firstN.setError("required before image");
                    lastN.setError("required before image");
                }
            }
        });

        //carImg on click handling
        carImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser(2);
            }
        });

        //create button handling
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkName()) {
                    if (checkEmail())
                        if(checkPhone())
                            if(checkAge())
                                if(role.equals("teacher")) {
                                    if (checkCar())
                                        if(checkOffice())
                                        createUser();
                                        else
                                            office.setError("required");
                                    else
                                        carType.setError("required");

                                }else{
                                    createUser();

                                }
                            else
                                age.setError("required:18 years or older");
                        else
                            phone.setError("invalid number");

                    else
                        mail.setError("required");
                }
                else{
                    firstN.setError("required");
                    lastN.setError("required");
                }



            }
        });

        //switching the page to student
        if(role.equals("student")){
           linearLayoutTeacher.setVisibility(View.GONE);
        }


    }

    //this method is used to create an auth to the user in the firebase
    private void createUser() {
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();
        String Email=mail.getText().toString();
        String pass=password.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(Email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(role.equalsIgnoreCase("teacher"))
                     addUser();
                else
                    addStudent();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addStudent() {


        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "addStudent.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Intent i=new Intent(register.this, LoginActivity.class);
                        Flags.Sent_from_register=true;
                        i.putExtra("email",mail.getText().toString());
                        i.putExtra("password",password.getText().toString());
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"created successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }

                    else
                    {Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();

                    param.put("email", mail.getText().toString());
                    param.put("fname", firstN.getText().toString());
                    param.put("lname", lastN.getText().toString());
                    param.put("password", password.getText().toString());
                    param.put("phone", phone.getText().toString());
                    param.put("city", niceSpinner.getSelectedItem().toString());
                    param.put("age", age.getText().toString());
                    param.put("role", role);
                    param.put("lat", "");
                    param.put("lng", "");
                    param.put("rate", "");
                    param.put("profileImg", downloadUriPro);





                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);





    }

    //this method is created to add the user info to users table in Db after he is signed up successfully
    private void addUser() {


        // if you are using php
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =   Config.url + "addUser.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Intent i=new Intent(register.this, LoginActivity.class);
                        Flags.Sent_from_register=true;
                        i.putExtra("email",mail.getText().toString());
                        i.putExtra("password",password.getText().toString());
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"created successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }

                    else
                    {Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();}
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                    errorDescription="Network Error";
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }else
                {
                    errorDescription="Connection Error";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                   param.put("email", mail.getText().toString());
                   param.put("fname", firstN.getText().toString());
                   param.put("lname", lastN.getText().toString());
                   param.put("password", password.getText().toString());
                   param.put("phone", phone.getText().toString());
                   param.put("city", niceSpinner.getSelectedItem().toString());
                   param.put("age", age.getText().toString());
                   param.put("exp", numberPicker.getValue() + "");
                   param.put("role", role);
                   param.put("carType", carType.getText().toString());
                   param.put("carImg", downloadUriCar);
                   param.put("office", office.getText().toString());
                   param.put("rate","5");
                   param.put("lat", "");
                   param.put("lng", "");
                   param.put("profileImg", downloadUriPro);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }


    //This method is responsible for getting images from phone gallary
    private void FileChooser(int requestCode){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,requestCode);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 1 && resultCode==RESULT_OK && data!=null){
            imgUriProfile = data.getData();
            profileImg.setImageURI(imgUriProfile);
            FileUpload(imgUriProfile,mStorgeref,1);
        }else if(requestCode== 2 && resultCode==RESULT_OK && data!=null){
            imgUriCar=data.getData();
            carImg.setImageURI(imgUriCar);
            FileUpload(imgUriCar,mStorgeref2,2);

        }

    }
    //this method is used to get the image extension(png ,jpg..etc)
    private String getExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    //firebase methods
    //this method is used to upload the image to firebase Storage
    private void FileUpload(Uri img,StorageReference storageReference,int code) {
        pd = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();
        final int n=(int)(1+Math.random()*1000);
        StorageReference ref=storageReference.child(firstN.getText().toString()+lastN.getText().toString()+n+"."+getExtension(img));


        storageTask= ref.putFile(img)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        getUri(n,img,code,storageReference);

                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }
     //this method is used to get the image uri after it has been uploaded to the firebase
    //so i can use that link in users table in mySql Db or firebase Db
    private void getUri(int n,Uri img,int code,StorageReference storageReference) {
        storageReference.child(firstN.getText().toString()+lastN.getText().toString()+n+"."+getExtension(img)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(code==1) {
                    downloadUriPro = uri.toString();
                }else{
                    downloadUriCar=uri.toString();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    //the following methods are used for validation
    public boolean checkName() {
       boolean flag=false;
        if (!(TextUtils.isEmpty(firstN.getText().toString())) && !(TextUtils.isEmpty(lastN.getText().toString())))
            flag=true;

        return flag;
    }
    public boolean checkEmail(){
        boolean flag=false;
        if(!(TextUtils.isEmpty(mail.getText().toString())))
            flag=true;

        return flag;
    }
    public boolean checkPhone(){
        boolean flag=false;
        if(!(TextUtils.isEmpty(phone.getText().toString()))){
                char d1=phone.getText().toString().charAt(1);
                char d2=phone.getText().toString().charAt(2);
                int n1=Integer.parseInt(d1+"");
                int n2=Integer.parseInt(d2+"");
                int sumOfDigits=n1+n2;
                if(phone.getText().toString().length()==10) {
                    if (sumOfDigits >= 14 && sumOfDigits <= 16) {
                        if (n1 == 7)
                            flag = true;
                        if (n2 >= 7 && n2 <= 9)
                            flag = true;
                    }
                }
        }
        return flag;
    }
    public boolean checkAge(){
        boolean flag=false;
        if(!(TextUtils.isEmpty(age.getText().toString()))){
            int yearOld=Integer.parseInt(age.getText().toString());
            if(yearOld>=18)
                flag=true;

        }
    return flag;
    }
    public boolean checkCar(){
        boolean flag=false;
        if(!(TextUtils.isEmpty(carType.getText().toString()))) {
            flag=true;
        }
        return flag;
        }

    public boolean checkOffice(){
        boolean flag=false;
        if(!(TextUtils.isEmpty(office.getText().toString()))) {
            flag=true;
        }
        return flag;
    }


    }



