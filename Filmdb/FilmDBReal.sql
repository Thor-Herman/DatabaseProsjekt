drop tables Videotype, Selskap, Video, Film, Serie, Bruker, Musikk, Kategori, 
Episode, Person, Skuespiller, 
Forfatter, Regissør, SpillerI, Regisserer, SkrevetManus, HarMusikk, KommentertFilm,
VurdertFilm, KommentertEpisode, VurdertEpisode, Kategorisert;



create table Selskap(
	SelskapID int not null auto_increment,
    Land varchar(50),
    Addresse varchar(70),
    URL varchar(30),
    primary key(SelskapID)
);

create table Videotype(
	Navn varchar(20) not null primary key
);

create table Video(
	VideoID int not null primary key auto_increment,
    Tittel varchar(40),
    Beskrivelse varchar(300),
    LansDato varchar(20),
    SelskapID int not null,
	Videotype varchar(20) not null,
    foreign key (SelskapID) references Selskap(SelskapID),
    foreign key (Videotype) references Videotype(Navn)
);

create table Film(
    Lengde int,
    UtgÅr int,
    VideoID int not null primary key,
    foreign key (VideoID) references Video(VideoID)
);

create table Serie(
    VideoID int not null primary key,
    foreign key (VideoID) references Video(VideoID)
);


    


create table Bruker(
	BrukerID int not null primary key auto_increment,
    Navn varchar(40)
);

create table Musikk(
	MusikkID int not null primary key,
    Komponist varchar(40),
    Fremført date
);

create table Kategori(
	KategoriNavn varchar(20) not null primary key
);

create table Episode(
	EpisodeID int not null primary key auto_increment,
    EpisodeNr int not null,
    UtgÅr int,
    Sesong int,
    Tittel varchar(30) not null,
    Beskrivelse varchar(300),
    VideoID int not null,
    foreign key (VideoID) references Serie(VideoID) on update cascade on delete cascade
);

create table Person(
	PersonNr int not null primary key auto_increment,
    Navn varchar(40),
    Fødselsdato date
);

create table Skuespiller(
	PersonNr int not null primary key,
    foreign key (PersonNr) references Person(PersonNr)
);

create table Forfatter(
	PersonNr int not null primary key,
    foreign key (PersonNr) references Person(PersonNr)
);

create table Regissør(
	PersonNr int not null primary key,
    foreign key (PersonNr) references Person(PersonNr)
);


create table SpillerI(
	PersonNr integer not null,
    VideoID integer not null,
    Rolle varchar(30),
    constraint spiller_i_pk primary key (PersonNr, VideoID),
    foreign key (PersonNr) references Skuespiller(PersonNr) on update cascade on delete cascade,
    foreign key (VideoID) references Video(VideoID)
);

create table Regisserer(
	PersonNr integer not null,
    VideoID integer not null,
    constraint regisserer_pk primary key (PersonNr, VideoID),
    foreign key (PersonNr) references Regissør(PersonNr) on update cascade on delete cascade,
    foreign key (VideoID) references Video(VideoID)
);

CREATE TABLE SkrevetManus (
    PersonNr INTEGER NOT NULL,
    VideoID INTEGER NOT NULL,
    CONSTRAINT skrevet_manus_pk PRIMARY KEY (PersonNr , VideoID),
    foreign key (PersonNr) references Forfatter(PersonNr) on update cascade on delete cascade,
    foreign key (VideoID) references Video(VideoID)
);

create table HarMusikk (
    MusikkID integer not null,
    VideoID integer not null,
    constraint har_musikk_pk primary key (MusikkID, VideoID),
    foreign key (MusikkID) references Musikk(MusikkID),
    foreign key (VideoID) references Video(VideoID)
);

create table KommentertFilm (
	BrukerID integer not null, 
    VideoID integer not null,
    Kommentar varchar(300),
    constraint kommentert_film_id primary key (BrukerID, VideoID),
    constraint kommentert_film_fk foreign key (BrukerID) references Bruker(BrukerID),
    constraint fk_video_id foreign key (VideoID) references Film(VideoID) on update cascade on delete cascade
);

create table VurdertFilm (
	BrukerID integer not null,
    VideoID integer not null,
    Vurdering integer,
    constraint vurdert_film_id primary key (BrukerID, VideoID),
    constraint vurdering_film_1_10 check (Vurdering < 11 and Vurdering > 0),
    constraint vurdert_film_fk foreign key(BrukerID) references Bruker(BrukerID),
    constraint video_id_fk foreign key (VideoID) references Film(VideoID) on update cascade on delete cascade
);

create table KommentertEpisode (
	BrukerID integer not null,
    EpisodeID integer not null,
    Kommentar varchar(300),
    constraint kommentert_episode_id primary key (BrukerID, EpisodeID),
    constraint kommentert_episode_fk foreign key (BrukerID) references Bruker(BrukerID),
    constraint fk_episode_id foreign key (EpisodeID) references Episode(EpisodeID) on update cascade on delete cascade
);

create table VurdertEpisode (
	BrukerID integer not null,
	EpisodeID integer not null,
    Vurdering integer,
    constraint kommentert_episode_id primary key (BrukerID, EpisodeID),
    constraint vurdering_episode_1_10 check (Vurdering < 11 and Vurdering > 0),
    constraint vurdert_episode_fk foreign key (BrukerID) references Bruker(BrukerID),
    constraint episode_id_fk foreign key (EpisodeID) references Episode(EpisodeID) on update cascade on delete cascade
);

create table Kategorisert (
	KategoriNavn varchar(20) not null,
    VideoID integer not null,
    constraint kategorisert_pk primary key (KategoriNavn, VideoID),
    constraint kategorisert_fk foreign key (KategoriNavn) references Kategori(KategoriNavn),
    constraint video_fk_id_episode foreign key (VideoID) references Video(VideoID) on update cascade on delete cascade
);



