package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Animal

data class StartPage(
    val countAnimals: Int,
    val countTrusteeAnimals: Int,
    val mustTrusteeAnimal: Animal,
    val listOfNoTrusteeAnimals: List<Animal>
) : ViewModel
