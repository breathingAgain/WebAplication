package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Animal

data class EditEntryVM(
    val form: WebForm = WebForm(),
    val animalList: List<Animal>,
    val animalId: Int = 1,
    val animalListError: Boolean = false
) : ViewModel
