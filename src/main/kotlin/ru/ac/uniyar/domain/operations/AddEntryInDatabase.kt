package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.tables.EntryTable

class AddEntryInDatabase(private val database: Database) {
    fun adding(entry: Entry) =
        database.insertAndGenerateKey(EntryTable) {
            set(it.date, entry.date)
            set(it.text, entry.text)
            set(it.animal_id, entry.animal.id)
            set(it.for_trustee, entry.forTrustee)
        }
}
