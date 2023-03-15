package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.Cookie
import org.http4k.core.cookie.SameSite
import org.http4k.core.cookie.cookie
import org.http4k.core.cookie.invalidateCookie
import org.http4k.core.with
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.CheckPasswordLoginUser
import ru.ac.uniyar.web.filters.JwtTools
import ru.ac.uniyar.web.filters.lensAuthUser
import ru.ac.uniyar.web.models.LoginPage
import ru.ac.uniyar.web.templates.ContextAwareViewRender

fun loginToApp(htmlView: ContextAwareViewRender): HttpHandler = { request ->
    Response(Status.OK).with(htmlView(request) of LoginPage())
}

fun loginToAppPost(
    htmlView: ContextAwareViewRender,
    checkPasswordLoginUser: CheckPasswordLoginUser,
    jwtTools: JwtTools
): HttpHandler = { request ->
    val form = lensAuthUser(request)

    if (form.errors.isEmpty()) {
        try {
            val user = checkPasswordLoginUser.check(
                User(
                    null, null,
                    form.fields["login"]!![0], "", form.fields["password"]!![0], null
                )
            )
            val token = jwtTools.create(user.login)
            if (token.isNullOrEmpty())
                Response(Status.INTERNAL_SERVER_ERROR)
            else {
                val authToken = Cookie("authToken", token, httpOnly = true, sameSite = SameSite.Strict)
                Response(Status.FOUND).header("location", "/").invalidateCookie("token").cookie(authToken)
            }
        } catch (_: NoSuchElementException) {
            Response(Status.BAD_REQUEST).with(htmlView(request) of LoginPage(form, true))
        }
    } else
        Response(Status.BAD_REQUEST).with(htmlView(request) of LoginPage(form))
}
