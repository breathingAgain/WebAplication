package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.update
import ru.ac.uniyar.domain.tables.AnimalTable

class EditAnimal(private val database: Database) {
    fun edit(animalId: Int, newView: String, newName: String, newDescription: String) =
        database
            .update(AnimalTable) {
                set(it.kind, newView)
                set(it.name, newName)
                set(it.description, newDescription)
                where { it.id eq animalId }
            }
}
