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
import ru.ac.uniyar.domain.operations.TrusteeAnimal
import ru.ac.uniyar.web.filters.dateFromLensList
import ru.ac.uniyar.web.filters.dateToLensList
import ru.ac.uniyar.web.filters.pageLens
import ru.ac.uniyar.web.filters.viewLensList
import ru.ac.uniyar.web.models.TrusteeAnimalListVM
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showTrusteeAnimalPage(
    htmlView: ContextAwareViewRender,
    userLens: RequestContextLens<User?>,
    permissionsLens: RequestContextLens<Permissions>,
    trusteeAnimal: TrusteeAnimal
): HttpHandler = { request ->
    try {
        val permission = permissionsLens(request)
        if (!permission.canWatchTrustedAnimal) {
            Response(Status.UNAUTHORIZED)
        } else {
            val user = userLens(request)!!
            val page = pageLens(request)
            val view = viewLensList(request).orEmpty().let { if (it.isNotEmpty()) viewLensList(request) else "%" }
            val dateFrom = makeDateFrom(request)
            val dateTo = makeDateTo(request)

            val animalList = trusteeAnimal.list(user.id!!, page, view!!.lowercase(), dateFrom, dateTo)
            val viewModel = TrusteeAnimalListVM(
                animalList, currentPage = page,
                countPage = trusteeAnimal.count(user.id, view.lowercase(), dateFrom, dateTo),
                form = WebForm(
                    fields = mapOf(
                        Pair("view", listOf(viewLensList(request).orEmpty())),
                        Pair("dateFrom", listOf(dateFromLensList(request).orEmpty())),
                        Pair("dateTo", listOf(dateToLensList(request).orEmpty()))
                    )
                )
            )
            Response(Status.OK).with(htmlView(request) of viewModel)
        }
    } catch (_: LensFailure) {
        Response(Status.BAD_REQUEST)
    }
}
