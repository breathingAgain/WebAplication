package ru.ac.uniyar.domain

data class Permissions(
    val id: Int,
    val name: String,
    val canBecomeTrustee: Boolean,
    val canWatchEntryForTrustee: Boolean,
    val canWatchTrustedAnimal: Boolean,
    val canWatchPersonalTape: Boolean,

    val canAddAnimal: Boolean,
    val canAddEntryAnimal: Boolean,
    val canWatchTrusteeByAnimal: Boolean,
    val canAddEntryAnimalForTrustee: Boolean
) {
    companion object {
        val guestRole = Permissions(
            1, "Гость",
            canBecomeTrustee = false,
            canWatchEntryForTrustee = false,
            canWatchTrustedAnimal = false,
            canWatchPersonalTape = false,
            canAddAnimal = false,
            canAddEntryAnimal = false,
            canWatchTrusteeByAnimal = false,
            canAddEntryAnimalForTrustee = false
        )
    }
}
