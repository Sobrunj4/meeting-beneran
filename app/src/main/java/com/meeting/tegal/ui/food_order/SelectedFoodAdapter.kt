package com.meeting.tegal.ui.food_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.meeting.models.Food
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.item_selected_food.view.*

class SelectedFoodAdapter (private val foods : MutableList<Food>, private val foodInterface: FoodClickInterface) : RecyclerView.Adapter<SelectedFoodAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(f: Food){
            with(itemView){
                food_name_textView.text = f.name
                food_price_textView.text = Constants.setToIDR((f.price ?: 0) * f.qty!!)
                food_imageView.load(f.image)
                food_quantity.text = f.qty.toString()
                food_increment_button.setOnClickListener {
                    foodInterface.increment(f)
                }
                food_decrement_button.setOnClickListener {
                    foodInterface.decrement(f)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_selected_food, parent, false))
    }

    override fun getItemCount() = foods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(foods[position])

    fun updateList(it: List<Food>){
        foods.clear()
        foods.addAll(it)
        notifyDataSetChanged()
    }
}