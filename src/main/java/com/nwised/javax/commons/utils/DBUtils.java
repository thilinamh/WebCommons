package com.nwised.javax.commons.utils;

import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DBUtils {

    public JsonObjectBuilder parseRowToJson(ResultSet result, ResultSetMetaData metaData) {
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        try {

            //ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int index = 1; index <= columnCount; index++) {
                String columnLabel = metaData.getColumnLabel(index).toLowerCase();
                switch (metaData.getColumnType(index)) {
                    case Types.DATE:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getDate(index), objBuilder);
                        break;
                    case Types.TIME:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getTime(index), objBuilder);
                        break;
                    case Types.TIMESTAMP:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getTimestamp(index), objBuilder);
                        break;
                    case Types.ARRAY:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getArray(index), objBuilder);
                        break;
                    case Types.BIGINT:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getInt(index), objBuilder);
                        break;
                    case Types.DECIMAL:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getBigDecimal(index), objBuilder);
                        break;
                    case Types.BOOLEAN:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getBoolean(index), objBuilder);
                        break;
                    case Types.BLOB:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getBlob(index), objBuilder);
                        break;
                    case Types.CLOB:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getClob(index), objBuilder);
                        break;
                    case Types.DOUBLE:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getDouble(index), objBuilder);
                        break;
                    case Types.FLOAT:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getFloat(index), objBuilder);
                        break;
                    case Types.INTEGER:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getInt(index), objBuilder);
                        break;
                    case Types.NVARCHAR:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getNString(index), objBuilder);
                        break;
                    case Types.VARCHAR:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getString(index), objBuilder);
                        break;
                    case Types.TINYINT:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getInt(index), objBuilder);
                        break;
                    case Types.SMALLINT:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getInt(index), objBuilder);
                        break;
                    default:
                        JsonHandler.addToJsonObjectWithNullCheck(columnLabel, result.getObject(index), objBuilder);
                        break;


                }


            }
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage());
        }
        return objBuilder;
    }

    public JsonArray parseResultSetToJsonArray(ResultSet reslt) throws SQLException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ResultSetMetaData resultSetMetaData = reslt.getMetaData();

        while (reslt.next()) {
            arrayBuilder.add(parseRowToJson(reslt, resultSetMetaData));
        }
        return arrayBuilder.build();
    }

    /**
     * Concatenates columns with comma separator
     *
     * @param columns
     * @return
     */
    public static String concatColumns(@NotNull String... columns) {
        StringBuilder finalStr = new StringBuilder();
        for (String column : columns) {
            finalStr.append(column).append(",");
        }
        finalStr.deleteCharAt(finalStr.lastIndexOf(","));
        return finalStr.toString();
    }

    public static String buildLimitQueryWithAlias(String select_query, String alias, int lower_bound, int upper_bound) {
        return "select * from ("
                + "  select alias.*, ROWNUM rnum from ("
                + select_query
                + ") alias where rownum <=" + upper_bound
                + ") " + alias
                + " where rnum >" + lower_bound;
    }

}
