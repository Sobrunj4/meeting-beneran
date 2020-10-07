package com.meeting.tegal.ui.main.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.meeting.tegal.Partner
import com.meeting.tegal.R
import com.meeting.tegal.ui.company.CompanyActivity
import com.meeting.tegal.utilities.toast
import kotlinx.android.synthetic.main.item_company.view.*

class HomeAdapter (private var partners : MutableList<Partner>, private var context: Context)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_company, parent, false))
    }

    override fun getItemCount(): Int = partners.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(partners[position], context)

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(partner: Partner, context: Context){
            with(itemView){
                txt_company_name.text = partner.nama_mitra
                txt_company_address.text = partner.alamat
                setOnClickListener {
                    context.startActivity(Intent(context, CompanyActivity::class.java).apply {
                        putExtra("COMPANY", partner)
                    })
                }
            }
        }
    }

    fun changeList(c : List<Partner>){
        partners.clear()
        partners.addAll(c)
        notifyDataSetChanged()
    }
}