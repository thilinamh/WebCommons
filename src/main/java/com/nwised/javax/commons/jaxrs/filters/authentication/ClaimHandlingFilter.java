package com.nwised.javax.commons.jaxrs.filters.authentication;

import com.nwised.javax.commons.jaxrs.webexceptions.ForbiddenOperation;
import com.nwised.javax.commons.jaxrs.webexceptions.SomethingWentWrong;
import com.nwised.javax.commons.jaxrs.utils.KeyHandler;
import com.nwised.javax.commons.mbeans.commonbeans.SessionData;
import io.jsonwebtoken.*;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NameBinding;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

;

/**
 * Created by thilina_h on 11/23/2017.
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
@ClaimHandlingFilter.ResolveClaim(claim_key = "", claims_values = {})
public class ClaimHandlingFilter implements ContainerRequestFilter {


    @NameBinding
    @JWTFilter.TokenAuthenticated
    @Repeatable(value = ResolveClaimsContainer.class)
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ResolveClaim {
        String claim_key();

        String[] claims_values();
    }

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ResolveClaimsContainer {
        ResolveClaim[] value();
    }

    @Context
    private ResourceInfo resourceInfo;
    @Inject
    private SessionData sessionData;
    @Context
    private HttpHeaders headers;
    @Inject
    private KeyHandler keyHandler;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        ResolveClaim[] annotations = resourceInfo.getResourceMethod().getAnnotationsByType(ResolveClaim.class);
        for (ResolveClaim annotation : annotations) {
            String claim_key = annotation.claim_key();
            String[] claimsValues = annotation.claims_values();
            Jws<Claims> jwt_claims = sessionData.getData("jwt_claims");

            if (jwt_claims == null) {
                try {
                    String compactJws = headers.getHeaderString("token");
                    jwt_claims = Jwts.parser().setSigningKey(keyHandler.getJWTSigningKey()).parseClaimsJws(compactJws);
                    sessionData.putData("jwt_claims",jwt_claims);
                } catch (SignatureException e) {//don't trust the JWT!
                    containerRequestContext.abortWith(new SomethingWentWrong("Invalid Token", false).getResponse());
                    return;
                }catch (ExpiredJwtException ex){
                    containerRequestContext.abortWith(new ForbiddenOperation("Invalid Session").getResponse());
                    return;
                }
            }
            if (!claim_key.isEmpty() && claimsValues.length != 0) {
                String claim = jwt_claims.getBody().get(claim_key, String.class);
                List<String> valueList = Arrays.asList(claimsValues);
                if (!valueList.contains(claim)) {//If the specified claim value is not in the required claim list, abort the request
                    containerRequestContext.abortWith(
                            new SomethingWentWrong("You do not have authority to perform this action", false)
                                    .getResponse());
                }
            }
        }

    }

}
