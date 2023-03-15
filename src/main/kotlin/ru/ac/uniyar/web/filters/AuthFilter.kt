package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.cookie.cookie
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.UserByLogin

fun authFilter(currentUser: BiDiLens<Request, User?>, jwtTools: JwtTools, userByLogin: UserByLogin) = Filter { next ->
    { request ->
        val requestWithUser = request.cookie("authToken")?.value?.let { token ->
            jwtTools.subject(token)
        }?.let { login ->
            userByLogin.fetch(login)
        }.let { user ->
            request.with(currentUser of user)
        } ?: request
        next(requestWithUser)
    }
}
