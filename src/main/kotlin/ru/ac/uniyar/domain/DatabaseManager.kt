package ru.ac.uniyar.domain

import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import ru.ac.uniyar.services.H2DatabaseManager

fun connectToDatabase() = Database.connect(
    url = H2DatabaseManager.JDBC_CONNECTION,
    driver = "org.h2.Driver",
    user = "sa",
    dialect = MySqlDialect(),
)
