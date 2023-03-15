package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.operations.UserById
import ru.ac.uniyar.web.filters.pathLensByIndex
import ru.ac.uniyar.web.models.TrusteeVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showTrusteeByIndex(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    userById: UserById
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canWatchTrusteeByAnimal)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val trustee = pathLensByIndex(request).let {
                try {
                    userById.fetch(it)
                } catch (_: Exception) {
                    throw LensFailure()
                }
            }
            val viewModel = TrusteeVM(trustee)
            Response(Status.OK).with(htmlView(request) of viewModel)
        } catch (_: LensFailure) {
            Response(Status.BAD_REQUEST)
        }
    }
}
