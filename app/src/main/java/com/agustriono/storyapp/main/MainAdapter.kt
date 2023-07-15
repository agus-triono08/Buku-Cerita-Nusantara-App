package com.agustriono.storyapp.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.agustriono.storyapp.detail.DetailActivity
import com.agustriono.storyapp.main.MainAdapter.MainViewHolder
import com.agustriono.storyapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.list_judul_cerita.view.*

/**
 * Created by Agus Triono on 06-07-2023
 * Youtube Channel : https://www.youtube.com/@kanggustri
 * Github : https://github.com/agus-triono08
 * Twitter : https://twitter.com/agus_triono08
 * Instagram : https://www.instagram.com/agus_triono08
 * Linkedin : https://www.linkedin.com/in/agustriono
 */

class MainAdapter(
    var context: Context,
    var modelMainList: MutableList<ModelMain>) : RecyclerView.Adapter<MainViewHolder>(), Filterable {

    var modelMainFilterList: List<ModelMain> = ArrayList(modelMainList)

    override fun getFilter(): Filter {
        return modelFilter
    }

    private val modelFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ModelMain> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(modelMainFilterList)
            } else {
                val filterPattern = constraint.toString().lowercase()
                for (modelMainFilter in modelMainFilterList) {
                    if (modelMainFilter.judul.lowercase().contains(filterPattern) ||
                        modelMainFilter.asal.lowercase().contains(filterPattern)
                    ) {
                        filteredList.add(modelMainFilter)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            modelMainList.clear()
            modelMainList.addAll(results.values as List<ModelMain>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_judul_cerita, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = modelMainList[position]

        holder.tvJudulUtama.text = data.judul
        holder.tvAsalUtama.text = data.asal
        holder.tvSinopsis.text = data.sinopsis

        Glide.with(context)
            .load(data.image)
            .transform(CenterCrop(), RoundedCorners(25))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageCerita)

        //send data to detail activity
        holder.cvListMain.setOnClickListener { view: View? ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.DETAIL_CERITA, modelMainList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return modelMainList.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvListMain: CardView
        var tvJudulUtama: TextView
        var tvAsalUtama: TextView
        var tvSinopsis: TextView
        var imageCerita: ImageView

        init {
            cvListMain = itemView.cvListMain
            tvJudulUtama = itemView.tvJudulUtama
            tvAsalUtama = itemView.tvAsalUtama
            tvSinopsis = itemView.tvSinopsis
            imageCerita = itemView.imageCerita
        }
    }

}