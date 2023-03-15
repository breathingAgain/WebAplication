package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel

data class RegPage(
    val form: WebForm = WebForm(),
    val isDiffPassword: Boolean = false,
    val isUnoriginalLogin: Boolean = false
) : ViewModel
