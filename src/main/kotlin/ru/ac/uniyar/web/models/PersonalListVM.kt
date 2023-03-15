package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entry

data class PersonalListVM(
    val entryList: List<Entry>,
    val currentPage: Int,
    val countPage: Int,
    val form: WebForm = WebForm()
) : ViewModel
