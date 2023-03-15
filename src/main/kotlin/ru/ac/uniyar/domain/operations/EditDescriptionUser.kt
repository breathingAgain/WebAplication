package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.tables.UserTable

class EditDescriptionUser(private val database: Database) {
    fun edit(userId: Int, newDescription: String) =
        database
            .update(UserTable) {
                set(UserTable.description, newDescription)
                where {
                    userId eq UserTable.id
                }
            }
}
