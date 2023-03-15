package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.lessEq
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.tables.AnimalTable
import ru.ac.uniyar.domain.tables.EntryTable
import ru.ac.uniyar.domain.tables.TrusteeTable
import java.time.LocalDateTime

class ListEntryForTrustee(private val database: Database) {
    fun list(page: Int, view: String, dateFrom: LocalDateTime, dateTo: LocalDateTime, userId: Int): List<Entry> =
        database
            .from(EntryTable)
            .innerJoin(AnimalTable, on = EntryTable.animal_id eq AnimalTable.id)
            .innerJoin(
                TrusteeTable,
                on = TrusteeTable.animal_id eq AnimalTable.id
                    and(TrusteeTable.user_id eq userId)
            )
            .select()
            .where {
                EntryTable.date.greaterEq(dateFrom) and EntryTable.date.lessEq(dateTo) and AnimalTable
                    .kind.toLowerCase().like(view)
            }
            .limit((page - 1) * 8, 8)
            .orderBy(EntryTable.date.desc())
            .mapNotNull { row ->
                Entry.entryFromDatabase(row, database)
            }
    fun count(view: String, dateFrom: LocalDateTime, dateTo: LocalDateTime, userId: Int) =
        (
            database
                .from(EntryTable)
                .innerJoin(AnimalTable, on = EntryTable.animal_id eq AnimalTable.id)
                .innerJoin(
                    TrusteeTable,
                    on = TrusteeTable.animal_id eq AnimalTable.id
                        and(TrusteeTable.user_id eq userId)
                )
                .select()
                .where {
                    EntryTable.date.greaterEq(dateFrom) and EntryTable.date.lessEq(dateTo) and AnimalTable
                        .kind.toLowerCase().like(view)
                }
                .totalRecords - 1
            ) / 8 + 1
}
