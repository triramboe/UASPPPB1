package com.example.calorieuas

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter

class SliderAdapter(var dataSlider: ArrayList<Int>, var context: FragmentActivity?) : PagerAdapter() {

    lateinit var layoutInflater: LayoutInflater
    override fun getCount(): Int {
       return dataSlider.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_slider,container, false)

        val imgViewSlider : ImageView
        imgViewSlider = view.findViewById(R.id.itemSlider)


        imgViewSlider.setImageResource(dataSlider[position])
        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}