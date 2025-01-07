PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;

DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS bookmarks;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS dish_ingredients;

CREATE TABLE dishes(
	dish_id integer NOT NULL primary key AUTOINCREMENT,
    user_id integer NOT NULL,
	servings integer,
    dish_name text NOT NULL UNIQUE,
    prep_time_hours integer default 0,
    prep_time_minutes integer default 0,
    foreign key (user_id) references users(user_id) on delete cascade
);

CREATE TABLE users(
	user_id integer NOT NULL primary key AUTOINCREMENT,
	username text NOT NULL,
    password text NOT NULL
);

CREATE TABLE bookmarks (
	user_id integer NOT NULL,
    dish_id integer NOT NULL,
    primary key (user_id, dish_id),
    foreign key (user_id) references users(user_id) on delete cascade,
	foreign key (dish_id) references dishes(dish_id) on delete cascade
);

CREATE TABLE IF NOT EXISTS ingredients(
	ingredient_id integer NOT NULL primary key AUTOINCREMENT,
	ingredient_name text NOT NULL UNIQUE
);

CREATE TABLE dish_ingredients (
	ingredient_id integer NOT NULL,
    dish_id integer NOT NULL,
    measurement_unit text, -- cups, liters, tablespoon
    quantity REAL NOT NULL,
    primary key (ingredient_id, dish_id),
    foreign key (ingredient_id) references ingredients(ingredient_id) on delete cascade,
    foreign key (dish_id) references dishes(dish_id) on delete cascade
);

COMMIT;