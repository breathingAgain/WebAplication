package ru.ac.uniyar.web.models

import org.http4k.template.ViewModel
import ru.ac.uniyar.domain.Entry

data class EntryVM(val entry: Entry) : ViewModel
