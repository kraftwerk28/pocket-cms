- # pocket-cms


## Description
A frontend for SQL (maybe NoSQL in future) DBMS, similar to [Data editor](https://www.jetbrains.com/help/datagrip/table-editor.html#) view in JetBrains DataGrip. Should include basic functionality of DDL and DML operations on databases and tables.


## Libraries & technologies

- [Exposed](https://github.com/JetBrains/Exposed) - SQL driver


## Purpose
Application data is stored on remote server including configs, secrets, analytics etc. I often need immediate access to modify config property or change app state through database when there is no desktop machine near me but android device. I use Termux along with `psql` client for accessing database and write raw sql which is slow and uncomfortable.
## Prerequisites

A PostgreSQL instance is deployed on DigitalOcean droplet and is accessible through authentification for testing and production.


## Checklist

- [ ] dislpaying databases list / creating database
- [ ] displaying/modifying database tables (`information_schema.tables`)
- [ ] displaying/modifying table comumn types (`information_schema.columns`)
- [ ] displaying/modifying data from table
- [ ] auth through fingerprint API
- [ ] tests
- [ ] ...
