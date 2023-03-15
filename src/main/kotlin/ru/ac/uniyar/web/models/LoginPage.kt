package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

data class LoginPage(val form: WebForm = WebForm(), val isInCorrectLoginOrPassword: Boolean = false) : ViewModel
