package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.Room
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var booksInteractor: BooksInteractor

    override fun onCreate() {
        super.onCreate()

        // TODO: Init Room Database
        val db = Room.databaseBuilder(
                this,
                ApplicationDatabase::class.java, "db_books"
        ).allowMainThreadQueries().build()
        // TODO: Init BooksInteractor
        booksInteractor =  BooksInteractor(db.bookDao())
    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    //function for validate connection to internet
    fun hasInternetConnection(): Boolean {
        // TODO: Add Internet Check logic.
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkCapabilities = connectivityManager?.activeNetwork ?: return false
        val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}