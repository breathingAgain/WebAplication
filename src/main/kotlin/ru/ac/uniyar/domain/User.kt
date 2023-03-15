package ru.ac.uniyar.domain

import org.ktorm.dsl.QueryRowSet
import ru.ac.uniyar.domain.tables.UserTable
import java.lang.NullPointerException
import java.time.LocalDateTime

data class User(
    val id: Int?,
    val dateCreation: LocalDateTime?,
    val login: String,
    val fio: String,
    val password: String,
    val role_id: Int?,
    val description: String? = null
) {
    companion object {
        fun userFromDatabase(row: QueryRowSet): User? {
            return try {
                User(
                    row[UserTable.id],
                    row[UserTable.date],
                    row[UserTable.login]!!,
                    row[UserTable.fio]!!,
                    row[UserTable.password]!!,
                    row[UserTable.roleId]!!,
                    row[UserTable.description]
                )
            } catch (_: NullPointerException) {
                null
            }
        }
    }
}
