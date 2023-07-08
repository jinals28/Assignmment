package com.example.linksdemo.Adapter

import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linksdemo.R
import com.example.linksdemo.Adapter.ListAdapter
import com.example.linksdemo.data.Link
import com.example.linksdemo.databinding.LinkCardBinding


class ListAdapter(private val links : List<Link>) : RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    private var linkList = emptyList<Link>()

    init {
        linkList = links
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.link_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val link = links[position]
        holder.bind(link)
    }

    override fun getItemCount(): Int {
        return links.size
    }

    fun setLinkList(list: List<Link>){
        this.linkList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val linkName: TextView = itemView.findViewById(R.id.linkName)
        private val linkDate: TextView = itemView.findViewById(R.id.linkDate)
        private val linkClickCount: TextView = itemView.findViewById(R.id.clickCount)
        private val smartLinkText: TextView = itemView.findViewById(R.id.smartLink)
        private val icon: ImageView = itemView.findViewById(R.id.linkIcon)

        fun bind(link : Link){
            linkName.text = link.title
            linkDate.text = link.timesAgo
            linkClickCount.text = link.totalClicks.toString()
            smartLinkText.text = link.smartLink

            val url = link.originalImage

            Glide.with(icon)
                .load(url)
                .placeholder(R.drawable.ic_dashboard_black_24dp)
                .into(icon)
        }
    }

}