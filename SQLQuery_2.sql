 
create database hotel_db
 

CREATE TABLE Persons(
PersonID BIGSERIAL PRIMARY KEY,
Name VARCHAR(50) NOT NULL,
Phone VARCHAR(13) NOT NULL
)
 
 
 
CREATE TABLE Users (
    UserID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(15),
   -- Address TEXT,
    DateOfBirth DATE,
    IsVIP BOOLEAN DEFAULT FALSE,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    PersonID BIGINT PRIMARY KEY Persons(PersonID)
);

 
CREATE TABLE Permissions(
    PermissionID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50)
); 


CREATE TABLE Departments(
    DepartmentID BIGSERIAL PRIMARY KEY,
    Name VARCHAR(50)
); 

CREATE TABLE Employees (
    EmployeeID BIGSERIAL PRIMARY KEY,
    HireDate DATE,
    Salary NUMERIC(10, 2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PersonID BIGINT PRIMARY KEY Persons(PersonID),
    DepartmentID BIGINT NOT NULL  REFERENCES Departments(DepartmentID),
    PermissionID BIGINT NOT NULL  REFERENCES Permission(PermissionID)
);


CREATE TABLE RoomTypes (
    RoomTypeID BIGSERIAL PRIMARY KEY,
    TypeName VARCHAR(50) NOT NULL,
    Description TEXT,
    EmployeeID INT NOT NULL REFERENCES Employees(EmployeeID)
    );
 
CREATE TABLE Rooms (
    RoomID BIGSERIAL PRIMARY KEY,
    RoomNumber VARCHAR(10) UNIQUE NOT NULL,
    Status VARCHAR(10) CHECK (Status IN ('Available', 'Booked', 'Under Maintenance')) DEFAULT 'Available',
    PricePerOneDay NUMERIC(10, 2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    RoomTypeID INT NOT NULL REFERENCES RoomTypes(RoomTypeID)
);


CREATE TABLE Bookings (
    BookingID BIGSERIAL PRIMARY KEY,
    Dayes INT NOT NULL,
    BookingStatus VARCHAR(50) CHECK (BookingStatus IN ('Pending', 'Confirmed', 'Cancelled')) DEFAULT 'Pending',
    FristPayment NUMERIC(10, 2) NOT NULL,
    TotalPrice NUMERIC(10, 2) ,
    ServicePayment NUMERIC(10, 2) DEFAULT 0,
    MaintincePayment NUMERIC(10, 2) DEFAULT 0,
    PaymentStatus VARCHAR(50) CHECK (PaymentStatus IN ('Paid', 'Unpaid', 'Partial')) DEFAULT 'Unpaid',
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    LeaveDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UserID INT NOT NULL REFERENCES Users(UserID),
    RoomID INT NOT NULL REFERENCES Rooms(RoomID)
);

 
CREATE TABLE Reviews (
    ReviewID BIGSERIAL PRIMARY KEY,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment TEXT,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UserID BIGINT NOT NULL REFERENCES Users(UserID),
    BookingID BIGINT NOT NULL REFERENCES Bookings(BookingID),
    RoomID INT NOT NULL REFERENCES Rooms(RoomID)
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
    ServiceID BIGINT NOT NULL REFERENCES Services(ServiceID)
);

 
CREATE TABLE Maintenances (
    MaintenanceID BIGSERIAL PRIMARY KEY,
    Description TEXT,
    StartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    EndDate TIMESTAMP NULL,
    Status VARCHAR(10) CHECK (Status IN ('Pending', 'In Progress', 'Completed')) DEFAULT 'Pending',
    Reason VARCHAR(100) CHECK (Status IN ('by User', 'normal Maintaince')) DEFAULT 'normal Maintaince',
    Cost NUMERIC(10, 2) NOT NULL,
    RoomID BIGINT NOT NULL REFERENCES Rooms(RoomID),
    BookingID BIGINT  NULL REFERENCES Bookings(BookingID),
);

