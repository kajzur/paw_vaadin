-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 29 Lis 2014, 21:34
-- Server version: 5.5.36
-- PHP Version: 5.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `trello_plus`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `boards`
--

CREATE TABLE IF NOT EXISTS `boards` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `marked` int(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Zrzut danych tabeli `boards`
--

INSERT INTO `boards` (`id`, `name`, `marked`) VALUES
(3, 'Tablica A', 1),
(5, 'Tablica B', 0),
(6, 'Tablica C', 1),
(7, 'a', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `boards_organizations`
--

CREATE TABLE IF NOT EXISTS `boards_organizations` (
  `id_board` int(11) NOT NULL,
  `id_organization` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `boards_organizations`
--

INSERT INTO `boards_organizations` (`id_board`, `id_organization`) VALUES
(5, 3),
(3, 1),
(3, 2),
(3, 3),
(2, 1),
(2, 2),
(6, 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `boards_users`
--

CREATE TABLE IF NOT EXISTS `boards_users` (
  `id_board` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `boards_users`
--

INSERT INTO `boards_users` (`id_board`, `id_user`) VALUES
(2, 11),
(3, 11),
(5, 11),
(6, 11);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `lists`
--

CREATE TABLE IF NOT EXISTS `lists` (
  `id_list` int(255) NOT NULL AUTO_INCREMENT,
  `id_board` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id_list`),
  KEY `id_list` (`id_list`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=68 ;

--
-- Zrzut danych tabeli `lists`
--

INSERT INTO `lists` (`id_list`, `id_board`, `name`) VALUES
(62, 5, 'Lista 1'),
(63, 5, 'Lista 2'),
(64, 3, 'Lista 3'),
(65, 3, 'Lista 4'),
(66, 6, 'Lista 5'),
(67, 6, 'Lista 6');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `organizations`
--

CREATE TABLE IF NOT EXISTS `organizations` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Zrzut danych tabeli `organizations`
--

INSERT INTO `organizations` (`id`, `name`) VALUES
(1, 'organizacja'),
(2, 'organizacja2'),
(3, 'organizacja3'),
(4, 'organizacja 5');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tasks`
--

CREATE TABLE IF NOT EXISTS `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_list` int(11) NOT NULL,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `marked` int(255) NOT NULL DEFAULT '0',
  `lp` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `id_list` (`id_list`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=101 ;

--
-- Zrzut danych tabeli `tasks`
--

INSERT INTO `tasks` (`id`, `id_list`, `name`, `description`, `marked`, `lp`) VALUES
(97, 62, 'name', 'desc', 0, 0),
(98, 63, 'name', 'desc', 0, 2),
(99, 62, '22', '22', 0, 1),
(100, 62, '33', '33', 0, 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tasks_users`
--

CREATE TABLE IF NOT EXISTS `tasks_users` (
  `id_task` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `task_comments`
--

CREATE TABLE IF NOT EXISTS `task_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `created` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `content` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=8 ;

--
-- Zrzut danych tabeli `task_comments`
--

INSERT INTO `task_comments` (`id`, `task_id`, `created`, `user_id`, `content`) VALUES
(1, 92, '2014-11-27 15:16:45', 11, 'komentarz'),
(2, 92, '2014-11-27 15:16:52', 11, 'komentarz2'),
(3, 92, '2014-11-27 16:17:43', 11, 'ffffff'),
(4, 89, '2014-11-29 14:26:28', 11, 'ddddd'),
(5, 89, '2014-11-29 14:26:38', 11, 'kakakakakaka'),
(6, 89, '2014-11-29 14:26:46', 11, 'ujdiwkea'),
(7, 88, '2014-11-29 14:27:06', 11, 'dddddeddd');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=18 ;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`id`, `login`, `password`) VALUES
(11, 'test@test.com', '8f0e2f76e22b43e2855189877e7dc1e1e7d98c226c95db247cd1d547928334a9'),
(12, 'mateusz@gmail.co', '15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225'),
(13, 'adam@gmail.com', '15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225'),
(14, 'krzysiek@gmail.c', '8a9bcf1e51e812d0af8465a8dbcc9f741064bf0af3b3d08e6b0246437c19f7fb'),
(15, 'natalia@gmail.co', '582823a83bad94cc9ed4ffb3f09623c9397df0725f16528ac1ffc97cbb8d7183'),
(16, 'maciej@gmail.com', 'c60e6ab5dcab898050d3ba315bb0be83be0ce897d81ea2e8f08659b3c6738fc9'),
(17, 'mmilak@gmail.com', '8f0e2f76e22b43e2855189877e7dc1e1e7d98c226c95db247cd1d547928334a9');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users_organizations`
--

CREATE TABLE IF NOT EXISTS `users_organizations` (
  `id_user` int(11) NOT NULL,
  `id_organization` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Zrzut danych tabeli `users_organizations`
--

INSERT INTO `users_organizations` (`id_user`, `id_organization`) VALUES
(11, 1),
(11, 2),
(11, 3),
(2, 1),
(2, 2),
(6, 3);
