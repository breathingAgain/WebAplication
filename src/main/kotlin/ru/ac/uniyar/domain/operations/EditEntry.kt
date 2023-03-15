package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.tables.EntryTable

class EditEntry(private val database: Database) {
    fun edit(id: Int, newText: String, newIdAnimal: Int, newForTrustee: Boolean) =
        database
            .update(EntryTable) {
                set(it.text, newText)
                set(it.animal_id, newIdAnimal)
                set(it.for_trustee, newForTrustee)
                where { it.id eq id }
            }
}
