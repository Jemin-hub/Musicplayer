-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 29, 2024 at 06:20 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `musicplayer`
--

-- --------------------------------------------------------

--
-- Table structure for table `english`
--

CREATE TABLE `english` (
  `songno` int(11) NOT NULL,
  `songname` varchar(20) NOT NULL,
  `songapp` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `english`
--

INSERT INTO `english` (`songno`, `songname`, `songapp`) VALUES
(1, 'brokenangal', 'spotify'),
(2, 'Let Me Down Slowly', 'spotify');

-- --------------------------------------------------------

--
-- Table structure for table `gujrati`
--

CREATE TABLE `gujrati` (
  `songno` int(11) NOT NULL,
  `songname` varchar(20) NOT NULL,
  `songapp` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gujrati`
--

INSERT INTO `gujrati` (`songno`, `songname`, `songapp`) VALUES
(1, 'Dwarka Na Dev', 'spotify');

-- --------------------------------------------------------

--
-- Table structure for table `hindi`
--

CREATE TABLE `hindi` (
  `songno` int(11) NOT NULL,
  `songname` varchar(20) NOT NULL,
  `songapp` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hindi`
--

INSERT INTO `hindi` (`songno`, `songname`, `songapp`) VALUES
(1, 'malangsajana', 'spotify'),
(2, 'Deva Deva', 'spotify'),
(3, 'Heeriye', 'spotify'),
(4, 'Husan', 'spotify');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `english`
--
ALTER TABLE `english`
  ADD PRIMARY KEY (`songno`);

--
-- Indexes for table `gujrati`
--
ALTER TABLE `gujrati`
  ADD PRIMARY KEY (`songno`);

--
-- Indexes for table `hindi`
--
ALTER TABLE `hindi`
  ADD PRIMARY KEY (`songno`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `english`
--
ALTER TABLE `english`
  MODIFY `songno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `gujrati`
--
ALTER TABLE `gujrati`
  MODIFY `songno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `hindi`
--
ALTER TABLE `hindi`
  MODIFY `songno` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
