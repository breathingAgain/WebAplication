package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.from
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.lessEq
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.tables.AnimalTable
import java.time.LocalDateTime

class ListAnimals(private val database: Database) {
    fun listNoLimit(): List<Animal> =
        database
            .from(AnimalTable)
            .select()
            .orderBy(AnimalTable.date.desc())
            .mapNotNull { row ->
                Animal.animalFromDatabase(row)
            }

    fun list(page: Int, view: String, dateFrom: LocalDateTime, dateTo: LocalDateTime): List<Animal> =
        database
            .from(AnimalTable)
            .select()
            .where {
                AnimalTable.kind.toLowerCase().like(view) and AnimalTable.date.greaterEq(dateFrom) and AnimalTable
                    .date.lessEq(dateTo)
            }
            .limit((page - 1) * 8, 8)
            .orderBy(AnimalTable.date.desc())
            .mapNotNull { row ->
                Animal.animalFromDatabase(row)
            }

    fun count(view: String, dateFrom: LocalDateTime, dateTo: LocalDateTime) =
        (
            database
                .from(AnimalTable)
                .select()
                .where {
                    AnimalTable.kind.toLowerCase().like(view) and AnimalTable.date.greaterEq(dateFrom) and AnimalTable
                        .date.lessEq(dateTo)
                }
                .totalRecords - 1
            ) / 8 + 1
}
