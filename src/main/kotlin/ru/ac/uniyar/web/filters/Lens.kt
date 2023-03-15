package ru.ac.uniyar.web.filters

import org.http4k.core.Body
import org.http4k.lens.FormField
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.Validator
import org.http4k.lens.boolean
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string
import org.http4k.lens.webForm

val lensAddAnimal1 = FormField.nonEmptyString().required("view", "Вид животного")
val lensAddAnimal2 = FormField.nonEmptyString().required("name", "Имя животного")
val lensAddAnimal3 = FormField.nonEmptyString().required("description", "Описание животного")
val lensAddAnimal = Body.webForm(Validator.Feedback, lensAddAnimal1, lensAddAnimal2, lensAddAnimal3).toLens()

val animalIdLens = Query.int().defaulted("animal_id", 1)
val lensAddEntry1 = FormField.nonEmptyString().required("text", "Текст блога")
val lensAddEntry2 = FormField.int().required("animal", "Животное про которое написан блог")
val lensAddEntry3 = FormField.boolean()
    .defaulted("forTrustee", false, "Пишется блог только для опекунов, либо для всех")
val lensAddEntry = Body.webForm(Validator.Feedback, lensAddEntry1, lensAddEntry2, lensAddEntry3).toLens()

val lensAddTrustee2 = FormField.nonEmptyString().defaulted("description", "", "Описание опекателя")
val lensAddTrustee = Body.webForm(Validator.Feedback, lensAddTrustee2).toLens()

val pathLensByIndex = Path.int().of("id")
val pageLens = Query.int().defaulted("page", 1)
val viewLensList = Query.string().optional("view")
val fioLensList = Query.string().optional("fio")
val dateFromLensList = Query.string().optional("dateFrom")
val dateToLensList = Query.string().optional("dateTo")

val loginLensReg = FormField.nonEmptyString().required("login", "ФИО пользователя")
val fioLensReg = FormField.nonEmptyString().required("fio", "ФИО пользователя")
val password1LensReg = FormField.nonEmptyString()
    .required("password1", "Пароль пользователя")
val password2LensReg = FormField.nonEmptyString()
    .required("password2", "Повторный ввод пароля пользователя")
val lensRegUser = Body.webForm(Validator.Feedback, loginLensReg, fioLensReg, password1LensReg, password2LensReg).toLens()

val passwordLensAuth = FormField.nonEmptyString()
    .required("password", "Пароль пользователя")
val lensAuthUser = Body.webForm(Validator.Feedback, loginLensReg, passwordLensAuth).toLens()
