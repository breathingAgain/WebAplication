package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.tables.EntryTable

class EntryByTrusteeAnimalCount(private val database: Database) {
    fun count(animalId: Int): Int =
        database
            .from(EntryTable)
            .select()
            .where { EntryTable.animal_id eq animalId and EntryTable.for_trustee }
            .totalRecords
}
