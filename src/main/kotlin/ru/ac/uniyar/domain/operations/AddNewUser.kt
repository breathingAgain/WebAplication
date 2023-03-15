package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import ru.ac.uniyar.domain.User
import java.sql.Timestamp
import java.time.LocalDateTime

class AddNewUser(private val database: Database, private val salt: String) {
    private val preparedSql = """
        INSERT INTO USERS (DATE_CREATION, LOGIN, FIO, PASSWORD, ROLE_ID) VALUES(?,?,?,HASH('SHA3-256', ?, 10), 2);
    """.trimIndent()

    fun adding(user: User) =
        database.useConnection { connection ->
            connection.prepareStatement(preparedSql, 1).use { statement ->
                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()))
                statement.setString(2, user.login)
                statement.setString(3, user.fio)
                statement.setString(4, user.password + salt)
                statement.execute()
            }
        }
}
