package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.desc
import org.ktorm.dsl.from
import org.ktorm.dsl.greaterEq
import org.ktorm.dsl.lessEq
import org.ktorm.dsl.like
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.orderBy
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.support.mysql.toLowerCase
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.tables.UserTable
import java.time.LocalDateTime

class ListUsers(private val database: Database) {
    fun list(page: Int, fio: String, dateFrom: LocalDateTime, dateTo: LocalDateTime) =
        database
            .from(UserTable)
            .select()
            .where {
                UserTable.date.greaterEq(dateFrom) and UserTable.date.lessEq(dateTo) and UserTable
                    .fio.toLowerCase().like("%$fio%")
            }
            .limit((page - 1) * 8, 8)
            .orderBy(UserTable.date.desc())
            .mapNotNull { row ->
                User.userFromDatabase(row)
            }
    fun count(fio: String, dateFrom: LocalDateTime, dateTo: LocalDateTime) =
        (
            database
                .from(UserTable)
                .select()
                .where {
                    UserTable.date.greaterEq(dateFrom) and UserTable.date.lessEq(dateTo) and UserTable
                        .fio.toLowerCase().like("%$fio%")
                }
                .totalRecords - 1
            ) / 8 + 1
}
