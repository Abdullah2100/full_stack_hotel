create database hotel_db;

CREATE TABLE Persons(
    PersonID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Phone VARCHAR(13) NOT NULL,
    Address TEXT NULL,
);

CREATE TABLE Departments(
    DepartmentID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL
);


CREATE TABLE Permissions(
    PermissionID BIGSERIAL PRIMARY KEY,
    PermissionNum INT NOT NULL,
    Name VARCHAR(50) NOT NULL,
    DepartmentID BIGINT NOT NULL REFERENCES Departments(DepartmentID),
);

CREATE TABLE Employees (
    EmployeeID BIGSERIAL PRIMARY KEY,
    HireDate DATE DEFAULT CURRENT_DATE,
    Salary NUMERIC(10, 2) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DepartmentID BIGINT NOT NULL REFERENCES Departments(DepartmentID),
    PermissionID BIGINT NOT NULL REFERENCES Permissions(PermissionID),
    PersonID BIGINT NOT NULL REFERENCES Persons(PersonID),
    AddBy BIGINT NULL REFERENCES Employees(EmployeeID),
    DeletedBy BIGINT NULL REFERENCES Employees(EmployeeID),
    UpdatedBy BIGINT NULL REFERENCES Employees(EmployeeID),
    IsDeleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE EmployeePermissions(
    EmployeePermissions BIGSERIAL PRIMARY KEY,
    PermissionID BIGINT NOT NULL REFERENCES PermissionS(PermissionID),
    EmployeeID BIGINT NULL REFERENCES Employees(EmployeeID)
);

CREATE TABLE EmployeeDeltedBy(
    EmployeeDeltedByID BIGSERIAL PRIMARY KEY,
    DeletedBy BIGINT NULL REFERENCES Employees(EmployeeID),
    EmployeeDeletedID BIGINT NULL REFERENCES Employees(EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--this trigger to handle if employee deleted
-- Function to handle employee deletion
CREATE
OR REPLACE FUNCTION handle_employee_deleted() RETURNS TRIGGER AS $ $ BEGIN IF OLD.IsDeleted = FALSE
AND OLD.DeletedBy IS NOT NULL THEN -- Mark the employee as deleted
UPDATE
    Employees
SET
    IsDeleted = TRUE,
    DeletedBy = OLD.DeletedBy,
    CreatedAt = CURRENT_TIMESTAMP
WHERE
    EmployeeID = OLD.EmployeeID;

INSERT INTO
    EmployeeDeltedBy (DeletedBy, EmployeeDeletedID, CreatedAt)
VALUES
    (OLD.DeletedBy, OLD.EmployeeID, CURRENT_TIMESTAMP);

END IF;

RETURN OLD;

END;

$ $ LANGUAGE plpgsql;

CREATE TRIGGER trg_employee_deleted
AFTER
UPDATE
    ON Employees FOR EACH ROW
    WHEN (
        OLD.IsDeleted = FALSE
        AND NEW.IsDeleted = TRUE
    ) EXECUTE FUNCTION handle_employee_deleted();

CREATE TABLE Users (
    UserID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(15),
    DateOfBirth DATE,
    IsVIP BOOLEAN DEFAULT FALSE,
    PersonID BIGINT PRIMARY KEY Persons(PersonID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP PersonID BIGINT PRIMARY KEY Persons(PersonID) IsDeleted BOOLEAN DEFAULT FALSE,
    DeletedDate TIMESTAMP NULL,
    DeletedBy BIGINT NULL REFERENCES Employees(EmployeeID)
);

CREATE TABLE RoomTypes (
    RoomTypeID BIGSERIAL PRIMARY KEY,
    TypeName VARCHAR(50) NOT NULL,
    Description TEXT,
    CreatedBy INT NOT NULL REFERENCES Employees(EmployeeID),
    DeletedBy INT NULL REFERENCES Employees(EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedAt TIMESTAMP NULL,
    IsDeleted BOOLEAN DEFAULT FALSE,
);

CREATE TABLE Rooms (
    RoomID BIGSERIAL PRIMARY KEY,
    RoomNumber VARCHAR(10) UNIQUE NOT NULL,
    Status VARCHAR(10) CHECK (
        Status IN ('Available', 'Booked', 'Under Maintenance')
    ) DEFAULT 'Available',
    PricePerOneDay NUMERIC(10, 2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    RoomTypeID INT NOT NULL REFERENCES RoomTypes(RoomTypeID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees(EmployeeID),
    CreatedBy BIGINT NOT NULL REFERENCES Users(UserID),
    DeletedBy BIGINT NULL REFERENCES Employees(EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedAt TIMESTAMP NULL,
    ExcptedAt TIMESTAMP NULL,
);

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
    UserID INT NOT NULL REFERENCES Users(UserID),
    RoomID INT NOT NULL REFERENCES Rooms(RoomID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees(EmployeeID),
    CreatedBy BIGINT NOT NULL REFERENCES Users(UserID),
    DeletedBy BIGINT NULL REFERENCES Employees(EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DeletedAt TIMESTAMP NULL,
    ExcptedAt TIMESTAMP NULL,
);

CREATE TABLE Reviews (
    ReviewID BIGSERIAL PRIMARY KEY,
    Rating INT CHECK (
        Rating BETWEEN 1
        AND 5
    ),
    Comment TEXT,
    CreatedBy BIGINT NOT NULL REFERENCES Users(UserID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    BookingID BIGINT NOT NULL REFERENCES Bookings(BookingID),
    RoomID INT NOT NULL REFERENCES Rooms(RoomID),
);

CREATE TABLE Payments (
    PaymentID BIGSERIAL PRIMARY KEY,
    PaymentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    --PaymentMethod VARCHAR(50),
    Amount NUMERIC(10, 2),
    PaymentStatus VARCHAR(10) CHECK (PaymentStatus IN ('Paid', 'Pending', 'Failed')) DEFAULT 'Pending',
    BookingID BIGINT NOT NULL REFERENCES Bookings(BookingID)
);

CREATE TABLE Services (
    ServiceID BIGSERIAL PRIMARY KEY,
    ServiceName VARCHAR(100) NOT NULL,
    Description TEXT,
    Price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE BookingServices (
    BookingServiceID BIGSERIAL PRIMARY KEY,
    TotalPrice NUMERIC(10, 2),
    BookingID BIGINT NOT NULL REFERENCES Bookings(BookingID),
    ServiceID BIGINT NOT NULL REFERENCES Services(ServiceID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees(EmployeeID),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);

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
    RoomID BIGINT NOT NULL REFERENCES Rooms(RoomID),
    BookingID BIGINT NULL REFERENCES Bookings(BookingID),
    ExpectedBy BIGINT NOT NULL REFERENCES Employees(EmployeeID),
    CreatedBy BIGINT NULL REFERENCES Users(UserID),
);