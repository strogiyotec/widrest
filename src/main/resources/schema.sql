/**
  Default widgets schema
 */
CREATE TABLE widgets
(
    id identity NOT NULL PRIMARY KEY,/* auto generated primary key*/
    x  int, /*x coordinate*/
    y  int, /*y coordinate*/
    z  int unique /*unique z-index of widget*/,
    width int /*widget width*/,
    height int /*widget height*/,
    last_updated TIME WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO widgets (x, y, z)
values (1, 1, 1);