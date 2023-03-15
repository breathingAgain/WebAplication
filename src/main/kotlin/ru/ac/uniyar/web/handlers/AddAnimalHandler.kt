package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.getFirst
import org.http4k.core.with
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.operations.AddAnimalInDatabase
import ru.ac.uniyar.web.filters.lensAddAnimal
import ru.ac.uniyar.web.models.AddAnimalVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender
import java.time.LocalDateTime

fun showNewAnimalForm(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canAddAnimal)
        Response(Status.UNAUTHORIZED)
    else
        Response(Status.OK).with(htmlView(request) of AddAnimalVM())
}

fun addAnimalPost(htmlView: ContextAwareViewRender, addAnimalInDatabase: AddAnimalInDatabase): HttpHandler = { request ->
    val form = lensAddAnimal(request)

    if (form.errors.isEmpty()) {
        val animal = Animal(
            0, LocalDateTime.now(), form.fields.getFirst("view")!!,
            form.fields.getFirst("name")!!, form.fields.getFirst("description")!!
        )
        addAnimalInDatabase.adding(animal)
        Response(Status.FOUND).header("location", "/animalList")
    } else {
        Response(Status.BAD_REQUEST).with(htmlView(request) of AddAnimalVM(form))
    }
}
