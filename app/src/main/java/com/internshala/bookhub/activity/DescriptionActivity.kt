package com.internshala.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import com.internshala.bookhub.R
import com.internshala.bookhub.database.BookDao
import com.internshala.bookhub.database.BookDatabase
import com.internshala.bookhub.database.BookEntitiy
import com.internshala.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName:TextView
    lateinit var txtBookAuthor:TextView
    lateinit var txtBookPrice:TextView
    lateinit var txtBookRating:TextView
    lateinit var imgBookImage:ImageView
    lateinit var btnAddToFavourites:Button
    lateinit var txtBookdesc:TextView
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    var bookId:String?="100"
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        txtBookName=findViewById(R.id.txtBookName)
        txtBookAuthor=findViewById(R.id.txtBookAuthor)
        txtBookPrice=findViewById(R.id.txtBookPrice)
        txtBookRating=findViewById(R.id.txtBookRating)
        imgBookImage=findViewById(R.id.imgBookIcon)
        txtBookdesc=findViewById(R.id.txtBookDesc)
        btnAddToFavourites=findViewById(R.id.btnAddToFavourites)
        progressBar=findViewById(R.id.progressBar)
        progressBar.visibility= View.VISIBLE
        progressLayout=findViewById(R.id.progressLayout)
        progressLayout.visibility=View.VISIBLE
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"

        if(intent!=null){
            bookId=intent.getStringExtra("book_id")
        }
        else{
            Toast.makeText(this@DescriptionActivity, "Some Unexpected Error has Occured", Toast.LENGTH_SHORT).show()
        }
        if(bookId=="100"){
            Toast.makeText(this@DescriptionActivity, "Some Unexpected Error has Occured", Toast.LENGTH_SHORT).show()
        }
        val queue= Volley.newRequestQueue(this@DescriptionActivity)
        val url="http://13.235.250.119/v1/book/get_book/"
        val jsonParams=JSONObject()
        jsonParams.put("book_id",bookId)
       if(ConnectionManager().checkConnectivity(this@DescriptionActivity)){
           val jsonRequest = object: JsonObjectRequest(Request.Method.POST,url,jsonParams,
               Response.Listener {
                   try{
                       val success=it.getBoolean("success")
                       if(success){
                           val bookJsonObject=it.getJSONObject("book_data")
                           progressBar.visibility=View.GONE
                           progressLayout.visibility=View.GONE
                           val bookImageUrl=bookJsonObject.getString("image")
                           Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.book_icon).into(imgBookImage)
                           txtBookName.text=bookJsonObject.getString("name")
                           txtBookAuthor.text=bookJsonObject.getString("author")
                           txtBookPrice.text=bookJsonObject.getString("price")
                           txtBookRating.text=bookJsonObject.getString("rating")
                           txtBookdesc.text=bookJsonObject.getString("description")
                           val bookEntitiy=BookEntitiy(
                               bookId?.toInt() as Int,
                               txtBookName.text.toString(),
                               txtBookAuthor.text.toString(),
                               txtBookPrice.text.toString(),
                               txtBookRating.text.toString(),
                               txtBookdesc.text.toString(),
                                bookImageUrl
                               )
                           val checkFav=DBAsyncTask(applicationContext,bookEntitiy,1).execute()
                           val isFav=checkFav.get()
                           if (isFav){
                               btnAddToFavourites.text="Remove from Favourite"
                               val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                               btnAddToFavourites.setBackgroundColor(favColor)
                           }
                           else{
                               btnAddToFavourites.text="Add to Favourite"
                               val noFavColor=ContextCompat.getColor(applicationContext,R.color.black)
                               btnAddToFavourites.setBackgroundColor(noFavColor)
                           }
                           btnAddToFavourites.setOnClickListener {
                               if(!DBAsyncTask(applicationContext,bookEntitiy,1).execute().get()){
                               val async=DBAsyncTask(applicationContext,bookEntitiy,2).execute()
                                   val result=async.get()
                                   if(result){
                                       Toast.makeText(this@DescriptionActivity, "Book added to the favourites", Toast.LENGTH_SHORT).show()
                                       btnAddToFavourites.text="Remove from favourites"
                                       val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                       btnAddToFavourites.setBackgroundColor(favColor)
                                   }else{
                                       Toast.makeText(this@DescriptionActivity, "Some error Occurred", Toast.LENGTH_SHORT).show()
                                   }
                               }
                               else{
                                   val async=DBAsyncTask(applicationContext,bookEntitiy,3).execute()
                                   val result=async.get()
                                   if(result){
                                       Toast.makeText(this@DescriptionActivity, "Book removed from the favourite", Toast.LENGTH_SHORT).show()
                                       btnAddToFavourites.text="Add To favourite"
                                       val noFavColor=ContextCompat.getColor(applicationContext,R.color.black)
                                       btnAddToFavourites.setBackgroundColor(noFavColor)

                                   }
                                   else{
                                       Toast.makeText(this@DescriptionActivity, "Some error Occurred", Toast.LENGTH_SHORT).show()
                                   }
                               }
                           }
                       }
                       else{
                           Toast.makeText(this@DescriptionActivity, "Sorry someUnexpected error occurred", Toast.LENGTH_SHORT).show()
                       }
                   }
                   catch(e : Exception){
                       Toast.makeText(this@DescriptionActivity, "Sorry someUnexpected error occurred", Toast.LENGTH_SHORT).show()
                   }

               },
               Response.ErrorListener {
                   Toast.makeText(this@DescriptionActivity, "Volley Error $it", Toast.LENGTH_SHORT).show()
               }){
               override fun getHeaders(): MutableMap<String, String> {
                   val headers=HashMap<String,String>()
                   headers["Content-type"]="application/json"
                   headers["token"]="9c10ced414a682"
                   return headers
               }
           }
           queue.add(jsonRequest)
       }
        else{
           val dialog= AlertDialog.Builder(this@DescriptionActivity)
           dialog.setTitle("Error ")
           dialog.setMessage("Internet Connection not Found")
           dialog.setPositiveButton("Open Settings"){text,listener->
               val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
               startActivity(settingsIntent)
               finish()
           }
           dialog.setNegativeButton("Exit"){text,listener->
               ActivityCompat.finishAffinity(this@DescriptionActivity)
           }
           dialog.create()
           dialog.show()
       }

    }
    class DBAsyncTask(val context: Context,val bookEntitiy: BookEntitiy,val mode:Int):AsyncTask<Void,Void,Boolean>() {
        /*
        mode 1->check the db if the book is in favourites or not
        mode 2-> save the book in the DB as favourite
        mode 3-> remove the book from the DB
         */

        val db=Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode){
                1 ->{
                    val book:BookEntitiy?=db.bookDao().getBookById(bookEntitiy.book_id.toString())
                    db.close()
                    return book!=null
                }
                2 ->{
                    db.bookDao().insertBook(bookEntitiy)
                    db.close()
                    return true
                }
                3 ->{
                    db.bookDao().deleteBook(bookEntitiy)
                    db.close()
                    return true

                }
            }
            return false
        }

    }
}