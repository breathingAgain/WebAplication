package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.tables.AnimalTable

class AddAnimalInDatabase(private val database: Database) {
    fun adding(animal: Animal) =
        database.insertAndGenerateKey(AnimalTable) {
            set(it.date, animal.date)
            set(it.kind, animal.view)
            set(it.name, animal.name)
            set(it.description, animal.description)
        }
}
