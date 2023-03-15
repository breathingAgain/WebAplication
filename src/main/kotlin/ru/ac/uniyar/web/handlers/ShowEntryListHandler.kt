package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.RequestContextLens
import org.http4k.lens.WebForm
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.ListEntryForTrustee
import ru.ac.uniyar.web.filters.dateFromLensList
import ru.ac.uniyar.web.filters.dateToLensList
import ru.ac.uniyar.web.filters.pageLens
import ru.ac.uniyar.web.filters.viewLensList
import ru.ac.uniyar.web.models.PersonalListVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showEntryList(
    htmlView: ContextAwareViewRender,
    userLens: RequestContextLens<User?>,
    permissionsLens: RequestContextLens<Permissions>,
    listEntryForTrustee: ListEntryForTrustee
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canWatchPersonalTape)
        Response(Status.UNAUTHORIZED)
    else {
        try {
            val user = userLens(request)!!
            val page = pageLens(request)
            val view = viewLensList(request).orEmpty().let { if (it.isNotEmpty()) viewLensList(request) else "%" }
            val dateFrom = makeDateFrom(request)
            val dateTo = makeDateTo(request)

            val entryList = listEntryForTrustee.list(page, view!!.lowercase(), dateFrom, dateTo, user.id!!)
            val viewModel = PersonalListVM(
                entryList, currentPage = page,
                countPage = listEntryForTrustee.count(view.lowercase(), dateFrom, dateTo, user.id),
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
}
