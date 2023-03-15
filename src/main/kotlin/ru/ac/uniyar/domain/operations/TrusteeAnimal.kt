package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.lessEq
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.tables.AnimalTable
import ru.ac.uniyar.domain.tables.TrusteeTable
import java.time.LocalDateTime

class TrusteeAnimal(private val database: Database) {
    fun list(userId: Int, page: Int, view: String, dateFrom: LocalDateTime, dateTo: LocalDateTime): List<Animal> =
        database
            .from(TrusteeTable)
            .innerJoin(AnimalTable, on = AnimalTable.id eq TrusteeTable.animal_id)
            .select()
            .where {
                TrusteeTable.user_id eq userId and AnimalTable.kind.toLowerCase().like(view) and
                    AnimalTable.date.greaterEq(dateFrom) and AnimalTable.date.lessEq(dateTo)
            }
            .limit(8 * (page - 1), 8)
            .mapNotNull { row ->
                Animal.animalFromDatabase(row)
            }
    fun count(userId: Int, view: String, dateFrom: LocalDateTime, dateTo: LocalDateTime): Int =
        (
            database
                .from(TrusteeTable)
                .innerJoin(AnimalTable, on = AnimalTable.id eq TrusteeTable.animal_id)
                .select()
                .where {
                    TrusteeTable.user_id eq userId and AnimalTable.kind.toLowerCase().like(view) and
                        AnimalTable.date.greaterEq(dateFrom) and AnimalTable.date.lessEq(dateTo)
                }
                .totalRecords - 1
            ) / 8 + 1
}
