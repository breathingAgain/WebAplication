package ru.ac.uniyar

import org.flywaydb.core.api.FlywayException
import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.RequestContexts
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.lens.RequestContextKey
import org.http4k.lens.RequestContextLens
import org.http4k.server.Http4kServer
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.ktorm.database.Database
import ru.ac.uniyar.domain.Permissions
import ru.ac.uniyar.domain.User
import ru.ac.uniyar.domain.connectToDatabase
import ru.ac.uniyar.domain.operations.OperationHolder
import ru.ac.uniyar.services.H2DatabaseManager
import ru.ac.uniyar.services.performMigrations
import ru.ac.uniyar.web.filters.JwtTools
import ru.ac.uniyar.web.filters.authFilter
import ru.ac.uniyar.web.filters.badRouteFilter
import ru.ac.uniyar.web.filters.permissionFilter
import ru.ac.uniyar.web.handlers.app
import ru.ac.uniyar.web.templates.ContextAwarePebbleTemplates
import ru.ac.uniyar.web.templates.ContextAwareViewRender

const val PORT = 9000
const val SALT = "eu9dIS3ZCj%D89kkk%S092SFOLp#hzf0p%42YkqSUfNMChf2ry&^I5XUS1F289ca7%oW4NG8i5IDYTmWN9WxBDxCCLl%Lst0%mhq"

fun startApplication(database: Database): Http4kServer {
    val renderer = ContextAwarePebbleTemplates().HotReload("src/main/resources")
    val htmlView = ContextAwareViewRender.withContentType(renderer, ContentType.TEXT_HTML)

    val contexts = RequestContexts()

    val userLens = RequestContextKey.optional<User>(contexts)
    val permissionsLens: RequestContextLens<Permissions> = RequestContextKey.required(contexts)

    val htmlViewWithContext = htmlView
        .associateContextLens("currentUser", userLens)
        .associateContextLens("permissions", permissionsLens)

    val jwtTools = JwtTools(SALT, "Zoo№1")

    val operationHolder = OperationHolder(database, SALT)
    val printingApp: HttpHandler = ServerFilters.InitialiseRequestContext(contexts)
        .then(badRouteFilter(htmlViewWithContext))
        .then(authFilter(userLens, jwtTools, operationHolder.userByLogin))
        .then(permissionFilter(userLens, permissionsLens, operationHolder.permissionByUser))
        .then(
            app(
                htmlViewWithContext, jwtTools,
                permissionsLens, userLens,
                operationHolder.entryByAnimalNoTrustee,
                operationHolder.entryByAnimalForTrustee,
                operationHolder.entryByIndex,
                operationHolder.animalByIndex,
                operationHolder.listEntryForTrustee,
                operationHolder.listAnimals,
                operationHolder.addEntryInDatabase,
                operationHolder.addTrusteeInDatabase,
                operationHolder.addAnimalInDatabase,
                operationHolder.infoAboutZoo,
                operationHolder.addNewUser,
                operationHolder.checkPasswordLoginUser,
                operationHolder.checkLoginOriginally,
                operationHolder.editAnimal,
                operationHolder.editEntry,
                operationHolder.deleteTrustAnimal,
                operationHolder.trusteeAnimal,
                operationHolder.entryByTrusteeAnimalCount,
                operationHolder.listUsersByAnimal,
                operationHolder.userById,
                operationHolder.listUsers,
                operationHolder.editDescriptionUser
            )
        )
    return printingApp.asServer(Undertow(PORT)).start()
}

fun main() {
    val h2databaseManager = H2DatabaseManager().initialize()
    try {
        performMigrations()
    } catch (_: FlywayException) {
        h2databaseManager.stopServers()
        return
    }
    val database = connectToDatabase()

    val webServer = startApplication(database)

    println("Сервер доступен по адресу http://localhost:" + webServer.port())
    println("Веб-интерфейс базы данных доступен по адресу http://localhost:${H2DatabaseManager.WEB_PORT}")
    println("Введите любую строку, чтобы завершить работу приложения")
    readln()
    webServer.stop()
    h2databaseManager.stopServers()
}
