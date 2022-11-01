/*
    create database to track data for stat_tracker app
*/

CREATE TABLE teams (
  team_id SERIAL PRIMARY KEY,
  team_name VARCHAR(100)
);

CREATE TABLE player_list (
  event_id int REFERENCES events(event_id),
  player_id int REFERENCES players(player_id)
);

CREATE TABLE players (
  team_id int REFERENCES teams(team_id),
  player_id SERIAL PRIMARY KEY,
  player_name VARCHAR(100),
  number int
);

CREATE TABLE events (
  team_id int REFERENCES teams(team_id),
  event_id SERIAL PRIMARY KEY,
  event_name VARCHAR(100)
);

CREATE TABLE action_list (
  event_id int REFERENCES events(event_id),
  action_type_id int REFERENCES action_type(action_type_id)
);

CREATE TABLE action_type (
  action_type_id SERIAL PRIMARY KEY,
  action_name VARCHAR(100)
);

CREATE TABLE statistics (
  event_id int REFERENCES events(event_id),
  player_id int REFERENCES players(player_id),
  stat_id SERIAL PRIMARY KEY,
  action_type_id int REFERENCES action_type(action_type_id)
);

/*
    dummy data includes:
    a team named "lucas team" with 4 players
    an event called "first tournament"
    a player_list for "first tournament"
    a action_list for "first tournament"
    a couple of statistics for event "first tournament"
*/

--insert a team
INSERT INTO teams (team_name) VALUES ('lucas team');

--insert players into team
INSERT INTO players (team_id, player_name, number) VALUES (1, 'lucas', 3);
INSERT INTO players (team_id, player_name, number) VALUES (1, 'fed', 4);
INSERT INTO players (team_id, player_name, number) VALUES (1, 'kevin', 5);
INSERT INTO players (team_id, player_name, number) VALUES (1, 'blake', 6);

--insert a event
INSERT INTO events (team_id, event_name) VALUES (1, 'first tournament');

--create action_type table
INSERT INTO action_type (action_name) VALUES ('serving ace');
INSERT INTO action_type (action_name) VALUES ('serving error');
INSERT INTO action_type (action_name) VALUES ('serving zero-serve');
INSERT INTO action_type (action_name) VALUES ('receiving 1-pass');
INSERT INTO action_type (action_name) VALUES ('receiving 2-pass');
INSERT INTO action_type (action_name) VALUES ('receiving 3-pass');
INSERT INTO action_type (action_name) VALUES ('receiving error');
INSERT INTO action_type (action_name) VALUES ('attacking kill');
INSERT INTO action_type (action_name) VALUES ('attacking error');
INSERT INTO action_type (action_name) VALUES ('attacking zero-attack ');
INSERT INTO action_type (action_name) VALUES ('setting assist');
INSERT INTO action_type (action_name) VALUES ('setting error');
INSERT INTO action_type (action_name) VALUES ('setting zero-assist ');

--insert players into a player_list for event
INSERT INTO player_list (event_id, player_id) VALUES (1, 1);
INSERT INTO player_list (event_id, player_id) VALUES (1, 3);
INSERT INTO player_list (event_id, player_id) VALUES (1, 4);

--insert actions into action_list for event
INSERT INTO action_list (event_id, action_type_id) VALUES (1, 4);
INSERT INTO action_list (event_id, action_type_id) VALUES (1, 5);
INSERT INTO action_list (event_id, action_type_id) VALUES (1, 6);
INSERT INTO action_list (event_id, action_type_id) VALUES (1, 7);

--insert statistics into statistics table for event
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 1, 4);
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 1, 4);
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 1, 4);
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 3, 6);
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 4, 7);
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 3, 5);
INSERT INTO statistics (event_id, player_id, action_type_id) VALUES (1, 4, 4);

/*
    query outputs columns number, player_name, player_id, selected where selected is > 0 if in event, < -1 if not in event
*/
SELECT p.number, p.player_name, p.player_id, coalesce(pl.event_id, -1) AS selected
FROM players p
LEFT JOIN (SELECT * FROM player_list WHERE event_id = 2) pl
ON p.player_id = pl.player_id
WHERE p.team_id = 2;

/*
    query outputs action_type_id, action_name, selected where selected is > 0 if in event, -1 if not in event
*/
SELECT atype.action_type_id, atype.action_name, coalesce(al.event_id, -1) AS selected
FROM action_type atype
LEFT JOIN (SELECT * FROM action_list WHERE event_id = 1) al
ON atype.action_type_id = al.action_type_id;

/*
    query outputs player_name, player_number, player_id, stat_id, action_type_id, action_name
*/
SELECT s.player_id, p.player_name, p.number, s.stat_id, s.action_type_id, atype.action_name
FROM statistics s
JOIN players p ON s.player_id = p.player_id
JOIN action_type atype ON s.action_type_id = atype.action_type_id
WHERE s.event_id = 9;
/*
    query outputs player_id, player_name from player_list given an event id
*/
SELECT pl.player_id, p.player_name
FROM player_list pl
JOIN players p ON pl.player_id = p.player_id
WHERE pl.event_id = 2;

/*
    query outputs action_type_id, action_name from action_list given an event id
*/
SELECT al.action_type_id, atype.action_name
FROM action_list al
JOIN action_type atype ON al.action_type_id = atype.action_type_id
WHERE al.event_id = 1;

/*
    deleting a team steps:
    delete all statistics associated with event in team
    delete all action_list items associated with event in team
    delete all player_list items associated with event in team
    delete all events associated with team
    delete all players associated with team
    delete team
*/
--in a for loop delete all events associated with teams
SELECT event_id FROM events WHERE team_id = 4;
--delete players from team
DELETE FROM players WHERE team_id = 4;
--delete team
DELETE FROM teams WHERE team_id = 4;


/*
    deleting an event:
    delete all statistics associated with event
    delete all action_list items associated with event
    delete all player_list items associated with event
    delete event
*/
BEGIN TRANSACTION;
    DELETE FROM statistics WHERE event_id = 9;
    DELETE FROM action_list WHERE event_id = 9;
    DELETE FROM player_list WHERE event_id = 9;
    DELETE FROM events WHERE event_id = 9;
END TRANSACTION;

/*
    deleting a player:
    delete statistics associated with player
    delete player_list items associated with player
    delete player from players
*/

BEGIN TRANSACTION;
    DELETE FROM statistics WHERE player_id = 4;
    DELETE FROM player_list WHERE player_id = 4;
    DELETE FROM players WHERE player_id = 4;
END TRANSACTION;

/*
    query outputs player_num, player_name, player_id, from player_list given an event_id
*/

SELECT p.number, p.player_name, pl.player_id
FROM player_list pl
JOIN players p ON p.player_id = pl.player_id
WHERE pl.event_id = 1;



