alter table skuespiller auto_increment = 1;
alter table selskap auto_increment = 1;
alter table video auto_increment = 1;
alter table person auto_increment = 1;
alter table bruker auto_increment = 1;
alter table episode auto_increment = 1;

insert into bruker (Navn) values ('Per');
insert into bruker (Navn) values ('Ole');
insert into bruker (Navn) values ('Per');


insert into Person (Navn, Fødselsdato) values('Al Pacino', '1940-10-01');
insert into Skuespiller values(1);
insert into Selskap (Land, Addresse, URL) values('USA', 'LA', 'www.paramount.com');
insert into Kategori values('Mafia-thriller');
insert into Videotype values('Kino');
insert into Video (Tittel, Beskrivelse, Lansdato, SelskapID, Videotype) values ('The Godfather', 
'Michael Corleone tries to resist the culture of his family,
 but can he succeed when things go wrong?', '1972-10-01', 1, 'Kino');
insert into Kategorisert values('Mafia-thriller',1);
insert into SpillerI values (1, 1, 'Michael Corleone');
insert into Film values (200, 1972, 1);