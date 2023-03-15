package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import ru.ac.uniyar.domain.tables.TrusteeTable

class DeleteTrustAnimal(private val database: Database) {
    fun delete(user_id: Int, animal_id: Int) =
        database
            .delete(TrusteeTable) {
                it.animal_id eq animal_id and(it.user_id eq user_id)
            }
}
