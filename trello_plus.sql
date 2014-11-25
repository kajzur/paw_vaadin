-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 25 Lis 2014, 21:04
-- Server version: 5.5.36
-- PHP Version: 5.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

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
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Zrzut danych tabeli `boards`
--

INSERT INTO `boards` (`id`, `name`, `marked`) VALUES
(2, 'tabliczka Paskudkowa', 0),
(3, 'taaaablicaaaa A', 0),
(5, 'mojaTablica', 0),
(6, 'tab', 0);

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
(6, 1);

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
  PRIMARY KEY (`id_list`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=62 ;

--
-- Zrzut danych tabeli `lists`
--

INSERT INTO `lists` (`id_list`, `id_board`, `name`) VALUES
(55, 3, 'nowa Lista'),
(56, 2, 'nowa Lista druga'),
(58, 3, 'listaaAAAA'),
(60, 2, 'lista11'),
(61, 6, 'nowaLista');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `organizations`
--

CREATE TABLE IF NOT EXISTS `organizations` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Zrzut danych tabeli `organizations`
--

INSERT INTO `organizations` (`id`, `name`) VALUES
(1, 'organizacja'),
(2, 'organizacja2'),
(3, 'organizacja3');

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=88 ;

--
-- Zrzut danych tabeli `tasks`
--

INSERT INTO `tasks` (`id`, `id_list`, `name`, `description`, `marked`) VALUES
(75, 58, 'Nowe zadanie', 'I jego opis', 0),
(76, 56, 'zadanko ', 'disis', 0),
(79, 58, 'tytulLLLLL', 'opisSSSSS', 0),
(80, 55, 'kolejny task', 'wykonac', 0),
(84, 58, 't27', 'o27', 0),
(85, 55, 't27', 'o27', 0),
(86, 60, 'tytul t', 'opis o', 0),
(87, 61, 'task', 'description', 0);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tasks_users`
--

CREATE TABLE IF NOT EXISTS `tasks_users` (
  `id_task` int(11) NOT NULL,
  `id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tasks_users`
--

INSERT INTO `tasks_users` (`id_task`, `id_user`) VALUES
(77, 11),
(78, 11),
(87, 11),
(79, 17),
(85, 16),
(80, 14),
(75, 13),
(76, 17),
(87, 11),
(86, 11),
(86, 12),
(86, 13);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=23 ;

--
-- Zrzut danych tabeli `task_comments`
--

INSERT INTO `task_comments` (`id`, `task_id`, `created`, `user_id`, `content`) VALUES
(1, 65, '2014-11-20 19:21:05', 11, 'testowy content'),
(8, 65, '2014-11-20 21:09:45', 11, 'nwy'),
(9, 65, '2014-11-20 21:12:36', 11, 'fsfsdf'),
(10, 65, '2014-11-20 21:15:48', 11, 'sdsd'),
(11, 65, '2014-11-20 21:19:39', 11, 'sdsd'),
(12, 65, '2014-11-20 21:19:40', 11, 'sdsdsads'),
(13, 65, '2014-11-20 21:19:41', 11, 'sdsdsads'),
(14, 65, '2014-11-20 21:19:41', 11, 'sdsdsads'),
(15, 65, '2014-11-20 21:19:42', 11, 'sdsdsads'),
(16, 63, '2014-11-20 21:22:40', 11, 'pierwszy'),
(17, 69, '2014-11-20 21:40:43', 11, 'aa'),
(18, 60, '2014-11-20 22:14:39', 11, 'brum\n'),
(19, 71, '2014-11-23 00:04:26', 11, 'dasd'),
(20, 71, '2014-11-23 00:04:27', 11, 'dasdadasdas'),
(21, 71, '2014-11-23 00:04:29', 11, 'dsad'),
(22, 86, '2014-11-25 20:52:48', 11, 'komentt');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
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
(6, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
