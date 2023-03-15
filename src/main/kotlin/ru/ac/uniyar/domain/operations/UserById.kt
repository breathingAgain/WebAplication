package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.tables.UserTable

class UserById(private val database: Database) {
    fun fetch(id: Int): User =
        database
            .from(UserTable)
            .select()
            .where { id eq UserTable.id }
            .mapNotNull { row ->
                User.userFromDatabase(row)
            }.first()
}
