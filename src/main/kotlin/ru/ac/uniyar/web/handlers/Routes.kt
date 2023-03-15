package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.lens.RequestContextLens
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.AddAnimalInDatabase
import ru.ac.uniyar.domain.operations.AddEntryInDatabase
import ru.ac.uniyar.domain.operations.AddNewUser
import ru.ac.uniyar.domain.operations.AddTrusteeInDatabase
import ru.ac.uniyar.domain.operations.AnimalByIndex
import ru.ac.uniyar.domain.operations.CheckLoginOriginally
import ru.ac.uniyar.domain.operations.CheckPasswordLoginUser
import ru.ac.uniyar.domain.operations.DeleteTrustAnimal
import ru.ac.uniyar.domain.operations.EditAnimal
import ru.ac.uniyar.domain.operations.EditDescriptionUser
import ru.ac.uniyar.domain.operations.EditEntry
import ru.ac.uniyar.domain.operations.EntryByAnimalForTrustee
import ru.ac.uniyar.domain.operations.EntryByAnimalNoTrustee
import ru.ac.uniyar.domain.operations.EntryByIndex
import ru.ac.uniyar.domain.operations.EntryByTrusteeAnimalCount
import ru.ac.uniyar.domain.operations.InfoAboutZoo
import ru.ac.uniyar.domain.operations.ListAnimals
import ru.ac.uniyar.domain.operations.ListEntryForTrustee
import ru.ac.uniyar.domain.operations.ListUsers
import ru.ac.uniyar.domain.operations.ListUsersByAnimal
import ru.ac.uniyar.domain.operations.TrusteeAnimal
import ru.ac.uniyar.domain.operations.UserById
import ru.ac.uniyar.web.filters.JwtTools
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun app(
    htmlView: ContextAwareViewRender,
    jwtTools: JwtTools,
    permissionsLens: RequestContextLens<Permissions>,
    userLens: RequestContextLens<User?>,
    entryByAnimalNoTrustee: EntryByAnimalNoTrustee,
    entryByAnimalForTrustee: EntryByAnimalForTrustee,
    entryByIndex: EntryByIndex,
    animalByIndex: AnimalByIndex,
    listEntryForTrustee: ListEntryForTrustee,
    listAnimals: ListAnimals,
    addEntryInDatabase: AddEntryInDatabase,
    addTrusteeInDatabase: AddTrusteeInDatabase,
    addAnimalInDatabase: AddAnimalInDatabase,
    infoAboutZoo: InfoAboutZoo,
    addNewUser: AddNewUser,
    checkPasswordLoginUser: CheckPasswordLoginUser,
    checkLoginOriginally: CheckLoginOriginally,
    editAnimal: EditAnimal,
    editEntry: EditEntry,
    deleteTrustAnimal: DeleteTrustAnimal,
    trusteeAnimal: TrusteeAnimal,
    entryByTrusteeAnimalCount: EntryByTrusteeAnimalCount,
    listUsersByAnimal: ListUsersByAnimal,
    userById: UserById,
    listUsers: ListUsers,
    editDescriptionUser: EditDescriptionUser
): HttpHandler = routes(
    "/entryList/{id}/edit" bind GET to showEditEntryPage(htmlView, permissionsLens, listAnimals, entryByIndex),
    "/entryList/{id}/edit" bind POST to editEntryPost(htmlView, listAnimals, editEntry, animalByIndex),
    "/entryList/{id}" bind GET to showEntryByIndex(htmlView, entryByIndex),
    "/entryList" bind GET to showEntryList(htmlView, userLens, permissionsLens, listEntryForTrustee),
    "/animalList/{id}/trustAnimal" bind GET to trustAnimal(permissionsLens, addTrusteeInDatabase, userLens),
    "/animalList/{id}/noTrustAnimal" bind GET to noTrustAnimal(permissionsLens, deleteTrustAnimal, userLens),
    "/animalList/{id}/edit" bind GET to showEditAnimalPage(htmlView, permissionsLens, animalByIndex),
    "/animalList/{id}/edit" bind POST to editAnimalPost(htmlView, editAnimal),
    "/animalList/{id}" bind GET to showAnimalByIndex(
        htmlView, permissionsLens, animalByIndex,
        entryByAnimalNoTrustee, entryByAnimalForTrustee, listUsersByAnimal, addTrusteeInDatabase,
        userLens, entryByTrusteeAnimalCount
    ),
    "/animalList" bind GET to showAnimalList(htmlView, listAnimals),
    "/trusteeList/{id}" bind GET to showTrusteeByIndex(htmlView, permissionsLens, userById),
    "/trusteeList" bind GET to showTrusteeList(htmlView, permissionsLens, listUsers),
    "/trusteeAnimal" bind GET to showTrusteeAnimalPage(htmlView, userLens, permissionsLens, trusteeAnimal),
    "/editDescription" bind GET to showEditDescriptionPage(htmlView, permissionsLens, userLens),
    "/editDescription" bind POST to editDescription(htmlView, userLens, editDescriptionUser),
    "/newEntry" bind GET to showNewEntryForm(htmlView, permissionsLens, listAnimals),
    "/newEntry" bind POST to addEntryPost(htmlView, listAnimals, animalByIndex, addEntryInDatabase),
    "/newAnimal" bind GET to showNewAnimalForm(htmlView, permissionsLens),
    "/newAnimal" bind POST to addAnimalPost(htmlView, addAnimalInDatabase),
    "/login" bind GET to loginToApp(htmlView),
    "/login" bind POST to loginToAppPost(htmlView, checkPasswordLoginUser, jwtTools),
    "/logout" bind GET to logoutUser(),
    "/registration" bind GET to regToApp(htmlView),
    "/registration" bind POST to regToAppPost(htmlView, addNewUser, checkLoginOriginally),
    "" bind GET to showStartPage(htmlView, infoAboutZoo),

    static(ResourceLoader.Classpath("/ru/ac/uniyar/public/")),

)
