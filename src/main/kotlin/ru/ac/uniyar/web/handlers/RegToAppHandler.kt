package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.AddNewUser
import ru.ac.uniyar.domain.operations.CheckLoginOriginally
import ru.ac.uniyar.web.filters.lensRegUser
import ru.ac.uniyar.web.models.RegPage
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun regToApp(htmlView: ContextAwareViewRender): HttpHandler = { request ->
    Response(Status.OK).with(htmlView(request) of RegPage())
}

fun regToAppPost(
    htmlView: ContextAwareViewRender,
    addNewUser: AddNewUser,
    checkLoginOriginally: CheckLoginOriginally
): HttpHandler = { request ->
    val form = lensRegUser(request)

    if (form.errors.isEmpty()) {
        if (form.fields["password1"]!![0] != form.fields["password2"]!![0])
            Response(Status.BAD_REQUEST).with(htmlView(request) of RegPage(form, isDiffPassword = true))
        else {
            if (!checkLoginOriginally.check(form.fields["login"]!![0]))
                Response(Status.BAD_REQUEST).with(htmlView(request) of RegPage(form, isUnoriginalLogin = true))
            else {
                addNewUser.adding(
                    User(
                        null, null,
                        form.fields["login"]!![0],
                        form.fields["fio"]!![0],
                        form.fields["password1"]!![0], null
                    )
                )
                Response(Status.FOUND).header("location", "/login")
            }
        }
    } else
        Response(Status.BAD_REQUEST).with(htmlView(request) of RegPage(form))
}
