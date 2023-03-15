package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.tables.AnimalTable

class AnimalByIndex(private val database: Database) {
    fun fetch(animalId: Int): Animal =
        database
            .from(AnimalTable)
            .select()
            .where { AnimalTable.id eq animalId }
            .mapNotNull { row ->
                Animal.animalFromDatabase(row)
            }.first()
}
