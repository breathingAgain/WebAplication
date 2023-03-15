package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.tables.EntryTable

class EntryByAnimalNoTrustee(private val database: Database) {
    fun list(animalId: Int, page: Int = 1): List<Entry> =
        database
            .from(EntryTable)
            .select()
            .where(EntryTable.animal_id eq animalId and !EntryTable.for_trustee)
            .limit((page - 1) * 3, 3)
            .mapNotNull { row ->
                Entry.entryFromDatabase(row, database)
            }
    fun count(animalId: Int): Int =
        (
            database
                .from(EntryTable)
                .select()
                .where(EntryTable.animal_id eq animalId and !EntryTable.for_trustee)
                .totalRecords - 1
            ) / 3 + 1
}
