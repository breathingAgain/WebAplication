package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.*
import ru.ac.uniyar.domain.tables.TrusteeTable
import java.time.LocalDateTime

class AddTrusteeInDatabase(private val database: Database) {
    fun adding(userId: Int, animalId: Int) =
        database.insertAndGenerateKey(TrusteeTable) {
            set(it.user_id, userId)
            set(it.animal_id, animalId)
            set(it.date, LocalDateTime.now())
        }
    fun checkTrust(userId: Int, animalId: Int): Boolean =
        database
            .from(TrusteeTable)
            .select()
            .where { userId eq TrusteeTable.user_id and(animalId eq TrusteeTable.animal_id) }
            .totalRecords == 0
}
