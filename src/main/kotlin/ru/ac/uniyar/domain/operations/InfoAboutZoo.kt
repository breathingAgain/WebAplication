package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.innerJoin
import org.ktorm.dsl.isNull
import org.ktorm.dsl.leftJoin
import org.ktorm.dsl.limit
import org.ktorm.dsl.mapNotNull
import org.ktorm.dsl.select
import org.ktorm.dsl.selectDistinct
import org.ktorm.dsl.where
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.tables.AnimalTable
import ru.ac.uniyar.domain.tables.TrusteeTable

class InfoAboutZoo(private val database: Database) {
    fun countAnimals() =
        database
            .from(AnimalTable)
            .select()
            .totalRecords

    fun countTrusteeAnimals() =
        database
            .from(AnimalTable)
            .innerJoin(TrusteeTable, on = AnimalTable.id eq TrusteeTable.animal_id)
            .selectDistinct(AnimalTable.id)
            .totalRecords

    fun mustTrusteeAnimal(): Animal {
        val animals = database
            .from(AnimalTable)
            .innerJoin(TrusteeTable, on = AnimalTable.id eq TrusteeTable.animal_id)
            .select(AnimalTable.id)
            .mapNotNull { row -> row[AnimalTable.id] }

        val map: MutableMap<Int, Int> = mutableMapOf()
        var mustTrusteeAnimalCount = 0
        var mustTrusteeAnimalId = 0
        for (animalId in animals)
            map[animalId] = map.getOrDefault(animalId, 0) + 1
        for (animalId in map.keys) {
            if (mustTrusteeAnimalCount < map[animalId]!!) {
                mustTrusteeAnimalCount = map[animalId]!!
                mustTrusteeAnimalId = animalId
            }
        }
        return database
            .from(AnimalTable)
            .select()
            .where { AnimalTable.id eq mustTrusteeAnimalId }
            .mapNotNull { row ->
                Animal(
                    row[AnimalTable.id]!!,
                    row[AnimalTable.date]!!,
                    row[AnimalTable.kind]!!,
                    row[AnimalTable.name]!!,
                    row[AnimalTable.description]!!
                )
            }.first()
    }

    fun noTrusteeAnimal() =
        database
            .from(AnimalTable)
            .leftJoin(TrusteeTable, on = AnimalTable.id eq TrusteeTable.animal_id)
            .select()
            .where { TrusteeTable.id.isNull() }
            .limit(5)
            .mapNotNull { row ->
                Animal(
                    row[AnimalTable.id]!!,
                    row[AnimalTable.date]!!,
                    row[AnimalTable.kind]!!,
                    row[AnimalTable.name]!!,
                    row[AnimalTable.description]!!
                )
            }
}
