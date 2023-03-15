package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.cookie.invalidateCookie

fun logoutUser(): HttpHandler = {
    Response(Status.FOUND).header("location", "/").invalidateCookie("authToken")
}
