use moviedb;
DELIMITER $$ 

CREATE PROCEDURE add_star (IN starName VARCHAR(100), IN birthyear INT(11))
BEGIN
DECLARE maxid VARCHAR(100);
SET maxid = CONCAT("nm", 1+ substring((select max(id) from stars), 3,10));
   IF (birthYear is null or birthYear = '') THEN
      INSERT into stars (id,name) VALUES (maxid, starName);
   ELSE
      INSERT into stars (id,name,birthYear) VALUES (maxid, starName, birthYear);
   END IF;
END
$$
DELIMITER ; 
