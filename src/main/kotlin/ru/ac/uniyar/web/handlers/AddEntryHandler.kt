package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.getFirst
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.operations.AddEntryInDatabase
import ru.ac.uniyar.domain.operations.AnimalByIndex
import ru.ac.uniyar.domain.operations.ListAnimals
import ru.ac.uniyar.web.filters.animalIdLens
import ru.ac.uniyar.web.filters.lensAddEntry
import ru.ac.uniyar.web.models.AddEntryVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender
import java.time.LocalDateTime

fun showNewEntryForm(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    listAnimals: ListAnimals
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canAddAnimal)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val id = animalIdLens(request)
            val viewModel = AddEntryVM(listAnimals.listNoLimit(), id)
            Response(OK).with(htmlView(request) of viewModel)
        } catch (_: LensFailure) {
            Response(Status.BAD_REQUEST)
        }
    }
}

fun addEntryPost(
    htmlView: ContextAwareViewRender,
    listAnimals: ListAnimals,
    animalByIndex: AnimalByIndex,
    addEntryInDatabase: AddEntryInDatabase
): HttpHandler = { request ->
    val form = lensAddEntry(request)
    if (form.errors.isEmpty()) {
        try {
            val entry = Entry(
                0, LocalDateTime.now(),
                animalByIndex.fetch(form.fields.getFirst("animal")!!.toInt()),
                form.fields.getFirst("text")!!,
                (form.fields.getFirst("forTrustee") ?: "false").toBoolean()
            )
            addEntryInDatabase.adding(entry)
            Response(FOUND).header("location", "/entryList")
        } catch (_: NoSuchElementException) {
            Response(Status.BAD_REQUEST)
                .with(
                    htmlView(request) of AddEntryVM(
                        listAnimals.listNoLimit(),
                        form = form, animalListError = true
                    )
                )
        }
    } else {
        Response(Status.BAD_REQUEST)
            .with(htmlView(request) of AddEntryVM(listAnimals.listNoLimit(), form = form))
    }
}
