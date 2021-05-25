@file:Suppress("UNCHECKED_CAST")

import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import rx.Observable

fun main() = HttpServer
    .newServer(8080)
    .start { req, resp ->
        val path = req.decodedPath
        val parameters = req.queryParameters
        val (status, response) = when (path) {
            "/addUser" -> {
                try {
                    val id = parameters["id"]!![0]
                    val currency = parameters["currency"]!![0].toCurrency()
                    HttpResponseStatus.OK to Driver.addUser(User(id, currency))
                        .map { "${User(id, currency)} inserted" }
                } catch (e: Throwable) {
                    HttpResponseStatus.BAD_REQUEST to Observable.just("BadRequest")
                }
            }
            "/addItem" -> {
                try {
                    val id = parameters["id"]!![0]
                    val currency = parameters["currency"]!![0].toCurrency()
                    val value = parameters["value"]!![0].toDouble()
                    HttpResponseStatus.OK to Driver.addItem(Item(id, value, currency))
                        .map { "${Item(id, value, currency)}" }
                } catch (e: Throwable) {
                    HttpResponseStatus.BAD_REQUEST to Observable.just("BadRequest")
                }
            }
            "/getItems" -> {
                try {
                    val id = parameters["id"]!![0]
                    HttpResponseStatus.OK to Driver
                        .getUserById(id)
                        .map(User::currency)
                        .flatMap { currency: Currency ->
                            Driver
                                .getItems()
                                .map { it.toOtherCur(currency) }
                                .map { "$it\n" }
                        }
                } catch (e: Throwable) {
                    HttpResponseStatus.BAD_REQUEST to Observable.just("BadRequest")
                }

            }
            else -> HttpResponseStatus.BAD_REQUEST to Observable.just("BadRequest")
        }
        resp.status = status
        resp.writeString(response as Observable<String>?)
    }
    .awaitShutdown()
