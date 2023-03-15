package ru.ac.uniyar.web.filters

import org.http4k.core.Filter
import org.http4k.core.Request
import org.http4k.core.with
import org.http4k.lens.BiDiLens
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.operations.PermissionByUser

fun permissionFilter(
    currentUser: BiDiLens<Request, User?>,
    currentPermissions: BiDiLens<Request, Permissions>,
    permissionByUser: PermissionByUser
) = Filter { next ->
    { request ->
        val permissions = currentUser(request)?.let { user ->
            permissionByUser.fetch(user)
        } ?: Permissions.guestRole
        next(request.with(currentPermissions of permissions))
    }
}
