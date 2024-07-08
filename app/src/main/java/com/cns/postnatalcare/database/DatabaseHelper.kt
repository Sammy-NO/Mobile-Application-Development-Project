package com.cns.postnatalcare.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLException

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        try{
        db.execSQL(CREATE_USERS_TABLE)
        db.execSQL(CREATE_CHILDREN_TABLE)
        db.execSQL(CREATE_VACCINES_TABLE)
        db.execSQL(CREATE_SCHEDULES_TABLE)
        db.execSQL(CREATE_PROFILES_TABLE)
        insertVaccines(db) } catch (e: SQLException){
            Log.e("DatabaseHelper", "Error creating tables", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS profiles")
        db.execSQL("DROP TABLE IF EXISTS schedules")
        db.execSQL("DROP TABLE IF EXISTS children")
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS vaccines")
        onCreate(db)
    }

    private fun insertVaccines(db: SQLiteDatabase) {
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('BCG', 'Tuberculosis Vaccine', 0)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('OPVO', 'Oral Polio Vaccine 0', 0)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('OPV1', 'Oral Polio Vaccine 1', 6)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('ROTA1', 'Rotavirus Vaccine 1', 6)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('DPT1', 'Diptheria,pertusis, tetanus', 6)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('HepB1', 'Hepatitis B', 6)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('PCV1', 'Pneumococcal conjugate vaccine', 6)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('OPV2', 'Oral Polio Vaccine 2', 10)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('ROTA2', 'Rotavirus Vaccine 1', 10)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('DPT2', 'Diptheria,pertusis, tetanus', 10)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('HepB2', 'Hepatitis B', 10)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('PCV2', 'Pneumococcal conjugate vaccine', 10)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('OPV3', 'Oral Polio Vaccine 3', 14)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('IPV', 'Inactivated polio vaccine', 14)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('DPT3', 'Diptheria,pertusis, tetanus',14)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('HepB3', 'Hepatitis B', 14)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('PCV3', 'Pneumococcal conjugate vaccine', 14)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('MR1', 'Measles, Rubella', 39)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('YF-VAX', 'Yellow Fever', 39)")
        db.execSQL("INSERT INTO vaccines (name, description, week_to_administer) VALUES ('MR2', 'Measles , Rubella', 78)")


    }


    companion object {
        private const val DATABASE_NAME = "vaccine_scheduler.db"
        private const val DATABASE_VERSION = 4


        private const val CREATE_USERS_TABLE = """
    CREATE TABLE users (
        user_id INTEGER PRIMARY KEY, 
        username TEXT, 
        password TEXT, 
        email TEXT
    )
"""

        private const val CREATE_CHILDREN_TABLE = """
    CREATE TABLE children (
        child_id INTEGER PRIMARY KEY, 
        user_id INTEGER, 
        name TEXT, 
        birth_date TEXT, 
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
    )
"""

        private const val CREATE_VACCINES_TABLE = """
    CREATE TABLE vaccines (
        vaccine_id INTEGER PRIMARY KEY, 
        name TEXT, 
        description TEXT, 
        week_to_administer INTEGER
    )
"""

        private const val CREATE_SCHEDULES_TABLE = """
    CREATE TABLE schedules (
        schedule_id INTEGER PRIMARY KEY, 
        child_id INTEGER, 
        vaccine_id INTEGER, 
        scheduled_date TEXT, 
        status TEXT, 
        FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE, 
        FOREIGN KEY (vaccine_id) REFERENCES vaccines(vaccine_id) ON DELETE CASCADE
    )
"""

        private const val CREATE_PROFILES_TABLE = """
    CREATE TABLE profiles (
        profile_id INTEGER PRIMARY KEY, 
        child_id INTEGER,
        vaccine_id INTEGER, 
        status INTEGER, 
        remaining_time INTEGER, 
        FOREIGN KEY (vaccine_id) REFERENCES vaccines(vaccine_id) ON DELETE CASCADE,
        FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE
    )
"""

    }
}
