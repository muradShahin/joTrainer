package com.murad.project1.RecyclersView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.murad.project1.R
import com.murad.project1.UsersClasses.TeacherStudents
import com.murad.project1.activites.ui.dashboard_teacher.DashboardFragment_teacher

class StudentsFilterAdapter(val fragmentTeacher: DashboardFragment_teacher,val items:ArrayList<TeacherStudents>)
    : RecyclerView.Adapter<StudentsFilterAdapter.StudentsViewHolder>(){


    class StudentsViewHolder(layout:ConstraintLayout) : RecyclerView.ViewHolder(layout){

        val studentImage=layout.findViewById<ImageView>(R.id.studentImg)
        val studentName=layout.findViewById<TextView>(R.id.name)
        val studentCard=layout.findViewById<ConstraintLayout>(R.id.students_card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val view=LayoutInflater.from(parent.context)
                .inflate(R.layout.students_row,parent,false) as ConstraintLayout

        return StudentsViewHolder(view)

    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        if(items[position].profileImg !=null){
            Glide.with(fragmentTeacher)
                    .load(items[position].profileImg)
                    .into(holder.studentImage)
        }

        if(items[position].fname !=null && items[position].lname !=null){
            holder.studentName.text=items[position].fname + " "+ items[position].lname
        }

        holder.studentCard.setOnClickListener {

            fragmentTeacher.getLessonsFilterByName(items[position].email)
        }
    }

    override fun getItemCount(): Int {

        return items.size
    }
}