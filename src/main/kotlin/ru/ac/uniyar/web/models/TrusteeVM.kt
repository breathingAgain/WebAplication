package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.User

data class TrusteeVM(val user: User) : ViewModel
