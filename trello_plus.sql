-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas wygenerowania: 15 Lis 2014, 23:00
-- Wersja serwera: 5.5.32
-- Wersja PHP: 5.4.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Baza danych: `trello_plus`
--
CREATE DATABASE IF NOT EXISTS `trello_plus` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `trello_plus`;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `boards`
--

CREATE TABLE IF NOT EXISTS `boards` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Zrzut danych tabeli `boards`
--

INSERT INTO `boards` (`id`, `name`) VALUES
(1, '12'),
(2, 'tabliczka Paskudkowa'),
(3, 'taaaablicaaaa A');

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
(3, 11);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `lists`
--

CREATE TABLE IF NOT EXISTS `lists` (
  `id_list` int(255) NOT NULL AUTO_INCREMENT,
  `id_board` int(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id_list`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=57 ;

--
-- Zrzut danych tabeli `lists`
--

INSERT INTO `lists` (`id_list`, `id_board`, `name`) VALUES
(36, 1, 'pierwsza'),
(37, 1, 'druga'),
(38, 1, 'trzecia'),
(39, 1, 'czwarta'),
(40, 1, 'piata'),
(41, 1, 'listaaaa'),
(42, 1, 'nowa lista dla uzytkownika nr 11'),
(43, 1, 'kolejny test dla uzytkownika 11'),
(44, 1, 'test for 11'),
(45, 1, 'test for 11'),
(47, 1, 'listaa'),
(48, 1, 'dddd'),
(49, 1, 'lista'),
(50, 1, 'jjj'),
(51, 1, 'kiki'),
(52, 1, 'agowa'),
(53, 1, 'agowa2'),
(54, 1, 'aaaaaa'),
(55, 1, 'nowa Lista'),
(56, 1, 'nowa Lista druga');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tasks`
--

CREATE TABLE IF NOT EXISTS `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_list` int(11) NOT NULL,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=77 ;

--
-- Zrzut danych tabeli `tasks`
--

INSERT INTO `tasks` (`id`, `id_list`, `name`, `description`) VALUES
(60, 36, 'task', 'w liscie 1'),
(61, 37, 'task 2', 'w liscie 1'),
(62, 38, 'task ', 'w liscie 2'),
(63, 38, 'task', 'w liscie 3'),
(64, 38, 'task 2', 'w liscie 3'),
(65, 41, '1', ''),
(66, 38, '2', ''),
(67, 39, '3', ''),
(68, 36, '4', ''),
(69, 40, '5', ''),
(70, 39, '6', ''),
(71, 37, ':)', ''),
(72, 37, 'zadanie', 'opis zadania'),
(73, 41, 'zadanie', 'ooooopisss zadanka'),
(74, 43, 'Zadanie', 'opis zadania'),
(75, 55, 'Nowe zadanie', 'I jego opis'),
(76, 56, 'zadanko ', 'disis');

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
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=17 ;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`id`, `login`, `password`) VALUES
(11, 'test@test.com', '8f0e2f76e22b43e2855189877e7dc1e1e7d98c226c95db247cd1d547928334a9'),
(12, 'mateusz@gmail.co', '15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225'),
(13, 'adam@gmail.com', '15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225'),
(14, 'krzysiek@gmail.c', '8a9bcf1e51e812d0af8465a8dbcc9f741064bf0af3b3d08e6b0246437c19f7fb'),
(15, 'natalia@gmail.co', '582823a83bad94cc9ed4ffb3f09623c9397df0725f16528ac1ffc97cbb8d7183'),
(16, 'maciej@gmail.com', 'c60e6ab5dcab898050d3ba315bb0be83be0ce897d81ea2e8f08659b3c6738fc9');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
