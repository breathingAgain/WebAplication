package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.WebForm
import ru.ac.uniyar.domain.operations.ListAnimals
import ru.ac.uniyar.web.filters.dateFromLensList
import ru.ac.uniyar.web.filters.dateToLensList
import ru.ac.uniyar.web.filters.pageLens
import ru.ac.uniyar.web.filters.viewLensList
import ru.ac.uniyar.web.models.AnimalsListVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showAnimalList(htmlView: ContextAwareViewRender, listAnimals: ListAnimals): HttpHandler = { request ->
    try {
        val page = pageLens(request)
        val view = viewLensList(request).orEmpty().let { if (it.isNotEmpty()) viewLensList(request) else "%" }
        val dateFrom = makeDateFrom(request)
        val dateTo = makeDateTo(request)

        val animalList = listAnimals.list(page, view!!.lowercase(), dateFrom, dateTo)
        val viewModel = AnimalsListVM(
            animalList, currentPage = page,
            countPage = listAnimals.count(view.lowercase(), dateFrom, dateTo),
            form = WebForm(
                fields = mapOf(
                    Pair("view", listOf(viewLensList(request).orEmpty())),
                    Pair("dateFrom", listOf(dateFromLensList(request).orEmpty())),
                    Pair("dateTo", listOf(dateToLensList(request).orEmpty()))
                )
            )
        )
        Response(Status.OK).with(htmlView(request) of viewModel)
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
