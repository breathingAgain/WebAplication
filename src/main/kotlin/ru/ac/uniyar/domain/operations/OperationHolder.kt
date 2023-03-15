package ru.ac.uniyar.domain.operations

import org.ktorm.database.Database

class OperationHolder(database: Database, salt: String) {
    val entryByAnimalNoTrustee = EntryByAnimalNoTrustee(database)
    val entryByIndex = EntryByIndex(database)
    val animalByIndex = AnimalByIndex(database)
    val listEntryForTrustee = ListEntryForTrustee(database)
    val listAnimals = ListAnimals(database)
    val addEntryInDatabase = AddEntryInDatabase(database)
    val addTrusteeInDatabase = AddTrusteeInDatabase(database)
    val addAnimalInDatabase = AddAnimalInDatabase(database)
    val infoAboutZoo = InfoAboutZoo(database)
    val addNewUser = AddNewUser(database, salt)
    val checkPasswordLoginUser = CheckPasswordLoginUser(database, salt)
    val checkLoginOriginally = CheckLoginOriginally(database)
    val userByLogin = UserByLogin(database)
    val permissionByUser = PermissionByUser(database)
    val editAnimal = EditAnimal(database)
    val editEntry = EditEntry(database)
    val deleteTrustAnimal = DeleteTrustAnimal(database)
    val userById = UserById(database)
    val entryByAnimalForTrustee = EntryByAnimalForTrustee(database)
    val trusteeAnimal = TrusteeAnimal(database)
    val entryByTrusteeAnimalCount = EntryByTrusteeAnimalCount(database)
    val listUsersByAnimal = ListUsersByAnimal(database)
    val listUsers = ListUsers(database)
    val editDescriptionUser = EditDescriptionUser(database)
}
