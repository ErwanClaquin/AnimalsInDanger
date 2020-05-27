package com.example.animalsindanger

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.PrimaryKey
import com.example.animalsindanger.MainActivity.Companion.lastActivity

class SearchAnimalsActivity : AppCompatActivity() {
    private lateinit var buttonReturn: ImageButton
    private val dbHelper = FeedReaderDbHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_animals)
        buttonReturn = findViewById(R.id.imageBack)
        buttonReturn.setOnClickListener {
            setLastView()
        }
        fillTable()
        getAllTable()
    }

    private fun setLastView() {
        val act: Activity = lastActivity.removeAt(lastActivity.lastIndex)
        val intent = Intent(this, act::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun fillTable() {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.NAME, "Le grand requin blanc")
            put(FeedReaderContract.FeedEntry.HABITAT, "eaux tempérées")
            put(FeedReaderContract.FeedEntry.NUMBER, 6)
            put(FeedReaderContract.FeedEntry.SIZE, 2700)
            put(FeedReaderContract.FeedEntry.SOLUTIONS, "Insert solutions")
            put(FeedReaderContract.FeedEntry.FOOD, "pinnipèdes, poissons, tortues marines, cétacés")
            put(FeedReaderContract.FeedEntry.MOBILE, true)
            put(FeedReaderContract.FeedEntry.LIFETIME, 50)
        }
        val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
    }

    private fun getAllTable() {
        val db = dbHelper.readableDatabase
        val sortOrder = "${FeedReaderContract.FeedEntry.NAME} DESC"

        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
            null,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )

        print(cursor)
    }


}

class Specie(
    var name: String,
    var habitat: String,
    var number: Int,
    var size: Float,
    var solution: String,
    var food: String,
    var mobile: Boolean,
    var lifetime: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


class AnimalViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.search_animals, parent, false)) {

    private var nameView: TextView? = null
    private var imageView: ImageView? = null

    init {
        nameView = itemView.findViewById(R.id.fragment_main_animal_title)
        imageView = itemView.findViewById(R.id.fragment_main_animal_image)
    }

    fun bind(animal: Specie) {
        nameView?.text = animal.name
        //Todo:image in bdd?
    }
}

class ListAdapter(private val list: List<Specie>) : RecyclerView.Adapter<AnimalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AnimalViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val movie: Specie = list[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = list.size

}

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "species"
        const val NAME = "name"
        const val HABITAT = "habitat"
        const val NUMBER = "number"
        const val SIZE = "size"
        const val SOLUTIONS = "solution"
        const val FOOD = "food"
        const val MOBILE = "mobile"
        const val LIFETIME = "lifetime"

    }
}

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry.NAME} TEXT," +
            "${FeedReaderContract.FeedEntry.HABITAT} TEXT," +
            "${FeedReaderContract.FeedEntry.NUMBER} INTEGER," +
            "${FeedReaderContract.FeedEntry.SIZE} REAL," +
            "${FeedReaderContract.FeedEntry.SOLUTIONS} TEXT," +
            "${FeedReaderContract.FeedEntry.FOOD} TEXT," +
            "${FeedReaderContract.FeedEntry.MOBILE} BOOLEAN," +
            "${FeedReaderContract.FeedEntry.LIFETIME} INTEGER)"

private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, "FeedReader.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}


