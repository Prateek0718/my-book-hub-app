package com.internshala.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.internshala.bookhub.model.Book

@Dao
interface BookDao {
    @Insert
    fun insertBook(bookEntitiy: BookEntitiy)

    @Delete
    fun deleteBook(bookEntitiy: BookEntitiy)

    @Query("SELECT * FROM books")
    fun getAllBooks():List<BookEntitiy>

    @Query("SELECT * FROM books WHERE book_id=:bookId")
    fun getBookById(bookId:String):BookEntitiy
}