package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.not
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.tables.EntryTable
import ru.ac.uniyar.domain.tables.TrusteeTable

class EntryByAnimalForTrustee(private val database: Database) {
    fun list(animalId: Int, page: Int = 1, userId: Int): List<Entry> {
        val list = database
            .from(EntryTable)
            .select()
            .where { !EntryTable.for_trustee and(EntryTable.animal_id eq animalId) }
            .mapNotNull { row ->
                Entry.entryFromDatabase(row, database)
            } +
            database
                .from(EntryTable)
                .innerJoin(TrusteeTable, on = TrusteeTable.animal_id eq animalId)
                .select()
                .where {
                    EntryTable.animal_id eq animalId and(
                        EntryTable.for_trustee
                            and(TrusteeTable.user_id eq userId)
                        )
                }
                .mapNotNull { row ->
                    Entry.entryFromDatabase(row, database)
                }.sortedBy { entry -> entry.date }
        return try {
            if (list.size < (page - 1) * 3) {
                list.subList(0, kotlin.math.min(3, list.size))
            } else {
                list.subList((page - 1) * 3, kotlin.math.min(page * 3, list.size))
            }
        } catch (_: IndexOutOfBoundsException) {
            list.subList(0, kotlin.math.min(3, list.size))
        }
    }

    fun count(animalId: Int, userId: Int): Int =
        (
            database
                .from(EntryTable)
                .select()
                .where { !EntryTable.for_trustee and(EntryTable.animal_id eq animalId) }
                .totalRecords +
                database
                    .from(EntryTable)
                    .innerJoin(TrusteeTable, on = TrusteeTable.animal_id eq animalId)
                    .select()
                    .where {
                        EntryTable.animal_id eq animalId and(
                            EntryTable.for_trustee
                                and(TrusteeTable.user_id eq userId)
                            )
                    }.totalRecords - 1
            ) / 3 + 1
}
