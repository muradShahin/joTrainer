package com.murad.project1.activites.student_activites;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerStudentMessages;
import com.murad.project1.contractClasses.Messages;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentLessInfo;
import com.murad.project1.supportClasses.Currrent_Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class studentMessages extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Messages> messagesArrayList=new ArrayList<>();


    public studentMessages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_student_messages, container, false);
        recyclerView=v.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
       getAllStudentMessages();


        return v;
    }
    private void getAllStudentMessages(){
        SweetAlertDialog pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getStudentsMessages.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

               // Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();

                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        int id=x.getInt("id");
                        String studentEmail=x.getString("student_email");
                        String teacher_email=x.getString("teacher_email");
                        String date=x.getString("date");
                        String post=x.getString("post");
                        String studentId=x.getString("studentId");
                        String teacherId=x.getString("teacherId");
                        String lessonsId=x.getString("lessonsId");
                        String status=x.getString("status");
                        String name=x.getString("name");
                        String img=x.getString("img");

                        Messages am=new Messages();
                        am.setStudentEmail(studentEmail);
                        am.setTeacherEmail(teacher_email);
                        am.setDate(date);
                        am.setId(id);
                        am.setLessId(lessonsId);
                        am.setStudentId(studentId);
                        am.setTeacherId(teacherId);
                        am.setPost(post);
                        am.setStatus(status);
                        am.setName(name);
                        am.setImage(img);

                        messagesArrayList.add(am);


                    }
                   RecyclerStudentMessages recyclerStudentMessages=new RecyclerStudentMessages(getActivity(),messagesArrayList);
                    recyclerView.setAdapter(recyclerStudentMessages);




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
                Toast.makeText(getActivity(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("email", Currrent_Student.email);



                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);






    }


}
