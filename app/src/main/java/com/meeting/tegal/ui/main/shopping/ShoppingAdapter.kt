package com.meeting.tegal.ui.main.shopping

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import com.meeting.tegal.models.Order
import com.meeting.tegal.utilities.gone
import com.meeting.tegal.utilities.visible
import kotlinx.android.synthetic.main.item_shooping.view.*

class ShoppingAdapter (private var orders : MutableList<Order>,
                       private var context: Context,
                       private var shoppingViewModel: ShoppingViewModel)
    : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shooping, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context, shoppingViewModel)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val paymentMidtrans = PaymentMidtrans()
        fun bind(order: Order, context: Context, shoppingViewModel: ShoppingViewModel){
            with(itemView){
                iv_ruangan.load(order.room!!.foto)
                tv_nama_ruangan.text = order.room!!.nama_tempat
                tv_keterangan.text = order.room!!.keterangan
                tv_alamat.text = order.partner!!.alamat
                tv_total_harga.text = Constants.setToIDR(order.totalPrice!!)

                if (order.verifikasi.equals("2")){
                    if (order.status.equals("pending")){
                        val url_snap = "https://app.sandbox.midtrans.com/snap/v2/vtweb/${order.snap}"
                        setOnClickListener {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url_snap)))
                        }
                        btn_pay.gone()
                    }else if(order.status.equals("none")){
                        paymentMidtrans.initPayment(context, order.id.toString(), shoppingViewModel)
                        btn_pay.visible()
                        btn_pay.setOnClickListener {
                            paymentMidtrans.showPayment(context, order.id.toString(), order.totalPrice!!, 1, order.partner!!.nama_mitra!!)
                        }
                    }
                }else{
                    btn_pay.gone()
                }
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}