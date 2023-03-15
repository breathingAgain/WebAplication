package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.tables.EntryTable

class EntryByIndex(private val database: Database) {
    fun fetch(id: Int): Entry =
        database
            .from(EntryTable)
            .select()
            .where(EntryTable.id eq id)
            .mapNotNull { row ->
                try {
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
            }.first()
}
