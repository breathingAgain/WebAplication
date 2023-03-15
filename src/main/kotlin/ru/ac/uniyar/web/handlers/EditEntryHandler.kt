package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.getFirst
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import org.http4k.lens.WebForm
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.operations.AnimalByIndex
import ru.ac.uniyar.domain.operations.EditEntry
import ru.ac.uniyar.domain.operations.EntryByIndex
import ru.ac.uniyar.domain.operations.ListAnimals
import ru.ac.uniyar.web.filters.lensAddEntry
import ru.ac.uniyar.web.filters.pathLensByIndex
import ru.ac.uniyar.web.models.EditEntryVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showEditEntryPage(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    listAnimals: ListAnimals,
    entryByIndex: EntryByIndex
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canAddEntryAnimal)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val entry = pathLensByIndex(request).let { id ->
                try {
                    entryByIndex.fetch(id)
                } catch (_: Exception) {
                    throw LensFailure()
                }
            }
            val form = WebForm(
                mapOf(
                    Pair("text", listOf(entry.text)),
                    Pair("animal", listOf(entry.animal.id.toString())),
                    Pair("forTrustee", listOf(entry.forTrustee.toString()))
                )
            )
            Response(Status.OK)
                .with(htmlView(request) of EditEntryVM(form, listAnimals.listNoLimit()))
        } catch (_: LensFailure) {
            Response(Status.BAD_REQUEST)
        }
    }
}

fun editEntryPost(
    htmlView: ContextAwareViewRender,
    listAnimals: ListAnimals,
    editEntry: EditEntry,
    animalByIndex: AnimalByIndex
): HttpHandler = { request ->
    val form = lensAddEntry(request)
    try {
        val id = pathLensByIndex(request)
        if (form.errors.isEmpty()) {
            animalByIndex.fetch(form.fields.getFirst("animal")!!.toInt())
            editEntry.edit(
                id, form.fields.getFirst("text")!!,
                form.fields.getFirst("animal")!!.toInt(),
                (form.fields.getFirst("forTrustee") ?: "false").toBoolean()
            )
            Response(Status.FOUND).header("location", "/entryList/$id")
        } else {
            Response(Status.BAD_REQUEST).with(htmlView(request) of EditEntryVM(form, listAnimals.listNoLimit()))
        }
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (_: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
            .with(
                htmlView(request) of EditEntryVM(
                    form,
                    listAnimals.listNoLimit(),
                    animalListError = true
                )
            )
    }
}
