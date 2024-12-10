\c postgres;
DROP DATABASE hotel_db;
CREATE DATABASE hotel_db;
\c hotel_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Persons (
    PersonID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(50) NOT NULL,
    Phone VARCHAR(13) NOT NULL UNIQUE,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Address TEXT NULL,
    IsDeleted bool DEFAULT FALSE
);


--dumy insert
INSERT INTO Persons(name, email, phone, address)
values('ahmed', 'fack@gmail.com', '735501225', 'fack');
--



CREATE TABLE PersonUpdated (
    PersonUpdateID BIGSERIAL PRIMARY KEY,
    PreviusName VARCHAR(50) NULL,
    PreviusPhone VARCHAR(13) NOT NULL,
    PreviusAddress TEXT NULL,
    CurrentName VARCHAR(50) NULL,
    CurrentPhone VARCHAR(13) NULL,
    CurrentAddress TEXT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PersonID UUID NOT NULL REFERENCES Persons (PersonID)
);

--


--
CREATE OR REPLACE FUNCTION fn_personUpdate_modi() RETURNS TRIGGER AS $$ BEGIN RETURN NULL;
END $$ LANGUAGE plpgsql;


--


--
CREATE TRIGGER tr_personUpdate_update BEFORE
UPDATE ON PersonUpdated FOR EACH ROW EXECUTE FUNCTION fn_personUpdate_modi();

--

--
CREATE TRIGGER tr_personUpdate_delete 
BEFORE DELETE ON PersonUpdated 
FOR EACH ROW EXECUTE FUNCTION fn_personUpdate_modi();


CREATE OR REPLACE FUNCTION fn_person_update () 
RETURNS TRIGGER 
AS $$
BEGIN 

UPDATE Persons
SET 
name = CASE WHEN OLD.name <> NEW.name THEN NEW.name ELSE OLD.name END,
phone = CASE WHEN  OLD.phone <> NEW.phone THEN NEW.phone ELSE OLD.phone END,
address = CASE WHEN OLD.address <> NEW.address THEN  NEW.address ELSE OLD.address END 
where PersonID = OLD.personid;

INSERT INTO PersonUpdated ( previusName, previusPhone, previusAddress, currentName, currentPhone, currentAddress, PersonID)
VALUES ( OLD.Name, OLD.Phone, OLD.Address, CurrentName, CurrentPhone, CurrentAddress, OLD.PersonID);

RETURN NULL;
EXCEPTION
WHEN OTHERS THEN -- Handle exceptions with a warning
RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

--

--
CREATE OR REPLACE FUNCTION fn_person_delete() RETURNS TRIGGER AS $$ BEGIN
UPDATE Persons
SET isdeleted = true
where personid = OLD.personid;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

--

--
CREATE TRIGGER tr_person_deleted BEFORE DELETE ON Persons FOR EACH ROW EXECUTE FUNCTION fn_person_delete ();

--

--
CREATE TRIGGER tr_peSrson_update BEFORE
UPDATE ON Persons FOR EACH ROW EXECUTE FUNCTION fn_person_update ();

--

--
--dumy update
UPDATE Persons
SET name = 'fackkdddd'
WHERE personid = '62597b4d-838d-4455-86f9-bd97b525b567';


--
CREATE TABLE Admins 
(
    AdminID UUID PRIMARY KEY,
    PersonID UUID NOT NULL REFERENCES Persons (PersonID),
    UserName VARCHAR(50) NOT NULL,
    Password TEXT NOT NULL
);

--

--
CREATE OR REPLACE FUNCTION fn_admin_get_by_id(id UUID) RETURNS TABLE(
        adminid UUID,
        personid UUID,
        name TEXT,
        phone TEXT,
        address TEXT,
        username TEXT
    ) AS $$ BEGIN RETURN QUERY
SELECT ad.adminid as adminid,
    ad.personid as personid,
    per.name as name,
    per.phone as phone,
    per.address as address,
    ad.username as userName
FROM Admins ad
    INNER JOIN Persons per ON ad.personid = per.personid
WHERE ad.adminid = id;
-- Assuming you want to filter by adminId
END;
$$ LANGUAGE plpgsql;

--



--
CREATE OR REPLACE FUNCTION fn_admin_get_username_password(username_a VARCHAR(50), password_a TEXT)
RETURNS TABLE(
        adminid UUID,
        personid UUID,
        name VARCHAR(50),
        phone VARCHAR(13),
        address TEXT,
        email VARCHAR(100),
        username VARCHAR(50)
    ) 

AS $$ 
BEGIN 
RETURN QUERY
    SELECT
        ad.adminid ,
        ad.personid ,
            per.name ,
            per.phone,
            per.address ,
            ad.username ,
            per.email
        FROM 
            admins ad
            INNER JOIN 
            persons per 
                ON ad.personid = per.personid
        WHERE  ad.username = username_a OR per.email = username_a
        AND ad.password =  password_a;
        EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
 
END;
$$ LANGUAGE plpgsql;


SELECT ad.adminid as adminid,
    ad.personid as personid,
    per.name as name,
    per.phone as phone,
    per.address as address,
    ad.username as userName,
    per.email
FROM Admins ad
    INNER JOIN Persons per ON ad.personid = per.personid
WHERE (ad.username = 'sdaf' OR per.email = 'sdaf')
    AND ad.password = '71620152A16EBE4E23FB4C550510C41113E41E4E58352718527BF859A91603B5';


-- {
--  "userName": "asdfasdfsdaf",
--  "password": "ASdf!@12",
--  "name": "ffffff",
--  "email": "gakkkkk@gmail.com",
--  "phone": "7894561234",
--  "address": "sdafasdfdasf"
-- }

--
CREATE OR REPLACE FUNCTION fn_admin_insert (
        adminid_n UUID,
        name VARCHAR(50),
        phone VARCHAR(13),
        email VARCHAR(100),
        address TEXT,
        username varchar(50),
        password TEXT
    ) RETURNS INT AS $$
DECLARE person_id UUID;
BEGIN BEGIN
INSERT INTO persons(name, email, phone, address)
values (name, email, phone, address)
RETURNING personid INTO person_id;
INSERT INTO Admins (adminid, personid, username, password)
VALUES (adminid_n, person_id, username, password);
RETURN 1;
EXCEPTION
WHEN OTHERS THEN -- Handle exceptions with a warning
RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN 0;
END;
END;
$$ LANGUAGE plpgsql;
--

--
select "fn_admin_insert" (
        uuid_generate_v4(),
        'fadsf',
        'fffffff',
        'ffff@gmail.com',
        'fffffff',
        'lommmmmm',
        '735555555asfd'
    );



--
CREATE OR REPLACE FUNCTION fn_admin_update (
        adminid UUID,
        name VARCHAR(50),
        phone VARCHAR(13),
        address TEXT,
        personid BIGINT,
        username varchar(50),
        password TEXT
    ) RETURNS INT AS $$
DECLARE person_id BIGINT;
BEGIN
UPDATE persons
SET name = name,
    phone = phone,
    address = address
WHERE personid = OLD.personid;
UPDATE admin
SET username = username,
    password = password
where adminid = adminid;
return 1;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN 0;
END;
$$ LANGUAGE plpgsql;
--


---
CREATE OR REPLACE FUNCTION fn_admin_delete() RETURNS TRIGGER AS $$ 
BEGIN 
RETURN null;
END;
$$ LANGUAGE plpgsql;

--


-- CREATE TRIGGER tr_admin_insert 
-- BEFORE INSERT ON Admins 
-- FOR EACH ROW EXECUTE FUNCTION fn_admin_insert();
CREATE TRIGGER tr_admin_update BEFORE
UPDATE ON Admins FOR EACH ROW EXECUTE FUNCTION fn_admin_update();
--

--
CREATE TRIGGER tr_admin_delete BEFORE DELETE ON Admins FOR EACH ROW EXECUTE FUNCTION fn_admin_delete ();
--


--dumy insert 
INSERT INTO Admins(adminid, personid, username, password)
values (
        uuid_generate_v4(),
        '62597b4d-838d-4455-86f9-bd97b525b567 ',
        'facknice',
        '771ali@..'
    );
--

--
CREATE TABLE AdminUpdate (
    AdminUpdateID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    AdminID UUID NOT NULL REFERENCES Admins (AdminID),
    PreviusUserName VARCHAR(50) NOT NULL,
    PreviusPassword VARCHAR(50) NOT NULL,
    CurrentUserName VARCHAR(50) NULL,
    CurrentPassword VARCHAR(50) NULL
);

--

--
CREATE OR REPLACE FUNCTION fn_adminUpdate_modi() RETURNS TRIGGER AS $$ BEGIN RETURN NULL;
END $$ LANGUAGE plpgsql;

--


--
CREATE TRIGGER tr_adminUpdate_update BEFORE
UPDATE ON AdminUpdate FOR EACH ROW EXECUTE FUNCTION fn_adminUpdate_modi();
--

--
CREATE TRIGGER tr_adminUpdate_delete BEFORE DELETE ON AdminUpdate FOR EACH ROW EXECUTE FUNCTION fn_adminUpdate_modi();
--

--
CREATE OR REPLACE FUNCTION fn_admin_update() RETURNS TRIGGER AS $$
DECLARE currentUserName VARCHAR(50) := NULL;
currentPassword VARCHAR(50) := NULL;
BEGIN -- Corrected NULL checks
IF NEW.username IS NOT NULL THEN currentUserName := NEW.username;
END IF;
IF NEW.Password IS NOT NULL THEN currentPassword := NEW.Password;
END IF;
-- Insert the old and new values into AdminUpdate
INSERT INTO AdminUpdate (
        AdminID,
        PreviusUserName,
        PreviusPassword,
        CurrentUserName,
        CurrentPassword
    )
VALUES (
        OLD.AdminID,
        OLD.UserName,
        OLD.Password,
        currentUserName,
        currentPassword
    );
RETURN NEW;
EXCEPTION
WHEN OTHERS THEN -- Handle exceptions with a warning
RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
--

--

CREATE  TABLE Users (
    UserID UUID PRIMARY KEY,
    DateOfBirth DATE NOT NULL,
    UserName VARCHAR(50) NOT NULL,
    Password TEXT NOT NULL,
    IsVIP bool DEFAULT FALSE,
    PersonID UUID NOT NULL REFERENCES Persons (personid),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedDate TIMESTAMP NULL,
    DeletedByItSelf BOOLEAN DEFAULT FALSE
);
--

--
CREATE TABLE UserUpdate(
    userUpdateID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    userId UUID REFERENCES Users(userid),
    currentUsername VARCHAR(50),
    currentpassword TEXT,
    currentIsVIP bool,
    username VARCHAR(50),
    password TEXT,
    IsVIP bool
);
--


--
CREATE  VIEW usersView AS
SELECT per.PersonID,
    per.Name,
    per.Phone,
    per.Email,
    per.Address,
    per.IsDeleted AS isPersonDeleted,
    us.UserID,
    us.UserName,
    us.Password,
    us.DateOfBirth,
    us.IsVIP
FROM Persons per
    INNER JOIN Users us ON per.PersonID = us.PersonID;
--

--  
CREATE OR REPLACE FUNCTION fn_user_insert (
        userId_u UUID,
        name VARCHAR(50),
        phone VARCHAR(13),
        email VARCHAR(100),
        address TEXT,
        username varchar(50),
        password TEXT,
        IsVIP bool,
        DateOfBirth DATE
    ) 
RETURNS INT 
AS $$
DECLARE 
person_id UUID;
BEGIN

INSERT INTO persons(name, email, phone, address)
VALUES (name, email, phone, address)
RETURNING personid INTO person_id;
INSERT INTO Users (
        userId,
        DateOfBirth,
        UserName,
        Password,
        IsVIP,
        personid
    ) VALUES( userId_u,DateOfBirth, UserName, Password, IsVIP, person_id); RETURN 1;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN 0;
END;
$$ LANGUAGE plpgsql;
--


--
CREATE OR REPLACE FUNCTION fn_user_update()
RETURNS TRIGGER 
AS $$
DECLARE
    userid_u UUID;
    name_u VARCHAR(50);
    phone_u VARCHAR(13);
    address_u TEXT;
    username_u varchar(50);
    password_u TEXT;
    IsVIP_u bool;
    personid_u UUID ;
BEGIN

   UPDATE persons SET 
   name = CASE WHEN name <> name_u THEN name_u ELSE name END, 
   phone = CASE WHEN phone <> phone_u THEN phone_u ELSE phone END, 
   address = CASE WHEN address <> address_u THEN address_u ELSE address END
   WHERE personid = person_id;

   UPDATE  users
   SET username = CASE WHEN username_u <> username THEN username_u ELSE username END,
    password = CASE WHEN password_u <> password THEN passowrd_u ELSE password END,
    username = CASE WHEN IsVIP_u <> isvip THEN IsVIP_u ELSE isvip END
   WHERE userid=userid_u;

   INSERT INTO UserUpdate(userId ,currentUsername , currentpassword , currentIsVIP, username , password , IsVIP ) 
   VALUES ( userid_u,OLD.username,OLD.password,OLD.isvip,NEW.username,NEW.password,NEW.svip);

   RETURN NEW;

   EXCEPTION
     WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
   RETURN NULL;
END;
$$LANGUAGE plpgsql;

--

--

CREATE OR REPLACE FUNCTION fn_user_deleted()
RETURNS TRIGGER  
AS $$
DECLARE
is_deleted BOOLEAN :=false;
BEGIN

  SELECT isdeleted
  INTO is_deleted
  FROM persons WHERE personid = OLD.personid;

  IF is_deleted = TRUE THEN 
  DELETE FROM users WHERE userid = OLD.userid;

  END IF;

  RETURN NULL;

  EXCEPTION
     WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
     RETURN NULL;
END;
$$LANGUAGE plpgsql;

CREATE TRIGGER tr_user_delete
BEFORE DELETE
ON Users FOR EACH ROW
EXECUTE FUNCTION fn_user_deleted();

CREATE  TRIGGER tr_user_update 
BEFORE UPDATE on users
FOR EACH ROW EXECUTE FUNCTION fn_user_update();
--



--
CREATE OR REPLACE FUNCTION isExistById(id UUID)
RETURNS BOOLEAN 
AS $$
DECLARE
email VARCHAR(100); 
personid UUID;
BEGIN
SELECT EXISTS(
        SELECT 1
        FROM persons per
        INNER JOIN admins ad ON per.personid = ad.personid
        WHERE ad.adminid = id

        UNION ALL

        SELECT 1
        FROM persons per
        INNER JOIN users us ON per.personid = us.personid
        WHERE us.userid = id
    )
    INTO email;

    RETURN email;

RETURN is_exist_id;
  EXCEPTION
     WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
   RETURN FALSE;

END;
$$LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION isExistByIdAndEmail(email_hold VARCHAR(100),id UUID)
RETURNS BOOLEAN
AS $$
DECLARE 
isExist INT; 
BEGIN
SELECT COUNT(*)
INTO isExist
FROM(
    SELECT * FROM 
    persons per 
    LEFT JOIN admins ad  ON per.personid = ad.personid 
    LEFT JOIN users use  ON per.personid = use.personid 
    WHERE per.email = email_hold AND ad.adminid = id OR use.userid =id
)d;
RETURN isExist>=1;
  EXCEPTION
     WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
   RETURN FALSE;
END;
$$LANGUAGE plpgsql;

--

CREATE TABLE Departments (
    DepartmentID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL
);

--

--

CREATE TABLE Permissions (
    PermissionID BIGSERIAL PRIMARY KEY,
    PermissionNum INT NOT NULL,
    DESCRIPTION VARCHAR(50)
);

--

--
CREATE TABLE Departments (
    DepartmentID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50)
);
--

--
CREATE TABLE Employees (
    EmployeeID BIGSERIAL PRIMARY KEY,
    HireDate DATE DEFAULT CURRENT_DATE,
    Salary NUMERIC(10, 2) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PersonID BIGINT NOT NULL REFERENCES Persons (PersonID),
    DepartmentID BIGINT NOT NULL REFERENCES Departments (DepartmentID),
    PermissionID BIGINT NOT NULL REFERENCES Permissions (PermissionID),
    PersonID BIGINT NOT NULL REFERENCES Persons (PersonID),
    AddBy BIGINT NULL REFERENCES Employees (EmployeeID),
    ModifiedBy BIGINT NULL REFERENCES Employees (EmployeeID),
    IsDeleted bool DEFAULT FALSE
);

--


--
CREATE TABLE EmployeePermissions (
    EmployeePermissions BIGSERIAL PRIMARY KEY,
    PermissionID BIGINT NOT NULL REFERENCES PermissionS (PermissionID),
    EmployeeID BIGINT NULL REFERENCES Employees (EmployeeID)
);

--


--
CREATE TABLE EmployeeDeltedBy (
    EmployeeDeltedByID BIGSERIAL PRIMARY KEY,
    DeletedBy BIGINT NULL REFERENCES Employees (EmployeeID),
    EmployeeDeletedID BIGINT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--


--this trigger to handle if employee deleted
CREATE OR REPLACE FUNCTION fn_employee_deleted() RETURNS TRIGGER AS $$ BEGIN IF OLD.IsDeleted = FALSE
    AND OLD.DeletedBy IS NOT NULL THEN
UPDATE Employees
SET IsDeleted = TRUE,
    DeletedBy = OLD.DeletedBy,
    DeletedDate = CURRENT_TIMESTAMP
WHERE EmployeeID = OLD.EmployeeID;
INSERT INTO EmployeeDeletedBy (DeletedBy, EmployeeDeletedID)
VALUES (OLD.DeletedBy, OLD.EmployeeID);
END IF;
RETURN OLD;
EXCEPTION
WHEN OTHERS THEN RETURN NULL;
END;
$$LANGUAGE plpgsql;
--


--
CREATE TRIGGER tr_employee_deleted BEFORE DELETE ON Employees FOR EACH ROW EXECUTE FUNCTION handle_employee_deleted ();
--


--
RoomTypes (
    RoomTypeID BIGSERIAL PRIMARY KEY,
    TypeName VARCHAR(50) NOT NULL,
    Description TEXT,
    CreatedBy INT NOT NULL REFERENCES Employees (EmployeeID),
    DeletedBy INT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedAt TIMESTAMP NULL,
    IsDeleted bool DEFAULT FALSE,
);

--


--
CREATE TABLE Rooms (
    RoomID BIGSERIAL PRIMARY KEY,
    RoomNumber VARCHAR(10) UNIQUE NOT NULL,
    Status VARCHAR(10) CHECK (
        Status IN ('Available', 'Booked', 'Under Maintenance')
    ) DEFAULT 'Available',
    PricePerOneDay NUMERIC(10, 2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    RoomTypeID INT NOT NULL REFERENCES RoomTypes (RoomTypeID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees (EmployeeID),
    CreatedBy BIGINT NOT NULL REFERENCES Users (UserID),
    DeletedBy BIGINT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedAt TIMESTAMP NULL,
    ExcptedAt TIMESTAMP NULL,
);

--

--

CREATE TABLE Bookings (
    BookingID BIGSERIAL PRIMARY KEY,
    Dayes INT NOT NULL,
    BookingStatus VARCHAR(50) CHECK (
        BookingStatus IN ('Pending', 'Confirmed', 'Cancelled')
    ) DEFAULT 'Pending',
    FristPayment NUMERIC(10, 2) NOT NULL,
    TotalPrice NUMERIC(10, 2),
    ServicePayment NUMERIC(10, 2) DEFAULT 0,
    MaintincePayment NUMERIC(10, 2) DEFAULT 0,
    PaymentStatus VARCHAR(50) CHECK (PaymentStatus IN ('Paid', 'Unpaid', 'Partial')) DEFAULT 'Unpaid',
    LeaveDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UserID INT NOT NULL REFERENCES Users (UserID),
    RoomID INT NOT NULL REFERENCES Rooms (RoomID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees (EmployeeID),
    CreatedBy BIGINT NOT NULL REFERENCES Users (UserID),
    DeletedBy BIGINT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedAt TIMESTAMP NULL,
    ExcptedAt TIMESTAMP NULL,
);

--

--

CREATE TABLE Reviews (
    ReviewID BIGSERIAL PRIMARY KEY,
    Rating INT CHECK (
        Rating BETWEEN 1 AND 5
    ),
    Comment TEXT,
    CreatedBy BIGINT NOT NULL REFERENCES Users (UserID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    BookingID BIGINT NOT NULL REFERENCES Bookings (BookingID),
    RoomID INT NOT NULL REFERENCES Rooms (RoomID),
);
--

--
CREATE TABLE Payments (
    PaymentID BIGSERIAL PRIMARY KEY,
    PaymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    --PaymentMethod VARCHAR(50),
    Amount NUMERIC(10, 2),
    PaymentStatus VARCHAR(10) CHECK (PaymentStatus IN ('Paid', 'Pending', 'Failed')) DEFAULT 'Pending',
    BookingID BIGINT NOT NULL REFERENCES Bookings (BookingID)
);

--

--
CREATE TABLE Services (
    ServiceID BIGSERIAL PRIMARY KEY,
    ServiceName VARCHAR(100) NOT NULL,
    Description TEXT,
    Price NUMERIC(10, 2) NOT NULL
);
--

--
CREATE TABLE BookingServices (
    BookingServiceID BIGSERIAL PRIMARY KEY,
    TotalPrice NUMERIC(10, 2),
    BookingID BIGINT NOT NULL REFERENCES Bookings (BookingID),
    ServiceID BIGINT NOT NULL REFERENCES Services (ServiceID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);
--

---
CREATE TABLE Maintenances (
    MaintenanceID BIGSERIAL PRIMARY KEY,
    Description TEXT,
    StartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    EndDate TIMESTAMP NULL,
    Status VARCHAR(10) CHECK (
        Status IN ('Pending', 'In Progress', 'Completed')
    ) DEFAULT 'Pending',
    Reason VARCHAR(100) CHECK (Status IN ('by User', 'normal Maintaince')) DEFAULT 'normal Maintaince',
    Cost NUMERIC(10, 2) NOT NULL,
    RoomID BIGINT NOT NULL REFERENCES Rooms (RoomID),
    BookingID BIGINT NULL REFERENCES Bookings (BookingID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees (EmployeeID),
    CreatedBy BIGINT NULL REFERENCES Users (UserID),
);