package edu.uoc.pac2.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import kotlinx.android.synthetic.main.activity_book_detail.*

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get Book for this detail screen
        loadBook()
    }


    // TODO: Get Book for the given {@param ARG_ITEM_ID} Book id
    private fun loadBook() {
        val booksInteracts = (activity?.application as MyApplication).getBooksInteractor()
        //get argument
        arguments?.let {
            booksInteracts.getBookById(it.getInt(ARG_ITEM_ID))?.let { it1 ->
                initUI(it1)
                activity!!.fab.setOnClickListener { shareContent(it1) }
            }
        }
    }

    // TODO: Init UI with book details
    private fun initUI(book: Book) {
        book?.let {
            activity?.toolbar_layout?.title = it.title
        }
        view!!.findViewById<TextView>(R.id.book_author).text = book.author
        view!!.findViewById<TextView>(R.id.book_date).text = book.publicationDate
        view!!.findViewById<TextView>(R.id.book_detail).text = book.description
        //set image with Glide
        GlideApp.with(this)
                .load(book.urlImage)
                .into(view!!.findViewById(R.id.book_image))
    }

    // TODO: Share Book Title and Image URL
    private fun shareContent(book: Book) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Book: ${book.title}\nURL: ${book.urlImage}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share book information to..")
        startActivity(shareIntent)
    }

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}