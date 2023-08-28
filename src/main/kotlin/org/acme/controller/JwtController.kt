package org.acme.controller

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("/generate-token")
class JwtController(private val tokenService: TokenService) {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun generateToken(): Response {
        val token = tokenService.generateToken()
        return Response.ok(TokenResponse(token)).build()
    }
}

data class TokenResponse(
    @field:JsonProperty("token")
    val token: String
)