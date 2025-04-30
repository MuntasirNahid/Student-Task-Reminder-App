package com.example.student_task_reminder_app.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.student_task_reminder_app.data.database.ReminderDatabase

/**
 * This ContentProvider makes app's reminders accessible to other apps or Android system components (like widgets,
 * shortcuts, or other apps needing my apps reminders).
 * It allows querying reminders through a standardized ContentResolver mechanism.
 *
 * In short:
 * It exposes your reminders database in a safe, controlled way
 * Only read/query operations are supported — insert, update, delete are disabled.
 */
class ReminderContentProvider : ContentProvider() {
    private lateinit var database: ReminderDatabase

    /**
     * UriMatcher helps match incoming Uris and decide what type of operation we want to do.
     *
     * NO_MATCH means initially there’s no match.
     * apply {} block:
     * Adds two match rules:
     * "reminders" → maps to constant REMINDERS
     * "reminders/#" (where # means any number, like reminder id) → maps to constant REMINDER_ID
     */

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "reminders", REMINDERS)
        addURI(AUTHORITY, "reminders/#", REMINDER_ID)
    }

    companion object {
        //AUTHORITY = unique name that identifies my provider (important for external apps to access it).

        const val AUTHORITY = "com.example.student_task_reminder_app.provider"
        const val REMINDERS = 1
        const val REMINDER_ID = 2
    }

    override fun onCreate(): Boolean {
        database = ReminderDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {

            /**
             * ContentProvider must return a Cursor object in its query() method,
             * since that's the standard way for external apps / widgets / shortcuts / system to fetch data through ContentResolver.
             */
            REMINDERS -> database.reminderDao().getRemindersCursor()
            REMINDER_ID -> {
                val id = uri.lastPathSegment?.toLongOrNull()
                database.reminderDao().getReminderCursor(id)
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    /**
     * Returns the MIME type of the data for the given URI.
     *
     * Helps external components understand whether the URI refers to a list or a single item.
     *
     * @param uri the URI to get the type for
     * @return a MIME type string
     *
     * MIME Type stands for Multipurpose Internet Mail Extensions Type.
     * In Android's ContentProvider, MIME type tells the Android system what kind of data the URI is pointing to
     */

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            REMINDERS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.reminders"
            REMINDER_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.reminders"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Insert not supported")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Delete not supported")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Update not supported")
    }
}