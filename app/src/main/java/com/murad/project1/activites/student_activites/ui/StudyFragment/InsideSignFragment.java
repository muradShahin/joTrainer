package com.murad.project1.activites.student_activites.ui.StudyFragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.murad.project1.QuestionClasses.Question;
import com.murad.project1.R;
import com.murad.project1.RecyclersView.RecyclerAllSigns;
import com.murad.project1.RecyclersView.RecyclerQuestions;
import com.murad.project1.activites.student_activites.studentGate;
import com.murad.project1.supportClasses.Config;
import com.murad.project1.supportClasses.CurrentCatalog;
import com.murad.project1.supportClasses.Signs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsideSignFragment extends Fragment {
      private TextView catalogName;
      private RecyclerView recyclerView;
    SweetAlertDialog pd;
    private ArrayList<Signs> signsArrayList=new ArrayList<>();
      private String filter;

    public InsideSignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inside_sign, container, false);
        catalogName=view.findViewById(R.id.catalogN);
        catalogName.setText(CurrentCatalog.title);
        recyclerView=view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        switch (CurrentCatalog.title){
            case "Warning signs":
                filter="war";
                break;

            case "Priority signs":
                filter="pri";
                break;

            case "Indicative signs":
                filter="ind";
                break;

            case "Compulsory signs":
                filter="comp";
                break;

            case "Prevention signs":
                filter="pro";
                break;
        }


        getAllSigns();


        return view;
    }

    private void getAllSigns() {
       pd = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pd.getProgressHelper().setBarColor(Color.parseColor("#f47b00"));
        pd.setTitleText("Loading");
        pd.setCancelable(false);
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url =   Config.url + "getAllSigns.php";





        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                //    Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();




                try {
                    JSONObject o=new JSONObject(s);
                    JSONArray arr=o.getJSONArray("result");

                    for(int i=0;i<arr.length();i++) {
                        JSONObject x = arr.getJSONObject(i);
                        String desc=x.getString("description");
                        String image=x.getString("image");
                        String title=x.getString("title");

                        if(filter.equalsIgnoreCase(title)){
                            Signs si=new Signs();
                            si.setImage(image);
                            si.setDesc(desc);
                            signsArrayList.add(si);


                        }



                    }

                    RecyclerAllSigns recyclerAllSigns=new RecyclerAllSigns(getActivity(),signsArrayList);
                    recyclerView.setAdapter(recyclerAllSigns);






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
