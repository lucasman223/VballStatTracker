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


















