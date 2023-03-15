package ru.ac.uniyar.services

import org.flywaydb.core.Flyway

fun performMigrations() {
    val flyway = Flyway
        .configure()
        .locations("ru/ac/uniyar/db/migrations")
        .validateMigrationNaming(true)
        .dataSource(H2DatabaseManager.JDBC_CONNECTION, "sa", null)
        .load()
    flyway.migrate()
}
