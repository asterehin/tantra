package com.andreyt.vk.style.factories

import android.content.*
import android.database.*
import android.view.*
import androidx.loader.content.Loader
import com.tantra.tantrayoga.R
import com.tantra.tantrayoga.model.Asana
import com.tantra.tantrayoga.model.database.AppDatabase.Companion.appDatabaseInstance
import com.tantra.tantrayoga.widget.BaseCursorLoader
import com.tantra.tantrayoga.widget.ListWithSearchDialog
import com.tantra.tantrayoga.widget.ListWithSearchDialogItemAdapter
import kotlinx.android.synthetic.main.selector_item.view.*

class AsanasDialogFactory(private val kco: Int, private val type: String) :
    ListWithSearchDialog.Factory<StdPhrasesAdapter.StdPhraseViewHolder, Asana>(Asana::class.java) {

    override fun createAdapter(
        context: Context
    ): ListWithSearchDialogItemAdapter<StdPhrasesAdapter.StdPhraseViewHolder> =
        StdPhrasesAdapter(context)

    override fun createLoader(context: Context, searchText: String?): Loader<Cursor>? =
        StdPhrasesLoader(context, kco, type, searchText)

    override fun createItem(cursor: Cursor): Asana = Asana(cursor)
}

class StdPhrasesAdapter(context: Context) :
    ListWithSearchDialogItemAdapter<StdPhrasesAdapter.StdPhraseViewHolder>(
        context,
        "name",
        "desc"
    ) {

    override fun onBindViewHolder(viewHolder: StdPhraseViewHolder, position: Int, cursor: Cursor) {
        super.onBindViewHolder(viewHolder, position, cursor)
        val stdPhrase = Asana(cursor)

        viewHolder.itemView.code.text = stdPhrase.name
        viewHolder.itemView.descriptionTextView.text = stdPhrase.desc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StdPhraseViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.selector_item, parent, false)
        return StdPhraseViewHolder(v)
    }

    class StdPhraseViewHolder(val itemView: View) :
        ListWithSearchDialogItemAdapter.ViewHolder(itemView)
}

private class StdPhrasesLoader(
    context: Context,
    private val kco: Int,
    private val type: String,
    private val searchText: String?
) :
    BaseCursorLoader(context) {

    override fun loadCursorInBackground(): Cursor {
        val db = appDatabaseInstance(context)
        val asanasTypeDao = db.asanaDao()

        return asanasTypeDao.getAsanasCursor()
    }
}
