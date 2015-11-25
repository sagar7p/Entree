DROP DATABASE Entree;
CREATE DATABASE Entree;

USE Entree;

-- this creates a table
CREATE TABLE Recipes (
    Title VARCHAR(40) primary key,
    Username VARCHAR(30) not null,
    Ingredients VARCHAR(4000),
    Instructions VARCHAR(4000),
    Reheats INT(11),
    Featured BOOL,
    TimeSlot VARCHAR(100)
);

-- insert Recipes
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Pizza','@sagar','Tomatoes,Cheese,Bread','Roll|Bake',10,True,'20150815234545');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Tacos','@sagar','Beans,Cheese,Salsa,Lettuce','Make it|Heat it|Eat it',8,False,'20150815234545');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Quesadillas','@kevin','Tortillas,Salsa,Cream Cheese','Put stuff|Eat',3,True,'20150815234546');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Muffins','@kevin','Bread,Cranberries,Flour,Sugar','Roll|Bake|Eat',14,False,'20150815234547');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Pad Thai','@samp','Noodles,Chicken,Veggies,Spices','Bake it together|Eat as much as you want',2,True,'20150815234548');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Chipotle','@samp','Lettuce,Tomatoes,Salsa,Corn,Chicken','Put everything together|Eat as much as you want',100,False,'20150815234549');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Cookies','@matt','Sugar,Chocalate,Dough','Bake it|Make it|Eat the delicious cookies',10,True,'20150815234550');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Sandwiches','@matt','Bread,Beef,Tomatoes,Lettuce','Put it all together|Roll it|Eat it',14,False,'20150815234551');

INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Curry','@sagar','Tomatoes,Cheese,Spices','Roll|Bake',12,True,'20150815234552');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Pop Tart','@kevin','Sugar,Bread,Sprinkles','Toast it|Eat it',41,False,'20150815234553');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Pumpkin Pie','@samp','Pumpkins,Bread,Sauce,Sprinkles','Roll|Bake|Eat',17,False,'20150815234554');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Turkey','@matt','Turkey,Gravy','Bake it together|Put the gravy in|Eat as much as you want',2,True,'2015081523455');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Tandoori Chicken','@sahil','Chicken,Spices,Pan','Mix it|Bake it|Put spices together',140,True,'20150815234556');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Dal','@sahil','Beans,Gravy,Spices','Mix it|Cook it|Eat it',5,False,'20150815234557');
INSERT INTO Recipes(Title, Username, Ingredients, Instructions, Reheats, Featured, TimeSlot) VALUES ('Ribs','@sahil','Meat,bones,spices','Put it all together|Grill it|Eat it',5,False,'20150815234558');

CREATE TABLE Users (
    Username VARCHAR(30) primary key,
    Email VARCHAR(30) not null,
    Pass INT(11) not null,
    Food VARCHAR(30) not null,
    Recipes VARCHAR(300),
    Followers VARCHAR(300),
    Following VARCHAR(300),
	Grocery VARCHAR(300),
    FirstName VARCHAR(200),
    LastName VARCHAR(200)

    
);

-- insert Users
INSERT INTO Users(Username, Email, Pass, Food, Recipes, Followers, Following, Grocery, FirstName, LastName) VALUES ('@sagar','sagar@email.com','106438207','Pizza','Pizza,Tacos,Curry','@kevin,@matt,@samp,@sahil','@kevin,@matt,@samp','Tomatoes,Cheese,Beans','Sagar','Punhani');
INSERT INTO Users(Username, Email, Pass, Food, Recipes, Followers, Following, Grocery, FirstName, LastName) VALUES ('@kevin','kevin@email.com','106438208','Quesidilla','Quesadillas,Muffins,Pop Tart','@sagar,@matt,@samp,@sahil','@sagar,@matt,@samp,@sahil','Ice Cream,Bananas,Cookies','Kevin','Wang');
INSERT INTO Users(Username, Email, Pass, Food, Recipes, Followers, Following, Grocery, FirstName, LastName) VALUES ('@samp','samp@email.com','106438209','Chipotle','Pad Thai,Chipotle,Pumpkin Pie','@kevin,@sagar,@matt,@sahil','@kevin,@sagar,@matt,@sahil','Tomatoes,Bean,Rice','Sampurna','Basu');
INSERT INTO Users(Username, Email, Pass, Food, Recipes, Followers, Following, Grocery, FirstName, LastName) VALUES ('@matt','matt@email.com','106438210','Cookies','Cookies,Sandwiches,Turkey','@kevin,@samp,@sagar,@sahil','@kevin,@samp,@sagar,@sahil','Veggies,Chicken,Lettuce','Matt','Wenner');
INSERT INTO Users(Username, Email, Pass, Food, Recipes, Followers, Following, Grocery, FirstName, LastName) VALUES ('@sahil','sahil@email.com','106438211','Dal','Tandoori Chicken,Dal,Ribs','@kevin,@matt,@samp','@kevin,@matt,@sagar,@samp','Beans,Chicken,Bones','Sahil','Lele');
INSERT INTO Users(Username, Email, Pass, Food) VALUES ('@guest','guest@email.com','guest','guest');



