package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.User

data class TrusteeListVM(
    val trusteeList: List<User>,
    val currentPage: Int,
    val countPage: Int,
    val form: WebForm = WebForm()
) : ViewModel
