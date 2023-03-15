package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.AddTrusteeInDatabase
import ru.ac.uniyar.domain.operations.AnimalByIndex
import ru.ac.uniyar.domain.operations.EntryByAnimalForTrustee
import ru.ac.uniyar.domain.operations.EntryByAnimalNoTrustee
import ru.ac.uniyar.domain.operations.EntryByTrusteeAnimalCount
import ru.ac.uniyar.domain.operations.ListUsersByAnimal
import ru.ac.uniyar.web.filters.pageLens
import ru.ac.uniyar.web.filters.pathLensByIndex
import ru.ac.uniyar.web.models.AnimalByIndexVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showAnimalByIndex(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    animalByIndex: AnimalByIndex,
    entryByAnimalNoTrustee: EntryByAnimalNoTrustee,
    entryByAnimalForTrustee: EntryByAnimalForTrustee,
    listUsersByAnimal: ListUsersByAnimal,
    addTrusteeInDatabase: AddTrusteeInDatabase,
    userLens: RequestContextLens<User?>,
    entryByTrusteeAnimalCount: EntryByTrusteeAnimalCount
): HttpHandler = { request ->
    val page = pageLens(request)
    try {
        val id = pathLensByIndex(request).let {
            try {
                animalByIndex.fetch(it)
                it
            } catch (_: Exception) {
                throw LensFailure()
            }
        }
        val isTrustedAnimal = userLens(request)?.let {
            !addTrusteeInDatabase.checkTrust(it.id!!, id)
        } ?: false

        val permission = permissionsLens(request)
        val userId = userLens(request)!!.id!!
        val listEntry = if (permission.canWatchEntryForTrustee) entryByAnimalForTrustee.list(id, page, userId)
        else entryByAnimalNoTrustee.list(id, page)
        val countPage = if (permission.canWatchEntryForTrustee) entryByAnimalForTrustee.count(id, userId)
        else entryByAnimalNoTrustee.count(id)

        val viewModel = AnimalByIndexVM(
            animalByIndex.fetch(id),
            listEntry, listUsersByAnimal.list(id), currentPage = page,
            countPage = countPage, isTrustedAnimal, entryByTrusteeAnimalCount.count(id)
        )
        Response(Status.OK).with(htmlView(request) of viewModel)
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
