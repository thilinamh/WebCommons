package com.nwised.javax.commons.db;

import com.nwised.javax.commons.utils.CDIUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;


@ApplicationScoped
public class DBHandler {

    //    @Resource(name = "survey")
    private DataSource ds;
    @Inject
    CDIUtils cdiUtils;


    public DBHandler() {
/**
 * Use this snippet if you failed to configure resource reference
 */
        if (ds == null) {
            try {
                Context ctx = (Context) new InitialContext();
                ds = (DataSource) ctx.lookup("java:/service_app"); //jndi name
//                ds = (javax.sql.DataSource) ctx.lookup("jdbc/ElectSvy"); //jndi name

            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    public DataSource getDataSource() {
        return ds;
    }

    public Connection getRawConnection() throws SQLException {
        try {
            Connection connection = ds.getConnection();
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            Logger.getLogger(DBHandler.class.getName()).log(Level.SEVERE, "Failed initializing row SQL Connection");
            throw e;
        }
    }

//    @javax.enterprise.inject.Produces
//    public static OnDemandConnection procudeConnection(@Default BeanManager beanManager) {
//        System.out.println("*********&&&&&&&&&&&");
//        if (beanManager.getContext(RequestScoped.class).isActive()) {
//            return CDI.current().select(OnDemandConnection.class).get();
//        } else {
//            return CDI.current().select(DemandConnection.class).get();
//        }
//
//    }

    public Connection resolveConnection() throws SQLException {
        return cdiUtils.isRequestScopedActive() ? CDI.current().select(OnDemandConnection.class).get() : getRawConnection();
    }

    /**
     * This is a RequestScoped rawConnection with auto commit disabled.
     */
    @RequestScoped
    public static class OnDemandConnection implements Connection {

        private Connection rawConnection;
        @Inject
        private DBHandler handler;

        private AtomicInteger transCount = new AtomicInteger(0);

        @PostConstruct
        private void initConnection() {
            try {
//                System.out.println("=============================================");
//                System.out.println("Instantiating DB Connection:" + this.getClass().getTypeName());
//                System.out.println("=============================================");
                this.rawConnection = handler.getRawConnection();
                this.rawConnection.setAutoCommit(false);
            } catch (SQLException ex) {
                Logger.getLogger(OnDemandConnection.class.getName()).log(Level.SEVERE, "Failed DB fetching connection", ex);
            }
        }

        @PreDestroy
        private void closeConnection() {
            try {
                if (transCount.get() == 0) {
//                    Logger.getLogger(OnDemandConnection.class.getName()).log(Level.INFO, "Committing transaction");
                    rawConnection.commit();
                } else {
                    this.rollback();
                    Logger.getLogger(OnDemandConnection.class.getName()).log(Level.INFO, "Transaction rolled back");
                }
//                System.out.println("Closing OnDemand connection");
                rawConnection.close();
            } catch (SQLException ex) {
                Logger.getLogger(OnDemandConnection.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        /**
         * Must call this before executing any data manipulation.
         */
        public void beginTransaction() {

//            if (transCount.get() > 0) {// which means previous trans has not been properly ended. so roll it back
//                try {
//                    this.rollback();
//                    transCount.set(0);
//                } catch (SQLException e) {
//                    Logger.getLogger(OnDemandConnection.class.getName()).log(Level.SEVERE, "Error while rolling back OnDemandConnection", e);
//                }
//            }
            transCount.incrementAndGet();
        }

        /**
         * Call this in the end of the transaction.
         * Transaction will not be committed until end of the scope
         */
        public void endTransaction() {
            if (transCount.get() > 0) {
                transCount.decrementAndGet();
            }

        }

        public Connection getRawConnection() {
            return rawConnection;
        }

        public void setRawConnection(Connection rawConnection) {
            this.rawConnection = rawConnection;
        }

        @Override
        public Statement createStatement() throws SQLException {
            return rawConnection.createStatement();
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return rawConnection.prepareCall(sql);
        }

        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return rawConnection.prepareCall(sql);
        }

        @Override
        public String nativeSQL(String sql) throws SQLException {
            return rawConnection.nativeSQL(sql);
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            rawConnection.setAutoCommit(autoCommit);
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return rawConnection.getAutoCommit();
        }

        /**
         * This will not commit anything.
         *
         * @throws SQLException
         * @see #forceCommit
         */
        @Override
        public void commit() throws SQLException {
//            connection.commit();
        }

        /**
         * Commits whatever in the transaction
         *
         * @throws SQLException
         */
        public void forceCommit() throws SQLException {
            rawConnection.commit();
        }

        @Override
        public void rollback() throws SQLException {
            rawConnection.rollback();
        }

        @Override
        public void close() throws SQLException {
            //connection.close();
        }

        @Override
        public boolean isClosed() throws SQLException {
            return rawConnection.isClosed();
        }

        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return rawConnection.getMetaData();
        }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            rawConnection.setReadOnly(readOnly);
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return rawConnection.isReadOnly();
        }

        @Override
        public void setCatalog(String catalog) throws SQLException {
            rawConnection.setCatalog(catalog);
        }

        @Override
        public String getCatalog() throws SQLException {
            return rawConnection.getCatalog();
        }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            rawConnection.setTransactionIsolation(level);
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return rawConnection.getTransactionIsolation();
        }

        @Override
        public SQLWarning getWarnings() throws SQLException {
            return rawConnection.getWarnings();
        }

        @Override
        public void clearWarnings() throws SQLException {
            rawConnection.clearWarnings();
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return rawConnection.createStatement(resultSetType, resultSetConcurrency);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return rawConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return rawConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return rawConnection.getTypeMap();
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            rawConnection.setTypeMap(map);
        }

        @Override
        public void setHoldability(int holdability) throws SQLException {
            rawConnection.setHoldability(holdability);
        }

        @Override
        public int getHoldability() throws SQLException {
            return rawConnection.getHoldability();
        }

        @Override
        public Savepoint setSavepoint() throws SQLException {
            return rawConnection.setSavepoint();
        }

        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return rawConnection.setSavepoint(name);
        }

        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            rawConnection.rollback(savepoint);
        }

        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            rawConnection.releaseSavepoint(savepoint);
        }

        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return rawConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return rawConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return rawConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return rawConnection.prepareStatement(sql, autoGeneratedKeys);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return rawConnection.prepareStatement(sql, columnIndexes);
        }

        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return rawConnection.prepareStatement(sql, columnNames);
        }

        @Override
        public Clob createClob() throws SQLException {
            return rawConnection.createClob();
        }

        @Override
        public Blob createBlob() throws SQLException {
            return rawConnection.createBlob();
        }

        @Override
        public NClob createNClob() throws SQLException {
            return rawConnection.createNClob();
        }

        @Override
        public SQLXML createSQLXML() throws SQLException {
            return rawConnection.createSQLXML();
        }

        @Override
        public boolean isValid(int timeout) throws SQLException {
            return rawConnection.isValid(timeout);
        }

        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            rawConnection.setClientInfo(name, value);
        }

        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            rawConnection.setClientInfo(properties);
        }

        @Override
        public String getClientInfo(String name) throws SQLException {
            return rawConnection.getClientInfo(name);
        }

        @Override
        public Properties getClientInfo() throws SQLException {
            return rawConnection.getClientInfo();
        }

        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return rawConnection.createArrayOf(typeName, elements);
        }

        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return rawConnection.createStruct(typeName, attributes);
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            rawConnection.setSchema(schema);
        }

        @Override
        public String getSchema() throws SQLException {
            return rawConnection.getSchema();
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            rawConnection.abort(executor);
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            rawConnection.setNetworkTimeout(executor, milliseconds);
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            return rawConnection.getNetworkTimeout();
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return rawConnection.unwrap(iface);
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return rawConnection.isWrapperFor(iface);
        }

    }


}
