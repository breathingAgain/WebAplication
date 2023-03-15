package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object AnimalTable : Table<Nothing>("ANIMALS") {
    val id = int("ID").primaryKey()
    val date = datetime("DATE_CREATION")
    val kind = varchar("KIND")
    val name = varchar("NAME")
    val description = varchar("DESCRIPTION")
}
