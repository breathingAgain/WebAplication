package ru.ac.uniyar.domain

import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.operations.AnimalByIndex
import ru.ac.uniyar.domain.tables.EntryTable
import java.time.LocalDateTime

data class Entry(
    val id: Int,
    val date: LocalDateTime,
    val animal: Animal,
    val text: String,
    val forTrustee: Boolean = false
) {
    companion object {
        fun entryFromDatabase(row: QueryRowSet, database: Database): Entry? {
            return try {
                Entry(
                    row[EntryTable.id]!!,
                    row[EntryTable.date]!!,
                    AnimalByIndex(database).fetch(row[EntryTable.animal_id]!!),
                    row[EntryTable.text]!!,
                    row[EntryTable.for_trustee]!!
                )
            } catch (_: NullPointerException) {
                null
            }
        }
    }
}
