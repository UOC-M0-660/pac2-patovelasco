package edu.uoc.pac2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"

    private lateinit var adapter: BooksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        // TODO: Add books data to Firestore [Use once for new projects with empty Firestore Database]
        //FirestoreBookData.addBooksDataToFirestoreDatabase()
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    // TODO: Get Books and Update UI
    private fun getBooks() {
        adapter.setBooks(loadBooksFromLocalDb())

        val internetConnection = (application as MyApplication).hasInternetConnection()
        if(internetConnection){
            val firestoreDatabase = Firebase.firestore
            val docRef = firestoreDatabase.collection("books")
            docRef.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val books: List <Book> = snapshots!!.documents.mapNotNull {it.toObject (Book:: class .java)}
                saveBooksToLocalDatabase(books)
                adapter.setBooks(books)
            }
        }

    }

    // TODO: Load Books from Room
    private fun loadBooksFromLocalDb(): List<Book> {
        val booksInteracts = castApplication()
        return booksInteracts.getAllBooks()
    }

    // TODO: Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        val booksInteracts = castApplication()
        booksInteracts.saveBooks(books)
    }

    //function fot cast to MyApplication
    private fun castApplication() = (application as MyApplication).getBooksInteractor()
}