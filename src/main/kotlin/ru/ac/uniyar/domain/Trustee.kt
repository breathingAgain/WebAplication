package ru.ac.uniyar.domain

import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.operations.AnimalByIndex
import ru.ac.uniyar.domain.operations.UserById
import ru.ac.uniyar.domain.tables.TrusteeTable
import java.time.LocalDateTime

data class Trustee(val id: Int, val user: User, val animal: Animal, val date: LocalDateTime) {
    companion object {
        fun trusteeFromDatabase(row: QueryRowSet, database: Database): Trustee? {
            return try {
                Trustee(
                    row[TrusteeTable.id]!!,
                    UserById(database).fetch(row[TrusteeTable.user_id]!!),
                    AnimalByIndex(database).fetch(row[TrusteeTable.animal_id]!!),
                    row[TrusteeTable.date]!!
                )
            } catch (_: NullPointerException) {
                null
            }
        }
    }
}
