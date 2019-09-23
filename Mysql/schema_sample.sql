-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 01, 2017 at 09:27 AM
-- Server version: 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id2133425_minip`
--

-- --------------------------------------------------------

--
-- Table structure for table `11answers`
--

CREATE TABLE `11answers` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `11answers`
--

INSERT INTO `11answers` (`id`, `byuser`, `content`, `votes`) VALUES
(11111, 'varun', 'Sample Answer 1', -1),
(11121, 'a', 'i know the ans', 0);

-- --------------------------------------------------------

--
-- Table structure for table `11doubts`
--

CREATE TABLE `11doubts` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL,
  `noa` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `11doubts`
--

INSERT INTO `11doubts` (`id`, `byuser`, `title`, `content`, `votes`, `noa`) VALUES
(1111, 'varun', 'Sample Doubt 1', 'Sample Doubt 1', 2, 1),
(1121, 'shreyas', 'working of logical', 'how does logical link control works', 1, 0),
(1131, 'dnt', 'sample doubt topic', 'sample doubt', 0, 0),
(1112, 'dnt', 'working', 'how does physical layer works', 1, 1),
(1115, 'a', 'physical layer', 'what is physical layer?', 2, 0);

-- --------------------------------------------------------

--
-- Table structure for table `11links`
--

CREATE TABLE `11links` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `url` varchar(512) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `11links`
--

INSERT INTO `11links` (`id`, `byuser`, `title`, `url`, `votes`) VALUES
(1111, 'varun', 'Sample Link 1', 'http://Samplelink1', -2),
(1121, 'shreyas', 'tutorialpoints', 'http://www.tutorialspoint.com', 0),
(1112, 'dnt', 'google', 'http://www.google.com', 1);

-- --------------------------------------------------------

--
-- Table structure for table `11tips`
--

CREATE TABLE `11tips` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `11tips`
--

INSERT INTO `11tips` (`id`, `byuser`, `title`, `content`, `votes`) VALUES
(1112, 'varun', 'Sample Tip', 'Sample Tip 1', 0),
(1131, 'dnt', 'how to do it', 'just click', -1);

-- --------------------------------------------------------

--
-- Table structure for table `12answers`
--

CREATE TABLE `12answers` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `12doubts`
--

CREATE TABLE `12doubts` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL,
  `noa` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `12links`
--

CREATE TABLE `12links` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `url` varchar(512) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `12tips`
--

CREATE TABLE `12tips` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `13answers`
--

CREATE TABLE `13answers` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `13doubts`
--

CREATE TABLE `13doubts` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL,
  `noa` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `13links`
--

CREATE TABLE `13links` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `url` varchar(512) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `13tips`
--

CREATE TABLE `13tips` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `21answers`
--

CREATE TABLE `21answers` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `21doubts`
--

CREATE TABLE `21doubts` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL,
  `noa` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `21links`
--

CREATE TABLE `21links` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `url` varchar(512) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `21tips`
--

CREATE TABLE `21tips` (
  `id` int(7) NOT NULL,
  `byuser` varchar(20) NOT NULL,
  `title` varchar(80) NOT NULL,
  `content` varchar(255) NOT NULL,
  `votes` int(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `subjects`
--

CREATE TABLE `subjects` (
  `id` int(3) NOT NULL,
  `name` varchar(30) NOT NULL,
  `nou` int(2) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subjects`
--

INSERT INTO `subjects` (`id`, `name`, `nou`) VALUES
(11, 'Computer Networks', 6),
(12, 'Database Management System', 6),
(13, 'Theory of Computation', 6),
(21, 'Microprocessor', 0);

-- --------------------------------------------------------

--
-- Table structure for table `units`
--

CREATE TABLE `units` (
  `id` int(4) NOT NULL,
  `name` varchar(50) NOT NULL,
  `nod` int(11) NOT NULL,
  `nol` int(11) NOT NULL,
  `noti` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `units`
--

INSERT INTO `units` (`id`, `name`, `nod`, `nol`, `noti`) VALUES
(111, 'Physical Layer', 5, 2, 2),
(112, 'Logic Link Control', 1, 1, 0),
(113, 'Medium Access Control', 1, 0, 1),
(114, 'Network Layer', 0, 0, 0),
(115, 'Transport Layer', 0, 0, 0),
(116, 'Application Layer', 0, 0, 0),
(121, 'Introduction', 0, 0, 0),
(122, 'SQL and PL/SQL', 0, 0, 0),
(123, 'Relational Database Design', 0, 0, 0),
(124, 'Database Transactions and Query Processing', 0, 0, 0),
(125, 'Parallel and Distributed Databases', 0, 0, 0),
(126, 'NoSQL Database', 0, 0, 0),
(131, 'Formal Language Theory and FA', 1, 0, 0),
(132, 'Regular Expressions (RE)', 0, 0, 0),
(133, 'CFG and Languages', 0, 0, 0),
(134, 'Turing Machines (TM)', 0, 0, 0),
(135, 'Pushdown Automata (PDA)', 0, 0, 0),
(136, 'Undecidablity & Intractable Problems', 0, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `emailid` varchar(255) DEFAULT NULL,
  `userid` int(5) NOT NULL,
  `user` varchar(20) NOT NULL,
  `pass` varchar(20) NOT NULL,
  `uora` varchar(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`emailid`, `userid`, `user`, `pass`, `uora`) VALUES
(NULL, 1, 'admin', 'admin', 'a'),
('varunwahi10@gmail.com', 3, 'varun', 'wahi', 'u'),
('shrey1812@gmail.com', 4, 'shreyas', 'shreyas', 'u'),
('tartednyanesh@gmail.com', 5, 'dnt', 'dnt', 'u'),
('himan21101998@gmail.com', 6, 'himan1308', 'himan', 'u'),
('warghadeamruta9130@gmail.com', 7, 'amruta', '1234', 'u'),
('nishant2397@gmail.com', 8, 'a', 'a', 'u');

-- --------------------------------------------------------

--
-- Table structure for table `userverification`
--

CREATE TABLE `userverification` (
  `code` int(6) NOT NULL,
  `emailid` varchar(255) NOT NULL,
  `datetime` datetime NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `votes`
--

CREATE TABLE `votes` (
  `username` varchar(20) NOT NULL,
  `contentid` int(10) NOT NULL,
  `type` varchar(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `votes`
--

INSERT INTO `votes` (`username`, `contentid`, `type`) VALUES
('varun', 1111, 'doubt'),
('varun', 1111, 'link'),
('varun', 1111, 'tip'),
('varun', 11111, 'answer'),
('shreyas', 1112, 'tip'),
('shreyas', 1111, 'link'),
('shreyas', 1111, 'doubt'),
('shreyas', 1121, 'doubt'),
('dnt', 1131, 'tip'),
('dnt', 1112, 'doubt'),
('dnt', 1112, 'link'),
('varun', 1112, 'doubt'),
('varun', 1112, 'tip'),
('himan1308', 1311, 'doubt'),
('amruta', 13111, 'answer'),
('himan1308', 13111, 'answer'),
('amruta', 1112, 'doubt'),
('himan1308', 1115, 'doubt'),
('dnt', 1115, 'doubt');

-- --------------------------------------------------------

--
-- Table structure for table `year`
--

CREATE TABLE `year` (
  `id` int(3) NOT NULL,
  `year` varchar(3) NOT NULL,
  `nos` int(2) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `year`
--

INSERT INTO `year` (`id`, `year`, `nos`) VALUES
(1, 'T.E', 3),
(2, 'SE', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`);

--
-- Indexes for table `year`
--
ALTER TABLE `year`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userid` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `year`
--
ALTER TABLE `year`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
