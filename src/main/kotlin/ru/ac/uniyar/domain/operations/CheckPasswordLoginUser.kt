package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.database.asIterable
import ru.ac.uniyar.domain.User

class CheckPasswordLoginUser(private val database: Database, private val salt: String) {
    private val preparedSql = """
        SELECT * FROM USERS WHERE LOGIN LIKE ? AND PASSWORD LIKE HASH('SHA3-256', ?, 10);
    """.trimIndent()

    fun check(user: User): User =
        database.useConnection { connection ->
            connection.prepareStatement(preparedSql).use { statement ->
                statement.setString(1, user.login)
                statement.setString(2, user.password + salt)
                statement.executeQuery()
                    .asIterable()
                    .firstNotNullOf { row ->
                        try {
                            User(
                                row.getInt(1),
                                row.getTimestamp(2).toLocalDateTime(),
                                row.getString(3),
                                row.getString(4),
                                row.getString(5),
                                row.getInt(6)
                            )
                        } catch (_: NullPointerException) {
                            null
                        }
                    }
            }
        }
}
