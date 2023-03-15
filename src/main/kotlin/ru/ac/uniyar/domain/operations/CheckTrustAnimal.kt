package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.tables.TrusteeTable

class CheckTrustAnimal(private val database: Database) {
    fun check(animalId: Int, userId: Int): Boolean =
        database
            .from(TrusteeTable)
            .select()
            .where { animalId eq TrusteeTable.animal_id and(userId eq TrusteeTable.user_id) }
            .totalRecords == 1
}
