package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.tables.PermissionsTable

class PermissionByUser(private val database: Database) {
    fun fetch(user: User): Permissions? =
        database
            .from(PermissionsTable)
            .select()
            .where { user.role_id!! eq PermissionsTable.id }
            .mapNotNull { row ->
                try {
                    Permissions(
                        row[PermissionsTable.id]!!,
                        row[PermissionsTable.name]!!,
                        row[PermissionsTable.canBecomeTrustee]!!.toBoolean(),
                        row[PermissionsTable.canWatchEntryForTrustee]!!.toBoolean(),
                        row[PermissionsTable.canWatchTrustedAnimal]!!.toBoolean(),
                        row[PermissionsTable.canWatchPersonalTape]!!.toBoolean(),
                        row[PermissionsTable.canAddAnimal]!!.toBoolean(),
                        row[PermissionsTable.canAddEntryAnimal]!!.toBoolean(),
                        row[PermissionsTable.canWatchTrusteeByAnimal]!!.toBoolean(),
                        row[PermissionsTable.canAddEntryAnimalForTrustee]!!.toBoolean(),
                    )
                } catch (_: NullPointerException) {
                    null
                }
            }.firstOrNull()
}
