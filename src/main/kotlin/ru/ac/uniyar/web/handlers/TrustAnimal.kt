package ru.ac.uniyar.web.handlers

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.lens.RequestContextLens
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.AddTrusteeInDatabase
import ru.ac.uniyar.domain.operations.DeleteTrustAnimal
import ru.ac.uniyar.web.filters.pathLensByIndex

fun trustAnimal(
    permissionsLens: RequestContextLens<Permissions>,
    addTrusteeInDatabase: AddTrusteeInDatabase,
    userLens: RequestContextLens<User?>
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canBecomeTrustee)
        Response(Status.UNAUTHORIZED)
    else {
        val user = userLens(request)!!
        val animalId = pathLensByIndex(request)
        if (addTrusteeInDatabase.checkTrust(user.id!!, animalId))
            addTrusteeInDatabase.adding(user.id, animalId)
        Response(Status.FOUND).header("location", "/animalList/$animalId")
    }
}

fun noTrustAnimal(
    permissionsLens: RequestContextLens<Permissions>,
    deleteTrustAnimal: DeleteTrustAnimal,
    userLens: RequestContextLens<User?>
): HttpHandler = { request ->
    val permission = permissionsLens(request)
    if (!permission.canBecomeTrustee)
        Response(Status.UNAUTHORIZED)
    else {
        val user = userLens(request)!!
        val animalId = pathLensByIndex(request)
        println(animalId)
        deleteTrustAnimal.delete(user.id!!, animalId)
        Response(Status.FOUND).header("location", "/animalList/$animalId")
    }
}
