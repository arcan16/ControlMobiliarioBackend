package com.mobiliario.infra.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    public String generateAccesToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Con este metodo confirmamos si el token recibido sea valido
    public boolean isTokenValid(String token){
        try{
            var x = getSignatureKey();
            Jwts.parser()
                    .setSigningKey(getSignatureKey())
                    .build().parseClaimsJws(token)
                    .getBody();
            return true;
        }catch(Exception e){
            return false;
        }

    }
    // Con este metodo extraemos la claim que nosotros necesitemos
    public String getUsernameFromToken(String token){
        return getClaim(token,Claims::getSubject);
    }

    // Con este metodo extraemos un Clain
    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    // Con este metodo extraemos todos los claims o caracteristicas del token
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Generamos una firma encriptada usando una clave que ya ha sido encriptada anteriormente
    public Key getSignatureKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Obtenemos el token del header de la peticion
    public String getTokenFromHeader(HttpServletRequest request){
        return request.getHeader("Authorization").substring(7);
    }
}
