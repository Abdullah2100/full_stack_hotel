\ c postgres;
DROP DATABASE hotel_db;
CREATE DATABASE hotel_db;
\ c hotel_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE Persons (
    PersonID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    Name VARCHAR(50) NOT NULL,
    Phone VARCHAR(13) NOT NULL UNIQUE,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Address TEXT NULL -- modifyBy UUID,
);
--dumy insert
INSERT INTO Persons(name, email, phone, address)
values('ahmed', 'fack@gmail.com', '735501225', 'fack');
--

-- CREATE TABLE PersonUpdated (
--     PersonUpdateID BIGSERIAL PRIMARY KEY,
--     PreviusName VARCHAR(50) NULL,
--     PreviusPhone VARCHAR(13) NOT NULL,
--     PreviusAddress TEXT NULL,
--     CurrentName VARCHAR(50) NULL,
--     CurrentPhone VARCHAR(13) NULL,
--     CurrentAddress TEXT NULL,
--     CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     PersonID UUID NOT NULL REFERENCES Persons (PersonID),
--     modifyBy UUID,
-- );
--

--
-- CREATE OR REPLACE FUNCTION fn_personUpdate_modi() RETURNS TRIGGER AS $$ BEGIN RETURN NULL;
-- END $$ LANGUAGE plpgsql;
-- --
-- --
-- CREATE TRIGGER tr_personUpdate_update BEFORE
-- UPDATE ON PersonUpdated FOR EACH ROW EXECUTE FUNCTION fn_personUpdate_modi();
-- --
-- --
-- CREATE TRIGGER tr_personUpdate_delete 
-- BEFORE DELETE ON PersonUpdated 
-- FOR EACH ROW EXECUTE FUNCTION fn_personUpdate_modi();
-- --
-- CREATE OR REPLACE FUNCTION fn_person_delete() RETURNS TRIGGER AS $$ 
-- BEGIN
-- RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
-- --
-- --
-- CREATE TRIGGER tr_person_deleted BEFORE DELETE ON Persons FOR EACH ROW EXECUTE FUNCTION fn_person_delete ();
--

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
-- SELECT ad.adminid as adminid,
--     ad.personid as personid,
--     per.name as name,
--     per.phone as phone,
--     per.address as address,
--     ad.username as userName,
--     per.email
-- FROM Admins ad
--     INNER JOIN Persons per ON ad.personid = per.personid
-- WHERE (ad.username = 'sdaf' OR per.email = 'sdaf')
--     AND ad.password = '71620152A16EBE4E23FB4C550510C41113E41E4E58352718527BF859A91603B5';
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
-- CREATE OR REPLACE FUNCTION fn_admin_delete() RETURNS TRIGGER AS $$ 
-- BEGIN 
-- RETURN null;
-- END;
-- $$ LANGUAGE plpgsql;
--

-- CREATE TRIGGER tr_admin_insert 
-- BEFORE INSERT ON Admins 
-- FOR EACH ROW EXECUTE FUNCTION fn_admin_insert();
-- CREATE TRIGGER tr_admin_update BEFORE
-- UPDATE ON Admins FOR EACH ROW EXECUTE FUNCTION fn_admin_update();
-- --
-- --
-- CREATE TRIGGER tr_admin_delete BEFORE DELETE ON Admins FOR EACH ROW EXECUTE FUNCTION fn_admin_delete ();
--

--dumy insert 
-- INSERT INTO Admins(adminid, personid, username, password)
-- values (
--         uuid_generate_v4(),
--         '62597b4d-838d-4455-86f9-bd97b525b567 ',
--         'facknice',
--         '771ali@..'
--     );
--

--
-- CREATE TABLE AdminUpdate (
--     AdminUpdateID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     AdminID UUID NOT NULL REFERENCES Admins (AdminID),
--     PreviusUserName VARCHAR(50) NOT NULL,
--     PreviusPassword VARCHAR(50) NOT NULL,
--     CurrentUserName VARCHAR(50) NULL,
--     CurrentPassword VARCHAR(50) NULL,
--     modifyBy UUID 
-- );
--

--
-- CREATE OR REPLACE FUNCTION fn_adminUpdate_modi() RETURNS TRIGGER AS $$ BEGIN RETURN NULL;
-- END $$ LANGUAGE plpgsql;
-- --
-- --
-- CREATE TRIGGER tr_adminUpdate_update BEFORE
-- UPDATE ON AdminUpdate FOR EACH ROW EXECUTE FUNCTION fn_adminUpdate_modi();
-- --
-- --
-- CREATE TRIGGER tr_adminUpdate_delete BEFORE DELETE ON AdminUpdate FOR EACH ROW EXECUTE FUNCTION fn_adminUpdate_modi();
--

--
-- CREATE OR REPLACE FUNCTION fn_admin_update() RETURNS TRIGGER AS $$
-- DECLARE currentUserName VARCHAR(50) := NULL;
-- currentPassword VARCHAR(50) := NULL;
-- BEGIN -- Corrected NULL checks
-- IF NEW.username IS NOT NULL THEN currentUserName := NEW.username;
-- END IF;
-- IF NEW.Password IS NOT NULL THEN currentPassword := NEW.Password;
-- END IF;
-- -- Insert the old and new values into AdminUpdate
-- INSERT INTO AdminUpdate (
--         AdminID,
--         PreviusUserName,
--         PreviusPassword,
--         CurrentUserName,
--         CurrentPassword
--     )
-- VALUES (
--         OLD.AdminID,
--         OLD.UserName,
--         OLD.Password,
--         currentUserName,
--         currentPassword
--     );
-- RETURN NEW;
-- EXCEPTION
-- WHEN OTHERS THEN -- Handle exceptions with a warning
-- RAISE EXCEPTION 'Something went wrong: %',
-- SQLERRM;
-- RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
--

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
    addBy UUID DEFAULT NULL,
    imagePath varchar(49) default null
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
CREATE TRIGGER tr_user_insrt BEFORE
INSERT ON Users FOR EACH ROW EXECUTE FUNCTION fn_user_insert();
CREATE OR REPLACE FUNCTION fn_user_deleted() RETURNS TRIGGER AS $$ BEGIN
UPDATE users
SET IsDeleted = TRUE
WHERE userid = OLD.userid;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER tr_user_deletet BEFORE DELETE ON users FOR EACH ROW EXECUTE FUNCTION fn_user_deleted();


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
    use.IsDeleted as ispersondeleted,
    use.imagepath
FROM users use
    INNER JOIN persons per ON use.personid = per.personid;
--
-- CREATE TABLE UserDeleted{
--     UserDeletedId UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     deletedBy UUID NOT NULL,
--     DeletedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     UserID UUID REFERENCES Users(UserID)
-- }
--

--
-- CREATE OR REPLACE FUNCTION fn_userDelete_insert()
-- RETURN TRIGGER AS $$
-- DECLARE 
-- is_canModifi BOOLEAN :=FALSE;
-- BEGIN
--   is_canModifi :=isAdminOrSomeOneHasPersmission(modifiBy_u);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FAIL THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE FUNCTION fn_user_update()
-- RETURN TRIGGER AS $$
-- DECLARE 
-- is_canModifi BOOLEAN :=FALSE;
-- BEGIN
--   is_canModifi :=isAdminOrSomeOneHasPersmission(modifiBy_u);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FAIL THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE FUNCTION fn_userUpdate_delete()
-- RETURN TRIGGER AS $$
-- DECLARE 
-- is_canModifi BOOLEAN :=FALSE;
-- BEGIN
--   is_canModifi :=isAdminOrSomeOneHasPersmission(modifiBy_u);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FAIL THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE TRIGGER tr_userUpdate_insert 
-- BEFORE INSERT ON UserUpdate FOR EACH ROW EXECUTE FUNCTION fn_userUpdate_insert();
-- --
-- --
-- CREATE OR REPLACE TRIGGER tr_userUpdate_insert 
-- BEFORE UPDATE ON UserUpdate FOR EACH ROW EXECUTE FUNCTION fn_userUpdate_update();
-- --
-- --
-- CREATE OR REPLACE TRIGGER tr_userUpdate_insert 
-- BEFORE DELETE ON UserUpdate FOR EACH ROW EXECUTE FUNCTION fn_userUpdate_delete();
-- --
--
-- CREATE TABLE UserUpdate(
--     userUpdateID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     userId UUID REFERENCES Users(userid),
--     currentUsername VARCHAR(50),
--     currentpassword TEXT,
--     currentIsVIP bool,
--     username VARCHAR(50),
--     password TEXT,
--     IsVIP bool,
--     modifyBy UUID,
--     modifyAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );
--

--
-- CREATE OR REPLACE FUNCTION fn_userUpdate_insert()
-- RETURN TRIGGER AS $$
-- DECLARE 
-- is_canModifi BOOLEAN :=FALSE;
-- BEGIN
--   is_canModifi :=isAdminOrSomeOneHasPersmission(modifiBy_u);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FAIL THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE FUNCTION fn_userUpdate_update()
-- RETURN TRIGGER AS $$
-- DECLARE 
-- is_canModifi BOOLEAN :=FALSE;
-- BEGIN
--   is_canModifi :=isAdminOrSomeOneHasPersmission(modifiBy_u);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FAIL THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE FUNCTION fn_userUpdate_delete()
-- RETURN TRIGGER AS $$
-- DECLARE 
-- is_canModifi BOOLEAN :=FALSE;
-- BEGIN
--   is_canModifi :=isAdminOrSomeOneHasPersmission(modifiBy_u);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FAIL THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE TRIGGER tr_userUpdate_insert 
-- BEFORE INSERT ON UserUpdate FOR EACH ROW EXECUTE FUNCTION fn_userUpdate_insert();
-- --
-- --
-- CREATE OR REPLACE TRIGGER tr_userUpdate_insert 
-- BEFORE UPDATE ON UserUpdate FOR EACH ROW EXECUTE FUNCTION fn_userUpdate_update();
-- --
-- --
-- CREATE OR REPLACE TRIGGER tr_userUpdate_insert 
-- BEFORE DELETE ON UserUpdate FOR EACH ROW EXECUTE FUNCTION fn_userUpdate_delete();
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
    CreatedAt TIMESTAMP,
    imagePath varchar(49)
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
    us.CreatedAt,
    us.imagepath
FROM Persons per
    INNER JOIN Users us ON per.PersonID = us.PersonID
ORDER BY us.CreatedAt DESC
LIMIT limitNumber OFFSET limitNumber * (pageNumber - 1);
-- Correct OFFSET calculation
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
    NULL,
    NULL;
-- Return an empty row in case of error
END;
$$ LANGUAGE plpgsql;
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
        addby UUID,
        imagePath_u varchar(49)
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
        addby,
        imagePath
    )
VALUES(
        userId_u,
        DateOfBirth,
        UserName,
        Password,
        IsVIP,
        person_id,
        addby,
        imagePath_u
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
brithday_u DATE,
imagePath_u VARCHAR(49) -- is_canModifi BOOLEAN,
-- modifiBy_u UUID
) RETURNS INT AS $$
DECLARE BEGIN -- Update 'persons' table
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
    END,
    imagepath = CASE WHEN imagePath_u IS NOT  NULL THEN imagePath_u  
    ELSE  imagepath   END
WHERE userid = userid_u;
RETURN 1;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
--
-- CREATE OR REPLACE FUNCTION fn_user_update_()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     is_canModifi :=isAdminOrSomeOneHasPersmission(NEW.modifyBy);
--     IF modifiBy_u <> userid_u.userId OR is_canModifi= FALSE THEN
--         EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'just user OR person that has permission can modify data ', SQLERRM;
--         RETURN NULL;
--     END IF;
--     RETURN NEW;
-- END;
-- $$LANGUAGE plpgsql;
--

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

-- CREATE TRIGGER tr_user_delete
-- BEFORE DELETE
-- ON Users FOR EACH ROW
-- EXECUTE FUNCTION fn_user_deleted();
-- CREATE  TRIGGER tr_user_update 
-- BEFORE UPDATE on users
-- FOR EACH ROW EXECUTE FUNCTION fn_user_update_();
--

--
-- CREATE OR REPLACE FUNCTION fn_person_update () 
-- RETURNS TRIGGER 
-- AS $$
-- BEGIN 
--   is_hasPermission_to_delete :=isAdminOrSomeOneHasPersmission(modifiBy);
--   IF   OLD.ModifyBy = OLD.userId OR is_hasPermission_to_delete = TRUE THEN 
--     RETURN NEW;
--   END IF;
--   REUTURN NULL;
--   ELSE
--     EXCEPTION
--         WHEN OTHERS THEN RAISE EXCEPTION  'only user or someone had permission can delete this user ';
-- RETURN NULL;
-- END;
-- $$ LANGUAGE plpgsql;
--

--
-- CREATE TRIGGER tr_person_update BEFORE
-- UPDATE ON Persons FOR EACH ROW EXECUTE FUNCTION fn_person_update ();
--

---#genral for all users Tables
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
    IsDeleted bool DEFAULT FALSE,
    imagePath   VARCHAR(50) DEFAULT NULL
);
-----

----
CREATE OR REPLACE FUNCTION fn_roomtype_insert_new(
    name_s VARCHAR,
    createdby_s UUID,
    imagepath VARCHAR
) RETURNS BOOLEAN AS $$
DECLARE
    roomType_id UUID;
BEGIN 
    -- Insert the new room type and capture the RoomTypeID
    INSERT INTO RoomTypes(name, createdby, imagepath) 
    VALUES (name_s, createdby_s, imagepath)
    RETURNING RoomTypeID INTO roomType_id;

    -- Check if the RoomTypeID was successfully inserted
    IF roomType_id IS NOT NULL THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'You do not have permission to create room type';
        RETURN FALSE;
END;
$$ LANGUAGE plpgsql;
---
---


CREATE OR REPLACE FUNCTION fn_roomtype_update_new(
    name_s VARCHAR,
    roomtypeid_s UUID,
    createdby_s UUID,
    imagepath_s VARCHAR
) RETURNS BOOLEAN AS $$
DECLARE 
    is_hasPermission BOOLEAN := FALSE;
    is_creation BOOLEAN := FALSE;
    name_compare VARCHAR;
BEGIN 
        is_hasPermission := isAdminOrSomeOneHasPersmission(createdby_s);
    if is_hasPermission = FALSE THEN 
      RETURN FALSE;
    END IF;

    UPDATE  RoomTypes SET
    name = CASE WHEN Name<>name_s THEN name_s ELSE name END, 
    createdby = CASE WHEN createdby_s<>createdby_s THEN createdby_s ELSE createdby END, 
    imagepath = CASE WHEN imagepath<>imagepath_s and imagepath_s IS NOT NULL THEN imagepath_s ELSE imagepath END 
    WHERE roomtypeid = roomtypeid_s;
   
    SELECT name INTO name_compare FROM RoomTypes WHERE roomtypeid = roomtypeid_s;

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
DECLARE 
    is_hasPermission BOOLEAN := FALSE;
    is_exist BOOLEAN := FALSE;
BEGIN 

    is_hasPermission := isAdminOrSomeOneHasPersmission(NEW.createdby);
    IF is_hasPermission = FALSE THEN 
    RETURN NULL;
    END IF;
    RETURN NEW;
EXCEPTION
WHEN OTHERS THEN  RAISE EXCEPTION 'you do not have permission to create roomtype';
RETURN NULL;
END;
$$ LANGUAGE plpgsql;
---
---
CREATE OR REPLACE FUNCTION fn_roomtype_delete()
RETURNS TRIGGER
AS $$
BEGIN

UPDATE roomtypes  SET IsDeleted = CASE WHEN OLD.isdeleted = TRUE THEN FALSE ELSE TRUE END WHERE roomtypeid = OLD.roomtypeid;
RETURN NULL;
EXCEPTION
WHEN OTHERS THEN  RAISE EXCEPTION 'you do not have permission to create roomtype';
RETURN NULL;

END

$$ Language plpgsql;

---

CREATE TRIGGER tr_roomtype_delete
BEFORE DELETE ON roomtypes FOR EACH 
ROW EXECUTE FUNCTION fn_roomtype_delete();

---
CREATE OR REPLACE TRIGGER tr_roomtType_insert
BEFORE INSERT
ON RoomTypes FOR EACH ROW EXECUTE FUNCTION fn_roomType_insert();

CREATE OR REPLACE TRIGGER tr_roomtType_update
BEFORE update
ON RoomTypes FOR EACH ROW EXECUTE FUNCTION fn_roomType_insert();




--
CREATE OR REPLACE FUNCTION fn_roomtype_update(name_n VARCHAR(50), createdBy_n UUID) RETURNS BOOLEAN AS $$
DECLARE is_hasPermission BOOLEAN := FALSE;
is_exist BOOLEAN := FALSE;
BEGIN is_hasPermission := isAdminOrSomeOneHasPersmission(CreatedBy);
IF is_hasPermission = false THEN RAISE EXCEPTION 'you do not have permission to update roomtype';
RETURN 0;
END IF;
SELECT COUT(*) > 0 INTO is_exist
FROM roomtypes
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

-- CREATE TABLE RoomTypeUpdate (
--     RoomTypeUpdateID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     RoomTypeID UUID NOT NULL   REFERENCES RoomTypes (RoomTypeID),
--     OldName VARCHAR(50) NOT NULL,
--     NewName VARCHAR(50) NOT NULL,
--     ModifiBy UUID NOT NULL,
--     CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
-- );
-- CREATE TABLE RoomTypeDeleted (
--     RoomTypeDeletedID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     RoomTypeID UUID NOT NULL   REFERENCES RoomTypes (RoomTypeID),
--     DeletedBy UUID NOT NULL,
--     DeletedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
-- );
-- CREATE OR REPLACE FUNCTION fn_roomType_insert()
-- RETURNS TRIGGER 
-- AS $$
-- DECLARE
-- is_hasPermission BOOLEAN;
-- BEGIN
--    is_hasPermission := isAdminOrSomeOneHasPersmission(NEW.ModifiBy)
--    IF is_hasPermission=FALSE THEN 
--     RAISE EXCEPTION  'has no permission to create roomtype: %', SQLERRM;
--    END IF;
--    INSERT INTO RoomTypes(RoomTypeID,CreatedBy,name) VALUES(NEW.roomtypeid,NEW.createdbyNEW.name);
--    RETURN NEW;
--    EXCEPTION
--      WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
--      RETURN NULL;
-- END;
-- $$LANGUAGE plpgsql;
--

--

-- CREATE OR REPLACE FUNCTION fn_roomType_update()
-- RETURNS TRIGGER 
-- AS $$
-- DECLARE
-- is_hasPermission BOOLEAN;
-- BEGIN
--    is_hasPermission := isAdminOrSomeOneHasPersmission(NEW.ModifiBy)
--    IF is_hasPermission=FALSE THEN 
--     RAISE EXCEPTION  'has no permission to create roomtype: %', SQLERRM;
--    END IF;
--    IF OLD.name != NEW.name THEN
--      INSERT INTO RoomTypeUpdate(RoomTypeID,OldName,NewName,ModifiBy)
--      VALUES(OLD.RoomTypeID,OLD.name,NEW.name,NEW.ModifiBy);
--      UPDATE RoomTypes SET name = NEW.name WHERE  RoomTypeID = OLD.RoomTypeID;
--      RETURN NEW;
--    END IF;
--    RETURN NULL;
--    EXCEPTION
--      WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
--      RETURN NULL;
-- END;
-- $$LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE FUNCTION fn_roomType_deleted()
-- RETURNS TRIGGER 
-- AS $$
-- DECLARE
-- is_hasPermission BOOLEAN;
-- BEGIN
--    is_hasPermission := isAdminOrSomeOneHasPersmission(NEW.CreatedBy)
--    IF is_hasPermission=FALSE THEN 
--     RAISE EXCEPTION  'has no permission to create roomtype: %', SQLERRM;
--    END IF;
--    INSERT INTO  RoomTypeDeleted (RoomTypeID , DeletedBy) VALUES (OLD.RoomTypeID,OLD.ModifiBy);
--    UPDATE RoomTypes SET IsDeleted = TRUE WHERE RoomTypeID = OLD.RoomTypeID; 
--    RETURN NEW;
--    EXCEPTION
--      WHEN OTHERS THEN RAISE EXCEPTION  'Something went wrong: %', SQLERRM;
--      RETURN NULL;
-- END;
-- $$LANGUAGE plpgsql;
--

--
-- CREATE OR REPLACE TRIGGER tr_roomtType_insert
-- BEFORE INSERT
-- ON RoomTypes FOR EACH ROW EXECUTE FUNCTION fn_roomType_insert();
-- --
-- CREATE  OR REPLACE TRIGGER tr_roomtype_update()
-- BEFORE UPDATE ON RoomTypes FOR EACH EXECUTE FUNCTION fn_roomType_update();
-- --
-- CREATE OR REPLACE TRIGGER tr_roomtype_deleted()
-- BEFORE DELETE ON RoomTypes FOR EACH EXECUTE FUNCTION fn_roomType_deleted();
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
--

--this table is for holding images for all tables 
CREATE TABLE Images_tb(
    imageId UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    belongTo UUID NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    path varchar(MAX) NOT NULL
);
CREATE OR REPLACE FUNCTION fn_images_tb_insert(belongTo_i UUID, path_i varchar(MAX)) RETURNS BOOLEAN AS $$
DECLARE is_exist BOOLEAN := FALSE;
BEGIN
SELECT COUNT(*) > 0
FROM RoomTypes
WHERE roomtypeid = belongTo_i;
IF is_exist THEN
INSERT INTO Images_tb(belongTo, path)
values(belongTo_i, path_i);
END IF;
EXCEPTION
WHEN OTHERS THEN RAISE EXCEPTION 'Something went wrong: %',
SQLERRM;
RETURN NULL;
END $$LANGUAGE plpgsql;