package com.meeting.tegal.ui.food_order

import com.example.meeting.models.Food

interface FoodClickInterface {
    fun click(food: Food)
    fun increment(food: Food)
    fun decrement(food: Food)
}