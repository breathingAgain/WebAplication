package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.tables.TrusteeTable
import ru.ac.uniyar.domain.tables.UserTable

class ListUsersByAnimal(private val database: Database) {
    fun list(animalId: Int) =
        database
            .from(UserTable)
            .innerJoin(TrusteeTable, on = TrusteeTable.user_id eq UserTable.id and(TrusteeTable.animal_id eq animalId))
            .select()
            .mapNotNull { row ->
                User.userFromDatabase(row)
            }
}
