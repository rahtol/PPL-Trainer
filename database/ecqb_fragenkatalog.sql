--
-- Database: ecqb_fragenkatalog
--

CREATE DATABASE ecqb_fragenkatalog;

USE ecqb_fragenkatalog;

-- --------------------------------------------------------

CREATE TABLE `question` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `no` int(11) NOT NULL,
  `category` char(3) NOT NULL,
  `question` varchar(800) NOT NULL,
  `resource` varchar(256)
);

CREATE TABLE `response` (
  `id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `question_id` int(11) NOT NULL,
  `response` varchar(200) NOT NULL
);
