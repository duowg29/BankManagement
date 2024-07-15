
create DATABASE Banking;


USE Banking;
GO

CREATE TABLE Users (
	CitizenID NVARCHAR(12) PRIMARY KEY,
	FullName NVARCHAR(50) NOT NULL,
	Gender NVARCHAR(10) NOT NULL,
	DateOfBirth NVARCHAR(15) NOT NULL,
	Province NVARCHAR(MAX) NOT NULL,
	PhoneNumbers VARCHAR(15) NOT NULL,
	Email NVARCHAR(100) NOT NULL UNIQUE,
);

CREATE TABLE Accounts (
    AccountID BIGINT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(50) NOT NULL,
    Roles VARCHAR(10) NOT NULL,
    AccountStatus VARCHAR(10) NOT NULL,
    Balance DECIMAL(18, 2) NOT NULL DEFAULT 0,
	CitizenID NVARCHAR(12) NOT NULL,
	FOREIGN KEY (CitizenID) REFERENCES Users(CitizenID)
);

CREATE TABLE Transactions (
    TransactionId bigint PRIMARY KEY,
    SenderAccount bigint NOT NULL,
    FOREIGN KEY (SenderAccount) REFERENCES Accounts(AccountID),
    ReceiverAccount bigint NOT NULL,
    FOREIGN KEY (ReceiverAccount) REFERENCES Accounts(AccountID),
    Amount decimal(18, 2) NOT NULL,
    TransactionTime datetime NOT NULL,
    Remarks varchar(100),
    TransactionRole varchar(10) NOT NULL 
);


CREATE TABLE OtpTable (
    AccountID BIGINT PRIMARY KEY,
    Otp VARCHAR(6) NOT NULL,
    FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID)
);

CREATE TABLE SavingAccounts (
    SavingAccountID INT PRIMARY KEY,
    AccountID BIGINT NOT NULL,
    Balance DECIMAL(18,2) NOT NULL,
    InterestRate DECIMAL(5,4) NOT NULL,
    StartDate DATETIME NOT NULL
);


