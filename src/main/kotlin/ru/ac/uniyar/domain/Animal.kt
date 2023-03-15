package ru.ac.uniyar.domain

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.AnimalTable
import java.time.LocalDateTime

data class Animal(val id: Int, val date: LocalDateTime, val view: String, val name: String, val description: String) {
    companion object {
        fun animalFromDatabase(row: QueryRowSet): Animal? {
            return try {
                Animal(
                    row[AnimalTable.id]!!,
                    row[AnimalTable.date]!!,
                    row[AnimalTable.kind]!!,
                    row[AnimalTable.name]!!,
                    row[AnimalTable.description]!!
                )
            } catch (_: NullPointerException) {
                null
            }
        }
    }
}
