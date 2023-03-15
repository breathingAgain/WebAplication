package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.operations.InfoAboutZoo
import ru.ac.uniyar.web.models.StartPage
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun showStartPage(htmlView: ContextAwareViewRender, infoAboutZoo: InfoAboutZoo): HttpHandler = { request ->
    val countAnimals = infoAboutZoo.countAnimals()
    val countTrusteeAnimals = infoAboutZoo.countTrusteeAnimals()
    val mustTrusteeAnimal = infoAboutZoo.mustTrusteeAnimal()
    val noTrusteeAnimal = infoAboutZoo.noTrusteeAnimal()

    val viewModel = StartPage(countAnimals, countTrusteeAnimals, mustTrusteeAnimal, noTrusteeAnimal)
    Response(Status.OK).with(htmlView(request) of viewModel)
}
