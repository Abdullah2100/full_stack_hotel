\c postgres;
DROP DATABASE hotel_db;

CREATE DATABASE  hotel_db;
\c hotel_db;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE Persons 
(
    PersonID UUID PRIMARY KEY DEFAULT uuid_generate_v4() ,
    Name VARCHAR(50) NOT NULL,
    Phone VARCHAR(13) NOT NULL,
    Address TEXT NULL,
    IsDeleted bool DEFAULT FALSE
);

--dumy insert
INSERT INTO Persons(name,phone,address) values('ahmed','735501225','fack');
--

CREATE TABLE PersonUpdated 
(
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
CREATE OR REPLACE FUNCTION fn_personUpdate_modi()
RETURNS TRIGGER 
AS $$
BEGIN
RETURN NULL;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_personUpdate_update
BEFORE UPDATE ON PersonUpdated
FOR EACH ROW EXECUTE FUNCTION fn_personUpdate_modi();

CREATE TRIGGER tr_personUpdate_delete
BEFORE DELETE ON PersonUpdated
FOR EACH ROW EXECUTE FUNCTION fn_personUpdate_modi();



CREATE OR REPLACE FUNCTION fn_person_update () 
RETURNS TRIGGER 
AS $$ 
    DECLARE
        CurrentName VARCHAR(50):=''; 
        CurrentPhone VARCHAR(13):='';
        CurrentAddress TEXT:='';
    BEGIN

        IF NEW.name IS  NULL THEN 
            CurrentName := NEW.name;
        ELSE 
            CurrentName := OLD.name;
        END IF;


        IF NEW.phone IS NULL  THEN 
            CurrentPhone := NEW.phone;
        ELSE 
            CurrentPhone := OLD.phone;
        END IF;


        IF NEW.Address IS NULL  THEN 
            CurrentAddress := NEW.address;
        ELSE 
            CurrentAddress := NEW.address;
        END IF;

            UPDATE Persons 
            SET name = CurrentName ,phone = CurrentPhone ,address = CurrentAddress
            where  PersonID = OLD.personid;

            INSERT INTO
                PersonUpdated (
                    previusName,
                    previusPhone,
                    previusAddress,
                    currentName,
                    currentPhone,
                    currentAddress,
                    PersonID
                )
            VALUES
                (
                    OLD.Name,
                    OLD.Phone,
                    OLD.Address,
                    CurrentName,
                    CurrentPhone,
                    CurrentAddress,
                    OLD.PersonID
                );

        RETURN NULL;
    EXCEPTION
        WHEN OTHERS THEN
            -- Handle exceptions with a warning
            RAISE WARNING 'Something went wrong: %', SQLERRM;
    RETURN NULL;
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION fn_person_delete() 
RETURNS TRIGGER 
AS $$
    BEGIN
        UPDATE Persons
        SET isdeleted = true
        where personid = OLD.personid;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER tr_person_deleted 
BEFORE DELETE ON Persons 
FOR EACH ROW EXECUTE FUNCTION fn_person_delete ();

CREATE TRIGGER tr_peSrson_update 
BEFORE UPDATE ON Persons 
FOR EACH ROW EXECUTE FUNCTION fn_person_update ();


--dumy update
UPDATE Persons SET name = 'fackkdddd' WHERE personid = '62597b4d-838d-4455-86f9-bd97b525b567';
--
CREATE TABLE Admins 
(
    AdminID UUID PRIMARY KEY,
    PersonID UUID NOT NULL REFERENCES Persons (PersonID),
    UserName VARCHAR(50) NOT NULL,
    Password TEXT NOT NULL
);

CREATE OR REPLACE FUNCTION fn_admin_get_by_id(id UUID)
RETURNS TABLE(adminid UUID, personid UUID, name TEXT, phone TEXT, address TEXT, username TEXT) AS $$
BEGIN
  RETURN QUERY
  SELECT
    ad.adminid as adminid,
    ad.personid as personid,
    per.name as name,
    per.phone as phone ,
    per.address as address,
    ad.username as userName
  FROM Admins ad
  INNER JOIN Persons per
    ON ad.personid = per.personid
  WHERE ad.adminid = id; -- Assuming you want to filter by adminId
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION fn_admin_get_username_password(u_userName VARCHAR(50),u_password TEXT)
RETURNS TABLE(adminid UUID, personid UUID, name TEXT, phone TEXT, address TEXT, username TEXT) AS $$
BEGIN
  RETURN QUERY
  SELECT
    ad.adminid as adminid,
    ad.personid as personid,
    per.name as name,
    per.phone as phone ,
    per.address as address,
    ad.username as userName
  FROM Admins ad
  INNER JOIN Persons per
    ON ad.personid = per.personid
  WHERE ad.username = username AND ad.password = passow; -- Assuming you want to filter by adminId
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION fn_admin_insert
(
    adminid UUID ,
    name VARCHAR(50),
    phone VARCHAR(13), 
    email VARCHAR(100),
    address TEXT,
    username varchar(50),
    password TEXT
) RETURNS INT
AS 
$$
DECLARE 
   person_id BIGINT;
BEGIN
    BEGIN 
        INSERT INTO persons(adminid,name,email,phone,address)
        values (adminid,name,email,phone,address) RETURNING personid INTO person_id;
        INSERT INTO Admins (personid,username,password)
        VALUES (person_id,username,password)RETURNING adminid;

    EXCEPTION
        WHEN OTHERS THEN
            -- Handle exceptions with a warning
            RAISE WARNING 'Something went wrong: %', SQLERRM;
            RETURN 0;
    END;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION fn_admin_update
( 
    adminid UUID,
    name VARCHAR(50),
    phone VARCHAR(13),
    address TEXT,
    personid BIGINT,
    username varchar(50),
    password TEXT
) RETURNS INT
AS 
$$
    DECLARE 
        person_id BIGINT;
    BEGIN

            UPDATE persons  SET name = name , phone= phone,address = address WHERE personid= OLD.personid;
            UPDATE admin SET  username=username,password =password where adminid=adminid;
            return 1;
        EXCEPTION
            WHEN OTHERS THEN
 
                RAISE WARNING 'Something went wrong: %', SQLERRM;
                RETURN 0;

    END;
$$ LANGUAGE plpgsql;

--dumy insert 
INSERT INTO Admins(adminid,personid,username,password) 
values (uuid_generate_v4(),'62597b4d-838d-4455-86f9-bd97b525b567 ','facknice','771ali@..');
--

CREATE TABLE AdminUpdate 
(
    AdminUpdateID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    AdminID UUID NOT NULL REFERENCES Admins (AdminID),
    PreviusUserName VARCHAR(50) NOT NULL,
    PreviusPassword VARCHAR(50) NOT NULL,
    CurrentUserName VARCHAR(50)  NULL,
    CurrentPassword VARCHAR(50)  NULL
);

CREATE OR REPLACE FUNCTION fn_adminUpdate_modi()
RETURNS TRIGGER 
AS $$
BEGIN
RETURN NULL;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_adminUpdate_update
BEFORE UPDATE ON AdminUpdate
FOR EACH ROW EXECUTE FUNCTION fn_adminUpdate_modi();

CREATE TRIGGER tr_adminUpdate_delete
BEFORE DELETE ON AdminUpdate 
FOR EACH ROW EXECUTE FUNCTION fn_adminUpdate_modi();


CREATE OR REPLACE FUNCTION fn_admin_update() 
RETURNS TRIGGER  
AS $$ 
    DECLARE 
        currentUserName VARCHAR(50)  := NULL;
        currentPassword VARCHAR(50) := NULL;

    BEGIN

        -- Corrected NULL checks
        IF NEW.username IS NOT NULL THEN 
            currentUserName := NEW.username;
        END IF;

        IF NEW.Password IS NOT NULL THEN 
            currentPassword := NEW.Password;
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
        WHEN OTHERS THEN
            -- Handle exceptions with a warning
            RAISE WARNING 'Something went wrong: %', SQLERRM;
    RETURN NULL;
    END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION fn_admin_insert() 
RETURNS TRIGGER 
AS $$ 
    BEGIN 
        IF NEW.AdminID <> 1 THEN
            RETURN NULL;
        END IF;

        RETURN NEW;
    EXCEPTION
        WHEN OTHERS THEN
            -- Handle exceptions with a warning
            RAISE WARNING 'Something went wrong: %', SQLERRM;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION fn_admin_delete() 
RETURNS TRIGGER 
AS $$
    BEGIN 
        RETURN null;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_admin_insert 
BEFORE INSERT ON Admins 
FOR EACH ROW EXECUTE FUNCTION fn_admin_insert();

CREATE TRIGGER tr_admin_update 
BEFORE UPDATE ON Admins
FOR EACH ROW EXECUTE FUNCTION fn_admin_update();

CREATE TRIGGER tr_admin_delete
BEFORE DELETE ON Admins
FOR EACH ROW EXECUTE FUNCTION fn_admin_delete ();










CREATE TABLE Departments 
(
    DepartmentID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL
);

CREATE TABLE Permissions 
(
    PermissionID BIGSERIAL PRIMARY KEY,
    PermissionNum INT NOT NULL,
    DESCRIPTION VARCHAR(50)   
);

CREATE TABLE Departments 
(
    DepartmentID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50)
);

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

CREATE TABLE EmployeePermissions 
(
    EmployeePermissions BIGSERIAL PRIMARY KEY,
    PermissionID BIGINT NOT NULL REFERENCES PermissionS (PermissionID),
    EmployeeID BIGINT NULL REFERENCES Employees (EmployeeID)
);

CREATE TABLE EmployeeDeltedBy 
(
    EmployeeDeltedByID BIGSERIAL PRIMARY KEY,
    DeletedBy BIGINT NULL REFERENCES Employees (EmployeeID),
    EmployeeDeletedID BIGINT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--this trigger to handle if employee deleted
CREATE OR REPLACE FUNCTION fn_employee_deleted() 
RETURNS TRIGGER 
AS$$
BEGIN 
    IF OLD.IsDeleted = FALSE  AND OLD.DeletedBy IS NOT NULL THEN
        UPDATE Employees
        SET
            IsDeleted = TRUE,
            DeletedBy = OLD.DeletedBy,
            DeletedDate = CURRENT_TIMESTAMP
        WHERE
            EmployeeID = OLD.EmployeeID;

        INSERT INTO  EmployeeDeletedBy (DeletedBy, EmployeeDeletedID)
        VALUES (OLD.DeletedBy, OLD.EmployeeID);

    END IF;
    RETURN OLD;
EXCEPTION 
    WHEN OTHERS THEN RETURN NULL;
END;
$$LANGUAGE plpgsql;

CREATE TRIGGER tr_employee_deleted 
BEFORE DELETE ON Employees 
FOR EACH ROW EXECUTE FUNCTION handle_employee_deleted ();

CREATE TABLE Users 
(
    UserID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(15),
    DateOfBirth DATE,
    IsVIP bool DEFAULT FALSE,
    PersonID BIGINT PRIMARY KEY Persons (PersonID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    IsDeleted bool DEFAULT FALSE,
    DeletedDate TIMESTAMP NULL,
    DeletedBy BIGINT NULL REFERENCES Employees (EmployeeID)
);

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


CREATE TABLE  Rooms 
(
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

CREATE TABLE  Bookings 
(
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

CREATE TABLE Reviews 
(
    ReviewID BIGSERIAL PRIMARY KEY,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    CreatedBy BIGINT NOT NULL REFERENCES Users (UserID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    BookingID BIGINT NOT NULL REFERENCES Bookings (BookingID),
    RoomID INT NOT NULL REFERENCES Rooms (RoomID),
);

CREATE TABLE Payments 
(
    PaymentID BIGSERIAL PRIMARY KEY,
    PaymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    --PaymentMethod VARCHAR(50),
    Amount NUMERIC(10, 2),
    PaymentStatus VARCHAR(10) CHECK (PaymentStatus IN ('Paid', 'Pending', 'Failed')) DEFAULT 'Pending',
    BookingID BIGINT NOT NULL REFERENCES Bookings (BookingID)
);

CREATE TABLE Services 
(
    ServiceID BIGSERIAL PRIMARY KEY,
    ServiceName VARCHAR(100) NOT NULL,
    Description TEXT,
    Price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE BookingServices 
(
    BookingServiceID BIGSERIAL PRIMARY KEY,
    TotalPrice NUMERIC(10, 2),
    BookingID BIGINT NOT NULL REFERENCES Bookings (BookingID),
    ServiceID BIGINT NOT NULL REFERENCES Services (ServiceID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees (EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);

CREATE TABLE Maintenances 
(
    MaintenanceID BIGSERIAL PRIMARY KEY,
    Description TEXT,
    StartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    EndDate TIMESTAMP NULL,
    Status VARCHAR(10) CHECK (Status IN ('Pending', 'In Progress', 'Completed')) DEFAULT 'Pending',
    Reason VARCHAR(100) CHECK (Status IN ('by User', 'normal Maintaince')) DEFAULT 'normal Maintaince',
    Cost NUMERIC(10, 2) NOT NULL,
    RoomID BIGINT NOT NULL REFERENCES Rooms (RoomID),
    BookingID BIGINT NULL REFERENCES Bookings (BookingID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees (EmployeeID),
    CreatedBy BIGINT NULL REFERENCES Users (UserID),
);