package com.nwised.javax.commons.jaxrs.utils;

import com.nwised.javax.commons.db.DBHandler;
import com.nwised.javax.commons.mbeans.config.Bootstrap;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.enterprise.event.Reception.ALWAYS;

/**
 * Created by tm on 7/14/17.
 */

@ApplicationScoped
public class KeyHandler {

    @Inject
    DBHandler dbHandler;

    private Key HMAC_SHA512_KEY;

    KeyHandler() {

    }


    public void onStart(@Observes(notifyObserver = ALWAYS) @Bootstrap.OnStart Bootstrap.LifecycleEvent evnt) {
        System.out.println("Fetching Server Signing Key");
    }

    @PostConstruct
    public void init() {
        fetchKeyFromDB();
    }

    public SecretKey getAES_SecretKey() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // for example
            SecretKey secretKey = keyGen.generateKey();
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, "Could not generate key", e);
            return null;
        }

    }

    private Key generateKey() {
        return MacProvider.generateKey(SignatureAlgorithm.HS512);
    }

    private Key getHMAC_SHA512_KEY() {
        return HMAC_SHA512_KEY;
    }

    public Key getJWTSigningKey() {
        return getHMAC_SHA512_KEY();
    }

    private void setHMAC_SHA512_KEY(Key HMAC_SHA512_KEY) {
        this.HMAC_SHA512_KEY = HMAC_SHA512_KEY;
    }

    private void fetchKeyFromDB() {
        try (Connection con = dbHandler.getRawConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT DATA FROM MOB_MASTERDATA " +
                     "WHERE CODE='JWT_KEY'")) {
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {//If a key exist in DB fetch it
                byte[] data = Base64.getDecoder().decode(resultSet.getString("data").getBytes("UTF-8"));
                SecretKey key = new SecretKeySpec(data, 0, data.length, SignatureAlgorithm.HS512.getJcaName());
                this.HMAC_SHA512_KEY = key;
            } else { // if not, generate a one and save
                try (PreparedStatement ins_stmt = con.prepareStatement(
                        "INSERT INTO MOB_MASTERDATA(CODE,DATA) VALUES ('JWT_KEY',?)")) {
                    Key temp = generateKey();
                    ins_stmt.setString(1, Base64.getEncoder().encodeToString(temp.getEncoded()));
                    if (ins_stmt.executeUpdate() > 0) {
                        this.HMAC_SHA512_KEY = temp;
                    } else {
                        Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, "Could not Save key");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Logger.getLogger(KeyHandler.class.getName()).log(Level.SEVERE, "Could not generate key", e);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
