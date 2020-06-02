package com.example.animalsindanger

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.PrimaryKey
import com.example.animalsindanger.MainActivity.Companion.lastActivity


class SearchAnimalsActivity : AppCompatActivity() {
    private lateinit var buttonReturn: ImageButton
    private val dbHelper = FeedReaderDbHelper(this)
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_animals)
        buttonReturn = findViewById(R.id.imageBack)
        buttonReturn.setOnClickListener {
            setLastView()
        }
        println("\nTABLE CREATING ...")
        fillTable()
        listView = findViewById(R.id.animal_list_view)
        val animalList = getAllTable()

        val listItems = arrayOfNulls<String>(animalList.size)

        for (i in animalList.indices) {
            val animal = animalList[i]

            listItems[i] = animal.name
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
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
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_CREATE_ENTRIES)
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

    private fun getAllTable(): List<Specie> {
        return dbHelper.readableDatabase.use { it ->
            dbHelper.readableDatabase
            val cursor =
                it.rawQuery("SELECT * FROM ${FeedReaderContract.FeedEntry.TABLE_NAME}", null)
            val species = arrayListOf<Specie>()
            if (!cursor.moveToFirst()) return@use species
            cursor.use {
                while (!it.isAfterLast) {
                    println("Create Animal")
                    val animal = Specie(
                        it.getString(1).toString(),
                        it.getString(2).toString(),
                        it.getInt(3),
                        it.getFloat(4),
                        it.getString(5).toString(),
                        it.getString(6).toString(),
                        it.getString(7)!!.toBoolean(),
                        it.getFloat(8)
                    )
                    println("Add Animal")
                    species.add(animal)
                    it.moveToNext()
                }

            }

            return@use species
        }
    }
}


data class Specie(
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

class FeedReaderDbHelper(context: Context) :
    SQLiteOpenHelper(context, "FeedReader.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        println("OnCreate FeedReaderDbHelper")
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


