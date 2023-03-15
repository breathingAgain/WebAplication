package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int

object TrusteeTable : Table<Nothing>("TRUSTEE_TO_ANIMAL") {
    val id = int("ID").primaryKey()
    val user_id = int("USER_ID")
    val animal_id = int("ANIMAL_ID")
    val date = datetime("DATE_CREATION")
}
