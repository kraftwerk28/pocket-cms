package com.kraftwerk28.pocketcms

import java.sql.Connection
import org.postgresql.Driver
import java.sql.DriverManager
import java.sql.ResultSet

fun ResultSet.toMap(): Map<String, Any> = (0 until metaData.columnCount)
    .fold(mutableMapOf())
    { acc, c ->
        acc.set(this.metaData.getColumnName(c), this.getObject(c))
        acc
    }

fun ResultSet.toList(): List<Any> = (0 until metaData.columnCount)
    .fold(mutableListOf()) { acc, c -> acc.add(getObject(c)); acc }

class Database {

    data class Credentials(
        val host: String,
        val port: Int,
        val username: String?,
        val password: String,
        val dbName: String
    ) {
        val connString: String
            get() = "jdbc:postgresql://$host:$port/$dbName" +
                    "?username=$username&password=$password"
    }

    lateinit var connection: Connection

    init {
        val driver = Driver()
        DriverManager.registerDriver(driver)
    }

    private fun prepareQueryList(query: String, l: List<Any> = listOf()) =
        l.foldIndexed(query)
        { i, acc, c -> acc.replace("$${i + 1}", c.toString()) }

    private fun prepareQueryList(
        query: String,
        map: Map<String, Any> = mutableMapOf()
    ) =
        map.entries.fold(query)
        { acc, c -> acc.replace("$${c.key}", c.value.toString()) }

    fun connect(creds: Credentials): Connection {
        connection = DriverManager.getConnection(creds.connString)
        return connection
    }

    fun query(statement: String, values: List<Any>): ResultSet {
        val prepared = prepareQueryList(statement, values)
        return connection.createStatement().executeQuery(prepared)
    }

    fun query(statement: String, values: Map<String, Any>): ResultSet {
        val prepared = prepareQueryList(statement, values)
        return connection.createStatement().executeQuery(prepared)
    }

    fun disconnect() {
        connection.close()
    }

    companion object {
        val instance = Database()
    }
}
