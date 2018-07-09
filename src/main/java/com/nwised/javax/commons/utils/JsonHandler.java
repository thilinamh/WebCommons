/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nwised.javax.commons.utils;

import javax.json.*;
import javax.json.stream.JsonParsingException;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.Map;


/**
 * @author thilina_h
 */
public class JsonHandler {
    public static JsonObjectBuilder toObjectBuilder(JsonObject source) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        source.entrySet().forEach((Map.Entry<String, JsonValue> e) -> builder.add(e.getKey(), e.getValue()));
        return builder;
    }

    public static void addToJsonObjectWithNullCheck(String key, Object value, JsonObjectBuilder objBuilder) {
        if (value != null) {
            if (value instanceof String) {
                objBuilder.add(key, ((String) value));
            } else if (value instanceof JsonValue) {
                objBuilder.add(key, (JsonValue) value);
            } else if (value instanceof java.sql.Date) {
                objBuilder.add(key, CommonUtils.DATE_TIME_FORMATS.DD_MM_YYYY_DASH.format(((java.sql.Date) value).toLocalDate()));
            } else if (value instanceof java.sql.Time) {
                objBuilder.add(key, CommonUtils.DATE_TIME_FORMATS.h_mm_a_COLON.format(((java.sql.Time) value).toLocalTime()));
            } else if (value instanceof java.sql.Timestamp) {
                objBuilder.add(key, CommonUtils.DATE_TIME_FORMATS.ISO_FORMAT.format(((java.sql.Timestamp)value).toLocalDateTime()));
            } else {
                objBuilder.add(key, value.toString());
            }

            /**
             * if (object instanceof Blob) {
             Blob img = (Blob) object;
             byte[] img_bytes = img.getBytes(1, (int) img.length());
             String img_b64 = new String(Base64.encodeBase64(img_bytes));
             img.free();
             JsonHandler.addToJsonObjectWithNullCheck(columnLabel, img_b64, objBuilder);
             } else if (object instanceof Clob) {
             Clob clob = (Clob) object;
             try (InputStream in = clob.getAsciiStream()) {
             String str = CommonUtils.streamToStr(in);
             JsonHandler.addToJsonObjectWithNullCheck(columnLabel, str, objBuilder);
             } catch (IOException ex) {

             }
             clob.free();

             }
             */
        } else {
            objBuilder.addNull(key);
        }
    }

    public static void addToJsonObjectWithNullReplace(String key, Object value, Object replace_on_null, JsonObjectBuilder objBuilder) {
        if (value != null) {
            if (value instanceof String) {
                objBuilder.add(key, ((String) value).trim());
            } else if (value instanceof JsonValue) {
                objBuilder.add(key, (JsonValue) value);
            } else if (value instanceof Number) {
                float f = ((Number) value).floatValue();
                int i = ((Number) value).intValue();
                if (f > i) {
                    objBuilder.add(key, f);
                } else {
                    objBuilder.add(key, i);
                }
            } else {
                objBuilder.add(key, value.toString());
            }
        } else if (replace_on_null instanceof String) {
            objBuilder.add(key, (String) replace_on_null);
        } else if (replace_on_null instanceof Number) {
            objBuilder.add(key, ((Number) replace_on_null).intValue());
        } else {
            objBuilder.addNull(key);
        }
    }

    private JsonHandler() {
    }

    ;

    public static JsonObject toJsonObject(String str) {

        try (JsonReader jsonReader = Json.createReader(new StringReader(str))) {
            return jsonReader.readObject();

        } catch (JsonParsingException e) {
            e.printStackTrace();
            throw new javax.ws.rs.client.ResponseProcessingException(Response.serverError().build(), "cannot parse input");
        } catch (JsonException e) {
            e.printStackTrace();
            throw new javax.ws.rs.NotSupportedException();
        }
    }

    public static JsonArray toJsonArray(String str) {
        try {
            JsonReader jsonReader = Json.createReader(new StringReader(str));
            return jsonReader.readArray();

        } catch (JsonParsingException e) {
            e.printStackTrace();
            throw new javax.ws.rs.client.ResponseProcessingException(Response.serverError().build(), "cannot parse input");
        } catch (JsonException e) {
            e.printStackTrace();
            throw new javax.ws.rs.NotSupportedException();
        }
    }

    public static void addToObjectBuilder(JsonObjectBuilder objectBuilder, JsonObject jsonBody) {
        for (Map.Entry<String, JsonValue> entry : jsonBody.entrySet()) {
            objectBuilder.add(entry.getKey(), entry.getValue());
        }
    }
}
