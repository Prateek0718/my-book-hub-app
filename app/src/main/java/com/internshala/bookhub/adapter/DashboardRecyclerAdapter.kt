package com.internshala.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.bookhub.R
import com.internshala.bookhub.activity.DescriptionActivity
import com.internshala.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter (val context:Context, val itemList:ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
    val book=itemList[position]
        holder.txtBookName.text=book.BookName
        holder.txtBookAthour.text=book.BookAuthorName
        holder.txtBookPrice.text=book.BookPrice
        holder.txtBookRating.text=book.BookRating
        //holder.imgBookImage.setImageResource(book.BookImage)
        Picasso.get().load(book.BookImage).error(R.drawable.book_icon).into(holder.imgBookImage)
        holder.llContent.setOnClickListener{
            val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.BookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
    return itemList.size
    }
    class DashboardViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtBookName:TextView=view.findViewById(R.id.txtBookName)
        val txtBookAthour:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice:TextView=view.findViewById(R.id.txtBookPrice)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val imgBookImage:ImageView=view.findViewById(R.id.imgBookIcon)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
    }
}