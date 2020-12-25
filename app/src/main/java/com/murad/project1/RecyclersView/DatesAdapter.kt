package com.murad.project1.RecyclersView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.murad.project1.R
import com.murad.project1.activites.ui.dashboard_teacher.DashboardFragment_teacher

class DatesAdapter(val fragment :DashboardFragment_teacher ,val items:ArrayList<String>) :RecyclerView.Adapter<DatesAdapter.DatesViewHolder>(){




    class DatesViewHolder(layout:ConstraintLayout):RecyclerView.ViewHolder(layout){

        val dateText=layout.findViewById<TextView>(R.id.date)
        val dateCons=layout.findViewById<ConstraintLayout>(R.id.date_cons);


    }


    override fun getItemCount(): Int {
        return  items.size
    }

    override fun onBindViewHolder(holder: DatesViewHolder, position: Int) {

          holder.dateText.text=items[position]




        holder.dateText.setOnClickListener{

            fragment.getLessonsOnFilterDate(holder.dateText.text.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesViewHolder {
        val view=LayoutInflater.from(parent.context)
                .inflate(R.layout.dates_row,parent,false) as ConstraintLayout;

        return DatesViewHolder(view);
    }
}