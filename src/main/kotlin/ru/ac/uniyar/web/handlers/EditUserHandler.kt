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
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.EditDescriptionUser
import ru.ac.uniyar.web.filters.lensAddTrustee
import ru.ac.uniyar.web.models.EditUserVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showEditDescriptionPage(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    userLens: RequestContextLens<User?>
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canWatchTrustedAnimal)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val user = userLens(request)!!
            val form = WebForm(mapOf(Pair("description", listOf(user.description ?: ""))))
            Response(Status.OK)
                .with(htmlView(request) of EditUserVM(form))
        } catch (_: LensFailure) {
            Response(Status.BAD_REQUEST)
        }
    }
}

fun editDescription(
    htmlView: ContextAwareViewRender,
    userLens: RequestContextLens<User?>,
    editDescriptionUser: EditDescriptionUser
): HttpHandler = { request ->
    val form = lensAddTrustee(request)
    try {
        if (form.errors.isEmpty()) {
            val user = userLens(request)!!
            editDescriptionUser.edit(user.id!!, form.fields.getFirst("description") ?: "")
            Response(Status.FOUND).header("location", "/")
        } else {
            Response(Status.BAD_REQUEST).with(htmlView(request) of EditUserVM(form))
        }
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    } catch (_: NoSuchElementException) {
        Response(Status.BAD_REQUEST)
    }
}
