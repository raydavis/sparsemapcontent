# Generic JDBC SPI Storage implementation

## Summary

This package is a generic JDBC implementation of the sparsemapcontent storage SPI. It provides a component (JDBCStorageClientPool) that allows you to configure common JDBC properties (e.g., driver class, jdbc url, username and password). This driver assumes that you will also make available the appropriate JDBC library on the classpath, as it does not embed any JDBC drivers itself.
