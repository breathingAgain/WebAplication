package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Animal
import ru.ac.uniyar.domain.Entry
import ru.ac.uniyar.domain.User

data class AnimalByIndexVM(
    val animal: Animal,
    val entryList: List<Entry>,
    val trusteeList: List<User>,
    val currentPage: Int,
    val countPage: Int,
    val isTrustedAnimal: Boolean = false,
    val countEntryForTrustee: Int
) : ViewModel
