package com.remindrx

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    val jwtAudience = "jwt-audience"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtSecret = "secret"
    routing {
        get("/") {
            call.respondText("RemindRX Backend Running")
        }
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            if (loginRequest.username != "user" || loginRequest.password != "123") {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                return@post
            }

            val token = JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(jwtDomain)
                .withClaim("userId", 1)
                .withClaim("role", "USER")
                .sign(Algorithm.HMAC256(jwtSecret))

            call.respond(mapOf("token" to token))
        }
        authenticate("auth-jwt") {
            get("/user") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.payload?.getClaim("role")?.asString()

                if (role != "USER") {
                    call.respond(HttpStatusCode.Forbidden, "Access denied")
                    return@get
                }

                call.respondText("User access granted")
            }
        }
    }
}
