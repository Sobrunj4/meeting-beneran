package com.meeting.tegal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meeting.models.Makanan
import com.example.meeting.utilities.Constants
import com.meeting.tegal.MyClickListener
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.item_pesan_makanan.view.*

class PesanMakananAdapter (private var makanans : MutableList<Makanan>, private var myClickListener: MyClickListener)
    : RecyclerView.Adapter<PesanMakananAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pesan_makanan, parent, false))
    }

    override fun getItemCount(): Int = makanans.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(makanans[position], myClickListener)


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private var isAdd : Boolean = false
        private var count = 1
        private var totalHarga = 0
        fun bind(makanan: Makanan, myClickListener: MyClickListener){
            with(itemView) {
                txt_nama_makanan.text = makanan.nama
                txt_harga_makanan.text = Constants.setToIDR(makanan.harga!!)
                checkbox_pesan_makanan.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked){
                        checkbox_pesan_makanan.isChecked = true
                        txt_count.text = "1"
                        btn_min.isEnabled = true
                        btn_plus.isEnabled = true
                        btn_plus.setOnClickListener {
                            if ( count > 49){
                                count = 50
                                txt_count.text = count.toString()
                            }else{
                                count++
                                txt_count.text = count.toString()
                            }

                            totalHarga = makanan.harga!!.times(count)
                            txt_harga_makanan.text = Constants.setToIDR(totalHarga)
                            myClickListener.clickPesanMakanan(makanan, count)
                        }
                        btn_min.setOnClickListener {
                            if (count < 2){
                                count = 1
                                txt_count.text = count.toString()
                            }else{
                                count--
                                txt_count.text = count.toString()
                            }
                            totalHarga = makanan.harga!!.times(count)
                            txt_harga_makanan.text = Constants.setToIDR(totalHarga)
                            myClickListener.clickPesanMakanan(makanan, count)
                        }
                    }else{
                        checkbox_pesan_makanan.isChecked = false
                        btn_min.isEnabled = false
                        btn_plus.isEnabled = false
                        txt_harga_makanan.text = Constants.setToIDR(makanan.harga!!)
                        count = 1
                        txt_count.text = "1"
                        myClickListener.clickPesanMakanan(null, count)
                    }
                }
            }
        }
    }

    fun changelist(c: List<Makanan>){
        makanans.clear()
        makanans.addAll(c)
        notifyDataSetChanged()
    }

}