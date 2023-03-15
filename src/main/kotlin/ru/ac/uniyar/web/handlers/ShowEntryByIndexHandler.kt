package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import ru.ac.uniyar.domain.operations.EntryByIndex
import ru.ac.uniyar.web.filters.pathLensByIndex
import ru.ac.uniyar.web.models.EntryVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showEntryByIndex(htmlView: ContextAwareViewRender, entryByIndex: EntryByIndex): HttpHandler = { request ->
    try {
        val entry = pathLensByIndex(request).let {
            try {
                entryByIndex.fetch(it)
            } catch (_: Exception) {
                throw LensFailure()
            }
        }
        val viewModel = EntryVM(entry)
        Response(Status.OK).with(htmlView(request) of viewModel)
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
