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
import ru.ac.uniyar.domain.operations.EditAnimal
import ru.ac.uniyar.web.filters.lensAddAnimal
import ru.ac.uniyar.web.filters.pathLensByIndex
import ru.ac.uniyar.web.models.EditAnimalVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showEditAnimalPage(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    animalByIndex: AnimalByIndex
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canAddAnimal)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val animal = pathLensByIndex(request).let { id ->
                try {
                    animalByIndex.fetch(id)
                } catch (_: Exception) {
                    throw LensFailure()
                }
            }
            val form = WebForm(
                mapOf(
                    Pair("view", listOf(animal.view)),
                    Pair("name", listOf(animal.name)), Pair("description", listOf(animal.description))
                )
            )
            Response(Status.OK).with(htmlView(request) of EditAnimalVM(form))
        } catch (_: LensFailure) {
            Response(Status.BAD_REQUEST)
        }
    }
}

fun editAnimalPost(htmlView: ContextAwareViewRender, editAnimal: EditAnimal): HttpHandler = { request ->
    val form = lensAddAnimal(request)

    try {
        val id = pathLensByIndex(request)
        if (form.errors.isEmpty()) {
            editAnimal.edit(
                id, form.fields.getFirst("view")!!,
                form.fields.getFirst("name")!!, form.fields.getFirst("description")!!
            )
            Response(Status.FOUND).header("location", "/animalList/$id")
        } else {
            Response(Status.BAD_REQUEST).with(htmlView(request) of EditAnimalVM(form))
        }
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
