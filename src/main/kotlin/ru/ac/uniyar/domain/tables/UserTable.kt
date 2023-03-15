package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserTable : Table<Nothing>("USERS") {
    val id = int("ID").primaryKey()
    val date = datetime("DATE_CREATION")
    val login = varchar("LOGIN")
    val fio = varchar("FIO")
    val password = varchar("PASSWORD")
    val roleId = int("ROLE_ID")
    val description = varchar("DESCRIPTION")
}
