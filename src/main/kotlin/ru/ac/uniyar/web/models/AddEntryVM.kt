package ru.ac.uniyar.web.models

import org.http4k.lens.WebForm
import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Animal

data class AddEntryVM(
    val animalList: List<Animal>,
    val animalId: Int = 1,
    val form: WebForm = WebForm(),
    val animalListError: Boolean = false
) : ViewModel
