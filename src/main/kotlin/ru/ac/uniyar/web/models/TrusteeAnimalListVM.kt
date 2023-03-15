package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Animal

data class TrusteeAnimalListVM(
    val animalList: List<Animal>,
    val currentPage: Int,
    val countPage: Int,
    val form: WebForm = WebForm()
) : ViewModel
