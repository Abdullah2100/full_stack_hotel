\ c postgres;
DROP DATABASE hotel_db;
CREATE DATABASE hotel_db;
\c hotel_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
---
---
CREATE TABLE images(
    imageid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200) NOT NULL,
    belongTo UUID NOT NULL,
    isThumnail BOOLEAN DEFAULT FALSE
);
---
---
CREATE TABLE Persons (
    PersonID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(50) NOT NULL,
    Phone VARCHAR(13) NOT NULL UNIQUE,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Address TEXT NULL
);
---
---
CREATE TABLE Admins (
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
END;
$$ LANGUAGE plpgsql;
--

--

CREATE OR REPLACE FUNCTION fn_admin_get_username_password(username_a VARCHAR(50), password_a TEXT) RETURNS TABLE(
        adminid UUID,
        personid UUID,
        name VARCHAR(50),
        phone VARCHAR(13),
        address TEXT,
        email VARCHAR(100),
        username VARCHAR(50)
    ) AS $$ BEGIN RETURN QUERY
SELECT ad.adminid,
    ad.personid,
    per.name,
    per.phone,
    per.address,
    ad.username,
    per.email
FROM admins ad
    INNER JOIN persons per ON ad.personid = per.personid
WHERE ad.username = username_a
    OR per.email = username_a
    AND ad.password = password_a;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN QUERY
SELECT NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL;
END;
$$ LANGUAGE plpgsql;
-- 
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
--
CREATE OR REPLACE FUNCTION isAdminOrSomeOneHasPersmission(id UUID)
RETURNS BOOLEAN AS $$
DECLARE is_exist_id BOOLEAN;
BEGIN
SELECT COUNT(*) >= 1
FROM persons per
    LEFT JOIN admins ad ON per.personid = ad.personid
WHERE ad.adminid = id INTO is_exist_id;
RETURN is_exist_id;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$LANGUAGE plpgsql;
--

--

CREATE TABLE Users (
    UserID UUID PRIMARY KEY,
    DateOfBirth DATE NOT NULL,
    UserName VARCHAR(50) NOT NULL,
    Password TEXT NOT NULL,
    IsVIP bool DEFAULT FALSE,
    PersonID UUID NOT NULL REFERENCES Persons (personid),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IsDeleted bool DEFAULT FALSE,
    addBy UUID DEFAULT NULL
);
--

CREATE OR REPLACE FUNCTION fn_user_insert() RETURNS TRIGGER AS $$
DECLARE isCanAdd BOOLEAN := FALSE;
BEGIN IF New.addby IS NOT NULL THEN isCanAdd := isAdminOrSomeOneHasPersmission(NEW.addBy);
IF isCanAdd = false THEN
DELETE FROM persons
WHERE personid = NEW.personid;
RETURN NULL;
ELSE RETURN NEW;
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;
---
----
CREATE TRIGGER tr_user_insrt BEFORE
INSERT ON Users FOR EACH ROW EXECUTE FUNCTION fn_user_insert();
---
---
CREATE OR REPLACE FUNCTION fn_user_deleted() RETURNS TRIGGER AS $$ BEGIN
UPDATE users
SET IsDeleted = CASE
        WHEN isdeleted = TRUE THEN FALSE
        ELSE TRUE
    END
WHERE userid = OLD.userid;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
--

--
CREATE TRIGGER tr_user_deletet BEFORE DELETE ON users FOR EACH ROW EXECUTE FUNCTION fn_user_deleted();
--

---
CREATE VIEW usersview AS
SELECT per.PersonID,
    per.Name,
    per.Phone,
    per.Email,
    per.Address,
    use.UserID,
    use.DateOfBirth,
    use.UserName,
    use.IsVIP,
    use.CreatedAt,
    use.password,
    use.IsDeleted as ispersondeleted
FROM users use
    INNER JOIN persons per ON use.personid = per.personid;
--

--
CREATE OR REPLACE FUNCTION getUserPagination(pageNumber INT, limitNumber INT)
RETURNS TABLE (
    PersonId UUID,
    Name VARCHAR(50),
    Phone VARCHAR(13),
    Email VARCHAR(100),
    Address TEXT,
    UserId UUID,
    IsDeleted BOOL,
    UserName VARCHAR(50),
    DateOfBirth DATE,
    IsVIP BOOL,
    CreatedAt TIMESTAMP
) AS $$ BEGIN RETURN QUERY
SELECT per.PersonId,
    per.Name,
    per.Phone,
    per.Email,
    per.Address,
    us.UserId,
    us.IsDeleted,
    us.UserName,
    us.DateOfBirth,
    us.IsVIP,
    us.CreatedAt
FROM Persons per
    INNER JOIN Users us ON per.PersonID = us.PersonID
ORDER BY us.CreatedAt DESC
LIMIT limitNumber OFFSET limitNumber * (pageNumber - 1);
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN QUERY
SELECT Null,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL;
END $$ LANGUAGE plpgsql;
--  
--
CREATE OR REPLACE FUNCTION fn_user_insert_in (
userId_u UUID,
name VARCHAR(50),
phone VARCHAR(13),
email VARCHAR(100),
address TEXT,
username varchar(50),
password TEXT,
IsVIP bool,
DateOfBirth DATE,
addby UUID
) RETURNS INT AS $$
DECLARE person_id UUID;
BEGIN
INSERT INTO persons(name, email, phone, address)
VALUES (name, email, phone, address)
RETURNING personid INTO person_id;
INSERT INTO Users (
        userid,
        dateofbirth,
        username,
        password,
        isvip,
        personid,
        addby
    )
VALUES(
        userId_u,
        DateOfBirth,
        UserName,
        Password,
        IsVIP,
        person_id,
        addby
    );
RETURN 1;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN 0;
END;
$$ LANGUAGE plpgsql;
--

--
CREATE OR REPLACE FUNCTION fn_user_update(
userid_u UUID,
name_u VARCHAR(50),
phone_u VARCHAR(13),
address_u TEXT,
username_u VARCHAR(50),
password_u TEXT,
IsVIP_u BOOLEAN,
personid_u UUID,
brithday_u DATE
) RETURNS INT AS $$
DECLARE BEGIN
UPDATE persons
SET name = CASE
        WHEN name <> name_u THEN name_u
        ELSE name
    END,
    phone = CASE
        WHEN phone <> phone_u THEN phone_u
        ELSE phone
    END,
    address = CASE
        WHEN address <> address_u THEN address_u
        ELSE address
    END
WHERE personid = personid_u;
UPDATE users
SET username = CASE
        WHEN username_u <> username THEN username_u
        ELSE username
    END,
    password = CASE
        WHEN password_u <> password THEN password_u
        ELSE password
    END,
    isvip = CASE
        WHEN IsVIP_u <> isvip THEN IsVIP_u
        ELSE isvip
    END,
    dateofbirth = CASE
        WHEN dateofbirth <> brithday_u THEN brithday_u
        ELSE dateofbirth
    END
WHERE userid = userid_u;
RETURN 1;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
----
----
CREATE OR REPLACE FUNCTION fn_user_deleted(personid UUID, modifiBy UUID) RETURNs INT AS $$
DECLARE is_deleted BOOLEAN := false;
is_hasPermission_to_delete BOOLEAN := FALSE;
BEGIN is_hasPermission_to_delete := isAdminOrSomeOneHasPersmission(modifiBy);
SELECT isdeleted INTO is_deleted
FROM persons
WHERE personid = OLD.personid;
IF is_deleted = FALSE
AND OLD.ModifyBy = OLD.userId
OR is_hasPermission_to_delete = TRUE THEN
UPDATE Users
SET IsDeleted = TRUE
WHERE UserId = OLD.userid;
INSERT INTO UserDeleted(deletedBy, UserID)
VALUES (OLD.ModifyBy, OLD.userid);
RETURN 1;
ELSE -- EXCEPTION
-- WHEN OTHERS THEN 
RAISE EXCEPTION 'only user or someone had permission can delete this user ';
RETURN 0;
END IF;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN 0;
END;
$$LANGUAGE plpgsql;
--

--
CREATE OR REPLACE FUNCTION isExistById(id UUID)
RETURNS BOOLEAN AS $$
DECLARE email VARCHAR(100);
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
    ) INTO email;
RETURN email;
RETURN is_exist_id;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$LANGUAGE plpgsql;
--

--
CREATE OR REPLACE FUNCTION isExistByIdAndEmail(email_hold VARCHAR(100),id UUID)
RETURNS BOOLEAN AS $$
DECLARE isExist INT;
BEGIN
SELECT COUNT(*) INTO isExist
FROM(
        SELECT *
        FROM persons per
            LEFT JOIN admins ad ON per.personid = ad.personid
            LEFT JOIN users use ON per.personid = use.personid
        WHERE per.email = email_hold
            AND ad.adminid = id
            OR use.userid = id
    ) d;
RETURN isExist >= 1;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$LANGUAGE plpgsql;
--

--

CREATE TABLE RoomTypes (
    RoomTypeID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(50) NOT NULL,
    CreatedBy UUID NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IsDeleted bool DEFAULT FALSE
);
-----
----
CREATE OR REPLACE FUNCTION fn_roomtype_insert_new(
        roomtype_id_holder UUID,
        name_s VARCHAR,
        createdby_s UUID
    ) RETURNS BOOLEAN AS $$
DECLARE roomType_id UUID;
BEGIN
INSERT INTO RoomTypes(roomtypeid, name, createdby)
VALUES (roomtype_id_holder, name_s, createdby_s)
RETURNING RoomTypeID INTO roomType_id;
IF roomType_id IS NOT NULL THEN RETURN TRUE;
ELSE RETURN FALSE;
END IF;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'You do not have permission to create room type: %',
SQLERRM;
RETURN FALSE;
END;
$$ LANGUAGE plpgsql;
---
---
CREATE OR REPLACE FUNCTION fn_roomtype_update_new(
        name_s VARCHAR,
        roomtypeid_s UUID,
        createdby_s UUID
    ) RETURNS BOOLEAN AS $$
DECLARE is_hasPermission BOOLEAN := FALSE;
is_creation BOOLEAN := FALSE;
name_compare VARCHAR;
BEGIN is_hasPermission := isAdminOrSomeOneHasPersmission(createdby_s);
if is_hasPermission = FALSE THEN RETURN FALSE;
END IF;
UPDATE RoomTypes
SET name = CASE
        WHEN Name <> name_s THEN name_s
        ELSE name
    END,
    createdby = CASE
        WHEN createdby_s <> createdby_s THEN createdby_s
        ELSE createdby
    END
WHERE roomtypeid = roomtypeid_s;
SELECT name INTO name_compare
FROM RoomTypes
WHERE roomtypeid = roomtypeid_s;
is_creation = (name_compare = name_s);
RETURN is_creation;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$ LANGUAGE plpgsql;
----
-----
CREATE OR REPLACE FUNCTION fn_roomtype_insert() RETURNS TRIGGER AS $$
DECLARE is_hasPermission BOOLEAN := FALSE;
is_exist BOOLEAN := FALSE;
BEGIN is_hasPermission := isAdminOrSomeOneHasPersmission(NEW.createdby);
IF is_hasPermission = FALSE THEN RETURN NULL;
END IF;
RETURN NEW;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'you do not have permission to create roomtype';
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
---
---
CREATE OR REPLACE FUNCTION fn_roomtype_delete() RETURNS TRIGGER AS $$ BEGIN
UPDATE roomtypes
SET IsDeleted = CASE
        WHEN OLD.isdeleted = TRUE THEN FALSE
        ELSE TRUE
    END
WHERE roomtypeid = OLD.roomtypeid;
RETURN NULL;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'you do not have permission to create roomtype';
RETURN NULL;
END $$ Language plpgsql;
---
--

CREATE TRIGGER tr_roomtype_delete BEFORE DELETE ON roomtypes FOR EACH ROW EXECUTE FUNCTION fn_roomtype_delete();
---
---
CREATE OR REPLACE TRIGGER tr_roomtType_insert BEFORE
INSERT ON RoomTypes FOR EACH ROW EXECUTE FUNCTION fn_roomType_insert();
--

--
CREATE OR REPLACE TRIGGER tr_roomtType_update BEFORE
update ON RoomTypes FOR EACH ROW EXECUTE FUNCTION fn_roomType_insert();
--

--
CREATE OR REPLACE FUNCTION fn_roomtype_update(name_n VARCHAR(50), createdBy_n UUID) RETURNS BOOLEAN AS $$
DECLARE is_hasPermission BOOLEAN := FALSE;
is_exist BOOLEAN := FALSE;
BEGIN is_hasPermission := isAdminOrSomeOneHasPersmission(CreatedBy);
IF is_hasPermission = false THEN RAISE EXCEPTION 'you do not have permission to update roomtype';
RETURN 0;
END IF;
SELECT COUT(*) > 0 INTO is_exist
FROM roomtypes;
UPDATE roomtypes
SET name = name_n,
    createdby = createdBy_n;
RETURN TRUE;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$ LANGUAGE plpgsql;
--

--

CREATE TABLE Rooms (
    RoomID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Status VARCHAR(10) CHECK (
        Status IN ('Available', 'Booked', 'Under Maintenance')
    ) DEFAULT 'Available',
    pricePerNight NUMERIC(10, 2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    roomtypeid UUID NOT NULL REFERENCES RoomTypes (roomtypeid),
    capacity INT NOT NULL,
    bedNumber INT NOT NULL,
    belongTo UUID,
    updateAt TIMESTAMP NULL,
    isBlock BOOLEAN DEFAULT FALSE,
    isDeleted BOOLEAN DEFAULT FALSE
);
--

--

CREATE OR REPLACE FUNCTION is_room_belong_to_user(
    room_id UUID,
    user_id UUID
) RETURNS BOOLEAN
AS $$
DECLARE
isBelongTo BOOLEAN;
BEGIN

SELECT 
COUNT(*)>0 INTO isBelongTo 
FROM rooms 
WHERE roomid = room_id AND belongto = user_id;
RETURN isBelongTo;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;

END;
$$LANGUAGE PLPGSQL;

--

--

CREATE OR REPLACE FUNCTION getRoomsByPage(pageNumber INT, limitNumber INT) RETURNS TABLE(
        RoomID UUID,
        Status VARCHAR(10),
        pricePerNight NUMERIC(10, 2),
        CreatedAt TIMESTAMP,
        roomtypeid UUID,
        capacity INT,
        bedNumber INT,
        belongTo UUID,
        isblock Boolean,
        isDeleted Boolean
    ) AS $$ BEGIN
RETURN QUERY SELECT
rom.roomid,
    rom.Status,
    rom.pricepernight,
    rom.CreatedAt,
    rom.roomtypeid,
    rom.capacity,
    rom.bedNumber,
    rom.belongTo
    ,rom.isblock,
    rom.isdeleted
FROM rooms rom
    INNER JOIN roomtypes romt ON rom.roomtypeid = romt.roomtypeid
    LEFT JOIN users usr ON rom.belongto = usr.userid
    LEFT JOIN admins adms ON rom.belongto = adms.adminid
WHERE rom.isBlock = FALSE
ORDER BY rom.CreatedAt DESC
LIMIT limitNumber OFFSET limitNumber * (pageNumber - 1);
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN QUERY
SELECT 
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL;
END;
$$ LANGUAGE plpgsql;
---



---
CREATE OR REPLACE FUNCTION fn_room_insert_new(
        roomid_u UUID,
        status VARCHAR(10),
        pricePerNight_ NUMERIC(10, 2),
        roomtypeid_ UUID,
        capacity_ INT,
        bedNumber_ INT,
        belongTo_ UUID
    ) RETURNS INT AS $$
DECLARE roomid_holder UUID;
is_hasPermission_to_delete BOOLEAN;
BEGIN -- Check if the user has permission to delete
-- is_hasPermission_to_delete := isAdminOrSomeOneHasPersmission(modifiBy);
-- IF is_hasPermission_to_delete = FALSE THEN
--     RAISE EXCEPTION 'You do not have permission to perform this action';
--     RETURN 0;  -- You might want to return a specific error code
-- END IF;
-- Insert the new room
INSERT INTO rooms(
        roomid,
        Status,
        pricePerNight,
        roomtypeid,
        capacity,
        bedNumber,
        belongTo
    )
VALUES (
        roomid_u,
        status,
        pricePerNight_,
        roomtypeid_,
        capacity_,
        bedNumber_,
        belongTo_
    )
RETURNING roomid INTO roomid_holder;
-- Check if the insertion was successful
IF roomid_holder IS NOT NULL THEN RETURN 1;
-- Return a success code
-- Return failure if roomid is NULL
END IF;
RETURN 0;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN 0;
END;
$$ LANGUAGE PLPGSQL;
---
---
CREATE OR REPLACE FUNCTION fn_room_insert() RETURNS TRIGGER AS $$
DECLARE isUserDeleted BOOLEAN;
BEGIN
SELECT isdeleted INTO isUserDeleted
FROM users
WHERE userid = NEW.userid;
IF isUserDeleted IS NOT NULL
AND isUserDeleted = TRUE THEN RETURN NEW;
END IF;
RETURN NULL;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
---
---
CREATE TRIGGER tr_roomt_insert BEFORE
INSERT ON rooms FOR EACH ROW EXECUTE FUNCTION fn_room_insert();
---
----
CREATE OR REPLACE FUNCTION fn_room_update_new (
        roomid_ UUID,
        status_ VARCHAR(10),
        pricePerNight_ NUMERIC(10, 2),
        roomtypeid_ UUID,
        capacity_ INT,
        bedNumber_ INT
    ) RETURNS Boolean AS $$
DECLARE updateAt_holder TIMESTAMP;
BEGIN
UPDATE rooms
SET Status = CASE
        WHEN status <> status_
        AND status_ IS NOT NULL THEN status_
        ELSE status
    END,
    pricePerNight = CASE
        WHEN pricePerNight_ <>  pricePerNight
        AND pricePerNight_ IS NOT NULL THEN pricePerNight_
        ELSE  pricePerNight
    END,
    roomtypeid = CASE
        WHEN roomtypeid_ <>  roomtypeid
        AND roomtypeid_ IS NOT NULL THEN roomtypeid_
        ELSE  roomtypeid
    END,
    capacity = CASE
        WHEN capacity_ <>  capacity
        AND capacity_ IS NOT NULL THEN capacity_
        ELSE  capacity
    END,
    bedNumber = CASE
        WHEN bedNumber_ <>  bedNumber
        AND bedNumber_ IS NOT NULL THEN bedNumber_
        ELSE  bedNumber
    END,
    updateAt = CURRENT_TIMESTAMP
WHERE roomid = roomid_;
SELECT updateAt INTO updateAt_holder
FROM rooms
WHERE roomid = roomid_;
RETURN updateAt_holder is NOT NULL;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN false;
END;
$$ LANGUAGE PLPGSQL;
---
---
CREATE OR REPLACE FUNCTION fn_room_update() RETURNS TRIGGER AS $$
DECLARE isUserDeleted BOOLEAN;
BEGIN
SELECT isdeleted INTO isUserDeleted
FROM users
WHERE userid = NEW.userid;
IF isUserDeleted IS NOT NULL
AND isUserDeleted = TRUE THEN RETURN NEW;
END IF;
RETURN NULL;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
--

--
CREATE TRIGGER tr_roomt_update BEFORE
INSERT ON rooms FOR EACH ROW EXECUTE FUNCTION fn_room_update();
----
----
CREATE OR REPLACE FUNCTION room_delete(
    room_id UUID,
    user_id UUID
) RETURNS BOOLEAN 
AS $$
DECLARE
isHasPermission BOOLEAN;
isItBelongToHim BOOLEAN;
BEGIN
isHasPermission = isAdminOrSomeOneHasPersmission(user_id);
isItBelongToHim = is_room_belong_to_user(user_id,user_id);

IF isHasPermission OR isItBelongToHim THEN
   UPDATE ROOMS SET  
   isdeleted = CASE WHEN  isdeleted=TRUE THEN FALSE ELSE TRUE END 
   WHERE roomid = room_id;
   RETURN TRUE;
END IF;

RETURN FALSE;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$ LANGUAGE plpgsql;
---

---

CREATE OR REPLACE FUNCTION fn_room_delete_tr()
RETURNS TRIGGER 
AS $$
BEGIN
return null;
END;
$$ LANGUAGE plpgsql;
---

---

CREATE TRIGGER tr_delete_room 
BEFORE DELETE 
ON rooms 
FOR EACH ROW EXECUTE FUNCTION fn_room_delete_tr();

----
----


----
----
CREATE TABLE Bookings (
    BookingID BIGSERIAL PRIMARY KEY,
    RoomID UUID NOT NULL REFERENCES Rooms (roomid),
    bookingStartYear Int Not Null Check(bookingStartYear >= YEAR(CURDATE())),
    bookingStartMonth Int Not Null Check(bookingStartMonth >= MONTH(CURDATE())),
    bookingStartDay Int Not Null Check(bookingStartDay >= Day(CURDATE())),
    bookingEndYear Int Not Null Check(bookingStartYear >= YEAR(CURDATE())),
    bookingEndMonth Int Not Null Check(bookingStartMonth >= MONTH(CURDATE())),
    bookingEndDay Int Not Null Check(bookingStartDay >= Day(CURDATE())),
    Dayes INT NOT NULL,
    BookingStatus VARCHAR(50) CHECK (
        BookingStatus IN ('Pending', 'Confirmed', 'Cancelled')
    ) DEFAULT 'Pending',
    TotalPrice NUMERIC(10, 2),
    -- FristPayment NUMERIC(10, 2) NOT NULL CHECK(FristPayment = TotalPrice / 3),
    ServicePayment NUMERIC(10, 2) DEFAULT 0,
    MaintincePayment NUMERIC(10, 2) DEFAULT 0,
    PaymentStatus VARCHAR(50) CHECK (PaymentStatus IN ('Paid', 'Unpaid'
    --, 'Partial'
    )) DEFAULT 'Unpaid',
    -- excpectedLeaveDate TIMESTAMP NOT NULL,
    -- LeaveDate TIMESTAMP DEFAULT NULL,
    userID UUID NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
----
----
CREATE OR REPLACE FUNCTION fn_bookin_insert(
    RoomID_ UUID  ,
    Dayes_ INT,
    BookingStatus_ VARCHAR(50) ,
    TotalPrice_ NUMERIC(10, 2),
    FristPayment_ NUMERIC(10, 2) ,
    ServicePayment_ NUMERIC(10, 2) ,
    MaintincePayment_ NUMERIC(10, 2) ,
     excpectedLeaveDate_ TIMESTAMP  ,
    userid_  UUID
     
)RETURNS BOOLEAN AS $$
DECLARE
isNotDeletion Boolean;
BEGIN

SELECT isdeleted INTO isNotDeletion FROM user WHERE userid = userid_;
IF isNotDeletion = TRUE THEN
RAISE EXCEPTION 'The user is deleted';
RETURN FALSE;
END IF;
INSERT INTO Bookings(
    RoomID,
    Dayes,
    BookingStatus,
    TotalPrice,
    FristPayment,
    ServicePayment,
    MaintincePayment,
    excpectedLeaveDate,
    belongTo
) VALUES(
    RoomID_,
    Dayes_,
    BookingStatus_,
    TotalPrice_,
    FristPayment_,
    ServicePayment_,
    MaintincePayment_,
    excpectedLeaveDate_,
    userid_
);


RETURN TRUE;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN FALSE;
END;
$$ LANGUAGE plpgsql;
---
---

CREATE OR REPLACE FUNCTION fn_booking_insert_tr()
RETURNS TRIGGER
AS $$
DECLARE
isValidId BOOLEAN;
BEGIN
isValidId = isExistById(NEW.belongto);
IF isValidId = FALSE THEN
RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END IF;
RETURN NEW;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
----
----
CREATE TRIGGER tr_bookin_insert
BEFORE INSERT ON bookings
FOR EACH ROW EXECUTE FUNCTION fn_booking_insert_tr();
----
----



----
----
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
- -
