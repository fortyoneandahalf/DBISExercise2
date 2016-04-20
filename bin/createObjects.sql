INSERT INTO estateagent (login, name, address, password)
VALUES ("tom12", "Tom Sawyer", "Hamburg", "tom");

INSERT INTO estate (login, city, postalcode, street, streetnumber, squarearea)
VALUES ("tom12", "Hannover", "22527", "Hagenbeckstrasse", "10", 24.0),
("tom12", "Kiel", "22427", "Bahnhofstrasse", "25A", 32.0);

INSERT INTO apartment (id, floor, rent, rooms, balcony, builtinkitchen)
VALUES (1, 12, 300.0, 3, 0, 1);

INSERT INTO house (id, floors, price, garden)
VALUES (2, 2, 150000, 1);

INSERT INTO person(firstname, name, address)
VALUES ("Incredible", "Hulk", "Bremen"),
("Iron", "Man", "Berlin");

INSERT INTO contract(date, place)
VALUES (2016-04-01, "Hamburg"),
(2016-04-12, "Berlin");

INSERT INTO purchasecontract(contractno, noofinstallments, interestrate, houseid, personid)
VALUES (1, 12, .05, 2, 1);

INSERT INTO tenancycontract(contractno, startdate, duration, additionalcosts, apartmentid, personid)
VALUES (2, 2016-05-01, 12, 0, 1, 2);