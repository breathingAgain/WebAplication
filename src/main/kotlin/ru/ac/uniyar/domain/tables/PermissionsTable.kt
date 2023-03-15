package ru.ac.uniyar.domain.tables

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object PermissionsTable : Table<Nothing>("PERMISSIONS") {
    val id = int("ID").primaryKey()
    val name = varchar("NAME")
    val canBecomeTrustee = varchar("CAN_BECOME_TRUSTEE")
    val canWatchEntryForTrustee = varchar("CAN_WATCH_ENTRY_BY_TRUSTEE")
    val canWatchTrustedAnimal = varchar("CAN_WATCH_TRUSTED_ANIMAL")
    val canWatchPersonalTape = varchar("CAN_WATCH_PERSONAL_TAPE")
    val canAddAnimal = varchar("CAN_ADD_ANIMAL")
    val canAddEntryAnimal = varchar("CAN_ADD_ENTRY_ANIMAL")
    val canWatchTrusteeByAnimal = varchar("CAN_WATCH_TRUSTEE_BY_ANIMAL")
    val canAddEntryAnimalForTrustee = varchar("CAN_ADD_ENTRY_FOR_TRUSTEE")
}
