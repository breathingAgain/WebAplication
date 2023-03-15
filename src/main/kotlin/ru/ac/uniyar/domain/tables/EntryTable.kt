package ru.ac.uniyar.domain.tables

import org.ktorm.schema.*

object EntryTable : Table<Nothing>("ENTRY") {
    val id = int("ID").primaryKey()
    val date = datetime("DATE_CREATION")
    val animal_id = int("ANIMAL_ID")
    val text = varchar("TEXT")
    val for_trustee = boolean("FOR_TRUSTEE")
}
