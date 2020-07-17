package com.meeting.tegal.ui.food_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meeting.models.Food
import com.example.meeting.utilities.Constants
import com.meeting.tegal.R
import kotlinx.android.synthetic.main.item_food.view.*

class FoodAdapter(private val foods: MutableList<Food>, private val foodInterface: FoodClickInterface) : RecyclerView.Adapter<FoodAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(food: Food){
            with(itemView){
                food_name.text = food.name
                food_quantity.text = food.qty.toString()
                food_price.text  = Constants.setToIDR(food.price!!)
                food_increment_button.setOnClickListener {
                    foodInterface.increment(food)
                }
                food_decrement_button.setOnClickListener {
                    foodInterface.decrement(food)
                }
            }
        }
    }

    fun updateList(it: List<Food>){
        foods.clear()
        foods.addAll(it)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false))
    }

    override fun getItemCount() = foods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(foods[position])
}