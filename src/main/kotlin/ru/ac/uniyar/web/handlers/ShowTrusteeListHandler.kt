package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import org.http4k.lens.WebForm
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.operations.ListUsers
import ru.ac.uniyar.web.filters.dateFromLensList
import ru.ac.uniyar.web.filters.dateToLensList
import ru.ac.uniyar.web.filters.fioLensList
import ru.ac.uniyar.web.filters.pageLens
import ru.ac.uniyar.web.models.TrusteeListVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showTrusteeList(
    htmlView: ContextAwareViewRender,
    permissionsLens: RequestContextLens<Permissions>,
    listUsers: ListUsers
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canWatchTrusteeByAnimal)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val page = pageLens(request)
            val fio = fioLensList(request).orEmpty().let { if (it.isNotEmpty()) fioLensList(request) else "%" }
            val dateFrom = makeDateFrom(request)
            val dateTo = makeDateTo(request)

            val trusteeList = listUsers.list(page, fio!!.lowercase(), dateFrom, dateTo)
            val viewModel = TrusteeListVM(
                trusteeList, currentPage = page,
                countPage = listUsers.count(fio, dateFrom, dateTo),
                form = WebForm(
                    fields = mapOf(
                        Pair("fio", listOf(fioLensList(request).orEmpty())),
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
}
