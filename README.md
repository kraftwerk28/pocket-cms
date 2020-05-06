# pocket-cms

## Description
A frontend for SQL (maybe NoSQL in future) DBMS, similar to [Data editor](https://www.jetbrains.com/help/datagrip/table-editor.html#) view in JetBrains DataGrip. Should include basic functionality of DDL and DML operations on databases and tables.


## Libraries & technologies
- [PostgreSQL JDBC driver](https://jdbc.postgresql.org/) - SQL driver


## Purpose
Application data is stored on remote server including configs, secrets, analytics etc. I often need immediate access to modify config property or change app state through database when there is no desktop machine near me but android device. I use Termux along with `psql` client for accessing database and write raw sql which is slow and uncomfortable. The app must provide useful GUI for viewing and modifying database contents


## Prerequisites
A PostgreSQL instance is deployed on DigitalOcean droplet and is accessible through authentification for testing and production.

## Checklist
- [x] dislpaying databases list / creating database
- [x] displaying/modifying database tables (`information_schema.tables`) (partially)
- [ ] displaying/modifying table comumn types (`information_schema.columns`)
- [ ] displaying/modifying data from table
- [x] auth through fingerprint API
- [ ] tests
- [ ] ...
