package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.tables.UserTable

class CheckLoginOriginally(private val database: Database) {
    fun check(login: String): Boolean =
        database
            .from(UserTable)
            .select()
            .where { UserTable.login eq login }
            .totalRecords == 0
}
