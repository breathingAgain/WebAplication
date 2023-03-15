package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.with
import ru.ac.uniyar.web.models.ErrorPageVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun badRouteFilter(htmlView: ContextAwareViewRender) = Filter { next: HttpHandler ->
    { request: Request ->
        val response = next(request)
        if (response.status.successful || (response.status.code == 400 && response.body.length != 0L)) {
            response
        } else {
            response.with(htmlView(request) of ErrorPageVM(request.uri))
        }
    }
}
